/*
 * UQAM - Été 2017 - INF4375 - Groupe 30 - Projet de session
 *    
 * Erreur.java - Fichier source .java de la classe Erreur
 *       
 * @Auteur Alexis Chrétien (CHRA25049209)
 * @Version 21 juillet 2017
 */

package ca.uqam.projet.resources;

import java.util.*;


public class Erreur {

  private static final String MESSAGE_400 = "Requête invalide";
  private static final String MESSAGE_404 = "Ressource inexistante";
  private static final String MESSAGE_UNKNOWN = "Erreur";

  private int code;
  private String message;
  private List<String> erreurs;

  // Constructeur
  public Erreur(int code, List<String> erreurs) {
    this.code = code;
    this.erreurs = erreurs;
    if (code == 400) {
      this.message = MESSAGE_400;
    }
    else if (code == 404) {
      this.message = MESSAGE_404;
    }
    else {
      this.message = MESSAGE_UNKNOWN;
    }
  }

  // getters
  public int getCode() {
    return code;
  }
  public String getMessage() {
    return message;
  }
  public List<String> getErreurs() {
    return erreurs;
  }

  // setters
  public void setCode(int code) {
    this.code = code;
  }
  public void setMessage(String message) {
    this.message = message;
  }
  public void setErreurs(List<String> erreurs) {
    this.erreurs = erreurs;
  }
}
