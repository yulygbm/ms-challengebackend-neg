# API Rest en Spring Boot
API Rest en Spring Boot con Java 21.

## Propiedad 
 - Yuly Botina Muñoz

## Operaciones
Funcionalidades por endpoints.

| Operación                              | Método | Descripción Capacidad                                              |
|----------------------------------------|--------|--------------------------------------------------------------------| 
| /calculo-dinamico/suma                 | GET    | Permite realizar la suma de dos números más un porcentaje dinámico |
| /calculo-dinamico/peticiones/historial |  GET   | Consulta el historial de las peticiones realizadas por el cliente  |

## 3. Dependencias
* [Spring Web](https://docs.spring.io/spring-boot/3.4.4/reference/web/servlet.html)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/3.4.4/reference/actuator/index.html)
* [Flyway Migration](https://docs.spring.io/spring-boot/3.4.4/how-to/data-initialization.html#howto.data-initialization.migration-tool.flyway)
* [okhttp3](Para llamada Http haciendo uso de Mock Web Server)
* [Spring Retry](Para reintentos automáticos hacia llamada Http)
* [Spring Redis](Para manejo de caché)

## Ciclo de Vida

### Configuraciones
Detalla las configuraciones necesarias:

#### Propiedades
| Clave                                     | Valor                                  | Descripción                                                                                                     | 
|-------------------------------------------|----------------------------------------|-----------------------------------------------------------------------------------------------------------------|
| external.api-porcentaje.url               | http://api-externa/porcentaje-dinamico | Url del servicio externo que provee el valor del porcentaje dinámico.                                           |
| external.api-porcentaje.retry.maxAttempts | 3                                      | Cantidad máxima de reintentos hacia el servicio externo que provee el valor del porcentaje dinámico.            |
| external.api-porcentaje.retry.maxDelay    | 2000                                   | Tiempo de espera ante un nuevo reintento hacia el servicio externo que provee el valor del porcentaje dinámico. |


## Docker & Docker Compose

Dockerfile:

```
FROM openjdk:21-jdk
ADD app/build/libs/app.jar /app/app.jar
WORKDIR /app
ENTRYPOINT ["java","-jar", "app.jar"]
```

```yml

services:
  db:
    image: "postgres"
    ports:
      - "5432:5432"
    environment:
      POSTGRES_DB: postgres
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres.2025
  cache:
    image: "redis"
    ports:
      - "6379:6379"
    environment:
      - ALLOW_EMPTY_PASSWORD=yes
      - REDIS_DISABLE_COMMANDS=FLUSHDB,FLUSHALL
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://db/postgres?currentSchema=calculos
      SPRING_DATASOURCE_USERNAME: postgres
      SPRING_DATASOURCE_PASSWORD: postgres.2025
      SPRING_REDIS_HOST: cache
      SPRING_REDIS_PORT: 6379
    depends_on:
      - db
      - cache
```

## Construir & Ejecutar Applicación

* Construir Java Jar.

```shell
 ./gradlew build
```

* Ejecutar Java Jar.

```shell
 ./gradlew bootRun
```

*  Docker Compose Build and Run

Construye una imagen y lo etiqueta
```shell
 docker build --build-arg JARFILE=app/build/libs/app.jar -t springio/api-rest-docker
```

Construye y corre la aplicación
```shell
 docker build -t springio/api-rest-docker . docker run -p 8080:8080 springio/api-rest-docker
```

Construir uma imagen Docker con Gradle
```shell
 ./gradlew bootBuildImage -- springioimagenName=springio/gs-api-rest-docker
```

Archivo compose
```shell
$ docker-compose build --no-cache
$ docker-compose up --force-recreate

```

## Endpoints
### Permite realizar la suma de dos números más un porcentaje dinámico.
```shell
   verbo: POST
   url:  /calculo-dinamico/suma
   header: Content-Type: application/json
   body: { "numero1": 1, "numero2": 2}
```

```shell   
 - curl -X POST -H 'Content-Type: application/json' -d '{ "numero1": 1, "numero2": 2} \
   http://localhost:8080/calculo-dinamico/suma
```

#### Suma - OK

![suma-ok](https://github.com/user-attachments/assets/2215b068-07fa-4429-a14b-ca77f42cfd33)

![suma-ok-request](https://github.com/user-attachments/assets/24d8cb25-e618-4e67-92bc-f20face4916c)

![suma-ok-response](https://github.com/user-attachments/assets/ec631a37-58ac-45e3-a79a-44aa84fa3567)


#### Suma - NOK

La url está incorrecta, le falta la letra a al final de suma
![suma-url-mala](https://github.com/user-attachments/assets/10ecb825-922d-42a4-aea9-98ac6e449a38)

El parámetro de numero1 tiene la letra "a"
![suma-nok-numero1-letra-a](https://github.com/user-attachments/assets/96949693-a0cf-4140-b696-cd844d2239fc)

![suma-nok-numero1-letra](https://github.com/user-attachments/assets/dc305568-cf51-4954-a2cb-83c9c79cee90)


### Consulta el historial de las peticiones realizadas por el cliente.
#### Consulta todo el historial de peticiones - OK
```shell
   verbo: GET
   url:  /calculo-dinamico/peticiones/historial
```
![consulta-ok-todo-el-historial-peticiones](https://github.com/user-attachments/assets/5f8ef20b-bd6d-44f4-b142-9c7bdd5ff78a)


#### Consulta el historial de peticiones por paginación - OK
```shell
   verbo: GET
   url:  /calculo-dinamico/peticiones/historial?offset=0&limit=1
```

![consulta-ok-todo-el-historial-peticiones-con-paginacion3](https://github.com/user-attachments/assets/b73cfffa-bfd6-430c-a139-c2d870a5676a)

![consulta-ok-todo-el-historial-peticiones-con-paginacion2](https://github.com/user-attachments/assets/ec7721ba-1137-4738-adb7-702691856d3f)

![consulta-ok-todo-el-historial-peticiones-con-paginacion1](https://github.com/user-attachments/assets/880f32ed-01dd-4edb-9b8a-410f5a43e055)


#### Consulta historial de peticiones con offset incorrecto - NOK
```shell
   verbo: GET
   url:  /calculo-dinamico/peticiones/historial?offset=a&limit=1
```

![consulta-nok-con-offset-no-numerico](https://github.com/user-attachments/assets/c0e25193-b75b-4c36-b5db-b10bd9dabc56)

![consulta-nok-con-offset-letra](https://github.com/user-attachments/assets/a69ebbf1-f7f1-4e34-81b1-bd79b66c3278)
