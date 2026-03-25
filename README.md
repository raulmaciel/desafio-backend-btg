# 🚀 BTG Pactual Backend Challenge - Order Microservice

![Java](https://img.shields.io/badge/Java-21-blue.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.0-brightgreen.svg)
![RabbitMQ](https://img.shields.io/badge/RabbitMQ-Message%20Broker-orange.svg)
![MongoDB](https://img.shields.io/badge/MongoDB-NoSQL-success.svg)

Este repositório contém a solução para o **Desafio Backend do BTG Pactual**. O microsserviço processa pedidos de forma assíncrona via RabbitMQ, persiste em MongoDB e disponibiliza agregações financeiras via API REST.

---

## 🧠 Arquitetura e Funcionalidades

1.  **Mensageria (RabbitMQ):** Consumo assíncrono da fila `btg-pactual-order-created`.
2.  **Persistência (MongoDB):** Armazenamento de pedidos e itens com foco em alta performance de escrita.
3.  **Agregações (Summary):** Uso de **MongoDB Aggregation Framework** para calcular o valor total e a contagem de pedidos por cliente em tempo real.
4.  **API REST**: Endpoints para resumo do cliente, detalhes de pedido e listagem paginada.

---

## 🛠️ Tecnologias Utilizadas

- **Java 21** & **Spring Boot 3.4.0**
- **Spring Data MongoDB** & **Spring AMQP**
- **RabbitMQ** (Message Broker)
- **MongoDB** (NoSQL Database)
- **Docker** & **Docker Compose**
- **MockitoBean** (Spring Boot 3.4.0+ testing pattern)

---

## ⚙️ Como Executar o Projeto (Via Docker Compose)

A forma recomendada de rodar a aplicação é utilizando o **Docker Compose**, que já orquestra a aplicação junto com o Banco de Dados e o Broker.

```bash
# Na raiz do projeto, execute:
docker-compose up --build
```

- **API**: `http://localhost:8080`
- **RabbitMQ Management**: `http://localhost:15672` (guest/guest)
- **MongoDB**: `mongodb://localhost:27017`

---

## 📡 API Endpoints

### 1. Resumo do Cliente
Retorna a quantidade total de pedidos e o valor somado gasto pelo cliente.
- `GET /customers/{customerId}/summary`

### 2. Listagem de Pedidos
Retorna os pedidos de forma paginada.
- `GET /customers/{customerId}/orders?page=0&pageSize=10`

### 3. Detalhes do Pedido
Retorna os dados básicos de um pedido específico.
- `GET /orders/{orderId}`

---

## ✉️ Como Testar

### Passo 1: Enviar Mensagem (RabbitMQ)
Publique um JSON na fila `btg-pactual-order-created` via Management Console:

```json
{
  "codigoPedido": 1001,
  "codigoCliente": 1,
  "itens": [
    { "produto": "Teclado", "quantidade": 1, "preco": 150.00 },
    { "produto": "Mouse", "quantidade": 2, "preco": 75.00 }
  ]
}
```

### Passo 2: Validar API
Acesse o resumo para ver o cálculo automático:
`GET http://localhost:8080/customers/1/summary`

---

## 🧪 Testes Automatizados
O projeto conta com testes unitários, de controller e de integração.
```bash
./mvnw test
```

Desenvolvido para resolução do **Desafio BTG Pactual.**
