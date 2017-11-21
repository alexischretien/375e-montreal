# inf4375-projet-e2017

Projet d'un site web basé sur le framework Spring-Boot utilisant des interfaces
programmatiques de type REST HTTP/JSON dans le cadre du cours INF4375 - Paradigmes 
des échanges internet.

Le site web offre des fonctionnalités permettant d'obtenir des informations
relatives aux activités de 375e de la Ville de Montréal ainsi que des stations bixis
et des pistes cyclables. 

Le système utilise une base de données Postgrsql pour sauvegarder les informations
des activités, des stations bixis et des pistes cyclables. Des requêtes à l'API de 
géocodage de Google sont périodiquement envoyées pour récupérer les coordonnées 
géographiques des activités du 375e.

## Auteur

Alexis Chrétien (CHRA25049209)

## Prérequis

- Java 1.8+
- Maven 3.0+
- PostgreSQL 9.4+ avec PostGIS 2.0+

## PostgreSQL

- [Installation de PostgreSQL et de PostGIS pour Windows](http://www.bostongis.com/PrinterFriendly.aspx?content_name=postgis_tut01)
- [PostgreSQL.app pour OSX](http://postgresapp.com/)

## Installation, compilation et exécution

- Vérifier que le mode d'autentification de l'utilisateur postgresql `postgres`
  est de type `md5` dans le fichier `pg\_bha.conf`:

    ```
     /etc/postgresql/9.X/main/pg\_hba.conf
    ```
- Assurez-vous que le mot de passe de l'utilisateur `postgres` soit `postgres`
- Installation de la base de données, des tables et des triggers : 

    ```
     psql -U postgres -f migrations/create-database.sql
     psql -U postgres -f migrations/create-schema.sql screencasts
    ```
- Compilation et exécution : 

    ```
     mvn compile
     mvn spring-boot:run
    ```

Le projet est alors disponible à l'adresse [http://localhost:8080/](http://localhost:8080/)

## Status

- [ ] Module A
  - [ ] A1 (5XP)
  - [x] A2 (8XP)
  - [x] A3 (5XP)
  - [ ] A4 (5XP)
- [ ] Module B
  - [ ] B1 (5XP)
  - [ ] B2 (5XP)
  - [ ] B3 (5XP)
  - [ ] B4 (2XP)
- [ ] Module C
  - [x] C1 (5XP)
  - [x] C2 (2XP)
  - [x] C3 (5XP)
  - [ ] C4 (5CP)
- [ ] Module D
  - [x] D1 (5XP)
  - [x] D2 (10XP)
  - [ ] D3 (5XP)
- [ ] Module E
  - [ ] E1 (10XP)
  - [ ] E2 (5XP)
  - [ ] E3 (10XP)
- [ ] Module F
  - [ ] F1 (5XP)
  - [ ] F2 (8XP)
  - [ ] F3 (2XP)
  - [ ] F4 (10XP)
- [ ] Module G
  - [ ] G1 (10XP)

## Routes disponibles

- [http://localhost:8080/activites-375e](http://localhost:8080/activites-375e) 
- [http://localhost:8080/stations-bixi](http://localhost:8080/stations-bixi/)
- [http://localhost:8080/pistes-cyclables](http://localhost:8080/pistes-cyclables/)

Veuillez vous référer au fichier `375e.raml` pour plus d'informations sur les APIs de
l'application web. 
