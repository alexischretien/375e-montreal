/*
 * UQAM - Été 2017 - INF4375 - Groupe 30 - Projet de session
 *
 * FrenchDateParser.java - Fichier source .java de la classe FrenchDateParser
 * offrant une méthode permettant de traduire des dates et des intervalles de dates
 * exprimées en français naturel en des listes de DateIntervals.
 *
 * @Auteur  Alexis Chrétien (CHRA25049209)
 * @Version 21 juillet 2017
 */

package ca.uqam.projet.resources;

import java.util.*;
import java.util.regex.*;

public class FrenchDateParser {

  /*
   * Constantes
   */
  public static final String JOUR_PREFIX = "(à partir (de|du)|arrivée le|depuis le|dès le|du|de|d'|le|jusqu'au|de retour le|fin|début)";
  public static final String MOIS = "(janvier|février|mars|avril|mai|juin|juillet|" +
                                    "août|septembre|octobre|novembre|décembre)";
  public static final String JOUR_SEM = "(lundi|mardi|mercredi|jeudi|vendredi|samedi|dimanche)";
  public static final int CURRENT_YEAR = Calendar.getInstance().get(Calendar.YEAR);


  /*
   * parse - méthode permettant de retourner une lsite de DateIntervals à partir
   * d'une expression en francais naturel
   *
   * @param  expression   L'expression en francais naturel
   * @return              La liste de DateIntervals correspondant
   */
  public static List<DateInterval> parse(String expression) {

    List<DateInterval> dateIntervals = execute(new ArrayList<DateInterval>(),
                                               expression.toLowerCase());

    dateIntervals = deduceMissingInfo(dateIntervals);
    dateIntervals = mergeIntervals(dateIntervals);

    return dateIntervals;
  }

  /*
   * execute - methode récursive privée appelée par la méthode publique parse. 
   *
   * Divise l'expression en français naturel en de plus petites expressions selon
   * les délimiteurs "et", "au", "à" et ",". Retourne une liste de DateIntervals
   * représentant l'expression en français naturel.
   *
   * @param  dates  La liste de DateIntervals déjà traitée (vide lors du premier appel,
   *                se remplit si la fonction effectue des appels récursifs)
   * @return        La liste de DateIntervals associée à l'expression en français naturel
   */
  private static List<DateInterval> execute(List<DateInterval> dates, String expression) {
    
    Matcher m; 
    Date date1;
    Date date2;
    
    Pattern etStatement = Pattern.compile("^(\")?(?<expression1>(.*?))( )*(,| et )( )*(?<expression2>.*)(\")?$");
    Pattern auStatement = Pattern.compile("^(\")?(?<date1>.*?)( )*( au | à |-)( )*(?<date2>.*)(\")?$");

    expression = expression.trim(); 


    if ((m = etStatement.matcher(expression)).matches()) {
      
       dates = execute(dates, m.group("expression1"));
       dates = execute(dates, m.group("expression2"));
    }
    else if ((m = auStatement.matcher(expression)).matches()) {
     
       date1 = parseJour(m.group("date1"));
       date2 = parseJour(m.group("date2"));
       
       dates.add(new DateInterval(date1, date2));
    }
    else { 
       date1 = parseJour(expression);
       date2 = new Date(date1);

       dates.add(new DateInterval(date1, date2));
    }
    return dates;
  }
  
  /*
   * parseJour - méthode permettant de retourner une date à partir d'un string
   * 
   * @param  expression   Une expression contenant potentiellement une date
   * @return              L'objet Date associé à l'expression. Si l'expression 
   *                      n'est pas une date, retourne un objet Date avec -1 comme
   *                      attribut jour, mois et annee. 
   */
  private static Date parseJour(String expression) {
  
    Date date;
    Matcher  m;

    expression = expression.trim(); 
    
    Pattern jmaStatement  = Pattern.compile("^(\")?" + JOUR_SEM + "?" + JOUR_PREFIX + "?( )*(?<jour>\\d?\\d)?(er)?" +
                                            "( )*(?<mois>" + MOIS + ")?( )*(?<annee>\\d\\d\\d\\d)?(.*)?$");
 
    if ((m = jmaStatement.matcher(expression)).matches()) {
      date = new Date(m.group("annee"), m.group("mois"), m.group("jour"));
    }
    else {
      date = new Date(-1,-1,-1);;
    }
 
    return date;
  }

