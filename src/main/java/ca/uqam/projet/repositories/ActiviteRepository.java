/*
 * UQAM - Été 2017 - INF4375 - Groupe 30 - Projet de session
 *
 *  ActiviteRepository.java - Fichier source .java de la classe ActiviteRepository
 *  offrant les services d'insertion et de recherche d'entrées d'activités dans la
 *  table "activites" de la base de données "screencasts". 
 *
 *  @Auteur Alexis Chrétien (CHRA25049209)
 *  @Version 8 juin 2017
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
public class ActiviteRepository {

  @Autowired private JdbcTemplate jdbcTemplate;

  private static final String FIND_FOR_GEO_UPDATE =
      " select"
    + "    *"  
    + " from"
    + "    activites"
    + " where"
    + "    geo_a_jour = false and"
    + "    lieux is not null and"
    + "    lieux <> '' "
    + " limit 25"
    ; 
  
  /*
   * findForGeoUpdate - méthode permettant de récupérer au plus 25 entrées
   * d'activités dont les coordonnées géométriques ne sont pas à jour. 
   *
   * @return  Une liste contenant au plus 25 entrées d'activités dont les
   *          coordonnées géométriques ne sont pas à jour.
   */
  public List<Activite> findForGeoUpdate() {
    return jdbcTemplate.query(FIND_FOR_GEO_UPDATE, new ActiviteRowMapper());
  }

  private static final String INSERT_STMT =
      " insert into activites (id, nom, description, arrondissement_ou_pole, gratuit_ou_payant," 
    + "                        interets_ou_type_evenement, interieur_ou_exterieur, dates,"
    + "                        dates_formelles, lieux, geo_a_jour)"
    + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, false)"
    + " on conflict (id) do update"
    + " set nom = ?"
    + "     , description = ?"
    + "     , arrondissement_ou_pole  = ?"
    + "     , gratuit_ou_payant = ?"
    + "     , interets_ou_type_evenement = ?"
    + "     , interieur_ou_exterieur = ?"
    + "     , dates = ?"
    + "     , dates_formelles = ?"
    + "     , lieux = ?"
    ;
  /*
   * insert - méthode permettant d'insérer une activité au de mettre à jour 
   * une entrée d'activité dans la table "activités". 
   *
   * S'il s'agit d'une insertion, la colonne de point géométrique sera vide :
   * une requête éventuelle vers l'API de géocodage de Google sera nécéssaire :
   * geo_a_jour = false.
   * S'il s'agit d'une mise à jour, un trigger vérifira si la mise à jour modifie
   * la valeur de lieux. Si le lieu est modifié, le trigger se chargera de mettre
   * le flag "geo_a_jour" à false. Si le lieu n'est pas modifié, le flag "geo_a_jour"
   * reste inchangé.
   *
   * @param  activite   L'activite à insérer ou à mettre à jour
   * @return            Le nombre de lignes de la table "activites" affectée 
   *                    par l'insertion ou à mise à jour
   */
  public int insert(Activite activite) {
    return jdbcTemplate.update(conn -> {
      PreparedStatement ps = conn.prepareStatement(INSERT_STMT);

      ps.setInt(1, activite.getId());
      ps.setString(2,  activite.getNom());
      ps.setString(3,  activite.getDescription());
      ps.setString(4,  activite.getArrondissement_ou_pole());
      ps.setString(5,  activite.getGratuit_ou_payant());
      ps.setString(6,  activite.getInterets_ou_type_evenement());
      ps.setString(7,  activite.getInterieur_ou_exterieur());
      ps.setString(8,  activite.getDates());
      ps.setString(9,  DateInterval.listAsString(activite.getDates_formelles()));
      ps.setString(10, activite.getLieux());

      ps.setString(11, activite.getNom());
      ps.setString(12, activite.getDescription());
      ps.setString(13, activite.getArrondissement_ou_pole());
      ps.setString(14, activite.getGratuit_ou_payant());
      ps.setString(15, activite.getInterets_ou_type_evenement());
      ps.setString(16, activite.getInterieur_ou_exterieur());
      ps.setString(17, activite.getDates());
      ps.SetString(18, DateInterval.listAsString(activite.getDates_formelles()));
      ps.setString(19, activite.getLieux());
      return ps;
    });
  }

  private static final String UPDATE_GEO = 
      " update activites"
    + "     set geometry_point = ST_GeomFromText(?,4326)"
    + "       , geo_a_jour = true"
    + "     where id = ?"
    ;
  
  /*
   * update_geo - méthode permettant de mettre à jour la valeur de point
   * géométrique d'une entrée d'activité de la table "activites". 
   *
   * Met également la valeur associée de la colonne "geo_a_jour" à "true".
   *
   * @param  id   L'identifiant de l'activité
   * @param  lng  La valeur de longitude associée à l'activité 
   * @param  lat  La valeur de latitude associée à l'activité
   * @return      Le nombre de lignes affectées par la mise à jour
   */
  public int update_geo(int id, double lng, double lat) {
    return jdbcTemplate.update(conn -> {
      PreparedStatement ps = conn.prepareStatement(UPDATE_GEO);
      ps.setString(1, String.format("POINT(%f %f)", lng, lat));
      ps.setInt   (2, id);
      return ps;
    });
  }
  
 
  private static final String UPDATE_NO_GOOGLE_RESULT = 
      " update activites"
    + "     set geo_a_jour = true"
    + "     where id = ?"
    ;
   
  /*
   * update_noGoogleResult - Methode permettant de mettre à "true" la valeur
   * de "geo_a_jour" associée à une entrée d'activité de la table "activites".
   *
   * La méthode est appelée si une requête vers l'API de géocodage de Google ne
   * retourne aucun résultat ou si le résultat est invalide. Permet d'éviter
   * que la méthode "findForGeoUpdate" retourne les mêmes entrées d'activités
   * si on sait que Google est incapable de retourner un résultat valide pour
   * la valeur "lieux" de cette activité. 
   *
   * @param  id   L'identifiant de l'activité
   * @return      Le nombre de lignes affectée par la modification
   */
  public int update_noGoogleResult(int id) {
    return jdbcTemplate.update(conn -> {
      PreparedStatement ps = conn.prepareStatement(UPDATE_NO_GOOGLE_RESULT);
      ps.setInt(1, id);
      return ps;
    });
  }
}

/*
 * Classe ActiviteRomMapper pour la sérialisation d'entrées d'activités de la base 
 * de données vers des objets de classe "Activite"
 */
class ActiviteRowMapper implements RowMapper<Activite> {
  public Activite mapRow(ResultSet rs, int rowNum) throws SQLException {
    return new Activite(
        rs.getInt("id")
      , rs.getString("nom")
      , rs.getString("description")
      , rs.getString("arrondissement_ou_pole")
      , rs.getString("gratuit_ou_payant")
      , rs.getString("interets_ou_type_evenement")
      , rs.getString("interieur_ou_exterieur")
      , rs.getString("dates")
      , DateInterval.stringAsList(rs.getString("dates_formelles"));
      , rs.getString("lieux")
    );
  }
}
