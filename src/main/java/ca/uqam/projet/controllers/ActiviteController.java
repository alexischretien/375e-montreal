/*  
 * UQAM - Été 2017 - INF4375 - Groupe 30 - Projet de session
 *
 * ActiviteController.java - Fichier source .java de la classe 
 *                           ActiviteController, offrant un API
 *                           pour traiter des requêtes relatives
 *                           aux activités stockées dans la base
 *                           de données
 * 
 * @Auteur Alexis Chrétien (CHRA25049209)
 * @Version 21 juillet 2017
 */
package ca.uqam.projet.controllers;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import ca.uqam.projet.repositories.*;
import ca.uqam.projet.resources.*;
import ca.uqam.projet.resources.Date.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;


@RestController
public class ActiviteController {


  public static final double RADIUS_DEFAULT = 5000;
  public static final double LAT_DEFAULT = 45.5089306;
  public static final double LNG_DEFAULT = -73.5685676;
  public static final String INVALID_DU_DATE_FORMAT = "Format de la date 'du' invalide";
  public static final String INVALID_AU_DATE_FORMAT = "Format de la date 'au' invalide";
  public static final String INVALID_DATE_ORDER = "Ordre des dates invalide";

  @Autowired ActiviteRepository repository;

  @RequestMapping(value = "/activites-375e", method = RequestMethod.GET)
  public ResponseEntity<Map<String, Object>> findActivites(
                                      @RequestParam(value = "du", 
                                                    required = false) String du, 
                                      @RequestParam(value = "au", 
                                                    required = false) String au,
                                      @RequestParam(value = "rayon", 
                                                    required = false) Double rayon,
                                      @RequestParam(value = "lat",
                                                    required = false) Double lat,
                                      @RequestParam(value = "lng",
                                                    required = false) Double lng) {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate localDate = LocalDate.now();   
    String today = localDate.format(formatter);
    String tomorrow = localDate.plusDays(1).format(formatter);

    ResponseEntity<Map<String, Object>> response; 
    List<Activite> activites = new ArrayList<Activite>();

    // "si aucune date n'a été donnée, le système ne doit pas appliquer les valeurs
    // par défaut associées à ces valeurs"
    if (du == null && au != null) 
      du = today;
    if (au == null && du != null)
      au = tomorrow;

    List<String> errors = validateRequestParameters(du, au, rayon, lng, lat);
    
    // si les paramètres sont invalides
    if (errors.size() > 0) {
      response = new ResponseEntity(new Erreur(400, errors), null, HttpStatus.valueOf(400));
    }
    // si les pramètres sont valides
    else {
      // "Si aucun rayon et aucune coordonnée GPS n'ont été données, le système ne
      // doit pas appliquer les valeurs par défaut associées à ces valeurs."
      if (lat == null && lng == null && rayon == null) {
         activites = repository.findAll();
      }
      else {
        if (rayon == null) rayon = RADIUS_DEFAULT;
        if (lat   == null) lat   = LAT_DEFAULT;
        if (lng   == null) lng   = LNG_DEFAULT;
        activites = repository.findByRadius(rayon, lng, lat);
      }
      if (du != null && au != null) {
        activites = filterByDates(activites, new DateInterval(new Date(du), new Date(au)));
      }
      response = new ResponseEntity(activites, null, HttpStatus.valueOf(200));
    }
    return response;
  }

  /*
   * valideRequestParameters - Méthode permettant de valider les paramètres de la requête
   *
   * @param stringDu   La chaine représentant une date de début
   * @param stringAu   La chaine représentant une date de fin
   * @param rayon      Le rayon, en mètres
   * @param lng        La longitude
   * @param lat        La latitude
   * @return           Une liste de messages d'erreurs associés aux paramètres donnés. Vide
   *                   si aucune erreur. 
   */
  static private List<String> validateRequestParameters(String stringDu, String stringAu, 
                                                     Double rayon, Double lng, Double lat) {

    List<String> errors;
    Date du = (stringDu == null ? null : new Date(stringDu));
    Date au = (stringAu == null ? null : new Date(stringAu));

    // validation du rayon et des coordonnées
    errors = GeographyValidator.validateGeography(rayon, lng, lat);

    // valeurs valides de dates
    if (du != null && Date.isValid(du) == false) {
      errors.add(INVALID_DU_DATE_FORMAT);
    }
    if (au != null && Date.isValid(au) == false) {  
      errors.add(INVALID_AU_DATE_FORMAT);
    }
    // date "du" doit être avant que date "au"
    if (du != null && au != null && 
        Date.isValid(du) && Date.isValid(au) &&
        du.isBefore(au) == false) {
      errors.add(INVALID_DATE_ORDER);  
      
    }
    return errors;     
  }

  /*
   * filterByDate - Méthode permettant de conserver uniquement les activités qui seront
   * actives durant une certaine période donnée.
   *
   * @param   activites    La liste des activites a traiter
   * @param   interval     L'intervalle de dates pour laquelle on veut récupérer les activités
   * @return               La liste d'activités qui seront actives during l'intervalle 'interval'
   */
  static private List<Activite> filterByDates(List<Activite> activites, DateInterval interval) {
    
    List<Activite> filteredActivites = new ArrayList<Activite>();

    for (Activite a : activites) {
      for (DateInterval di : a.getDates_formelles()) {
        if (di.getDateDebut().isBefore(interval.getDateFin()) &&
              interval.getDateDebut().isBefore(di.getDateFin())) {

          filteredActivites.add(a);   
          break;
        }
      }
    }
    return filteredActivites;
  }  
}
