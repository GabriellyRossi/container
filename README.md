
ainda em evolução, não repara a bagunça




# Containerização e CI/CD de Aplicação Spring Boot





## O que estamos fazendo?
1. **Containerização**: Empacotar a aplicação Java em uma imagem Docker para rodar em qualquer ambiente.
2. **CI/CD Pipeline**: Automatizar testes, construção da imagem e publicação no GitHub Container Registry.

## Pré-requisitos
- ☕ JDK 17
- 🐳 Podman/Docker
- ⚙️ Maven
- 🐙 Conta no GitHub

## Receita Passo a Passo

### 1. Containerização da Aplicação 🐋

#### 1.1 Clone o projeto
```bash
git clone https://github.com/GabriellyZup/container.git
cd container
1.2 Construa o JAR
bash
Copy
mvn clean package
1.3 Construa a imagem Docker
bash
Copy
podman build -t spring-container .
1.4 Execute o container localmente
bash
Copy
podman run --rm -p 8080:8080 spring-container
1.5 Teste o endpoint
Copy
http://localhost:8080/hello
2. CI/CD Pipeline com GitHub Actions 🚀
2.1 Estrutura do workflow (.github/workflows/main.yml)
yaml


Copy
name: Build and Push Docker Image

on:
  push:
    branches:
      - main

jobs:
  build-and-push:
    runs-on: ubuntu-latest

    permissions:
      contents: read
      packages: write

    steps:
      # Passo 1: Baixa o código do repositório
      - name: Checkout code
        uses: actions/checkout@v4

      # Passo 2: Configura o JDK 17
      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'

      # Passo 3: Compila o projeto com Maven
      - name: Build with Maven
        run: mvn clean package

      # Passo 4: Faz login no GitHub Container Registry
      - name: Log in to GitHub Container Registry
        uses: docker/login-action@v3
        with:
          registry: ghcr.io
          username: ${{ github.actor }}
          password: ${{ secrets.GITHUB_TOKEN }}

      # Passo 5: Constrói e envia a imagem Docker
      - name: Build and push Docker image
        uses: docker/build-push-action@v5
        with:
          context: .
          push: true
          tags: ghcr.io/${{ format('{0}/{1}', 'gabriellyzup', 'container') }}:latest


2.2 O que esse pipeline faz?
✅ Roda automaticamente ao fazer push no main

✅ Testa e constrói o JAR

✅ Publica a imagem Docker em ghcr.io/gabriellyzup/container

Link do GHCR:
https://ghcr.io/gabriellyzup/container

2.3 Como usar a imagem publicada?
bash
Copy

podman run --rm -p 8080:8080 ghcr.io/gabriellyzup/container:latest
Fluxo de Desenvolvimento Recomendado
Desenvolva localmente

Teste com Podman/Docker

Faça push para o GitHub

O pipeline publica a nova imagem automaticamente

Use a imagem em Kubernetes/AWS/etc. 🎉

💡 Dica: Para projetos novos, basta copiar o Dockerfile e o workflow.yml!kflow.yml




Guia para Criação e Configuração do Podman Compose
Este guia fornece um passo a passo detalhado para criar e configurar um arquivo podman-compose.yml para integração com projetos que utilizam banco de dados PostgreSQL e uma aplicação Spring Boot. O objetivo é criar uma receita reutilizável para futuros projetos.

1. Pré-requisitos
Antes de começar, certifique-se de que você possui os seguintes itens instalados e configurados:

Podman: Ferramenta para gerenciar containers.
Guia de instalação do Podman
Podman Compose: Ferramenta para orquestrar múltiplos containers com Podman.
Instale com o comando:


pip install podman-compose
Projeto Spring Boot: Um projeto configurado com as dependências necessárias para conexão com o banco de dados PostgreSQL.
Banco de Dados PostgreSQL: Certifique-se de que o banco de dados está configurado corretamente no projeto.
2. Estrutura do Projeto
Certifique-se de que o projeto possui a seguinte estrutura básica:

projeto/ ├── src/ ├── .env ├── application.properties ├── podman-compose.yml └── README.md
.env: Contém as variáveis de ambiente para o banco de dados.
application.properties: Configurações do Spring Boot.
podman-compose.yml: Arquivo de configuração do Podman Compose.
3. Criando o Arquivo .env
Crie um arquivo .env na raiz do projeto para armazenar as variáveis de ambiente do banco de dados. Isso facilita a reutilização e evita expor informações sensíveis diretamente no código.

Exemplo de .env


POSTGRES_DB_URL=jdbc:postgresql://db:5432/cadastro_clientes
POSTGRES_DB_USERNAME=postgres
POSTGRES_DB_PASSWORD=postgres
4. Configurando o application.properties
No arquivo application.properties do Spring Boot, configure as propriedades para conexão com o banco de dados utilizando as variáveis de ambiente.

Exemplo de application.properties


spring.datasource.url=${POSTGRES_DB_URL}
spring.datasource.username=${POSTGRES_DB_USERNAME}
spring.datasource.password=${POSTGRES_DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
5. Criando o Arquivo podman-compose.yml
Crie o arquivo podman-compose.yml na raiz do projeto. Este arquivo define os serviços necessários para o projeto, como a aplicação e o banco de dados.

Exemplo de podman-compose.yml


Explicação do compose.yaml (Para o README):
yaml
Copy
version: "3.9"  

services:  
  app:  
    image: ghcr.io/gabriellyzup/container:latest  # Imagem do seu app  
    ports:  
      - "8080:8080"  # Expõe a porta da aplicação  
    environment:  
      SPRING_DATASOURCE_URL: jdbc:postgresql://db:5432/cadastro_clientes  # URL do banco  
      SPRING_DATASOURCE_USERNAME: postgres  # Usuário do banco  
      SPRING_DATASOURCE_PASSWORD: postgres  # Senha do banco  
    depends_on:  
      - db  # Garante que o banco suba primeiro  

  db:  
    image: postgres:latest  # Imagem oficial do PostgreSQL  
    environment:  
      POSTGRES_USER: postgres  # Usuário padrão  
      POSTGRES_PASSWORD: postgres  # Senha  
      POSTGRES_DB: cadastro_clientes  # Nome do banco  
    volumes:  
      - postgres-data:/var/lib/postgresql/data  # Persistência dos dados  

volumes:  
  postgres-data:  # Volume para dados do PostgreSQL
  
  
Explicação do Arquivo
version: Define a versão do Compose. Aqui usamos a versão 3.9.
services: Define os serviços que serão executados.
app: Serviço da aplicação.
image: Imagem Docker da aplicação.
ports: Mapeia a porta 8080 do container para a porta 8080 do host.
environment: Define as variáveis de ambiente para a aplicação.
depends_on: Define que o serviço app depende do serviço db.
command: Adiciona a flag --tls-verify=false para ignorar a verificação de certificados TLS.
db: Serviço do banco de dados PostgreSQL.
image: Imagem Docker do PostgreSQL.
environment: Define as variáveis de ambiente para o banco de dados.
volumes: Monta um volume para persistir os dados do banco.
volumes: Define o volume nomeado postgres-data para persistência de dados.
6. Executando o Podman Compose
Passo 1: Navegue até o Diretório do Projeto
No terminal, navegue até o diretório onde o arquivo podman-compose.yml está localizado:



cd /caminho/para/seu/projeto
Passo 2: Suba os Serviços
Execute o comando abaixo para iniciar os serviços definidos no podman-compose.yml:



podman-compose up --tls-verify=false
Passo 3: Verifique os Logs
Certifique-se de que os serviços estão funcionando corretamente verificando os logs:



podman logs <nome-do-container>
Passo 4: Acesse a Aplicação
Abra o navegador e acesse a aplicação na URL:

http://localhost:8080
7. Dicas e Boas Práticas
Preservação de Dados: O volume postgres-data garante que os dados do banco sejam preservados entre reinicializações.
Variáveis de Ambiente: Use o arquivo .env para gerenciar variáveis sensíveis e facilitar a configuração.
TLS Verify: A flag --tls-verify=false é útil para ignorar a verificação de certificados TLS ao baixar imagens.
Limpeza de Containers: Para remover os containers e volumes, use:


podman-compose down
8. Solução de Problemas
Erro de Conexão com o Banco de Dados:

Verifique se as variáveis de ambiente estão corretas no arquivo .env.
Certifique-se de que o serviço db está em execução.
Porta em Uso:

Certifique-se de que a porta 8080 não está sendo usada por outro processo.

_______________

# Testando Integração com LocalStack (EC2 e S3)

Este guia fornece um passo a passo detalhado para testar a integração com LocalStack utilizando os serviços EC2 e S3. Certifique-se de que você já tenha o LocalStack e o AWS CLI instalados em sua máquina. O ambiente utilizado é Windows (PowerShell), IntelliJ IDEA, Java 17 e testes realizados via Postman.

---

## Pré-requisitos

1. **LocalStack** instalado e em execução.
2. **AWS CLI** configurado.
3. **Java 17** instalado.
4. **IntelliJ IDEA** configurado com o projeto.
5. **Postman** instalado para realizar os testes.

---

## Passo 1: Iniciar o LocalStack

1. Abra o terminal PowerShell.
2. Inicie o LocalStack com o comando:
   ```powershell
   localstack start
