````markdown
# SmokeSignal Spring Application

Este projeto Spring Boot, chamado **SmokeSignal**, tem como objetivo replicar, em uma aplicação Java com arquitetura MVC, a solução “core” de Data Science apresentada no documento PDF “GS_2 – Data Science”. A aplicação expõe endpoints REST para:

1. **Listar amostras de dados de incêndios** (leitura de CSV).
2. **Listar previsões históricas de incêndios** (armazenadas em banco de dados H2).
3. **Gerar nova previsão de incêndio** com base em estado e mês (utilizando o mesmo modelo de regressão logística descrito no Data Science).
4. **Atualizar previsões existentes**.
5. **Deletar previsões**.

A seguir, instruções de uso, justificativas técnicas e a organização geral do projeto.

---

## Índice

1. [Pré-requisitos](#pré-requisitos)  
2. [Como executar](#como-executar)  
3. [Configurações e Dependências](#configurações-e-dependências)  
4. [Organização do Projeto](#organização-do-projeto)  
   - 4.1. Estrutura de Pacotes  
   - 4.2. Principais Classes e Funcionalidades  
5. [Endpoints REST](#endpoints-rest)  
   - 5.1. **Incêndios**  
   - 5.2. **Previsões**  
6. [Exemplos de Requisições](#exemplos-de-requisições)  
7. [Justificativas Técnicas](#justificativas-técnicas)  
8. [Considerações Finais](#considerações-finais)  

---

## 1. Pré-requisitos

- **Java 21** (JDK instalado e variável `JAVA_HOME` configurada).  
- **Maven 3.6+** (utilizado para build e gerenciamento de dependências).  
- **Git** (opcional, caso deseje clonar o repositório).  

---

## 2. Como executar

1. **Clone o repositório** (ou obtenha o código-fonte):  
   ```bash
   git clone https://lucasz-g/smokesignal.git
   cd smokesignal
````

2. **Verifique se o arquivo CSV de amostra existe em**
   `src/main/resources/amostra_500k.csv`

   * Este é o mesmo arquivo gerado pelo pipeline de Data Science (500 000 registros) e utilizado pelo serviço `IncendioService` para listar as primeiras 100 amostras.

3. **Compile e rode a aplicação com Maven**:

   ```bash
   mvn clean package
   mvn spring-boot:run
   ```

   * A aplicação será iniciada na porta padrão **8080**.
   * Alternativamente, é possível gerar um JAR executável e executá-lo diretamente:

     ```bash
     mvn clean package
     java -jar target/smokesignal-0.0.1-SNAPSHOT.jar
     ```

4. **Acesse os endpoints via `http://localhost:8080/api/v1/…`** (ver seção [Endpoints REST](#endpoints-rest)).

---

## 3. Configurações e Dependências

O projeto utiliza **Spring Boot 3.5.0** como base (parent POM) e conta com as seguintes principais dependências (ver `pom.xml`):

* `spring-boot-starter-web`: para criar endpoints REST e embutir o servidor Tomcat.
* `spring-boot-starter-data-jpa`: para integração com JPA/Hibernate e persistência em banco de dados.
* `com.h2database:h2` (runtime): banco de dados em memória, ideal para testes rápidos e prototipagem.
* `com.opencsv:opencsv`: leitura e mapeamento do CSV de amostra.
* `com.github.haifengl:smile-core`: biblioteca Smile (versão 4.3.0) utilizada para cálculos de probabilidade / modelo de Machine Learning, replicando o modelo de regressão logística criado no pipeline de Python.
* `spring-boot-starter-validation`: para anotações de validação nos DTOs, se necessário.
* `lombok` (opcional): para geração de getters, setters e construtores automaticamente (configurado como opcional no build).

A configuração padrão do Spring Boot para JPA/H2 (sem customização) criará um banco em memória. Caso seja interessante persistir em arquivo ou outro banco, basta alterar as propriedades em `application.properties`.

---

## 4. Organização do Projeto

### 4.1. Estrutura de Pacotes

```
├───src
│   ├───main
│   │   ├───java
│   │   │   └───br
│   │   │       └───com
│   │   │           └───fiap
│   │   │               └───smokesignal
│   │   │                   │   SmokesignalApplication.java
│   │   │                   │
│   │   │                   ├───config
│   │   │                   │       WebConfig.java
│   │   │                   │
│   │   │                   ├───controller
│   │   │                   │       LogDeRequisicaoController.java
│   │   │                   │       PrevisaoController.java
│   │   │                   │
│   │   │                   ├───entity
│   │   │                   │   │   IncendioEntity.java
│   │   │                   │   │   LocalidadeEntity.java
│   │   │                   │   │   LogDeRequisicaoEntity.java
│   │   │                   │   │   PrevisaoEntity.java
│   │   │                   │   │   TipoUsuarioEnum.java
│   │   │                   │   │   UsuarioEntity.java
│   │   │                   │   │
│   │   │                   │   └───DTO
│   │   │                   │           PrevisaoRequestDTO.java
│   │   │                   │           PrevisaoResponseDTO.java
│   │   │                   │
│   │   │                   ├───interceptor
│   │   │                   │       LogDeRequisicaoInterceptor.java
│   │   │                   │
│   │   │                   ├───mock
│   │   │                   │       UsuarioLogadoMock.java
│   │   │                   │
│   │   │                   ├───repository
│   │   │                   │       LogDeRequisicaoRepository.java
│   │   │                   │       PrevisaoRepository.java
│   │   │                   │
│   │   │                   ├───security
│   │   │                   │       PermissaoValidator.java
│   │   │                   │
│   │   │                   ├───service
│   │   │                   │   │   IncendioService.java
│   │   │                   │   │   LogDeRequisicaoService.java
│   │   │                   │   │   PrevisaoService.java
│   │   │                   │   │
│   │   │                   │   ├───csvServices
│   │   │                   │   │       CsvToIncendioMapper.java
│   │   │                   │   │       ReaderCsvService.java
│   │   │                   │   │
│   │   │                   │   ├───machineLearningServices
│   │   │                   │   │       DataFrameService.java
│   │   │                   │   │       ModeloService.java
│   │   │                   │   │
│   │   │                   │   └───test
│   │   │                   │           DataFrameServiceTest.java
│   │   │                   │
│   │   │                   └───smokesignal
│   │   └───resources
│   │       │   amostra_500k.csv
│   │       │   application.properties
│   │       │   df_modelo_dummies.csv
│   │       │
│   │       ├───static
│   │       └───templates
│   └───test
│       └───java
│           └───br
│               └───com
│                   └───fiap
│                       └───smokesignal
│                           └───smokesignal
│                                   DataFrameServiceTest.java
│                                   SmokesignalApplicationTests.java
```

### 4.2. Principais Classes e Funcionalidades

* **PrevisaoController**

  * Mapeado em `/api/v1` (anotação `@RequestMapping("/api/v1")`).
  * Endpoints para listar/incorporar/atualizar/excluir previsões e listar incêndios.
  * Injeta `PrevisaoService`, `IncendioService` e `PermissaoValidator`.
  * Exemplo de métodos:

    * `GET /incendios` → chama `incendioService.buscarTodosIncendios()` para retornar as primeiras 100 entidades `IncendioEntity`.
    * `GET /previsoes` → retorna `List<PrevisaoEntity>` de todas as previsões salvas.
    * `GET /previsoes/estado/{state}` → retorna previsões filtradas por estado (sem case sensitive).
    * `POST /previsoes` → cria nova previsão, exigindo permissão de admin (método mock em `PermissaoValidator`).
    * `PUT /previsoes/{id}` → atualiza uma previsão existente (recalcula probabilidade).
    * `DELETE /previsoes/{id}` → apaga previsão (retorna mensagem de sucesso ou falha).

* **PrevisaoService**

  * Contém a lógica de negócio relacionada a previsões.
  * Injeta `ModeloService` (cálculo de probabilidade ML) e `PrevisaoRepository` (persistência).
  * Métodos principais:

    * `listarPrevisoes()`, `listarPorEstado(String)`, `salvarPrevisao(PrevisaoRequestDTO)`, `atualizarPrevisao(Long, PrevisaoRequestDTO)` e `deletarPrevisao(Long)`.
  * Ao criar ou atualizar previsão, chama `modeloService.calcularProb(estado, mes)` para obter a probabilidade (0.0–1.0). Converte para percentual (multiplica por 100) e armazena em `BigDecimal`.

* **IncendioService**

  * Lê CSV completo em `src/main/resources/amostra_500k.csv` usando `ReaderCsvService`.
  * Mapeia cada linha para `IncendioEntity` por meio de `CsvToIncendioMapper`.
  * Retorna os primeiros 100 registros (para não sobrecarregar a resposta).
  * Permite ao usuário inspecionar amostra histórica de incêndios (sem persistir no banco).

* **ModeloService** (pacote `machineLearningServices`)

  * Responsável por encapsular o modelo de regressão logística treinado em Python (ou reconstruído em Java/Smile).
  * Método `calcularProb(String estado, Integer mes)` retorna um `double` entre 0.0 e 1.0.
  * Implementação baseia-se no documento PDF de Data Science: utiliza as frequências históricas de incêndios por estado e mês, aplica one-hot encoding e chama o modelo de regressão logística carregado ou reconstruído em tempo de execução.
  * Garante que as previsões em Java reproduzam os valores obtidos no pipeline Python (p. ex., 91.10% para Califórnia em julho).

* **ReaderCsvService** e **CsvToIncendioMapper** (pacote `csvServices`)

  * `ReaderCsvService.lerCsv(String caminho)` → retorna `List<String[]>`, onde cada `String[]` representa os valores de uma linha do CSV (primeiro índice = cabeçalho).
  * `CsvToIncendioMapper.map(String[] linha, String[] header)` → converte cada linha em uma instância de `IncendioEntity`, populando atributos como `FIRE_ID`, `FIRE_YEAR`, `NWCG_GENERAL_CAUSE`, `STATE`, `FIRE_SIZE`, `LATITUDE`, `LONGITUDE`, etc.
  * Permite abstrair a leitura cru do CSV e manter o `IncendioService` focado apenas em lógica de “retornar primeiras 100 linhas mapeadas”.

* **PermissaoValidator** (pacote `security`)

  * Classe simples (mock) que verifica se o usuário atual tem permissão de “admin” para criar/atualizar/excluir previsões.
  * Caso a validação falhe, lança exceção genérica ou retorna erro HTTP 403.
  * Em produção, seria substituída pelo mecanismo real de autenticação/autorização (JWT, OAuth, RBAC, etc.).

* **PrevisaoEntity**, **IncendioEntity** (pacote `entity`)

  * **PrevisaoEntity**: anotada com `@Entity`, possui atributos:

    * `Long id` (chave primária `@Id @GeneratedValue`).
    * `String estado`, `Integer mes`.
    * `BigDecimal probabilidadeIncendio` (percentual, ex.: “91.10”).
    * `LocalDateTime dataHora` (timestamp de criação/atualização).
  * **IncendioEntity**: anotada com `@Entity`, contém colunas relevantes do CSV (ex.: `fireId`, `fireYear`, `generalCause`, `state`, `fireSize`, `latitude`, `longitude`, etc.).
  * Os mapeamentos JPA seguem convenções padrão ou anotações em cada campo.

* **PrevisaoRepository** (pacote `repository`)

  * Interface que estende `JpaRepository<PrevisaoEntity, Long>`.
  * Método customizado: `List<PrevisaoEntity> findByEstadoIgnoreCase(String state)`.

---

## 5. Endpoints REST

Todas as rotas estão sob o prefixo base `/api/v1`.

### 5.1. **Incêndios**

| Verbo HTTP | Endpoint            | Descrição                                                                   | Retorno                             |
| ---------- | ------------------- | --------------------------------------------------------------------------- | ----------------------------------- |
| `GET`      | `/api/v1/incendios` | Retorna uma lista (máx. 100) de entidades `IncendioEntity` mapeadas do CSV. | `List<IncendioEntity>` (JSON array) |

### 5.2. **Previsões**

| Verbo HTTP | Endpoint                       | Descrição                                                                               | Retorno                             |
| ---------- | ------------------------------ | --------------------------------------------------------------------------------------- | ----------------------------------- |
| `GET`      | `/api/v1/previsoes`            | Lista todas as previsões salvas no banco H2.                                            | `List<PrevisaoEntity>` (JSON array) |
| `GET`      | `/api/v1/previsoes/estado/{s}` | Lista previsões filtradas por estado (case-insensitive).                                | `List<PrevisaoEntity>`              |
| `POST`     | `/api/v1/previsoes`            | Cria uma nova previsão. Exige permissão de “admin” (mock). Recebe `PrevisaoRequestDTO`. | `PrevisaoResponseDTO` (JSON)        |
| `PUT`      | `/api/v1/previsoes/{id}`       | Atualiza previsão existente. Exige permissão de “admin”. Recebe `PrevisaoRequestDTO`.   | `PrevisaoResponseDTO` (JSON)        |
| `DELETE`   | `/api/v1/previsoes/{id}`       | Deleta previsão pelo `id`. Exige permissão de “admin”. Retorna mensagem simples.        | `String` (mensagem)                 |

---

## 6. Exemplos de Requisições

### 6.1. Listar Incêndios (GET)

```bash
curl -X GET http://localhost:8080/api/v1/incendios
```

**Resposta** (formato JSON, array de objetos):

```json
[
  {
    "fireId": 123456,
    "fireYear": 2018,
    "discoveryDate": "2018-07-14T13:20:00",
    "generalCause": "Natural",
    "state": "CA",
    "fireSize": 2000.5,
    "latitude": 36.7783,
    "longitude": -119.4179,
    ...
  },
  ...
]
```

### 6.2. Listar Todas as Previsões (GET)

```bash
curl -X GET http://localhost:8080/api/v1/previsoes
```

**Resposta** (exemplo):

```json
[
  {
    "id": 1,
    "estado": "CA",
    "mes": 7,
    "probabilidadeIncendio": 91.10,
    "dataHora": "2025-06-05T15:30:12.345"
  },
  {
    "id": 2,
    "estado": "TX",
    "mes": 12,
    "probabilidadeIncendio": 3.00,
    "dataHora": "2025-06-05T15:35:20.123"
  }
]
```

### 6.3. Listar Previsões por Estado (GET)

```bash
curl -X GET http://localhost:8080/api/v1/previsoes/estado/CA
```

**Resposta**:

```json
[
  {
    "id": 1,
    "estado": "CA",
    "mes": 7,
    "probabilidadeIncendio": 91.10,
    "dataHora": "2025-06-05T15:30:12.345"
  }
]
```

### 6.4. Criar Nova Previsão (POST)

* **Body** (JSON, `application/json`):

  ```json
  {
    "estado": "CA",
    "mes": 7
  }
  ```
* **Resposta** (`PrevisaoResponseDTO`):

  ```json
  {
    "estado": "CA",
    "mes": 7,
    "probabilidade": 0.9110
  }
  ```

  * A probabilidade retornada é um valor de 0.0 a 1.0 (por exemplo, 0.9110 = 91.10%).
  * Internamente, o serviço converte para `BigDecimal.valueOf(prob * 100)` e armazena em `probabilidadeIncendio`.

### 6.5. Atualizar Previsão (PUT)

```bash
curl -X PUT http://localhost:8080/api/v1/previsoes/1 \
  -H "Content-Type: application/json" \
  -d '{
        "estado": "TX",
        "mes": 12
      }'
```

**Resposta** (`PrevisaoResponseDTO`):

```json
{
  "estado": "TX",
  "mes": 12,
  "probabilidade": 0.0300
}
```

### 6.6. Deletar Previsão (DELETE)

```bash
curl -X DELETE http://localhost:8080/api/v1/previsoes/1
```

**Resposta** (texto puro):

```
Previsão deletada com sucesso!
```

---

## 7. Justificativas Técnicas

### 7.1. Por que Spring Boot e Arquitetura MVC?

* **Produtividade e Convenções**:
  O Spring Boot oferece convenções “opinionated” que reduzem a quantidade de configuração manual. Ao seguir padrões de camada (Controller → Service → Repository), facilitamos a manutenção e a testabilidade do código.

* **Injeção de Dependências (DI)**:
  Através de anotações `@Autowired`, promovemos baixo acoplamento. Cada camada possui responsabilidade única:

  1. **Controller**: expõe endpoints REST e recebe/direciona requisições HTTP.
  2. **Service**: contém lógica de negócio (cálculo de probabilidade, regras de negócio, validações).
  3. **Repository**: abstrai o acesso ao banco de dados (JPA/Hibernate).

* **Facilidade de Integração**:

  * **Spring Data JPA** permite criar repositórios com apenas interfaces, sem precisar escrever implementações.
  * **H2 Database** em modo memória/in-file facilita testes e demonstrações sem overhead de configurar bancos externos.

* **Separação de Responsabilidades**:
  Organizar o código em pacotes distintos (controller, service, repository, entity, security, etc.) melhora a escalabilidade e a legibilidade.

### 7.2. Modelagem de Machine Learning em Java

* O documento PDF “GS\_2 – Data Science” implementou, em Python, vários passos:

  1. Pré-processamento (amostragem, conversão de tipos, remoção de valores ausentes).
  2. Análise exploratória (estatísticas, gráficos, etc.).
  3. Modelos de regressão e regressão logística para prever probabilidade de incêndio (com ROC AUC = 0.888).

* **Decisão Técnica**:
  Para a entrega em Java, foi optado por reverter o modelo treinado em Python para o ambiente Java:

  * Utiliza-se a biblioteca Smile (`smile-core` 4.3.0) para reconstruir/regressão logística.
  * O `ModeloService` carrega, na inicialização, pesos do modelo ou recalcula treinamentos simplificados (usando `Smile` API).
  * Desse modo, obtém-se a **mesma probabilidade** que o pipeline Python gerou (ex.: 0.9110 para CA em julho).

### 7.3. Uso de DTOs

* **DTO (Data Transfer Object)**

  * **PrevisaoRequestDTO**: contém apenas `estado` (String) e `mes` (Integer).
  * **PrevisaoResponseDTO**: devolve ao cliente os mesmos campos de request mais `probabilidade` (double).

* **Motivo**:
  Separar objeto de domínio (`PrevisaoEntity`) do objeto de entrada/saída padroniza a API, facilita validações e evita expor campos internos (como `id` ou `dataHora` no corpo da requisição de criação).

### 7.4. Manipulação de CSV de Incêndios

* Em vez de persistir 500 000 registros em banco relacional, optou-se por:

  1. Manter o CSV original em `src/main/resources/amostra_500k.csv`.
  2. Ao chamar `GET /incendios`, o `IncendioService` lê o CSV in-memory e retorna apenas as **100 primeiras linhas** mapeadas.
  3. Essa estratégia evita sobrecarregar o banco e a aplicação, fornecendo amostra representativa (conforme definição de amostra no pipeline Python).

* **Vantagens**:

  * Não há necessidade de migrar dados para tabelas relacionais.
  * Mantém a consistência com o pipeline de Data Science (mesmo arquivo de entrada).
  * Reduz tempo de deploy e complexidade para demonstrações.

### 7.5. Banco de Dados H2

* **H2** foi escolhido por ser:

  * Leve e **embutido** (não requer servidor externo).
  * Compatível com SQL padrão e JPA/Hibernate sem ajustes.
  * Permite persistir previsões temporárias em disco (caso se deseje configuração `spring.datasource.url=jdbc:h2:file:./data/smokesignal`).

* **Aplicação Prática**:

  1. Ao criar ou atualizar previsão, a entidade `PrevisaoEntity` é salva no H2.
  2. No ciclo de vida da aplicação, mantém histórico das previsões geradas.
  3. Em ambiente de produção, basta trocar a dependência H2 por MySQL/PostgreSQL sem alterar código-fonte (apenas configurações).

### 7.6. Permissões (Mock)

* A classe `PermissaoValidator` contém métodos como `validarAdmin()`, que atualmente lança exceção caso o usuário não seja “admin”.
* **Ponto de Extensão**:

  * Em entregas futuras, integrar com Spring Security (JWT, OAuth2, Keycloak) para gerenciar autenticação real.
  * O mock serve apenas para ilustrar restrição de endpoints sensíveis (POST, PUT, DELETE).

## 8. Logs de Requisição

A aplicação inclui uma funcionalidade de **auditoria de acesso aos endpoints**, registrando automaticamente cada requisição feita ao sistema. Os logs são armazenados em uma tabela `LogDeRequisicaoEntity`, persistida via JPA.

### Objetivos:

* Auditar as chamadas aos endpoints (ex: `/api/v1/previsoes`, `/api/v1/incendios`, etc.).
* Fornecer um histórico de uso da API.
* Suportar futuras funcionalidades como geração de relatórios de uso e segurança.

### Implementação

#### Entidade: `LogDeRequisicaoEntity`

Contém os seguintes atributos:

* `Long id` – chave primária.
* `String endpoint` – URI do endpoint acessado.
* `LocalDateTime dataHora` – data e hora do acesso.

#### Serviço: `LogDeRequisicaoService`

Métodos:

* `registrar(String endpoint)`: cria e salva o log com base na URI acessada e na data/hora atual.
* `listarTodos()`: retorna todos os logs persistidos.

#### Controller: `LogDeRequisicaoController`

Disponibiliza o endpoint:

```http
GET /api/v1/logs
```

* Lista todos os registros de logs.
* Acesso restrito a usuários com permissão de administrador (via `PermissaoValidator`).

Exemplo de resposta:

```json
[
  {
    "id": 1,
    "endpoint": "/api/v1/previsoes",
    "dataHora": "2025-06-05T16:01:23"
  },
  {
    "id": 2,
    "endpoint": "/api/v1/incendios",
    "dataHora": "2025-06-05T16:03:02"
  }
]
```

### Futuras melhorias possíveis

* **Registrar automaticamente todas as requisições via Interceptor ou Filter Spring**.
* **Adicionar campo de usuário autenticado** (para rastreamento por usuário).
* **Exportar logs como CSV/Excel**.
* **Integração com ELK Stack ou serviços como CloudWatch, Datadog**.

---

## 9. Considerações Finais

* Esta aplicação **Spring MVC** reproduz integralmente o fluxo de dados e lógica descritos no documento de Data Science:

  * **Dados brutos**: CSV de amostra de 500 000 registros.
  * **Modelagem**: regressão logística para probabilidade de incêndio por estado e mês.
  * **Persistência de previsões**: banco H2 em memória.
  * **Exposição via API REST**: endpoints para listar, criar, atualizar e deletar previsões.

* **Possíveis extensões**:

  1. **Dashboard Web**: implementar frontend (React/Angular/Vue) para exibir amostras e previsões em gráficos (mapas, histograma, etc.).
  2. **Integração com dados meteorológicos em tempo real**: refinar o modelo de Machine Learning com variáveis como temperatura, umidade, velocidade do vento.
  3. **Testes automatizados**: criar testes unitários (JUnit 5) e de integração (Spring Boot Test) para endpoints e serviços.
  4. **Deploy em nuvem**: configurar CI/CD (GitHub Actions, GitLab CI) para build e deploy em Heroku, AWS Elastic Beanstalk ou outro serviço.

---

### Contato dos Desenvolvedores

* **RM 554070** – Lucas Garcia
* **RM 554272** – Enzo Barbeli
* **RM 554259** – Felipe Santana

---

> **Observação**: Para consultar a solução “core” em Data Science que serviu de base, veja o PDF `GS_2 – Data Science` no diretório raiz do projeto.

## 10. Diagrama de Classes

O diagrama abaixo representa a estrutura de entidades e serviços da aplicação SmokeSignal:

[![Visualizar no Miro](./diagrama.png)](https://miro.com/app/board/uXjVIulEnyY=/?share_link_id=969084609614)