
# 📦 Sistema de Cadastro de Clientes com Integração AWS

![Java](https://img.shields.io/badge/Java-17-%23ED8B00?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.3-%236DB33F?logo=spring)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-13-%23336791?logo=postgresql)
![Podman](https://img.shields.io/badge/Podman-4+-%230DB7ED?logo=podman)
![AWS LocalStack](https://img.shields.io/badge/LocalStack-2.0+-%23FF9900?logo=amazonaws)

## 📋 Estrutura do Projeto
```bash
        projeto/
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

### Clone e Build
```bash
        git clone https://github.com/GabriellyZup/container.git
        cd container
        mvn clean package
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