Verifique se o LocalStack está em execução:


localstack status
Passo 2: Configurar o AWS CLI para LocalStack
Configure o AWS CLI para apontar para o LocalStack:



aws configure
AWS Access Key ID: test
AWS Secret Access Key: test
Default region name: us-east-1
Default output format: json
Teste a configuração listando os buckets S3 (deve retornar vazio inicialmente):



aws --endpoint-url=http://localhost:4566 s3 ls
Passo 3: Executar a Aplicação
Abra o projeto no IntelliJ IDEA.
Certifique-se de que o arquivo application.properties ou .env está configurado corretamente.
Execute a classe principal ContainerApplication para iniciar a aplicação.
Passo 4: Testar os Endpoints com Postman
4.1 Criar um Bucket S3
Abra o Postman.
Configure uma nova requisição:
Método: POST
URL: http://localhost:8080/aws/s3/buckets/{bucketName}
Substitua {bucketName} pelo nome do bucket que deseja criar.
Clique em Send.
Verifique no terminal do LocalStack se o bucket foi criado:


aws --endpoint-url=http://localhost:4566 s3 ls
4.2 Fazer Upload de um Arquivo para o S3
No Postman, configure uma nova requisição:
Método: POST
URL: http://localhost:8080/aws/s3/upload
Body:
Selecione a opção x-www-form-urlencoded.
Adicione os seguintes campos:
bucketName: Nome do bucket criado anteriormente.
key: Nome do arquivo no bucket (exemplo: meuarquivo.txt).
filePath: Caminho completo do arquivo na sua máquina (exemplo: C:\Users\SeuUsuario\Documents\meuarquivo.txt).
Clique em Send.
Verifique no terminal do LocalStack se o arquivo foi enviado:


aws --endpoint-url=http://localhost:4566 s3 ls s3://{bucketName}
4.3 Listar Instâncias EC2
No Postman, configure uma nova requisição:
Método: GET
URL: http://localhost:8080/aws/ec2/instances
Clique em Send.
Verifique no console do IntelliJ IDEA a saída com as instâncias listadas.
Passo 5: Encerrar o LocalStack
Para encerrar o LocalStack, execute:


localstack stop