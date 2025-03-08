# Start Jobs - Backend

## Descrição
O **Start Jobs** é um sistema backend desenvolvido em **Spring Boot** para gerenciar vagas de emprego, candidaturas, usuários, dicas e muito mais. Este projeto foi projetado para ser robusto, escalável e seguro, utilizando as melhores práticas e tecnologias modernas.

---

## Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.4.1**
  - Spring Data JPA
  - Spring Security
  - Spring DevTools
- **MySQL 8.0** (Banco de Dados Relacional)
- **Hibernate ORM** (Mapeamento Objeto-Relacional)
- **JWT (JSON Web Tokens)** para autenticação
- **HikariCP** para conexões de banco de dados
- **Maven** (Gerenciador de Dependências)

---

## Funcionalidades

### Usuário
- Cadastro de novos usuários com criptografia de senha.
- Login com autenticação JWT.
- Recuperação de usuários por username.

### Vagas
- Cadastro de vagas de emprego.
- Consulta de vagas cadastradas.

### Candidaturas
- Registro de candidaturas a vagas de emprego.
- Atualização do status de uma candidatura.
- Consulta de candidaturas por usuário.

### Dicas
- Cadastro de dicas relacionadas a carreira e vagas.
- Consulta de dicas.

---

## Como Configurar e Executar o Projeto

### 1. Clonar o Repositório
```bash
$ git clone <URL_DO_REPOSITORIO>
$ cd start-jobs
```

### 2. Configurar o Banco de Dados
- Certifique-se de que o **MySQL** esteja instalado e em execução.
- Crie um banco de dados chamado `startjobs`:
  ```sql
  CREATE DATABASE startjobs;
  ```
- Configure as credenciais do banco de dados no arquivo `src/main/resources/application.properties`:
  ```properties
  spring.datasource.url=jdbc:mysql://localhost:3306/startjobs
  spring.datasource.username=SEU_USUARIO
  spring.datasource.password=SUA_SENHA
  spring.jpa.hibernate.ddl-auto=update
  ```

### 3. Instalar Dependências e Rodar o Projeto
Certifique-se de que o **Maven** está instalado e execute os seguintes comandos:

```bash
# Instalar dependências
$ mvn clean install

# Rodar o projeto
$ mvn spring-boot:run
```

### 4. Endpoints Disponíveis
A aplicação rodará em `http://localhost:8080`. Aqui estão os principais endpoints:

#### Usuário
- **POST** `/api/usuarios` - Criar novo usuário
- **POST** `/api/usuarios/login` - Login e geração de token JWT

#### Vagas
- **POST** `/api/vagas` - Criar nova vaga
- **GET** `/api/vagas` - Listar todas as vagas

#### Candidaturas
- **POST** `/api/candidaturas` - Registrar candidatura
- **GET** `/api/candidaturas/usuario/{idUsuario}` - Consultar candidaturas de um usuário

#### Dicas
- **POST** `/api/dicas` - Criar nova dica
- **GET** `/api/dicas` - Listar todas as dicas

### 5. Testando com Postman
- Use o Postman ou outra ferramenta similar para testar os endpoints.
- Exemplo de **requisição POST** para criar um usuário:
  ```json
  POST /api/usuarios
  {
      "nome": "Juliana Maria",
      "email": "juliana@example.com",
      "telefone": "123456789",
      "username": "julianamaria",
      "senha": "senha123",
      "imagem": "https://loremflickr.com/200/200?random=1"
  }
  ```

---

## Estrutura do Projeto

```text
src/main/java
├── com.example.start_jobs
    ├── config         # Configurações (ex.: Spring Security)
    ├── controller    # Endpoints da API
    ├── dto           # Objetos de Transferência de Dados
    ├── entity        # Entidades do Banco de Dados
    ├── exception     # Manipulação de exceções
    ├── repository    # Interfaces de Repositórios (Spring Data JPA)
    ├── service       # Regras de Negócio
    └── util          # Utilitários (ex.: JWT)
```

---

## Dependências Maven
As dependências principais do projeto estão listadas no arquivo **`pom.xml`**. Aqui estão algumas delas:

```xml
<dependencies>
    <!-- Spring Boot Starter Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- Spring Boot Starter Data JPA -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>

    <!-- MySQL Connector -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
    </dependency>

    <!-- Spring Boot Starter Security -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>

    <!-- JWT -->
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-api</artifactId>
        <version>0.11.5</version>
    </dependency>

    <!-- Lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

---

## Contribuição
- Sinta-se à vontade para criar issues ou enviar pull requests.
- Para contribuições maiores, é recomendado abrir uma discussão antes para alinhar as ideias.

---

## Autores
* **Aesley Soares Nobre**
* **Alexandre Brito**
* **Igor Sobral**
* **Juliana Maria de Sousa Mesquita**
---

## Licença
Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.
