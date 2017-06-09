package ca.uqam.projet.tasks;

import ca.uqam.projet.resources.*;

import java.util.*;
import ca.uqam.projet.repositories.*;

import java.util.*;
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
import java.lang.Object;
import java.nio.charset.Charset;

@Component
public class FetchActiviteTask {

  private static final Logger log = LoggerFactory.getLogger(FetchActiviteTask.class);
  private static final String URL = "http://donnees.ville.montreal.qc.ca/dataset/989ab100" 
                                  + "-b278-4a96-9967-59ce8116ea55/resource/12382aee-b5c8-" 
                                  + "4106-9056-f117bcab2cd5/download/programmation375.csv";

  @Autowired private ActiviteRepository repository;

  // Scheduled for very week (every monday at 12h10)
  @Scheduled(cron="*/3 * * * * ?")
//  @Scheduled(cron="0 10 12 ? * MON") 
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

  private List<Activite> CsvActiviteParser(String corps) {

    List<Activite> activites = new ArrayList<Activite>();
    corps = corps.substring(corps.indexOf('\n') + 1, corps.length());
    String[] lignes = corps.split("\r\n");

    for(String ligne : lignes) {
      String[] args = ligne.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
      List<DateInterval> dates = new FrenchDateParser().parse(args[7]);
      activites.add(new Activite(Integer.parseInt(args[0]), args[1], args[2], args[3], 
                                                  args[4],  args[5], args[6], DateInterval.listToString(dates), args[8]));  
     
      System.out.println(args[7]);
      for (DateInterval date : dates) System.out.println(date);
      System.out.println("\n");
    }
    return activites;
  }
}

