# Estágio de build
FROM ubuntu:latest AS build

# Atualiza o sistema e instala o JDK e Maven
RUN apt-get update && apt-get install -y openjdk-17-jdk maven

# Define o diretório de trabalho
WORKDIR /app

# Copia os arquivos do projeto para dentro do container
COPY . .

# Compila o projeto e gera o arquivo .jar
RUN mvn clean install

# Estágio de execução
FROM openjdk:17-jdk-slim

# Define a porta que será exposta
EXPOSE 8080

# Copia o .jar gerado no estágio de build
COPY --from=build /app/target/start-jobs-0.0.1-SNAPSHOT.jar app.jar

# Define o comando de entrada
ENTRYPOINT ["java", "-jar", "app.jar"]
