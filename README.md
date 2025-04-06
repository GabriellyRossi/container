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
    branches: [main]

jobs:
  build-and-push:
    runs-on: ubuntu-latest
    steps:
      - name: Checkout code
        uses: actions/checkout@v4

      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'

      - name: Build with Maven
        run: mvn clean package

      - name: Login to GitHub Container Registry
        uses: docker/login-action@v3
        with:
          registry: ghcr.io
          username: ${{ github.actor }}
          password: ${{ secrets.GITHUB_TOKEN }}

      - name: Build and push
        uses: docker/build-push-action@v5
        with:
          context: .
          push: true
          tags: ghcr.io/gabriellyzup/container:latest
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

💡 Dica: Para projetos novos, basta copiar o Dockerfile e o workflow.yml!kflow.ym