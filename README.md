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

## ✉️ Guia de Teste Passo a Passo

Para validar se tudo está funcionando (Injeção de dados -> Processamento -> Consulta), siga este fluxo:

### 1. Criar Dados (Via RabbitMQ)
Como a aplicação é orientada a eventos, precisamos "alimentar" o sistema simulando a criação de um pedido.
1. Acesse o painel do Rabbit: [http://localhost:15672](http://localhost:15672) (guest/guest).
2. Vá em **Queues** e selecione a fila `btg-pactual-order-created`.
3. Na seção **Publish Message**, envie o JSON abaixo:

```json
{
  "codigoPedido": 1001,
  "codigoCliente": 1,
  "itens": [
    { "produto": "Teclado", "quantidade": 1, "preco": 150.00 },
    { "produto": "Mouse Gamer", "quantidade": 2, "preco": 70.00 }
  ]
}
```
*O que acontece?* A aplicação consome a mensagem, calcula o total (R$ 290,00) e salva no MongoDB.

### 2. Validar o Resumo (Summary)
Este é o requisito principal do desafio: saber quanto o cliente já gastou no total.
- **URL**: `GET http://localhost:8080/customers/1/summary`
- **Utilidade**: Fornece uma visão rápida de fidelidade (total de pedidos e ticket médio acumulado).

### 3. Explorar os Pedidos (Pagination)
Veja a lista detalhada de compras do cliente.
- **URL**: `GET http://localhost:8080/customers/1/orders?page=0&pageSize=10`
- **Utilidade**: Para carregar o histórico de compras em uma interface de usuário.

### 4. Consultar Pedido Único
- **URL**: `GET http://localhost:8080/orders/1001`
- **Utilidade**: Útil para exibir detalhes específicos de um ticket de compra.

---

## 🧪 Testes Automatizados
O projeto conta com uma pirâmide de testes completa:
- **Unitários**: Regras de negócio e cálculos de totais.
- **Controllers**: Validação dos contratos da API e paginação.
- **Integração**: Fluxo completo simulando banco e mensageria (usando `@MockitoBean`).

Para rodar os testes: `./mvnw test`

Desenvolvido para resolução do **Desafio BTG Pactual.**
