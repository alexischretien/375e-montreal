package ca.uqam.projet.tasks;

import ca.uqam.projet.resources.*;

import java.util.*;
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
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.*;
import java.lang.Object;
import java.nio.charset.Charset;

@Component
public class FetchPisteTask {

  private static final Logger log = LoggerFactory.getLogger(FetchPisteTask.class);
  private static final String URL = "http://donnees.ville.montreal.qc.ca/dataset/5ea29f40" 
                                  + "-1b5b-4f34-85b3-7c67088ff536/resource/0dc6612a-be66-" 
                                  + "406b-b2d9-59c9e1c65ebf/download/reseaucyclable2016dec2016.geojson";
  @Autowired private PisteRepository repository;

  // Scheduled for twice a month (first day of January and July at 18:00)  
  @Scheduled(cron="0 0 18 1 1,12 ?") 
  public void execute() {
  try {
    RestTemplate restTemplate = new RestTemplate();
    String response = restTemplate.getForObject(URL, String.class);
 
     geoJsonPisteParser(response.split("(\n)")).stream()
      .peek(c -> log.info(c.toString()))
      .forEach(repository::insert)
      ;

    } catch (EmptyResultDataAccessException e) {
        log.warn("Caught an exception during FetchPisteTask : " + e.toString()); 
    } catch (HttpClientErrorException e) {
        log.warn("Caught an exception during FetchPisteTask : " + e.toString());
    } catch (HttpServerErrorException e) {
        log.warn("Caught an exception during FetchPisteTask :" + e.toString());
    }   
  } 

  private List<Piste> geoJsonPisteParser(String[] rows) {

    List<Piste> pistes  = new ArrayList<Piste>();
    Pattern regEx = Pattern.compile("\\{ \"type\": (.*), \"properties\": \\{ \"ID\": (.*), " 
                                  + "\"ID_TRC_GEOBASE\": (.*), \"TYPE_VOIE\": (.*), \"TYPE_VOIE2\": (.*), " 
                                  + "\"LONGUEUR\": (.*), \"NBR_VOIE\": (.*), \"SEPARATEUR\": (.*), " 
                                  + "\"SAISONS4\": (.*), \"PROTEGE_4S\": (.*), \"Ville_MTL\": (.*), " 
                                  + "\"NOM_ARR_VILLE\": (.*) \\}, \"geometry\": \\{ \"type\": (.*), " 
                                  + "\"coordinates\": (.*) \\} \\},");
    for (String row : rows) {      
      Matcher m = regEx.matcher(row);

      if (m.matches()) {
        pistes.add(new Piste(Double.parseDouble(m.group(2)), 
                             Double.parseDouble(m.group(3)), 
                             Integer.parseInt(m.group(4)), 
                             Integer.parseInt(m.group(5)),
                             Integer.parseInt(m.group(6)),
                             Integer.parseInt(m.group(7)),
                             trimQuotationMarks(m.group(8)),
                             trimQuotationMarks(m.group(9)),
                             trimQuotationMarks(m.group(10)),
                             trimQuotationMarks(m.group(11)),
                             trimQuotationMarks(m.group(12)),
                             trimQuotationMarks(m.group(14))));
      }
    }
    return pistes;
  }

  String trimQuotationMarks (String aString) {

    Pattern regEx = Pattern.compile("^\"(.*)\"$");
    Matcher m = regEx.matcher(aString);
    
    if(m.matches()) {
      aString = m.group(1);
    }
    return aString;
  }
}
/*
//@JsonIgnoreProperties(ignoreUnknown = true)
class JsonPisteResponse {
  @JsonProperty("type")     String type;
  @JsonProperty("crs")      Object crs;
  @JsonProperty("features") Feature[] features;
  
}
class Feature {
  @JsonProperty("type")        String type;
  @JsonProperty("properties")  PisteProperties properties;
  @JsonProperty("geometry")    Coordonnees geometry;
}

//@JsonIgnoreProperties(ignoreUnknown = true)
class PisteProperties {
  @JsonProperty("ID")              double id;
  @JsonProperty("ID_TRC_GEOBASE")  double id_trc_geobase;
  @JsonProperty("TYPE_VOIE")       int type_voie;
  @JsonProperty("TYPE_VOIE2")      int type_voie2;
  @JsonProperty("LONGUEUR")        int longueur;
  @JsonProperty("NBR_VOIE")        int nbr_voie;
  @JsonProperty("SEPARATEUR")      String separateur;
  @JsonProperty("SAISONS4")        String saisons4;
  @JsonProperty("PROTEGE_4S")      String protege_4s;
  @JsonProperty("Ville_MTL")       String ville_mtl;
  @JsonProperty("NOM_ARR_VILLE")   String nom_arr_ville;
}

class Coordonnees {
  @JsonProperty("type")        String type;
  @JsonProperty("coordinates") double[][] coordinates;

}*/
