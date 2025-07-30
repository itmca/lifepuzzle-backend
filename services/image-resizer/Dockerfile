FROM golang:1.21-alpine AS builder

WORKDIR /app

# Copy go workspace files
COPY go.work go.work.sum* ./
COPY shared/go ./shared/go

# Copy service files
COPY services/image-resizer ./services/image-resizer

WORKDIR /app/services/image-resizer

RUN go mod download
RUN CGO_ENABLED=0 GOOS=linux go build -a -installsuffix nocgo -o main .

FROM alpine:latest

RUN apk --no-cache add ca-certificates

WORKDIR /root/

COPY --from=builder /app/services/image-resizer/main .

CMD ["./main"]