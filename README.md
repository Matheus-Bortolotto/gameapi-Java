# gameapi

API REST para **cadastro de Games e Players**, feita com **Spring Boot 3**, **JPA/Hibernate**, **Lombok**, **Swagger (springdoc)** e perfis para **H2 (dev)** e **Oracle (prod)**.  
Inclui CRUD completo de `Game` e `Player` e relação `Player → Game (ManyToOne)`.

---

## Sumário
- [Stack / Requisitos](#stack--requisitos)
- [Arquitetura & Pacotes](#arquitetura--pacotes)
- [Modelos & Regras](#modelos--regras)
- [Perfis de execução](#perfis-de-execução)
- [Como rodar](#como-rodar)
- [Swagger & H2 Console](#swagger--h2-console)
- [Endpoints & exemplos (cURL)](#endpoints--exemplos-curl)
- [Consultas no Oracle / H2](#consultas-no-oracle--h2)
- [Solução de problemas](#solução-de-problemas)
- [Licença](#licença)

---

## Stack / Requisitos

- **Java 17+**
- **Maven 3.9+**
- Spring Boot: Web, Data JPA, Validation
- Lombok
- springdoc-openapi (Swagger UI)
- **H2** (dev) e **Oracle** (prod)

---

## Arquitetura & Pacotes

```
com.curso.gameapi
├─ controller/         # REST controllers (GameController, PlayerController)
├─ dto/                # DTOs e Mappers (GameRequest/Response, PlayerRequest/Response)
├─ models/             # Entidades JPA (Game, Player)
├─ repository/         # Spring Data JPA (GameRepository, PlayerRepository)
└─ GameapiApplication  # classe main
```

### Lombok
Entidades usam `@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString`.

---

## Modelos & Regras

### Game
- `idGame` (PK, **SEQUENCE**, Oracle-friendly)
- `titulo`, `editora`, `genero`
- `anoLancamento` (**Integer**)

### Player
- `idPlayer` (PK, **SEQUENCE**)
- `nome`
- `gameFav` (**ManyToOne** → `Game`), coluna `GAME_FAV_ID`

> **Nomes de colunas** seguem o padrão do Hibernate (snake_case):  
> `idGame` → `ID_GAME`, `anoLancamento` → `ANO_LANCAMENTO`, etc.

---

## Perfis de execução

### `prod` (padrão) — **Oracle**
`src/main/resources/application-prod.properties`:
```properties
spring.datasource.url=jdbc:oracle:thin:@//oracle.fiap.com.br:1521/orcl
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver

spring.jpa.hibernate.ddl-auto=update
# opcional para debug:
# spring.jpa.show-sql=true
# spring.jpa.properties.hibernate.format_sql=true
```

### `dev` — **H2 em memória**
`src/main/resources/application-dev.properties`:
```properties
spring.datasource.url=jdbc:h2:mem:gameapi;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.username=teste
spring.datasource.password=1234
spring.datasource.driver-class-name=org.h2.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

### Profile ativo por padrão
`src/main/resources/application.properties`:
```properties
spring.profiles.active=prod
springdoc.swagger-ui.path=/swagger-ui
springdoc.swagger-ui.display-request-duration=true
springdoc.swagger-ui.operationsSorter=alpha
springdoc.swagger-ui.tagsSorter=alpha
```

---

## Como rodar

### Pré-requisitos
- Java 17 instalado e no PATH
- Maven instalado
- Banco Oracle acessível (se executar em `prod`)

### Subir aplicação

**Produção (Oracle) – padrão**
```bash
mvn spring-boot:run
```

**Desenvolvimento (H2)**
```bash
# Windows PowerShell
$env:SPRING_PROFILES_ACTIVE='dev'
mvn spring-boot:run
```

> **Porta** padrão: `8080`. Para mudar:  
> `mvn -Dspring-boot.run.jvmArguments="-Dserver.port=8081" spring-boot:run`

---

## Swagger & H2 Console

- **Swagger (dev/prod):**  
  `http://localhost:8080/swagger-ui/index.html`

- **H2 Console (apenas dev):**  
  `http://localhost:8080/h2-console`  
  - JDBC URL: `jdbc:h2:mem:gameapi;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE`  
  - User: `teste` | Password: `1234`

---

## Endpoints & exemplos (cURL)

> **Observação (Windows PowerShell):** use `curl.exe` para evitar o alias.  
> Para **importar no Postman**, use as versões em **uma linha**.

### Games

**Criar**
```bash
curl --request POST --url http://localhost:8080/api/games --header "Content-Type: application/json" --data "{ \"titulo\":\"Zelda\", \"editora\":\"Nintendo\", \"genero\":\"Aventura\", \"anoLancamento\":2017 }"
```

**Listar**
```bash
curl --request GET --url http://localhost:8080/api/games
```

**Buscar por ID**
```bash
curl --request GET --url http://localhost:8080/api/games/1
```

**Atualizar**
```bash
curl --request PUT --url http://localhost:8080/api/games/1 --header "Content-Type: application/json" --data "{ \"titulo\":\"Zelda BOTW\", \"editora\":\"Nintendo\", \"genero\":\"Aventura\", \"anoLancamento\":2018 }"
```

**Deletar**
```bash
curl --request DELETE --url http://localhost:8080/api/games/1
```

### Players

**Criar** (associando a um `Game` existente, ex.: id 2)
```bash
curl --request POST --url http://localhost:8080/api/players --header "Content-Type: application/json" --data "{ \"nome\":\"Link\", \"gameFavId\":2 }"
```

**Listar**
```bash
curl --request GET --url http://localhost:8080/api/players
```

**Buscar por ID**
```bash
curl --request GET --url http://localhost:8080/api/players/1
```

**Atualizar**
```bash
curl --request PUT --url http://localhost:8080/api/players/1 --header "Content-Type: application/json" --data "{ \"nome\":\"Geralt of Rivia\", \"gameFavId\":2 }"
```

**Deletar**
```bash
curl --request DELETE --url http://localhost:8080/api/players/1
```

---

## Consultas no Oracle / H2

**Ver Games**
```sql
SELECT ID_GAME, TITULO, EDITORA, GENERO, ANO_LANCAMENTO
FROM GAME
ORDER BY ID_GAME DESC;
```

**Ver Players + Game favorito**
```sql
SELECT P.ID_PLAYER, P.NOME, P.GAME_FAV_ID, G.TITULO AS GAME_TITULO
FROM PLAYER P
LEFT JOIN GAME G ON G.ID_GAME = P.GAME_FAV_ID
ORDER BY P.ID_PLAYER DESC;
```

**Ver sequences (Oracle)**
```sql
SELECT SEQUENCE_NAME FROM USER_SEQUENCES
WHERE SEQUENCE_NAME IN ('SEQ_GAME','SEQ_PLAYER');
```

---

## Solução de problemas

- **PowerShell quebra `-Dspring-boot.run.profiles=...`**  
  Use variável de ambiente:  
  `"$env:SPRING_PROFILES_ACTIVE='dev'; mvn spring-boot:run"`  
  ou modo literal: `--% mvn -Dspring-boot.run.profiles=dev spring-boot:run`

- **H2 console 404**  
  Só existe no **dev** (`/h2-console`). Em prod (Oracle) é esperado 404.

- **ORA-00904 (identificador inválido)**  
  Use nomes **snake_case**: `ID_GAME`, `ANO_LANCAMENTO`, etc.

- **Faltando validação (jakarta.validation.NoProviderFoundException)**  
  Garanta a dependência `spring-boot-starter-validation` no `pom.xml`.

- **Erro de codificação em `.properties`**  
  Salve como **UTF-8** e desabilite filtering no `pom.xml`:
  ```xml
  <resources>
    <resource>
      <directory>src/main/resources</directory>
      <filtering>false</filtering>
    </resource>
  </resources>
  ```

---

## Licença
Uso educacional/livre. Adapte conforme sua necessidade.
