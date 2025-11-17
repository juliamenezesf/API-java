# 🌿 Wellbeing API – Global Solution FIAP 2025  
### Repositório: **API-java**

API desenvolvida em **Java + Quarkus** para o projeto **Global Solution**, contemplando os módulos:

- Gestão de Usuários  
- Tasks  
- Mood Logs  
- Recomendações de Pausa (Break Recommendations)

A API integra-se ao **banco Oracle da FIAP** e fornece endpoints RESTful utilizados pelo **Front-End** e pela aplicação **Python** do projeto.

---

## 🎥 Vídeo de Apresentação  
➡️ **URL do vídeo:** _adicione aqui quando estiver disponível_

---

## 🚀 Deploy em Produção  
A API está publicada na Render:

👉 **https://api-java-1-w4eg.onrender.com**

---

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

---

## 🗄️ Banco de Dados – Oracle FIAP  

A API utiliza um banco Oracle remoto contendo as tabelas:

- `USERS`
- `TASKS`
- `MOOD_LOGS`
- `BREAK_RECOMMENDATIONS`

Principais características do acesso a dados:

- Uso dos nomes reais das colunas (ex.: `ID_USER`, `CREATED_AT`)  
- Respeito às constraints originais  
- Validação de STATUS, PRIORITY e TASK_TYPE  
- Geração de IDs Oracle com `MAX(ID) + 1`  
- Nenhuma tabela é criada ou alterada em produção  

---

## 🧱 Estrutura do Projeto

API-java/
├── .idea/
├── .mvn/
├── mvnw
├── mvnw.cmd
├── pom.xml
└── src/
├── main/
│ ├── docker/
│ ├── java/
│ │ └── com/
│ │ └── gs/
│ │ ├── bo/ # Regras de negócio
│ │ ├── dao/ # Acesso ao banco Oracle (DAO)
│ │ ├── dto/ # Objetos de transferência de dados
│ │ ├── exception/ # Exceptions personalizadas
│ │ ├── model/ # Entidades/Modelos
│ │ ├── resource/ # Endpoints REST
│ │ └── GreetingResource.java
│ └── resources/
│ └── application.properties
└── test/

yaml
Copy code

---

## ⚙️ Perfis de Execução

### 🧪 DEV – Banco H2 (local)
%dev.quarkus.datasource.db-kind=h2
%dev.quarkus.datasource.jdbc.url=jdbc:h2:mem:wellbeing

graphql
Copy code

### 🚀 PROD – Banco Oracle FIAP (Render)
%prod.quarkus.datasource.db-kind=oracle
%prod.quarkus.datasource.jdbc.url=jdbc:oracle:thin:@${DB_HOST}:${DB_PORT}:${DB_SID}
%prod.quarkus.datasource.username=${DB_USER}
%prod.quarkus.datasource.password=${DB_PASSWORD}

yaml
Copy code

### 🔧 Variáveis de Ambiente na Render

| Variável | Valor |
|----------|--------|
| QUARKUS_PROFILE | prod |
| DB_HOST | oracle.fiap.com.br |
| DB_PORT | 1521 |
| DB_SID | ORCL |
| DB_USER | rmXXXXX |
| DB_PASSWORD | ***** |

---

## 🏃 Como Rodar Localmente (DEV – H2)

1. Clonar o repositório:
bash
git clone https://github.com/SEU_USUARIO/API-java.git
cd API-java
Iniciar em modo DEV:

bash
Copy code
mvnw quarkus:dev
API disponível em:
👉 http://localhost:8080

🏁 Como Rodar em Produção (Oracle FIAP)
Gerar o JAR:

bash
Copy code
mvnw clean package -DskipTests
Executar com as variáveis de ambiente configuradas:

bash
Copy code
set QUARKUS_PROFILE=prod
set DB_HOST=oracle.fiap.com.br
set DB_PORT=1521
set DB_SID=ORCL
set DB_USER=rmXXXXX
set DB_PASSWORD=*****
java -jar target/wellbeing-api-1.0.0-SNAPSHOT.jar
🌐 Endpoints da API
👤 USERS
Método	Endpoint
GET	/v1/users
GET	/v1/users/{id}
POST	/v1/users
DELETE	/v1/users/{id}

📋 TASKS
Método	Endpoint
GET	/v1/tasks
GET	/v1/tasks/{id}
GET	/v1/tasks/user/{userId}
POST	/v1/tasks
DELETE	/v1/tasks/{id}

😄 MOOD LOGS
Método	Endpoint
GET	/v1/mood-logs
GET	/v1/mood-logs/{id}
GET	/v1/mood-logs/user/{userId}
POST	/v1/mood-logs
DELETE	/v1/mood-logs/{id}

🧘 RECOMMENDATIONS
Método	Endpoint
GET	/v1/recommendations
GET	/v1/recommendations/user/{userId}
POST	/v1/recommendations

👥 Integrantes do Projeto
Júlia Menezes – RM565568

Pedro Costa – RM559932

📝 Licença
Projeto acadêmico desenvolvido para a FIAP – Global Solution 2025.

🌟 Obrigado por acessar nossa API!
