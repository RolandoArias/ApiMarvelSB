## ApiRest

Configuraciones necesarias para que el proyecto pueda funcionar

Archivo
src/main/resources/application.properties

### Credenciales Marvel

marvel.api.publicKey=2d6ac0b8a2d80a1752e0f8fc6af5d346 

marvel.api.privateKey=6ecc018de90eff2aedfd9130fe4c8938bfed3e07

marvel.api.baseUrl=http://gateway.marvel.com/v1/public


### Credenciales de BD

spring.datasource.url=jdbc:mysql://localhost:3306/marvel

spring.datasource.username=rolando

spring.datasource.password=password

### Se adunta los assets necesarios, como script de bd, y la colleccion de Postman, para el uso de la api
/assets/ApiMarvel.sql

/assets/MarvelApi.postman_collection 
