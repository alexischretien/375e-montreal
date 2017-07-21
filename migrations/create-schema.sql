/*
 * UQAM - Été 2017 - INF4375 - Groupe 30 - Projet de session
 *
 * create-schema.sql - Script .sql générant les différentes tables et 
 * triggers de la base de données du site web. 
 *
 * @Auteur  : Alexis Chrétien - CHRA25049209 
 * @Version : 8 juin 2016
 *
 */

drop table if exists activites;
create table activites (
    id                         int   primary key 
  , nom                        text  
  , description                text
  , arrondissement_ou_pole     text
  , gratuit_ou_payant          text
  , interets_ou_type_evenement text
  , interieur_ou_exterieur     text
  , dates                      text
  , dates_formelles            text
  , lieux                      text
  , geo_a_jour                 bool
);

select AddGeometryColumn ('public', 'activites', 'geometry_point', 4326,  'POINT', 2, false);

drop table if exists bixis;

create table bixis (
    id  int     primary key
  , s   text
  , n   text
  , st  int
  , b   bool
  , su  bool
  , m   bool
  , lu  bigInt
  , lc  bigInt
  , bk  bool
  , bl  bool
  , la  double precision
  , lo  double precision
  , da  int
  , dx  int
  , ba  int
  , bx  int
);

select AddGeometryColumn ('public', 'bixis', 'geometry_point', 4326, 'POINT', 2, false); 


drop table if exists pistes;

create table pistes (
    id             double precision  primary key
  , id_trc_geobase double precision
  , type_voie      double precision
  , type_voie2     double precision
  , longueur       double precision
  , nbr_voie       double precision
  , separateur     text
  , saisons4       text
  , protege_4s     text
  , ville_mtl      text
  , nom_arr_ville  text
  , coordinates    text
);

select AddGeometryColumn ('public', 'pistes', 'geom_multilinestring', 4326, 'MULTILINESTRING', 3, false);


 create or replace function mise_a_jour_lieux()
      returns trigger as
 $$
 begin
 update activites
     set geo_a_jour = false
     where id = old.id;
 return new;
 end;
 $$
 language 'plpgsql';
 

 create trigger changement_lieux
     after update on activites
     for each row
     when (old.lieux is distinct from new.lieux)
     execute procedure mise_a_jour_lieux();
