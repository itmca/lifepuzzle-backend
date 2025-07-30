package messaging

import (
	"encoding/json"
	"fmt"
	"log"

	amqp "github.com/rabbitmq/amqp091-go"
)

type Message struct {
	ID  int `json:"id"`
	Ack func() error
	Nack func() error
}

type RabbitMQConsumer struct {
	connection *amqp.Connection
	channel    *amqp.Channel
	queue      amqp.Queue
}

func NewRabbitMQConsumer(url, queueName string) (*RabbitMQConsumer, error) {
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

	return &RabbitMQConsumer{
		connection: conn,
		channel:    ch,
		queue:      q,
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

			msg := Message{
				ID: msgData.ID,
				Ack: func() error {
					return d.Ack(false)
				},
				Nack: func() error {
					return d.Nack(false, true) // requeue
				},
			}

			messageChan <- msg
		}
	}()

	return messageChan, nil
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