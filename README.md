# Containerização de Aplicação Spring Boot

## Pré-requisitos
- Podman/Docker
- JDK 17
- Maven

## Passo a Passo
1. Clone o repositório:
   ```bash  
   git clone https://github.com/seu-usuario/nome-repositorio.git  
Construa o .jar:

bash
Copy
mvn clean package  
Construa a imagem Docker:

bash
Copy
podman build -t spring-container .  
Execute o container:

bash
Copy
podman run --rm -p 8080:8080 spring-container  
Acesse o endpoint:

Copy
http://localhost:8080/hello 

