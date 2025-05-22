✅ Checkpoint 3 - Sistema de Agenda de Consultas
Autores

Pedro Cavariani - RM 551380

📖 Sobre o Projeto
Este é um projeto de backend desenvolvido em Java com Spring Boot, criado para gerenciar o agendamento de consultas médicas. O sistema permite o cadastro de pacientes e profissionais, o agendamento de consultas e a consulta de registros com filtros por status e datas. O projeto foi desenvolvido como parte do Checkpoint 3 da disciplina de Microserviços da FIAP.
🛠 Tecnologias Utilizadas

✅ Java 17  
✅ Spring Boot 3.4.6  
✅ Maven  
✅ Swagger (OpenAPI via SpringDoc)  
✅ IntelliJ IDEA (ou outra IDE de sua preferência)


Nota: Este projeto usa listas em memória para armazenar os dados, simplificando a implementação para fins de aprendizado.

🚀 Como Executar o Projeto
🔧 Pré-requisitos

Java 17 instalado  
Maven instalado  
Uma IDE (como IntelliJ IDEA, Eclipse ou VS Code)

⚙️ Passos para Rodar

Clone o repositório:

Copie o projeto para o seu computador ou faça o download diretamente do GitHub.


Abra o projeto na IDE:

Importe o projeto como um projeto Maven na sua IDE.


Execute a aplicação:

Rode o projeto pela IDE (ex.: clique em "Run" no IntelliJ) ou use o comando Maven no terminal:./mvnw spring-boot:run


O servidor será iniciado na porta 8080.


Acesse a aplicação:

Após iniciar, você pode testar os endpoints usando o Swagger ou uma ferramenta como Postman.



📜 Documentação da API
A documentação da API está disponível via Swagger. Com o projeto rodando, acesse:

http://localhost:8080/swagger-ui.html

📍 Endpoints Disponíveis
🔹 Pacientes

POST /pacientes - Cadastra um novo paciente  
GET /pacientes?sort={asc,desc} - Lista todos os pacientes, ordenados por nome  
GET /pacientes/{id} - Busca um paciente por ID  
PUT /pacientes/{id} - Atualiza um paciente existente  
DELETE /pacientes/{id} - Remove um paciente  
GET /pacientes/{id}/consultas?status=&data_de=&data_ate= - Lista consultas de um paciente com filtros

🔹 Profissionais

POST /profissionais - Cadastra um novo profissional  
GET /profissionais?sort={asc,desc} - Lista todos os profissionais, ordenados por nome  
GET /profissionais/{id} - Busca um profissional por ID  
PUT /profissionais/{id} - Atualiza um profissional existente  
DELETE /profissionais/{id} - Remove um profissional  
GET /profissionais/{id}/stats - Retorna estatísticas do profissional (total de consultas e realizadas)  
GET /profissionais/{id}/consultas?status=&data_de=&data_ate= - Lista consultas de um profissional com filtros

🔹 Consultas

POST /consultas - Agenda uma nova consulta  
GET /consultas - Lista todas as consultas  
GET /consultas/{id} - Busca uma consulta por ID  
PUT /consultas/{id} - Atualiza uma consulta existente  
DELETE /consultas/{id} - Remove uma consulta  
GET /consultas?status=&data_de=&data_ate= - Lista consultas com filtros de status e data

🎯 Objetivo do Projeto
Este projeto foi desenvolvido para o Checkpoint 3 da FIAP, com o objetivo de aplicar conceitos de desenvolvimento de APIs RESTful usando Spring Boot. O foco foi criar uma aplicação funcional para gerenciar agendamentos de consultas, com endpoints bem definidos e documentação via Swagger.
