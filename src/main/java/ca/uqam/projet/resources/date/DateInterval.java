/*
 * UQAM - Été 2017 - INF4375 - Groupe 30 - Projet de session
 *
 * DateInterval.java - Fichier source .java de la classe DateInterval
 *
 * @Auteur  Alexis Chrétien (CHRA25049209)
 * @Version 8 juin 2017
 */


package ca.uqam.projet.resources;

import java.util.*;


public class DateInterval {

  /*
   * Attributs
   */
  private Date dateBegin;
  private Date dateEnd;

  /*
   * Constructeurs
   */
  public DateInterval(Date dateBegin, Date dateEnd) {

    this.dateBegin = dateBegin;
    this.dateEnd   = dateEnd;
  }

  /*
   * Getters 
   */  
  public Date getDateBegin() { return dateBegin; }
  public Date getDateEnd()   { return dateEnd; }
  

  /*
   * Setters
   */
  public void setDateBegin(Date date) { this.dateBegin = date; }
  public void setDateEnd  (Date date) { this.dateEnd   = date; }

  /*
   * Méthodes
   */
  public String toString() { 
    
    return String.format("[%s,%s]", dateBegin, dateEnd);
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
  public static List<DateInterval> StringtoList(String stringDateIntervals) {

    Matcher m;
    Pattern diPattern = Pattern.compile("^[?[(?<a1>\\d\\d\\d\\d)-(?<m1>\\d\\d)-(?<j1>\\d\\d\\),"
                                          + "(?<a2>\\d\\d\\d\\d)-(?<m2>\\d\\d)-(?<j2>\\d\\d\\)]]?$");
    List<DateInterval> dateIntervals = new ArrayList<DateInterval>();
 
    for (String di : stringDateIntervals.split(" ")) {
        m = diPattern.matcher(di);

        if (di.matches()) {
          dateIntervals.add(new DateInterval(new Date(a1,m1,j1), new Date(a2,m2,j2));
        }
    }
    return dateIntervals;
  }
}

