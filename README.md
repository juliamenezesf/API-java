# 🌿 Wellbeing API – Global Solution FIAP 2025

API desenvolvida em **Java + Quarkus** para o projeto **Global Solution**, integrando os módulos:

- Gestão de Usuários  
- Tasks  
- Mood Logs  
- Recomendações de Pausa (Break Recommendations)

A API conecta-se ao **banco Oracle da FIAP** e fornece endpoints RESTful usados tanto pelo **Front-End** quanto pela **aplicação Python**.

---

## 🚀 Tecnologias Utilizadas

| Tecnologia | Finalidade |
|-----------|------------|
| **Quarkus 3** | Framework Java de alta performance e baixo consumo |
| **Java 17** | Linguagem base |
| **Maven** | Gerenciamento de dependências |
| **Oracle JDBC (ojdbc11)** | Conexão com Oracle |
| **Quarkus JDBC Oracle** | Pool de conexões (Agroal) |
| **RESTEasy Reactive** | API REST de alta performance |
| **Render** | Deploy em produção |
| **Oracle FIAP** | Banco de dados utilizado pela aplicação |

---

## 🗄️ Banco de Dados – Oracle FIAP

A API conecta-se a um banco Oracle remoto (FIAP), com as tabelas:

- `USERS`
- `TASKS`
- `MOOD_LOGS`
- `BREAK_RECOMMENDATIONS`

Cada DAO foi ajustado para trabalhar com:

- Nomes reais de colunas (ex: `ID_USER`, `CREATED_AT`, etc.)
- Constraints originais do banco
- Validação de STATUS, PRIORITY e TASK_TYPE
- Geração de IDs no padrão Oracle: `MAX(ID) + 1`

Nenhuma tabela é criada ou alterada pela API em produção.

---

## 🧱 Estrutura Principal do Projeto

src/
main/
java/
com.gs.dao/ # Acesso ao banco (Oracle)
com.gs.model/ # Entidades da API
com.gs.bo/ # Regras de negócio
com.gs.resource/ # Endpoints REST
resources/
application.properties
pom.xml
mvnw / mvnw.cmd

yaml
Copy code

---

## ⚙️ Perfis de Execução

### 🧪 Ambiente DEV (local – H2)
properties
%dev.quarkus.datasource.db-kind=h2
%dev.quarkus.datasource.jdbc.url=jdbc:h2:mem:wellbeing

🚀 Ambiente PROD (Render – Oracle FIAP)
properties
Copy code
%prod.quarkus.datasource.db-kind=oracle
%prod.quarkus.datasource.jdbc.url=jdbc:oracle:thin:@${DB_HOST}:${DB_PORT}:${DB_SID}
%prod.quarkus.datasource.username=${DB_USER}
%prod.quarkus.datasource.password=${DB_PASSWORD}

🔧 Variáveis de Ambiente (Render)
Variável	Valor
QUARKUS_PROFILE	prod
DB_HOST	oracle.fiap.com.br
DB_PORT	1521
DB_SID	ORCL
DB_USER	rmXXXXX
DB_PASSWORD	******

🏃 Como Rodar Localmente (DEV – H2)
1. Clonar o repositório
bash
Copy code
git clone https://github.com/SEU_USUARIO/wellbeing-api.git
cd wellbeing-api
2. Rodar em modo DEV
bash
Copy code
mvnw quarkus:dev
A API sobe em:
👉 http://localhost:8080

🏁 Como Rodar em Produção (Oracle FIAP)
1. Gerar o JAR
bash
Copy code
mvnw clean package -DskipTests
2. Rodar com Oracle
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

🚀 Deploy no Render
Build Command
bash
Copy code
./mvnw clean package -DskipTests
Start Command
bash
Copy code
java -jar target/wellbeing-api-1.0.0-SNAPSHOT.jar
URL da API
https://api-java-1-w4eg.onrender.com

👥 Integrantes do Projeto
Júlia Menezes - RM565568
Pedro Costa - RM559932

📝 Licença
Projeto acadêmico desenvolvido para a FIAP – Global Solution 2025.

🌟 Obrigado por acessar nossa API!
