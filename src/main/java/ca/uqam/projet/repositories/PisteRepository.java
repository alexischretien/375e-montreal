/*
 * UQAM - Été 2017 - INF4375 - Groupe 30 - Projet de session
 *
 * PisteRepository.java - Fichier source .java de la classe PisteRepository
 * offrant les services d'insertion et de recherche d'entrées de pistes 
 * cyclables dans la table "pistes" de la base de données "screencasts".
 *
 * @Auteur  Alexis Chrétien (CHRA25049209)
 * @Version 21 juillet 2017
 */

package ca.uqam.projet.repositories;
import ca.uqam.projet.resources.*;

import java.util.*;
import java.util.stream.*;
import java.sql.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.dao.*;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.*;
import org.springframework.stereotype.*;

@Component
public class PisteRepository {

  @Autowired private JdbcTemplate jdbcTemplate;
  
  private static final String FIND_BY_RADIUS =
      " select"
    + "     id, id_trc_geobase, type_voie, type_voie2, longueur,"
    + "     nbr_voie, separateur, saisons4, protege_4s, ville_mtl,"
    + "     nom_arr_ville, coordinates"
    + " from"
    + "   pistes"
    + " where"
    + "   ST_Distance("
    + "      Geography(geom_multilinestring)," 
    + "      Geography(ST_GeomFromText(?,4326))) <= ?"
    ;

  /*
   * findByRadius - méthode permettant de retourner la liste des pistes se
   * trouvant à l'intérieur d'un certain rayon d'un point géométrique.
   *
   * @param  rayon   Le rayon en mètres
   * @param  lng     La longitude du point géométrique
   * @param  lat     La latitude du point géométrique
   * @return         La liste de pistes
   */
  public List<Piste> findByRadius(double rayon, double lng, double lat) {

    return jdbcTemplate.query(conn -> {
      PreparedStatement ps = conn.prepareStatement(FIND_BY_RADIUS);
      ps.setString(1, String.format("POINT(%f %f)", lng, lat));
      ps.setDouble(2, rayon);
      return ps;
    }, new PisteRowMapper());
  } 
  
  private static final String INSERT =

      " insert into pistes (id, id_trc_geobase, type_voie, type_voie2,"
    + "                     longueur, nbr_voie, separateur, saisons4," 
    + "                     protege_4s, ville_mtl, nom_arr_ville," 
    + "                     coordinates, geom_multilinestring)"
    + " values (?,?,?,?,?,?,?,?,?,?,?,?,ST_Transform(ST_GeomFromText(?, 2950), 4326))"
    + " on conflict (id) do update"
    + " set"  
    + "       id_trc_geobase = ?"
    + "     , type_voie      = ?"
    + "     , type_voie2     = ?"
    + "     , longueur       = ?"
    + "     , nbr_voie       = ?"
    + "     , separateur     = ?"
    + "     , saisons4       = ?"
    + "     , protege_4s     = ?"
    + "     , ville_mtl      = ?"
    + "     , nom_arr_ville  = ?"
    + "     , coordinates    = ?" 
    + "     , geom_multilinestring = ST_Transform(ST_GeomFromText(?, 2950), 4326)"
    ;

  /*
   * insert - méthode permettant d'insérer ou de mettre à jour une entrée de 
   * piste cyclable
   *
   * @param      La piste à insérer dans la base de données
   * @return     Le nombre de lignes affectées par l'insertion ou la mise à jour
   */
  public int insert(Piste piste) {

    return jdbcTemplate.update(conn -> {
      PreparedStatement ps = conn.prepareStatement(INSERT);

      ps.setDouble(1,  piste.getId());
      ps.setDouble(2,  piste.getId_trc_geobase());
      ps.setDouble(3,  piste.getType_voie());
      ps.setDouble(4,  piste.getType_voie2());
      ps.setDouble(5,  piste.getLongueur());
      ps.setDouble(6,  piste.getNbr_voie());
      ps.setString(7,  piste.getSeparateur());
      ps.setString(8,  piste.getSaisons4());
      ps.setString(9,  piste.getProtege_4s());
      ps.setString(10, piste.getVille_mtl());
      ps.setString(11, piste.getNom_arr_ville());
      ps.setString(12, piste.printCoordinates(true));
      ps.setString(13, piste.printCoordinates(false)); 

      ps.setDouble(14, piste.getId_trc_geobase());
      ps.setDouble(15, piste.getType_voie());
      ps.setDouble(16, piste.getType_voie2());
      ps.setDouble(17, piste.getLongueur());
      ps.setDouble(18, piste.getNbr_voie());
      ps.setString(19, piste.getSeparateur());
      ps.setString(20, piste.getSaisons4());
      ps.setString(21, piste.getProtege_4s());
      ps.setString(22, piste.getVille_mtl());
      ps.setString(23, piste.getNom_arr_ville());
      ps.setString(24, piste.printCoordinates(true));
      ps.setString(25, piste.printCoordinates(false));
      return ps;
    });
  }
} 

/*
 * Classe PisteRowMapper pour la sérialisation d'entrées de pistes cyclables
 * de la base de données vers des objets de la classe "Piste".
 */
class PisteRowMapper implements RowMapper<Piste> {
  public Piste mapRow(ResultSet rs, int rowNum) throws SQLException {
    Piste piste =  new Piste(
        rs.getDouble("id")
      , rs.getDouble("id_trc_geobase")
      , rs.getDouble("type_voie")
      , rs.getDouble("type_voie2")
      , rs.getDouble("longueur")
      , rs.getDouble("nbr_voie")
      , rs.getString("separateur")
      , rs.getString("saisons4")
      , rs.getString("protege_4s")
      , rs.getString("ville_mtl")
      , rs.getString("nom_arr_ville")
      , null
    ); 
    piste.setCoordinates(rs.getString("coordinates"));
    return piste;
  } 
}
