# 🚀 BTG Pactual Backend Challenge

![Java](https://img.shields.io/badge/Java-23-blue.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.3-brightgreen.svg)
![RabbitMQ](https://img.shields.io/badge/RabbitMQ-Message%20Broker-orange.svg)
![MongoDB](https://img.shields.io/badge/MongoDB-NoSQL-success.svg)

Este repositório contém a solução para o **Desafio Backend do BTG Pactual**. O objetivo do projeto é construir um microsserviço eficiente e assíncrono capaz de processar milhares de pedidos de clientes e disponibilizar informações sumarizadas através de uma API Restful.

## 🧠 Arquitetura e Fluxo

1. **Mensageria (RabbitMQ):** A aplicação escuta ativamente a fila `btg-pactual-order-created`. Todas as mensagens (pedidos) publicadas nessa fila em formato JSON são consumidas via listeners.
2. **Processamento Assíncrono:** Ao ler a mensagem, o microsserviço traduz o JSON utilizando `JacksonJsonMessageConverter` de volta para entidades de domínio Java.
3. **Persistência (MongoDB):** A escolha de um banco NoSQL foi pautada na velocidade de escrita massiva e no formato flexível em que os dados de compras costumam trafegar (aninhados, arrays de subdocumentos). O pedido é consolidado e armazenado de forma não bloqueante.
4. **Respostas (API Rest):** A camada de Controllers disponibiliza dados calculados em tempo real do Total Financeiro, a Contagem e o Extrato dos Pedidos por Cliente.

---

## 🛠️ Tecnologias Utilizadas

- **Java 23**
- **Spring Boot 4.x** (Web, Data MongoDB, AMQP)
- **RabbitMQ** (Message Broker)
- **MongoDB** (Banco de Dados NoSQL)
- **Maven** (Gerenciador de Dependências)

---

## ⚙️ Como Executar o Projeto Localmente

### Pré-requisitos
Para rodar este projeto, você precisará ter instalado em sua máquina:
- [Java 23+](https://www.oracle.com/java/technologies/javase/jdk23-archive-downloads.html)
- [Docker](https://www.docker.com/) (Para inicializar o RabbitMQ e o MongoDB rapidamente sem instalação local engessada)

### 1. Subir a Infraestrutura (RabbitMQ & MongoDB)
Dentro do seu terminal de preferência, rode os comandos Docker para subir o Mongo e o Rabbit com as mesmas credenciais padronizadas neste projeto (user: `admin` | pass: `admin`):

```bash
# Subir RabbitMQ via Container
docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management

# Subir MongoDB via Container
docker run -d --name mongodb -p 27017:27017 -e MONGO_INITDB_ROOT_USERNAME=admin -e MONGO_INITDB_ROOT_PASSWORD=admin mongo:latest
```

### 2. Rodar a Aplicação Spring Boot
Abra um terminal na pasta do projeto e inicie a execução do Spring via Maven Wrapper:
```bash
./mvnw spring-boot:run
```
*(Caso esteja no Windows, use `mvnw.cmd spring-boot:run`)*

---

## 📡 API Endpoints (Em Desenvolvimento)

**Base URL:** `http://localhost:8080`

1. **Visão Global dos Pedidos por Cliente:**
   * `GET /customers/{customerId}/orders`
   * **Retorno Esperado:** Lista completa de pedidos que aquele cliente já fez, sua contagem volumétrica total, e a soma financeira exata de todas as suas compras reunidas. *(Funcionalidade aguardando implementação final).*

---

## ✉️ Payload Padrão do RabbitMQ (Entrada)
Estrutura exata de mensagem JSON que a aplicação está aguardando você publicar na fila `btg-pactual-order-created`:

```json
{
  "codigoPedido": 1001,
  "codigoCliente": 1,
  "itens": [
    {
      "produto": "lápis",
      "quantidade": 100,
      "preco": 1.10
    },
    {
      "produto": "caderno",
      "quantidade": 10,
      "preco": 1.00
    }
  ]
}
```

---

Desenvolvido para resolução do **Desafio BTG Pactual.**
