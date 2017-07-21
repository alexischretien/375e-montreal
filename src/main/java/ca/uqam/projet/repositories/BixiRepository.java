/*
 * UQAM - Été 2017 - INF4375 - Groupe 30 - Projet de session
 * BixiRepository.java - Fichier source .java de la classe BixiRepository
 * offrant les services d'insertion et de recherche d'entrées de stations
 * bixi dans la table "bixis" de la base de données "screencasts".
 *
 * @Auteur  Alexis Chrétien (CHRA25049209)
 * @Version 21 juillet 2017
 */

package ca.uqam.projet.repositories;

import java.util.*;
import java.util.stream.*;
import java.sql.*;

import ca.uqam.projet.resources.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.dao.*;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.*;
import org.springframework.stereotype.*;

@Component
public class BixiRepository {

  @Autowired private JdbcTemplate jdbcTemplate;

  private static final String FIND_BY_RADIUS =
      " select"
    + "     id, s,  n,  st, b,  su, m,  lu, lc,"
    + "     bk, bl, la, lo, da, dx, ba, bx"
    + " from"
    + "   bixis"
    + " where"
    + "   ba >= ? and"
    + "   ST_Distance_Sphere(geometry_point, ST_GeomFromText(?,4326)) <= ?"
    ;

/*
 * findByRadius - méthode permettant de récupérer les informations des
 * stations bixis se trouvant à l'intérieur d'un certain rayon d'un
 * point géométrique et qui ont au moins un certain nombre de vélos
 * disponibles. 
 *
 * @param  min_bixis_dispo   Le nombre de vélos minimal
 * @param  rayon             Le rayon 
 * @param  lng               La longitude du point géométrique
 * @param  lat               La latitude du point géométrique
 * @return                   La liste de stations bixis. 
 */
  public List<Bixi> findByRadius(int min_bixi_dispo, double rayon, 
                                 double lng, double lat) {

    return jdbcTemplate.query(conn -> {
      PreparedStatement ps = conn.prepareStatement(FIND_BY_RADIUS);
      ps.setInt   (1, min_bixi_dispo);
      ps.setString(2, String.format("POINT(%f %f)", lng, lat));
      ps.setDouble(3, rayon);
      return ps;
    }, new BixiRowMapper());
  } 

  private static final String FIND_BY_ID_STMT =
      " select"
    + "     id, s,  n,  st, b,  su, m,  lu, lc,"
    + "     bk, bl, la, lo, da, dx, ba, bx"
    + " from"
    + "   bixis"
    + " where"
    + "   id = ?"
    ;
  
  /*
   * findById - méthode permettant de récupérer les informations d'une station
   * bixi selon son identifiant
   *
   * @param  id     L'identifiant de la station bixi.
   * @return        L'objet de clase Bixi associé à l'identifiant 
   */
  public Bixi findById(int id) {

    Bixi bixi = null;

    try {
      bixi = jdbcTemplate.queryForObject(FIND_BY_ID_STMT, new Object[]{id}, new BixiRowMapper());
    }
    catch (EmptyResultDataAccessException e) {}

    return bixi;
  }
  

  private static final String INSERT_STMT =
      " insert into bixis (id, s,  n,  st, b,  su, m,  lu, lc,"
    + "                    bk, bl, la, lo, da, dx, ba, bx, geometry_point)"
    + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,ST_GeomFromText(?, 4326))"
    + " on conflict (id) do update"
    + " set"  
    + "       s  = ?"
    + "     , n  = ?"
    + "     , st = ?"
    + "     , b  = ?"
    + "     , su = ?"
    + "     , m  = ?"
    + "     , lu = ?"
    + "     , lc = ?"
    + "     , bk = ?"
    + "     , bl = ?"
    + "     , la = ?"
    + "     , lo = ?"
    + "     , da = ?" 
    + "     , dx = ?"
    + "     , ba = ?"
    + "     , bx = ?"
    + "     , geometry_point = ST_GeomFromText(?,4326)"
    ;

  /*
   * insert - méthode permettant d'insérer ou de mettre à jour une entrée
   * de station bixi.
   *
   * @param bixi   La station bixi à insérer dans la base de données
   * @return       Le nombre de lignes affectées par l'insertion ou la 
   *               mise à jour
   */
  public int insert(Bixi bixi) {
    return jdbcTemplate.update(conn -> {
      PreparedStatement ps = conn.prepareStatement(INSERT_STMT);
      ps.setInt    (1,  bixi.getId());
      ps.setString (2,  bixi.getNom());
      ps.setString (3,  bixi.getIdTerminal());
      ps.setInt    (4,  bixi.getEtat());
      ps.setBoolean(5,  bixi.getEstBloquee());
      ps.setBoolean(6,  bixi.getEstSuspendue());
      ps.setBoolean(7,  bixi.getEstHorsService());
      ps.setLong   (8,  bixi.getDerniereMiseAJour());
      ps.setLong   (9,  bixi.getDerniereCommunication());
      ps.setBoolean(10, bixi.getBk());
      ps.setBoolean(11, bixi.getBl());
      ps.setDouble (12, bixi.getLatitude());
      ps.setDouble (13, bixi.getLongitude());
      ps.setInt    (14, bixi.getNbBornesDisponibles());
      ps.setInt    (15, bixi.getNbBornesIndisponibles());
      ps.setInt    (16, bixi.getNbVelosDisponibles());
      ps.setInt    (17, bixi.getNbVelosIndisponibles());
      ps.setString (18, String.format("POINT(%f %f)", bixi.getLongitude(), bixi.getLatitude()));

      ps.setString (19, bixi.getNom());
      ps.setString (20, bixi.getIdTerminal());
      ps.setInt    (21, bixi.getEtat());
      ps.setBoolean(22, bixi.getEstBloquee());
      ps.setBoolean(23, bixi.getEstSuspendue());
      ps.setBoolean(24, bixi.getEstHorsService());
      ps.setLong   (25, bixi.getDerniereMiseAJour());
      ps.setLong   (26, bixi.getDerniereCommunication());
      ps.setBoolean(27, bixi.getBk());
      ps.setBoolean(28, bixi.getBl());
      ps.setDouble (29, bixi.getLatitude());
      ps.setDouble (30, bixi.getLongitude());
      ps.setInt    (31, bixi.getNbBornesDisponibles());
      ps.setInt    (32, bixi.getNbBornesIndisponibles());
      ps.setInt    (33, bixi.getNbVelosDisponibles());
      ps.setInt    (34, bixi.getNbVelosIndisponibles());
      ps.setString (35, String.format("POINT(%f %f)", bixi.getLongitude(), bixi.getLatitude()));
      return ps;
    });
  }
} 

/*
 * Classe BixiRowMapper pour la sérialisation d'entrées de bixis de la base
 * de données vers des objets de classe "Bixi".
 */
class BixiRowMapper implements RowMapper<Bixi> {
  public Bixi mapRow(ResultSet rs, int rowNum) throws SQLException {
    return new Bixi(
        rs.getInt("id")
      , rs.getString("s")
      , rs.getString("n")
      , rs.getInt("st")
      , rs.getBoolean("b")
      , rs.getBoolean("su")
      , rs.getBoolean("m")
      , rs.getLong("lu")
      , rs.getLong("lc")
      , rs.getBoolean("bk")
      , rs.getBoolean("bl")
      , rs.getDouble("la")
      , rs.getDouble("lo")
      , rs.getInt("da")
      , rs.getInt("dx")
      , rs.getInt("ba")
      , rs.getInt("bx")
    ); 
  } 
}
