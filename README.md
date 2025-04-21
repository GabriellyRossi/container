
# 🚀 Projeto de Estudo: API de Cadastro de Clientes com Integração AWS

![Java](https://img.shields.io/badge/Java-17-%23ED8B00?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.3-%236DB33F?logo=spring)
![Docker](https://img.shields.io/badge/Docker-24.+-%230DB7ED?logo=docker)
![Podman](https://img.shields.io/badge/Podman-4+-%230DB7ED?logo=podman)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-13-%23336791?logo=postgresql)
![GitHub Actions](https://img.shields.io/badge/GitHub_Actions-%232088FF?logo=githubactions)
![LocalStack](https://img.shields.io/badge/LocalStack-2.+-%23FF9900?logo=amazonaws)

**Objetivo do Projeto**:  
Desenvolver uma API de estudos e testes práticos para cadastro de clientes, explorando técnicas modernas de desenvolvimento e infraestrutura.

## 🧠 Estudos Realizados
- **Containerização**: Dockerfile multi-stage e otimização de imagens
- **Orquestração**: Podman Compose com health checks
- **CI/CD**: Pipeline automatizado no GitHub Actions
- **Cloud Local**: Integração com S3 e EC2 via LocalStack
- **Boas Práticas**: Variáveis de ambiente, segurança básica, logging
- **Documentação**: README como guia replicável

## 🌟 Funcionalidades-Chave
```http
GET /clientes/{id}       # Busca cliente por ID
POST /aws/s3/upload      # Upload de arquivos para bucket S3
GET /aws/ec2/instances   # Lista instâncias EC2 (LocalStack)
```

## 🛠️ Tecnologias Utilizadas
Área	Tecnologias
Backend	Java 17, Spring Boot 3, JPA/Hibernate, Lombok
Banco de Dados	PostgreSQL 13, Flyway (implícito no ddl-auto)
Infra	Docker, Podman, GitHub Actions
AWS Local	LocalStack (S3, EC2), AWS SDK v2
Ferramentas	Maven, Dotenv, SpringDoc OpenAPI
📌 Por que este projeto é relevante?
É um case completo que demonstra habilidades em:

🔄 Deploy Containerizado: Do Dockerfile ao Podman Compose

☁️ Integração Cloud: Simulação realista de serviços AWS

⚙️ Automação: Pipeline CI/CD profissional

📊 Gestão de Estado: Persistência de dados com PostgreSQL

🔍 Debugging: Configuração de health checks e logs


## 📋 Estrutura do Projeto
```bash
        container/
        ├── src/
        │   ├── main/java/com/study/container/
        │   │   ├── config/       # Configurações AWS
        │   │   ├── controller/   # Controladores REST
        │   │   ├── entity/       # Entidades JPA
        │   │   ├── repository/   # Repositórios Spring Data
        │   │   ├── service/      # Lógica de negócio
        │   │   └── Application.java
        ├── .github/workflows/
        │   └── main.yml          # Pipeline CI/CD
        ├── podman-compose.yml    # Orquestração de containers
        ├── Dockerfile            # Build da imagem
        ├── .env                  # Variáveis de ambiente
        └── README.md
```

## 🚀 Funcionalidades Principais
- Cadastro de Clientes (CRUD Completo)
- Integração com S3 (Upload de arquivos)
- Gestão de Instâncias EC2 (Listagem via LocalStack)
- Health Check Automático do PostgreSQL

## 🛠️ Configuração Inicial

### Pré-requisitos
- ☕ JDK 17
- 🐳 Podman 4+
- 📦 Maven 3.8+
- ⚡ LocalStack (para testes AWS)

▶️ Como Executar
bash
# Clone o repositório
git clone https://github.com/GabriellyZup/container.git

# Inicie os containers
podman-compose up --build

# Acesse a API
curl http://localhost:8080/hello

### Clone e Build
```bash
        git clone https://github.com/GabriellyZup/container.git
        cd container
        mvn clean package
```
## 🐋 Dockerfile - Construção da Imagem
```dockerfile
        FROM maven:3.8.6-openjdk-17 AS build
        WORKDIR /app
        COPY pom.xml .
        COPY src ./src
        RUN mvn clean package -DskipTests

        FROM openjdk:17-jdk-slim
        WORKDIR /app
        COPY --from=build /app/target/*.jar app.jar
        EXPOSE 8080
        ENTRYPOINT ["java", "-jar", "app.jar"]
```

**Explicação do Dockerfile:**
- **Multi-stage build:**
- Primeiro estágio (build): Compila o projeto com Maven
- Segundo estágio: Cria imagem final leve com o JAR
- **Otimizações:**
- Usa JDK apenas na fase de compilação
- Mantém imagem final com ~150MB usando jdk-slim
- **Boas Práticas:**
- `-DskipTests`: Ignora testes durante o build da imagem
- `EXPOSE 8080`: Expõe porta padrão do Spring Boot
- `WORKDIR`: Define diretório de trabalho organizado

**Para construir a imagem localmente:**
```bash
        podman build -t cliente-api .
```


## 🐳 Podman Compose - Arquivo de Configuração
```yaml
        version: '3.8'

        services:
          app:
            build: .
            image: impostocalc:latest
            container_name: cliente-app
            env_file: .env
            ports:
              - "8080:8080"
            depends_on:
              db:
                condition: service_healthy
            networks:
              - cliente-network

          db:
            image: postgres:13
            container_name: postgres-db
            env_file: .env
            ports:
              - "5432:5432"
            volumes:
              - postgres_data:/var/lib/postgresql/data
            healthcheck:
              test: ["CMD-SHELL", "pg_isready -U postgres -d cadastro_clientes"]
              interval: 5s
              timeout: 5s
              retries: 5
            networks:
              - cliente-network

        volumes:
          postgres_data:

        networks:
          cliente-network:
            driver: bridge
```

## Comandos Essenciais
```bash
 # Iniciar serviços
        podman-compose up --build

# Ver logs do banco
        podman logs postgres-db

# Parar e limpar
        podman-compose down -v
```

## 🔐 Variáveis de Ambiente (.env)

```init
# PostgreSQL
        POSTGRES_DB=cadastro_clientes
        POSTGRES_USER=postgres
        POSTGRES_PASSWORD=postgres

# Spring
        SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/cadastro_clientes
        SPRING_DATASOURCE_USERNAME=postgres
        SPRING_DATASOURCE_PASSWORD=postgres
```

## 🌐 Endpoints Principais
        | Método | Endpoint                  | Descrição                  |
        |--------|---------------------------|----------------------------|
        | GET    | /hello                    | Health Check da API        |
        | POST   | /clientes                 | Cria novo cliente          |
        | GET    | /clientes/{id}            | Busca cliente por ID       |
        | DELETE | /clientes/{id}            | Exclui cliente             |
        | POST   | /aws/s3/buckets/{nome}    | Cria bucket S3             |
        | POST   | /aws/s3/upload            | Upload de arquivo          |
        | GET    | /aws/ec2/instances        | Lista instâncias EC2       |

## 🔧 Troubleshooting Comum

### Erro ao Conectar no PostgreSQL
Verifique healthcheck do container:
```bash
        podman inspect postgres-db | jq '.[0].State.Health'
```

Confira mapeamento de portas:
```bash
        podman port postgres-db
```

### Testes Falhando no Maven
```bash
        # Executar build ignorando testes
        mvn clean install -DskipTests

# Rodar testes isoladamente
        mvn test -Dtest=ClienteServiceTest
```

## 🛠️ Estrutura de Classes Principais

### Entidade JPA - Cliente
```java
        @Entity
        @Table(name = "clientes")
        public class Cliente {
            @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
            private Long id;
            
            @Column(nullable = false)
            private String nome;
            
            @Column(unique = true)
            private String email;
        }
```

### Controlador AWS
```java
        @RestController
        @RequestMapping("/aws")
        public class AwsController {
            
            private final S3Service s3Service;
            
            @PostMapping("/s3/buckets/{bucketName}")
            public ResponseEntity<Void> criarBucket(@PathVariable String bucketName) {
                s3Service.criarBucket(bucketName);
                return ResponseEntity.ok().build();
            }
        }
```

## ⚙️ CI/CD Pipeline (.github/workflows/main.yml)
```yaml
        name: Build and Deploy

        on: [push]

        jobs:
          build:
            runs-on: ubuntu-latest
            steps:
              - uses: actions/checkout@v4
              - uses: actions/setup-java@v3
                with: { java-version: '17' }
              - run: mvn clean package
              - uses: docker/build-push-action@v5
                with: 
                  context: .
                  tags: ghcr.io/gabriellyzup/container:latest
                  push: true
```

## ☁️ Testes com LocalStack

### Configuração AWS CLI
```bash
        aws configure set aws_access_key_id test
        aws configure set aws_secret_access_key test
        aws configure set region us-east-1
```

### Comandos Úteis
```bash
# Listar buckets S3
        aws --endpoint-url=http://localhost:4566 s3 ls

# Listar instâncias EC2
        aws --endpoint-url=http://localhost:4566 ec2 describe-instances
```

## 📚 Recursos Adicionais
- [Documentação Podman](https://podman.io/getting-started/)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [LocalStack Docs](https://docs.localstack.cloud/)