## 🏥 API de Agendamento de Consultas Médicas

API REST desenvolvida em **Java + Spring Boot** para gerenciamento completo de agendamentos médicos, permitindo que pacientes agendem consultas, médicos definam horários de atendimento, bloqueiem intervalos, e controlem o status das consultas.

O projeto foi construído com foco em **regras de negócio claras**, **boas práticas de arquitetura**, **testes automatizados** e **evolução contínua via refatoração**.

---

## 🚀 Funcionalidades Principais

### 👤 Paciente

* Cadastro de paciente
* Atualização dos próprios dados
* Listagem de consultas do paciente

### 🧑‍⚕️ Médico

* Cadastro de médico
* Atualização dos próprios dados
* Definição de horários de atendimento (agenda semanal)
* Bloqueio de intervalos dentro do expediente
* Listagem de consultas do médico no dia

### 📅 Agendamentos

Criação de consultas respeitando:

* Horários de atendimento do médico
* Duração da consulta
* Horários bloqueados
* Conflito com outras consultas

Além disso:

* Cancelamento de consulta (com regra de prazo mínimo)
* Finalização de consulta

---

## 🔐 Autenticação

* Autenticação de paciente e médico
* JWT para proteção das rotas
* Spring Security

---

## 🧠 Regras de Negócio Implementadas

* ❌ Não é possível agendar ou bloquear horários no passado
* ❌ Não é possível agendar fora do expediente do médico
* ❌ Não é possível agendar em horários já ocupados ou bloqueados
* ❌ Não é possível bloquear horários que conflitam com consultas
* ❌ Cancelamento respeita prazo mínimo antes da consulta
* ✅ Médicos controlam sua própria agenda

Todas as regras são validadas na **camada de Use Case**, mantendo os **controllers enxutos e sem lógica de negócio**.

---

## 🧪 Testes

### 🔹 Testes Unitários

Testes unitários desenvolvidos com **JUnit 5** e **Mockito**, cobrindo:

* Casos de sucesso
* Casos de erro
* Regras de negócio críticas

Padrão adotado:

* `@BeforeEach` para setup
* Métodos auxiliares privados (builders)
* Separação clara entre cenários válidos e inválidos

Refatorações são feitas garantindo **zero mudança de comportamento**, sempre validadas pelos testes.

---

### 🔹 Teste de Integração – Agendamento de Consultas

Foi implementado um **teste de integração específico para o fluxo de criação de consultas**, considerado o ponto mais crítico do sistema por concentrar múltiplas regras de negócio.

Esse teste valida o **fluxo real da aplicação**, incluindo:

* Requisição HTTP real via **MockMvc**
* Passagem pelos filtros de autenticação e segurança (**Spring Security + JWT**)
* Execução completa da camada de **Use Case**
* Persistência e leitura de dados em **banco em memória (H2)**

#### 🔎 O cenário cobre e valida que:

* ❌ O médico só pode receber consultas em dias em que está trabalhando
* ❌ Não é possível criar consultas fora do horário de expediente
* ❌ Não é possível criar consultas em horários já ocupados
* ❌ Não é possível criar consultas em horários bloqueados
* ✅ O sistema responde com **HTTP 201 (Created)** em cenários válidos
* ❌ O sistema retorna **HTTP 409 (Conflict)** quando há violação de regras de horário

Cada execução do teste utiliza um **banco em memória isolado**, garantindo:

* Independência entre cenários
* Confiabilidade dos resultados
* Ausência de efeitos colaterais entre testes

Para manter fidelidade total ao comportamento real da aplicação, o **JWT utilizado nos testes de integração é gerado pela mesma classe de geração de token usada em produção**, garantindo que todo o fluxo de autenticação seja validado.

---

## 🛠 Tecnologias Utilizadas

* Java 17+
* Spring Boot
* Spring Security
* JWT
* JPA / Hibernate
* Banco relacional (H2 / PostgreSQL)
* JUnit 5
* Mockito
* Maven
* Swagger / OpenAPI

---

## 📌 Endpoints Principais

### 🔐 Autenticação

* `POST /login` – Autenticar usuário

### 👤 Paciente

* `POST /patient` – Cadastrar paciente
* `PATCH /patient/me` – Atualizar dados
* `GET /appointments/patient` – Listar consultas

### 🧑‍⚕️ Médico

* `POST /doctor` – Cadastrar médico
* `PATCH /doctor/me` – Atualizar dados
* `POST /doctor/schedule` – Definir horários de atendimento
* `DELETE /doctor/{id}` – Deletar horário
* `POST /doctor/block` – Bloquear intervalo
* `GET /appointments/doctor` – Listar consultas do dia

### 📅 Agendamentos

* `POST /appointments/{doctorId}` – Agendar consulta
* `PATCH /appointments/{id}/finish` – Finalizar consulta
* `PATCH /appointments/{id}/cancel` – Cancelar consulta

---

## 📖 Documentação

Documentação completa disponível via **Swagger UI**:

```
http://localhost:8080/swagger-ui.html
```

---

## ▶️ Como Executar o Projeto

```bash
# Clonar o repositório
git clone https://github.com/seu-usuario/seu-repositorio.git

# Entrar no projeto
cd agendamento-consultas-medicas

# Rodar a aplicação
mvn spring-boot:run
```

A aplicação sobe em:

```
http://localhost:8080
```

---

## 🧩 Arquitetura

* Controllers: apenas HTTP
* UseCases: regras de negócio
* Repositories: acesso a dados
* DTOs: entrada e saída
* Mappers: conversão Entity → Response

Estrutura modular focada em **clareza, manutenção e testabilidade**.

---

## 🔮 Próximos Passos

* Refresh Token
* Expansão dos testes de integração
* Dockerização
* Deploy em ambiente cloud
* Monitoramento e logs

---

## ✍️ Autor

**Weldys Carmo**
Desenvolvedor Java em evolução contínua 🚀

Projeto desenvolvido como estudo prático de backend, regras de negócio e testes automatizados.