  /*
   * deduceMissingInfo - méthode permettant de déduire les informations manquantes
   * d'une liste de DateIntervals. 
   *
   * Une information manquante est charactérisée par un -1 comme attribut jour, 
   * mois ou annee d'un attribut dateDebut ou dateFin d'un element de la liste
   * de DateInterval. 
   *
   * @param  di   La liste de DateIntervals manquant potentiellement des données
   * @return      La liste de DateIntervals avec les données manquantes  déduites
   */
  private static List<DateInterval> deduceMissingInfo(List<DateInterval> di) {
     
    Date date1;
    Date date2;
    
    for (int i = di.size() - 1 ; i >= 0 ; --i) {        
      date1 = di.get(i).getDateFin();
      date2 = di.get(i).getDateDebut();

      if (date1.getMois() == -1 && date2.getMois() == -1) {
          di.remove(i);
      }
      else {
        if (date1.getAnnee() == -1 && date2.getAnnee() == -1) {
          date1.setAnnee(CURRENT_YEAR);
          date2.setAnnee(CURRENT_YEAR);
        }
        else if (date1.getAnnee() != -1 && date2.getAnnee() == -1) {
          date2.setAnnee(date1.getAnnee());
        }

        if (date1.getMois() != -1 && date2.getMois() == -1) {
          date2.setMois(date1.getMois());
        }

        if (date1.getJour() == -1) {
          date1.setJour(nbJours(date1.getMois()));
        }
        if (date2.getJour() == -1) {
          date2.setJour(1);
        }
 
        di.get(i).setDateFin(date1);
        di.get(i).setDateDebut(date2);

        if (i > 0) {
          date1 = date2;
          date2 = di.get(i-1).getDateFin();
      
          if (date1.getAnnee() == -1 && date1.getAnnee() == -1) {
            date1.setAnnee(CURRENT_YEAR);
            date2.setAnnee(CURRENT_YEAR);
          }
          else if (date1.getAnnee() != -1 && date2.getAnnee() == -1) {
            date2.setAnnee(date1.getAnnee());
          }
          if (date1.getMois() != -1 && date2.getMois() == -1) {
            date2.setMois(date1.getMois());
          }
          di.get(i).setDateDebut(date1);
          di.get(i-1).setDateFin(date2);
        }
      }
    }
    return di;
  }
  
  /*
   * mergeIntervals - méthode qui combine les intervalles de dates d'une liste
   * de dateIntervals si la date de fin d'un élément est la date de début ou 
   * le jour précédant de l'élément suivant. 
   *
   * @param  di               La liste de dateIntervals
   * @return                  La liste de dateIntervals dont les intervalles 
   *                          de dates consécutives ont été combinées
   */

  private static List<DateInterval> mergeIntervals(List<DateInterval> di) {
    
    for (int i = 0 ; i < di.size() - 1 ; ++i) {
      while (i < di.size() - 1 &&
             (di.get(i).getDateFin().isSame(di.get(i + 1).getDateDebut()) ||
              di.get(i).getDateFin().isEve (di.get(i + 1).getDateDebut()))) {

        di.set(i, new DateInterval(di.get(i).getDateDebut(), di.get(i + 1).getDateFin()));
        di.remove(i + 1);    
      } 
    }
    return di;
  }
  /*
   * nbJours - méthode permettant de retourner le nombre de jours maximum
   * associé à un mois. 
   *
   * @param  mois   L'entier associé à un mois (1 = janvier, 12 - décembre)
   * @return        Le nombre de jours du mois
   */
  private static int nbJours(int mois) {
    if (mois == 1 || mois == 3 || mois == 5 ||
        mois == 7 ||mois == 9 || mois == 11) {
      return 31;
    }
    else if (mois != 2) {
      return 30;
    }
    else {
      return 29;
    }  
 }
}  
