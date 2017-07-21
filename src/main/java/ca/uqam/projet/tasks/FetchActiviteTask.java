/*
 * UQAM - Été 2017 - INF4375 - Groupe 30 - Projet de session
 *
 * FetchActiviteTask.java - Fichier source .java de la classe FetchActiviteTask.
 * Une méthode périodique permet de récupérer et de sauvegarder les données des 
 * activités du 375e de la Ville de Montréal à toutes les semaines.
 *
 * @Auteur  Alexis Chrétien (CHRA25049209)
 * @Version 21 juillet 2017
 */

package ca.uqam.projet.tasks;

import ca.uqam.projet.resources.*;
import ca.uqam.projet.repositories.*;

import java.util.*;
import java.util.regex.*;
import java.util.stream.*;

import com.fasterxml.jackson.annotation.*;
import org.jsoup.*;
import org.slf4j.*;

import org.springframework.http.converter.*;
import org.springframework.http.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.web.client.*;
import org.springframework.dao.*;

import java.io.*;
import java.lang.*;
import java.nio.charset.*;

import javax.annotation.PostConstruct;

@Component
public class FetchActiviteTask {

  private static final Logger log = LoggerFactory.getLogger(FetchActiviteTask.class);
  private static final String URL = "http://donnees.ville.montreal.qc.ca/dataset/989ab100" 
                                  + "-b278-4a96-9967-59ce8116ea55/resource/12382aee-b5c8-" 
                                  + "4106-9056-f117bcab2cd5/download/programmation375.csv";

  @Autowired private ActiviteRepository repository;

  // À toutes les semaines (Tous les lundi à 12h10)
  @PostConstruct
  @Scheduled(cron="0 10 12 ? * MON") 
  public void execute() {

    try {
      HttpHeaders entete = new HttpHeaders();
      entete.set("Content-type", "text/csv");

      HttpEntity entite = new HttpEntity(entete);
  
      RestTemplate restTemplate = new RestTemplate();
      restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
      ResponseEntity<String> reponse = restTemplate.exchange(URL, HttpMethod.GET, entite, String.class, new Object());

      CsvActiviteParser(reponse.getBody())
        .stream()
        .peek(c -> log.info(c.toString()))
        .forEach(repository::insert)
        ;
   
    } catch (EmptyResultDataAccessException e) {
        log.warn("Caught an exception during FetchActiviteTask : " + e.toString()); 
    } catch (HttpClientErrorException e) {
        log.warn("Caught an exception during FetchActiviteTask : " + e.toString());
    } catch (HttpServerErrorException e) {
        log.warn("Caught an exception during FetchActiviteTask : " + e.toString());  
    }
  }


  /*
   * CsvActiviteParser - Méthode permettant d'extraire les données
   * contenues dans le fichier de format .csv des activités du 375e de la
   * Ville de Montréal. 
   *
   * @param corps   Le corps du fichier .csv
   * @return        La liste d'activités
   */
  private List<Activite> CsvActiviteParser(String corps) {

    int id = 0;
    List<Activite> activites = new ArrayList<Activite>();
    corps = corps.substring(corps.indexOf('\n') + 1, corps.length());
    String[] lignes = corps.split("\r\n");
            
    try {
      for(String ligne : lignes) {
        String[] args = ligne.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        activites.add(new Activite(Integer.parseInt(args[0]), 
                                   trimQuotationMarks(args[1]),
                                   trimQuotationMarks(args[2]), 
                                   trimQuotationMarks(args[3]), 
                                   trimQuotationMarks(args[4]),  
                                   trimQuotationMarks(args[5]), 
                                   trimQuotationMarks(args[6]), 
                                   trimQuotationMarks(args[7]), 
                                   FrenchDateParser.parse(args[7]),
                                   trimQuotationMarks(args[8])));
      }
    }
    catch (ArrayIndexOutOfBoundsException e) {
        log.warn("Caught an exception during FetchActiviteTask : " + e.toString());
    }
    return activites;
  }
    
  /*
   * trimQuotationMarks - Méthode permettant d'enlever les guillemets en début
   * et en fin d'une chaine de caractères
   *
   * @param   aString  la chaine à traiter
   * @return           la chaine traitée
   */
  private String trimQuotationMarks (String aString) {

    Pattern regEx = Pattern.compile("^\"(.*)\"$");
    Matcher m = regEx.matcher(aString);
    
    if(m.matches()) {
      aString = m.group(1);
    }
    return aString;
  }


}

