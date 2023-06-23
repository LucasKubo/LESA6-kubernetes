# Define a imagem base como OpenJDK 17
FROM openjdk:17

# Define o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copia o arquivo JAR da aplicação para o contêiner
COPY target/MeuRemedio-0.0.1-SNAPSHOT.jar app.jar

# Expõe a porta que a aplicação está usando
EXPOSE 8080

# Define o nome da imagem
LABEL app.name="meu-remedio"

# Define o comando para executar a aplicação quando o contêiner iniciar
CMD ["java", "-jar", "app.jar"]