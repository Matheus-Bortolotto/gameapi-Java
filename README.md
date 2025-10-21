# gameapi (com perfis dev H2 e prod MySQL)

## Rodar (perfil DEV/H2)
```bash
./mvnw spring-boot:run
# ou
mvn spring-boot:run
```
- H2 Console: http://localhost:8080/h2 (user: **teste**, pass: **1234**)
- Swagger UI: http://localhost:8080/swagger-ui/index.html

## Rodar (perfil PROD/MySQL)
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
# ou
java -jar target/gameapi-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```
Configure as variáveis de ambiente ou edite `src/main/resources/application-prod.properties`:
- `spring.datasource.url`
- `spring.datasource.username`
- `spring.datasource.password`

## Endpoints

**Games**
- `GET /api/games`
- `GET /api/games/{id}`
- `POST /api/games`
- `PUT /api/games/{id}`
- `DELETE /api/games/{id}`

**Players**
- `GET /api/players`
- `GET /api/players/{id}`
- `POST /api/players`
- `PUT /api/players/{id}`
- `DELETE /api/players/{id}`

## Notas
- Projeto atualizado para usar **Lombok** nas entidades (`@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@ToString`).
- Perfil **dev** usa H2 conforme solicitado; perfil **prod** usa MySQL.
- Foram adicionados controladores CRUD completos para *Game* e *Player*, além de DTOs e mapeadores de *Player*.

## Perfil ORACLE
Configure `src/main/resources/application-oracle.properties` e rode:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=oracle
# ou
java -jar target/gameapi-0.0.1-SNAPSHOT.jar --spring.profiles.active=oracle
```
