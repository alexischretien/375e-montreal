/*
 * UQAM - Été 2017 - INF4375 - Groupe 30 - Projet de session
 *
 * Date.java - Fichier source .java de la classe Date
 *
 * @Auteur  Alexis Chrétien (CHRA25049209)
 * @Version 21 juillet 2017
 */

package ca.uqam.projet.resources;
   
import java.util.*;
import java.util.regex.*;
 
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
  public Date(String date) {
   
    Matcher m;
    Pattern p = Pattern.compile("\\[?(?<annee>\\d\\d\\d\\d)\\-" 
                                 +"(?<mois>\\d?\\d)\\-"
                                 +"(?<jour>\\d?\\d)\\]?");
    m = p.matcher(date);
    if (m.matches()) {
      this.annee = Integer.parseInt(m.group("annee"));
      this.mois  = Integer.parseInt(m.group("mois"));
      this.jour  = Integer.parseInt(m.group("jour"));
    }
    else {
      this.annee = -1;
      this.mois = -1;
      this.jour = -1;
    }
  }
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
   * isValideDate - méthode qui vérifie si la date en argument est valide. 
   *
   * @param Date    La date
   * @return        vrai si la date est valide, faux sinon
   */ 
  public static boolean isValid(Date date) {
    
    int jour = date.getJour();
    int mois = date.getMois();
    int annee = date.getAnnee();
    
    if (mois < 1 || mois > 12 || jour < 1 || jour > 31) 
      return false;
    if ((mois == 1 || mois == 3  || mois == 5 || mois == 7 ||
         mois == 8 || mois == 10 || mois == 12) && jour > 31) 
      return false;
    if ((mois == 4 || mois == 6  || mois == 9 || 
         mois == 11) && jour > 30)
      return false;

    // si février
    if (mois == 2) {
      // si annee non-bissextile
      if (!((annee % 4   == 0 && annee % 100 != 0) || annee % 400 == 0) && jour > 28) {
          return false;
      }
      else if (jour > 29) {
        return false;
      }
   }
   return true;
  } 

  /*
   * isBefore - méthode qui vérifie si *this* date précède la date
   * en argument.
   *
   * @param date   La date à comparer
   * @return       vrai si *this* est avant "date" ou s'il s'agit 
   *               de mêmes dates, faux sinon.
   */
  public boolean isBefore(Date date) {
    if (date.annee < this.annee) 
      return false;
    else if (date.annee == this.annee &&
             date.mois  <  this.mois)
      return false;
    else if (date.annee == this.annee &&
             date.mois  == this.mois &&
             date.jour  <  this.jour)
      return false;
   
    return true;
  }
  
  /*
   * isSame - méthode qui vérifie si *this* date est la même date que
   * la date en argument.
   *
   * @param  date  La date à comparer
   * @return       Vrai si *this* est la même date que "date", faux sinon
   */
  public boolean isSame(Date date) {
    
    return (this.annee == date.annee &&
            this.mois  == date.mois  &&
            this.jour  == date.jour);
  }
  /*
   * isEve - méthode qui vérifie si *this* date est  la date 
   * "veille" de la date en argument
   *
   * @param date   La date à comparer
   * @return       Vrai si *this* est la date immédiatement précédente,
   *               faux sinon.
   */
  public boolean isEve(Date date) {
    
    return (this.addDay().isSame(date));

    }
 /*
  * addDay - méthode qui retourne la même date que la date *this*, mais
  * incrémentée d'un jour
  *
  * @param date   La date à incrémenter
  * @return       La date incrémentée
  */
  public Date addDay() {
    
    Date date = new Date(this.annee, this.mois, this.jour + 1);
    
    if (isValid(date) == false) {
      date.setJour(1);

      if (date.getMois() < 12) {
        date.setMois(date.getMois() + 1);
      }
      else {
        date.setMois(1);
        date.setAnnee(date.getAnnee() + 1);
      }
    }
    return date;
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
