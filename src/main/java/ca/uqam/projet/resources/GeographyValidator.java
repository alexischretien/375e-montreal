/*
 *  UQAM - Été 2017 - INF4375 - Groupe 30 - Projet de session
 *    
 *  GeographyValidator.java - Fichier source .java de la classe GeographyValidator
 *       
 *  @Auteur Alexis Chrétien (CHRA25049209)
 *  @Version 21 juillet 2017
 */

package ca.uqam.projet.resources;

import java.util.*;

public class GeographyValidator {

   public static final String INVALID_RAYON = "Valeur de rayon plus petite ou égale à 0";
   public static final String INVALID_LON_MIN = "Valeur de longitude plus petit que -180";
   public static final String INVALID_LON_MAX = "Valeur de longitude plus grande que 180";
   public static final String INVALID_LAT_MIN = "Valeur de latitude plus petite que -90";
   public static final String INVALID_LAT_MAX = "Valeur de latitude plus grande que 90";

  /*
   * validateGeography - Méthode permettant de retourner une liste de chaine de caractères,
   * ou chaque element de la liste correspond à une erreur relative aux paramètres donnés
   *
   * @param  rayon      Le rayon en metres
   * @param  lng        La longitude
   * @param  lat        La latitude
   * @return            La liste de chaines de messages d'erreurs
   */
  public static List<String> validateGeography(Double rayon, Double lng, Double lat) {

    List<String> errors = new ArrayList<String>();
    
    // valeur valide de rayon
    if (rayon != null && rayon <= 0) {
        errors.add(INVALID_RAYON);
    }
    // valeurs valides de longitude
    if (lng != null) {
      if (lng < -180) {
        errors.add(INVALID_LON_MIN);
      }
      else if (lng > 180) {
        errors.add(INVALID_LON_MAX);
      }
    }
    // valeurs valides de latitude
    if (lat != null) {
      if (lat < -90) {
        errors.add(INVALID_LAT_MIN);
      }
      if (lat > 90) {
        errors.add(INVALID_LAT_MAX);
      }  
    }
    return errors;
  }
}
