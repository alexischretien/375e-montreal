/*
 * UQAM - Été 2017 - INF4375 - Groupe 30 - Projet de session
 *
 * Activite.java - Fichier source .java de la classe Activite
 *
 * @Auteur Alexis Chrétien (CHRA25049209)
 * @Version 21 juillet 2017
 */

package ca.uqam.projet.resources;

import java.util.*;

public class Activite {

  /*
   * Attributs
   */
  private int id;
  private String nom;
  private String description;
  private String arrondissement_ou_pole;
  private String gratuit_ou_payant;
  private String interets_ou_type_evenement;
  private String interieur_ou_exterieur;
  private String dates;
  private List<DateInterval> dates_formelles;
  private String lieux;
  
  /*
   * Constructeur  
   */
  public Activite(int id, 
                  String nom, 
                  String description,
                  String arrondissement_ou_pole,
                  String gratuit_ou_payant,
                  String interets_ou_type_evenement, 
                  String interieur_ou_exterieur, 
                  String dates,
                  List<DateInterval> dates_formelles, 
                  String lieux) {
    this.id = id;
    this.nom = nom;
    this.description = description;
    this.arrondissement_ou_pole = arrondissement_ou_pole;
    this.gratuit_ou_payant = gratuit_ou_payant;
    this.interets_ou_type_evenement = interets_ou_type_evenement;
    this.interieur_ou_exterieur = interieur_ou_exterieur;
    this.dates = dates;
    this.dates_formelles = dates_formelles;
    this.lieux = lieux;
  }
  
  /*
   * Getters  
   */
  public int getId() { 
    return id; 
  }
  public String getNom() { 
    return nom; 
  }
  public String getDescription() { 
    return description; 
  }
  public String getArrondissement_ou_pole() { 
    return arrondissement_ou_pole; 
  }
  public String getGratuit_ou_payant() { 
    return gratuit_ou_payant; 
  }
  public String getInterets_ou_type_evenement() { 
    return interets_ou_type_evenement; 
  }
  public String getInterieur_ou_exterieur() { 
    return interieur_ou_exterieur; 
  }
  public String getDates() { 
    return dates; 
  }
  public List<DateInterval> getDates_formelles() { 
    return dates_formelles; 
  }
  public String getLieux() { 
    return lieux; 
  }

  /* 
   * Méthodes
   */ 
  @Override public String toString() {
    return String.format("<<%s>> -- %s -- %s -- %s -- %s -- %s -- %s -- %s -- %s -- %s ", 
                          id, nom, description, arrondissement_ou_pole, gratuit_ou_payant, 
                          interets_ou_type_evenement, interieur_ou_exterieur, dates,
                          dates_formelles, lieux);
  }
}
