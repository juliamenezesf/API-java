# 🌿 Worklife API -- Global Solution FIAP 2025

### Repositório: **API-java**

API desenvolvida em **Java + Quarkus** para o projeto **Global
Solution**, contemplando os módulos:

-   Gestão de Usuários\
-   Tasks\
-   Mood Logs\
-   Recomendações de Pausa (Break Recommendations)

A API integra-se ao **banco Oracle da FIAP** e fornece endpoints RESTful
utilizados pelo **Front-End** e pela aplicação **Python** do projeto.

------------------------------------------------------------------------

## 🎥 Vídeo de Apresentação

➡️ **URL do vídeo:** *adicione aqui quando estiver disponível*

------------------------------------------------------------------------

## 🚀 Deploy em Produção

A API está publicada na Render:

👉 **https://api-java-1-w4eg.onrender.com**

------------------------------------------------------------------------

## 🛠️ Tecnologias Utilizadas

| Tecnologia | Função |
|-----------|--------|
| **Quarkus 3** | Framework Java de alta performance |
| **Java 17** | Linguagem utilizada |
| **Maven** | Gerenciamento de dependências |
| **Oracle JDBC (ojdbc11)** | Conexão com Oracle |
| **Quarkus JDBC Oracle (Agroal)** | Pool de conexões |
| **RESTEasy Reactive** | API REST otimizada |
| **Render** | Deploy em produção |
| **Oracle FIAP** | Banco de dados remoto |

## 🗄️ Banco de Dados -- Oracle FIAP

A API utiliza um banco Oracle remoto contendo as tabelas:

-   `USERS`
-   `TASKS`
-   `MOOD_LOGS`
-   `BREAK_RECOMMENDATIONS`

Características principais:

-   Uso dos nomes reais das colunas\
-   Respeito às constraints do banco\
-   Validação de STATUS, PRIORITY e TASK_TYPE\
-   Geração de IDs usando `MAX(ID) + 1`\
-   Nenhuma tabela é criada ou modificada em produção

------------------------------------------------------------------------

## 🧱 Estrutura do Projeto

    API-java/
    ├── .idea/
    ├── .mvn/
    ├── mvnw
    ├── mvnw.cmd
    ├── pom.xml
    └── src/
        ├── main/
        │   ├── docker/
        │   ├── java/
        │   │   └── com/
        │   │       └── gs/
        │   │           ├── bo/
        │   │           ├── dao/
        │   │           ├── dto/
        │   │           ├── exception/
        │   │           ├── model/
        │   │           ├── resource/
        │   │           └── GreetingResource.java
        │   └── resources/
        │       └── application.properties
        └── test/

------------------------------------------------------------------------

## ⚙️ Perfis de Execução

### 🧪 Ambiente DEV (H2)

    %dev.quarkus.datasource.db-kind=h2
    %dev.quarkus.datasource.jdbc.url=jdbc:h2:mem:wellbeing

### 🚀 Ambiente PROD (Oracle FIAP)

    %prod.quarkus.datasource.db-kind=oracle
    %prod.quarkus.datasource.jdbc.url=jdbc:oracle:thin:@${DB_HOST}:${DB_PORT}:${DB_SID}
    %prod.quarkus.datasource.username=${DB_USER}
    %prod.quarkus.datasource.password=${DB_PASSWORD}

------------------------------------------------------------------------

## 🔧 Variáveis de Ambiente -- Render

  Variável          Valor
  ----------------- --------------------
  QUARKUS_PROFILE   prod
  DB_HOST           oracle.fiap.com.br
  DB_PORT           1521
  DB_SID            ORCL
  DB_USER           rmXXXXX
  DB_PASSWORD       \*\*\*\*\*

------------------------------------------------------------------------

## 🏃 Como Rodar Localmente (DEV -- H2)

1.  Clonar o repositório:

``` bash
git clone https://github.com/SEU_USUARIO/API-java.git
cd API-java
```

2.  Rodar a aplicação:

``` bash
mvnw quarkus:dev
```

API disponível em:\
👉 http://localhost:8080

------------------------------------------------------------------------

## 🏁 Como Rodar em Produção (Oracle FIAP)

1.  Gerar o JAR:

``` bash
mvnw clean package -DskipTests
```

2.  Executar com variáveis de ambiente:

``` bash
set QUARKUS_PROFILE=prod
set DB_HOST=oracle.fiap.com.br
set DB_PORT=1521
set DB_SID=ORCL
set DB_USER=rmXXXXX
set DB_PASSWORD=*****
java -jar target/wellbeing-api-1.0.0-SNAPSHOT.jar
```

------------------------------------------------------------------------

## 🌐 Endpoints da API

### 👤 USERS

  Método   Endpoint
  -------- ----------------
 - GET      /v1/users
 - GET      /v1/users/{id}
 - POST     /v1/users
 - DELETE   /v1/users/{id}

------------------------------------------------------------------------

### 📋 TASKS

  Método   Endpoint
  -------- -------------------------
 - GET      /v1/tasks
 - GET      /v1/tasks/{id}
 - POST     /v1/tasks
 - DELETE   /v1/tasks/{id}

------------------------------------------------------------------------

### 😄 MOOD LOGS

  Método   Endpoint
  -------- ----------------------------
  - GET      /v1/mood-logs
  - GET      /v1/mood-logs/{id}
  - POST     /v1/mood-logs
  - DELETE   /v1/mood-logs/{id}

------------------------------------------------------------------------

### 🧘 RECOMMENDATIONS

  Método   Endpoint
  -------- -----------------------------------
  - GET      /v1/recommendations
  - POST     /v1/recommendations

------------------------------------------------------------------------

## 👥 Integrantes do Projeto

  Nome                RM
  ------------------- ----------
  - **Júlia Menezes**   RM565568
  - **Pedro Costa**     RM559932

------------------------------------------------------------------------

## 📝 Licença

Projeto acadêmico desenvolvido para a **FIAP -- Global Solution 2025**.

------------------------------------------------------------------------

🌟 Obrigado por acessar nossa API!
