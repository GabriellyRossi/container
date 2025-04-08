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


version: "3.9"

services:
  app:
    image: ghcr.io/gabriellyzup/container:latest
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://db:5432/cadastro_clientes
      SPRING_DATASOURCE_USERNAME: postgres
      SPRING_DATASOURCE_PASSWORD: postgres
    depends_on:
      - db
    command: ["--tls-verify=false"]

  db:
    image: postgres:latest
    environment:
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
      POSTGRES_DB: cadastro_clientes
    volumes:
      - postgres-data:/var/lib/postgresql/data

volumes:
  postgres-data:
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