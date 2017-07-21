/*
 * UQAM - Été 2017 - INF4375 - Groupe 30 - Projet de session
 *
 * DateInterval.java - Fichier source .java de la classe DateInterval
 *
 * @Auteur  Alexis Chrétien (CHRA25049209)
 * @Version 21 juillet 2017
 */


package ca.uqam.projet.resources;

import java.util.*;
import java.util.regex.*;

public class DateInterval {

  /*
   * Attributs
   */
  private Date dateDebut;
  private Date dateFin;

  /*
   * Constructeurs
   */
  public DateInterval(Date dateDebut, Date dateFin) {

    this.dateDebut = dateDebut;
    this.dateFin   = dateFin;
  }

  /*
   * Getters 
   */  
  public Date getDateDebut() { return dateDebut; }
  public Date getDateFin()   { return dateFin; }
  

  /*
   * Setters
   */
  public void setDateDebut(Date date) { this.dateDebut = date; }
  public void setDateFin  (Date date) { this.dateFin   = date; }

  /*
   * Méthodes
   */
  public String toString() { 
    
    return String.format("[%s,%s]", dateDebut, dateFin);
  }
  
  /*
   * listToString - méthode statique permettant de retourner la représentation 
   * String d'une liste de DateIntervals
   *
   * @param list   La liste de DateIntervals a représenter sous forme de String
   * @return       La représentation String de "list". 
   */
  public static String listToString(List<DateInterval> list) {
    
    String string = "";

    for (DateInterval di : list) {
       string += di.toString() + " ";
    }
    return string;
  }  

  /*
   * StringToList - méthode statique permettant de retourner une liste de dateIntervals
   * à partir de sa représentation sous forme de String.  
   *
   * @param  stringDateIntervals  La représentation sous forme de String de la liste 
   *                              de DateIntervals
   * @return                      la liste de DateIntervals
   */ 
  public static List<DateInterval> stringToList(String stringDateIntervals) {

    int a1, a2, m1, m2, j1, j2;
    Matcher m;
    Pattern diPattern = Pattern.compile("^\\[?\\[(?<a1>\\d\\d\\d\\d)\\-(?<m1>\\d\\d)\\-(?<j1>\\d\\d),"
                                              + "(?<a2>\\d\\d\\d\\d)\\-(?<m2>\\d\\d)\\-(?<j2>\\d\\d)\\]\\]?$");
    List<DateInterval> dateIntervals = new ArrayList<DateInterval>();
    
    for (String di : stringDateIntervals.split(" ")) {
      m = diPattern.matcher(di);
        
      if (m.matches()) {
        a1 = Integer.parseInt(m.group("a1"));
        a2 = Integer.parseInt(m.group("a2"));
        m1 = Integer.parseInt(m.group("m1"));
        m2 = Integer.parseInt(m.group("m2"));
        j1 = Integer.parseInt(m.group("j1"));
        j2 = Integer.parseInt(m.group("j2")); 
        dateIntervals.add(new DateInterval(new Date(a1, m1, j1), new Date(a2, m2, j2)));
      }
    }
    return dateIntervals;
  }
}

