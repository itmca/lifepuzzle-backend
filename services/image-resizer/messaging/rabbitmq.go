package messaging

import (
	"encoding/json"
	"fmt"
	"log"
	"time"

	amqp "github.com/rabbitmq/amqp091-go"
)

type Message struct {
	ID           int `json:"id"`
	RetryCount   int
	Ack          func() error
	Nack         func() error
	NackNoRequeue func() error
	SendToDLQ     func() error
}

type RabbitMQConsumer struct {
	connection   *amqp.Connection
	channel      *amqp.Channel
	queue        amqp.Queue
	dlqQueue     amqp.Queue
	exchangeName string
	routingKey   string
}

func NewRabbitMQConsumer(url, queueName, exchangeName, routingKey string) (*RabbitMQConsumer, error) {
	conn, err := amqp.Dial(url)
	if err != nil {
		return nil, fmt.Errorf("failed to connect to RabbitMQ: %w", err)
	}

	ch, err := conn.Channel()
	if err != nil {
		conn.Close()
		return nil, fmt.Errorf("failed to open channel: %w", err)
	}

	err = ch.Qos(10, 0, false) // prefetch 10 messages
	if err != nil {
		ch.Close()
		conn.Close()
		return nil, fmt.Errorf("failed to set QoS: %w", err)
	}

	// Declare exchange
	err = ch.ExchangeDeclare(
		exchangeName,
		"topic", // type: topic exchange for routing key matching
		true,    // durable
		false,   // auto-deleted
		false,   // internal
		false,   // no-wait
		nil,     // arguments
	)
	if err != nil {
		ch.Close()
		conn.Close()
		return nil, fmt.Errorf("failed to declare exchange: %w", err)
	}

	// Declare DLQ first
	dlqName := queueName + ".dlq"
	dlqQueue, err := ch.QueueDeclare(
		dlqName,
		true,  // durable
		false, // delete when unused
		false, // exclusive
		false, // no-wait
		nil,   // arguments
	)
	if err != nil {
		ch.Close()
		conn.Close()
		return nil, fmt.Errorf("failed to declare DLQ: %w", err)
	}

	// Bind DLQ to exchange with DLQ routing key
	dlqRoutingKey := routingKey + ".dlq"
	err = ch.QueueBind(
		dlqQueue.Name,     // queue name
		dlqRoutingKey,     // routing key
		exchangeName,      // exchange
		false,
		nil,
	)
	if err != nil {
		ch.Close()
		conn.Close()
		return nil, fmt.Errorf("failed to bind DLQ: %w", err)
	}

	// Declare main queue
	q, err := ch.QueueDeclare(
		queueName,
		true,  // durable
		false, // delete when unused
		false, // exclusive
		false, // no-wait
		nil,   // arguments
	)
	if err != nil {
		ch.Close()
		conn.Close()
		return nil, fmt.Errorf("failed to declare queue: %w", err)
	}

	// Bind queue to exchange with routing key
	err = ch.QueueBind(
		q.Name,       // queue name
		routingKey,   // routing key
		exchangeName, // exchange
		false,
		nil,
	)
	if err != nil {
		ch.Close()
		conn.Close()
		return nil, fmt.Errorf("failed to bind queue: %w", err)
	}

	return &RabbitMQConsumer{
		connection:   conn,
		channel:      ch,
		queue:        q,
		dlqQueue:     dlqQueue,
		exchangeName: exchangeName,
		routingKey:   routingKey,
	}, nil
}

func (r *RabbitMQConsumer) Consume() (<-chan Message, error) {
	msgs, err := r.channel.Consume(
		r.queue.Name,
		"",    // consumer
		false, // auto-ack
		false, // exclusive
		false, // no-local
		false, // no-wait
		nil,   // args
	)
	if err != nil {
		return nil, fmt.Errorf("failed to register consumer: %w", err)
	}

	messageChan := make(chan Message)

	go func() {
		defer close(messageChan)
		for d := range msgs {
			var msgData struct {
				ID int `json:"id"`
			}
			if err := json.Unmarshal(d.Body, &msgData); err != nil {
				log.Printf("Failed to unmarshal message: %v", err)
				d.Nack(false, false)
				continue
			}

			// Get retry count from headers
			retryCount := 0
			if d.Headers != nil {
				if count, ok := d.Headers["x-retry-count"].(int32); ok {
					retryCount = int(count)
				}
			}

			msg := Message{
				ID:         msgData.ID,
				RetryCount: retryCount,
				Ack: func() error {
					return d.Ack(false)
				},
				Nack: func() error {
					return d.Nack(false, true) // requeue
				},
				NackNoRequeue: func() error {
					return d.Nack(false, false) // don't requeue
				},
				SendToDLQ: func() error {
					return r.sendToDLQ(d)
				},
			}

			messageChan <- msg
		}
	}()

	return messageChan, nil
}

func (r *RabbitMQConsumer) sendToDLQ(d amqp.Delivery) error {
	// Create headers with failure information
	headers := amqp.Table{}
	if d.Headers != nil {
		// Copy existing headers
		for k, v := range d.Headers {
			headers[k] = v
		}
	}
	
	// Add DLQ specific headers
	headers["x-death-reason"] = "max-retries-exceeded"
	headers["x-death-time"] = time.Now().Unix()
	headers["x-original-queue"] = r.queue.Name
	headers["x-original-routing-key"] = r.routingKey

	// Publish to DLQ
	dlqRoutingKey := r.routingKey + ".dlq"
	err := r.channel.Publish(
		r.exchangeName, // exchange
		dlqRoutingKey,  // routing key
		false,          // mandatory
		false,          // immediate
		amqp.Publishing{
			ContentType:  d.ContentType,
			Body:         d.Body,
			Headers:      headers,
			DeliveryMode: amqp.Persistent, // make it persistent
		})
	
	if err != nil {
		log.Printf("Failed to publish message to DLQ: %v", err)
		return err
	}
	
	// Acknowledge the original message after successfully sending to DLQ
	return d.Ack(false)
}

func (r *RabbitMQConsumer) Close() error {
	if r.channel != nil {
		r.channel.Close()
	}
	if r.connection != nil {
		r.connection.Close()
	}
	return nil
}