drop trigger if exists changement_lieux on activites;
/*drop trigger if exists changement_coord on bixis; */

/*
 *
 */
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

/*
 *
create or replace function mise_a_jour_geom()
   returns trigger as
$$
begin
update bixis
    set geometry = ST_SetSRID(ST_MakePoint(new.la,new.lo),4326)
    where id = old.id;
return new;
end; 
$$
language 'plpgsql'
 *
 */
create trigger changement_lieux
    after update on activites
    for each row
    when (old.lieux is distinct from new.lieux) 
    execute procedure mise_a_jour_lieux();

/*
 *
create trigger changement_coord
    after insert or update on bixis
    for each row
    when (old.lo is distinct from new.lo or
          old.la is distinct from new.la)
    execute procedure mise_a_jour_geom();
*/
