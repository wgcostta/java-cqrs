# Spring Boot CQRS Demo para Microsserviços

Este projeto é uma implementação do padrão CQRS (Command Query Responsibility Segregation) usando Spring Boot, projetado especificamente para comunicação entre microsserviços. O CQRS separa as operações de escrita (commands) das operações de leitura (queries) para melhorar a escalabilidade, desempenho e manutenção do sistema.

## Tecnologias Utilizadas

- Java 17
- Spring Boot 3.2.3
- Spring Data JPA
- MySQL (produção)
- H2 Database (testes)
- Lombok
- JUnit 5 e Mockito
- Maven

## Arquitetura do Projeto

O projeto segue uma arquitetura CQRS pura, com comunicação direta entre serviços via API Gateway, sem depender de APIs REST para comunicação interna:

```
src/main/java/com/example/cqrsdemo/
├── CqrsDemoApplication.java
├── adapter/
│   └── rest/
│       └── ProductRestAdapter.java (opcional, ativado via configuração)
├── command/
│   ├── api/
│   │   ├── CreateProductCommand.java
│   │   ├── UpdateProductCommand.java
│   │   └── DeleteProductCommand.java
│   └── handler/
│       └── ProductCommandHandler.java
├── query/
│   ├── api/
│   │   ├── GetProductQuery.java
│   │   └── GetAllProductsQuery.java
│   └── handler/
│       └── ProductQueryHandler.java
├── domain/
│   ├── model/
│   │   └── Product.java
│   └── repository/
│       └── ProductRepository.java
├── gateway/
│   └── CqrsApiGateway.java (ponto de entrada principal para comunicação CQRS)
├── config/
│   └── CqrsConfig.java
└── event/
    ├── ProductCreatedEvent.java
    ├── ProductUpdatedEvent.java
    └── ProductDeletedEvent.java
```

## Diferencial deste Projeto

Ao contrário das implementações CQRS tradicionais que usam controladores REST para comunicação, este projeto:

1. Utiliza um `CqrsApiGateway` como único ponto de entrada para processamento de comandos e consultas
2. Implementa comunicação direta entre microsserviços através de chamadas de método Java
3. Separa completamente os mecanismos de comunicação (interno ou externo) da lógica de negócios
4. Oferece um adaptador REST opcional que pode ser ativado apenas para testes ou exposição externa

## Configuração

### Pré-requisitos

- JDK 17 ou superior
- Maven
- MySQL (para ambiente de produção)

### Banco de Dados

1. Configure o MySQL no arquivo `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/cqrs_demo
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```

2. Para testes, utiliza-se um banco H2 em memória configurado em `application-test.properties`:
```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=sa
spring.datasource.password=
spring.datasource.driver-class-name=org.h2.Driver
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
```

### Construção e Execução

1. Clone o repositório:
```bash
git clone https://github.com/seu-usuario/cqrs-demo.git
cd cqrs-demo
```

2. Construa o projeto:
```bash
mvn clean install
```

3. Execute a aplicação:
```bash
mvn spring-boot:run
```

## Usando o Sistema

### Comunicação Interna entre Microsserviços

Para comunicação interna entre microsserviços, injete o `CqrsApiGateway` como dependência:

```java
@Service
public class OutroMicroservico {

    private final CqrsApiGateway apiGateway;

    @Autowired
    public OutroMicroservico(CqrsApiGateway apiGateway) {
        this.apiGateway = apiGateway;
    }

    // Exemplo de envio de comando
    public void criarProduto(String nome, String descricao, double preco, int quantidade) {
        CreateProductCommand command = new CreateProductCommand();
        command.setName(nome);
        command.setDescription(descricao);
        command.setPrice(preco);
        command.setQuantity(quantidade);
        
        String productId = apiGateway.sendCommand(command);
        // Faça algo com o ID do produto retornado
    }

    // Exemplo de envio de consulta
    public Product obterProduto(String id) {
        GetProductQuery query = new GetProductQuery(id);
        return apiGateway.sendQuery(query);
    }
}
```

### Exposição REST Externa (Opcional)

Se precisar expor a API para sistemas externos, ative o adaptador REST no `application.properties`:

```properties
# Ativar a API REST
cqrs.rest.enabled=true
```

Quando ativado, o adaptador REST expõe os seguintes endpoints:

#### Comandos

- **Criar Produto**: `POST /api/products`
  ```json
  {
    "name": "Produto Exemplo",
    "description": "Descrição do produto",
    "price": 1999.99,
    "quantity": 10
  }
  ```

- **Atualizar Produto**: `PUT /api/products/{id}`
  ```json
  {
    "name": "Produto Atualizado",
    "description": "Descrição atualizada",
    "price": 1899.99,
    "quantity": 15
  }
  ```

- **Excluir Produto**: `DELETE /api/products/{id}`

#### Consultas

- **Obter Produto**: `GET /api/products/{id}`
- **Listar Produtos**: `GET /api/products`

## Executando os Testes

O projeto inclui três níveis de testes:

### 1. Testes Unitários

Testam classes individuais com suas dependências mockadas:

```bash
mvn test -Dtest=ProductCommandHandlerTest,ProductQueryHandlerTest
```

### 2. Testes do Gateway CQRS

Testam o funcionamento do gateway de comunicação CQRS:

```bash
mvn test -Dtest=CqrsApiGatewayTest
```

### 3. Testes de Integração

Testam o fluxo completo de comandos e consultas com banco de dados em memória:

```bash
mvn test -Dtest=CqrsIntegrationTest -Dspring.profiles.active=test
```

### 4. Testes do Adaptador REST (Opcional)

Se o adaptador REST estiver ativado:

```bash
mvn test -Dtest=ProductRestAdapterTest
```

## Implementando Event Sourcing (Opcional)

O projeto atual utiliza persistência direta no banco de dados, mas pode ser estendido para usar Event Sourcing:

1. Adicione uma tabela `event_store` para armazenar eventos
2. Modifique o `ProductCommandHandler` para persistir eventos no `event_store`
3. Implemente projeções que consomem eventos para construir o estado atual

## Considerações para Produção

1. **Segregação de Dados**: Em um sistema CQRS mais avançado, considere separar os bancos de dados de leitura e escrita
2. **Processamento Assíncrono**: Adicione uma fila de mensagens (RabbitMQ, Kafka) para processamento assíncrono de comandos
3. **Consistência Eventual**: Este modelo usa consistência eventual entre o lado de comando e consulta
4. **Monitoramento**: Adicione métricas e logs para monitorar processamento de comandos e consultas

## Contribuição

Contribuições são bem-vindas! Sinta-se à vontade para abrir issues ou enviar pull requests.

## Licença

Este projeto está licenciado sob a licença MIT - veja o arquivo LICENSE para detalhes.