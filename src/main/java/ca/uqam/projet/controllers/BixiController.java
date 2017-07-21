/*  
 * UQAM - Été 2017 - INF4375 - Groupe 30 - Projet de session
 *
 * BixiController.java - Fichier source .java de la classe 
 *                       BixiController, offrant un API
 *                       pour traiter des requêtes relatives
 *                       aux stations bixi stockées dans la base
 *                       de données
 * 
 * @Auteur Alexis Chrétien (CHRA25049209)
 * @Version 21 juillet 2017
 */

package ca.uqam.projet.controllers;

import java.util.*;
import java.time.LocalDate;

import ca.uqam.projet.repositories.*;
import ca.uqam.projet.resources.*;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.hibernate.validator.constraints.*;


@RestController
public class BixiController {

  public static final Integer BIKE_NB_DEFAULT = 0;
  public static final Double  RADIUS_DEFAULT = 1000.0;
  public static final Double  LAT_DEFAULT = 45.5089306;
  public static final Double  LNG_DEFAULT = -73.5685676;
  public static final String  INVALID_BIXI_ID = "Aucune station Bixi ne possède l'identifiant spécifié";
  public static final String  INVALID_BIXI_ID_FORMAT = "Format de l'identifiant de la station bixi invalide";
  public static final String  INVALID_BIKE_NUMBER = "Valeur de nombre vélos disponibles minimal invalide";
  public static final String  NO_PARAMETERS = "Aucun paramètre donné pour la requête";
  @Autowired BixiRepository repository;

  @RequestMapping(value = "/stations-bixi", method = RequestMethod.GET)
  public ResponseEntity<Map<String, Object>> findByRadius(
                                 @RequestParam(value = "min_bixi_dispo", 
                                               required = false) Integer min_bixi_dispo,
                                 @RequestParam(value = "rayon",
                                               required = false) Double rayon,
                                 @RequestParam(value = "lat", 
                                               required = false) Double lat,
                                 @RequestParam(value = "lng", 
                                               required = false) Double lng) {

    ResponseEntity<Map<String, Object>> response;
    List<Bixi> bixis = new ArrayList<Bixi>();

    List<String> errors = validateRequestParameters(min_bixi_dispo, rayon, lng, lat);

    if (errors.size() > 0) {
      response = new ResponseEntity(new Erreur(400, errors), null, HttpStatus.valueOf(400));
    }
    else {
      if (min_bixi_dispo == null) min_bixi_dispo = BIKE_NB_DEFAULT;
      if (rayon == null) rayon = RADIUS_DEFAULT;
      if (lat == null) lat = LAT_DEFAULT;
      if (lng == null) lng = LNG_DEFAULT;

      bixis = repository.findByRadius(min_bixi_dispo, rayon, lng, lat);
      response = new ResponseEntity(bixis, null, HttpStatus.valueOf(200));
    }
    return response;
  }

  @RequestMapping(value = "stations-bixi/{bixiId}", method = RequestMethod.GET)
  public ResponseEntity<Map<String, Object>> findById(@PathVariable("bixiId") Integer bixiId) {

    ResponseEntity<Map<String, Object>> response;
    List<String> errors = new ArrayList<String>();

    // bixiId est bien un Integer
    if(bixiId != null) {
      Bixi bixi = repository.findById(bixiId);

      // Bixi existe
      if (bixi != null) {
        response = new ResponseEntity(bixi, null, HttpStatus.valueOf(200));
      } 
      // Bixi existe pas
      else {
        errors.add(INVALID_BIXI_ID);
        response = new ResponseEntity(new Erreur(404, errors), null, HttpStatus.valueOf(404));
      }
    }
    // bixi n'est pas un Integer
    else {
      errors.add(INVALID_BIXI_ID_FORMAT);
      response = new ResponseEntity(new Erreur(400, errors), null, HttpStatus.valueOf(400));
    }
    return response;
  }

/*
 * valideRequestParameters - Méthode permettant de valider les paramètres de la requête
 *
 * @param min_bixi_dispo   Le nombre minimal de vélos devant être offert par la station
 * @param rayon      Le rayon, en mètres
 * @param lng        La longitude
 * @param lat        La latitude
 * @return           Une liste de messages d'erreurs associés aux paramètres donnés. Vide
 *                   si aucune erreur. 
 */
  private List<String> validateRequestParameters(Integer min_bixi_dispo, Double rayon, 
                                                 Double lng, Double lat) {

    List<String> errors = new ArrayList<String>();

    if (min_bixi_dispo == null && rayon == null && lng == null && lat == null) {
        errors.add(NO_PARAMETERS);
    }
    else {
      errors = GeographyValidator.validateGeography(rayon, lng, lat);

      if (min_bixi_dispo != null && min_bixi_dispo < 0) {
        errors.add(INVALID_BIKE_NUMBER); 
      }
    }
    return errors;    
  }
} 
