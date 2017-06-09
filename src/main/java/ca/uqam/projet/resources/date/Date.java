/*
 * UQAM - Été 2017 - INF4375 - Groupe 30 - Projet de session
 *
 * Date.java - Fichier source .java de la classe Date
 *
 * @Auteur  Alexis Chrétien (CHRA25049209)
 * @Version 8 juin 2017
 */

package ca.uqam.projet.resources;
   
import java.util.*;
 
public class Date {
  
  /*
   * Attributs
   */
  private int annee;
  private int mois;
  private int jour;

  /*
   * Constructeurs
   */ 
  public Date(Date date) {
    
    this.annee = date.annee;
    this.mois  = date.mois;
    this.jour  = date.jour;
  }
  public Date(int annee, int mois, int jour) {

    this.annee = annee;
    this.mois  = mois;
    this.jour  = jour;
  }
  
  public Date(String annee, String mois, String jour) {
    
    this.annee = (annee == null ? -1 : Integer.parseInt(annee));
    this.mois  = (mois  == null ? -1 : moisFromString(mois));
    this.jour  = (jour  == null ? -1 : Integer.parseInt(jour));
  }

  /*
   * Getters
   */
  public int getAnnee() { return annee; }
  public int getMois()  { return mois;  }
  public int getJour()  { return jour;  }

  /*
   * Setters
   */
  public void setAnnee(int annee) { this.annee = annee; }
  public void setMois (int mois ) { this.mois  = mois;  }
  public void setJour (int jour ) { this.jour  = jour;  }

  /*
   * Méthodes
   */
  public String toString() {
 
    return String.format("%s-%s-%s", annee, 
                                     (mois < 10 && mois >= 0 ? "0" + mois : mois), 
                                     (jour < 10 && jour >= 0 ? "0" + jour : jour)); 
  }
  
  /*
   * moisFromString - méthode retournant le nombre associé à un mois
   * sous forme de String.
   *
   * @param  mois  Le mois sous forme de String
   * @return       L'entier associé au mois
   */
  private int moisFromString(String mois) {

    mois = mois.trim().toLowerCase();
 
    if      (mois.equals("janvier"))   return 1;
    else if (mois.equals("février")
          || mois.equals("fevrier"))   return 2;
    else if (mois.equals("mars"))      return 3;
    else if (mois.equals("avril"))     return 4;
    else if (mois.equals("mai"))       return 5;
    else if (mois.equals("juin"))      return 6;
    else if (mois.equals("juillet"))   return 7;
    else if (mois.equals("août")       
          || mois.equals("aout"))      return 8;
    else if (mois.equals("septembre")) return 9;
    else if (mois.equals("octobre"))   return 10;
    else if (mois.equals("novembre"))  return 11;
    else if (mois.equals("décembre") 
         ||  mois.equals("decembre"))  return 12;
    return -1;
  }
}
