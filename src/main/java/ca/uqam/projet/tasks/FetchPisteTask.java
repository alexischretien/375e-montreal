/*
 * UQAM - Été 2017 - INF4375 - Groupe 30 - Projet de session
 *
 * FetchPisteTask.java - Fichier source .java de la classe FetchPisteTask.
 * Une méthode périodique permet de récupérer et de sauvegarder les données des
 * pistes cyclables de la Ville de Montréal à tous les 6 mois.
 *
 * @Auteur   Alexis Chrétien (CHRA25049209)
 * @Version  8 juin 2017
 */

package ca.uqam.projet.tasks;

import ca.uqam.projet.resources.*;
import ca.uqam.projet.repositories.*;

import java.io.*;
import java.lang.Object;
import java.util.*;
import java.util.stream.*;

import org.jsoup.*;
import org.slf4j.*;

import org.springframework.http.converter.*;
import org.springframework.http.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.web.client.*;
import org.springframework.dao.*;

import org.codehaus.jackson.*;
import org.codehaus.jackson.map.*;
import org.codehaus.jackson.annotate.*;

import javax.annotation.PostConstruct;

@Component
public class FetchPisteTask {

  private static final Logger log = LoggerFactory.getLogger(FetchPisteTask.class);
  private static final String URL = "http://donnees.ville.montreal.qc.ca/dataset/" + 
                                    "5ea29f40-1b5b-4f34-85b3-7c67088ff536/resource/" + 
                                    "0dc6612a-be66-406b-b2d9-59c9e1c65ebf/download/" + 
                                    "reseaucyclable2017juin2017.geojson";
  @Autowired private PisteRepository repository;

  // Deux fois par année (1er janvier et 1er Juillet à 18:00)  
  @PostConstruct
  @Scheduled(cron="0 0 18 1 1,12 ?") 
  public void execute() {

    try {
      RestTemplate restTemplate = new RestTemplate(); 
      ObjectMapper mapper = new ObjectMapper();
    
      JsonPisteResponse response = mapper.readValue(
             restTemplate.getForObject(URL, String.class), JsonPisteResponse.class);

      for (Feature f : response.features) {
        Piste p = asPiste(f);
        log.info(p.toString());
        repository.insert(p);
      } 
    } catch (EmptyResultDataAccessException e) {
        log.warn("Caught an exception during FetchPisteTask : " + e.toString()); 
    } catch (HttpClientErrorException e) {
        log.warn("Caught an exception during FetchPisteTask : " + e.toString());
    } catch (HttpServerErrorException e) {
        log.warn("Caught an exception during FetchPisteTask :" + e.toString());
    } catch (JsonGenerationException e) {
        log.warn("Caught an exception during FetchPisteTask :" + e.toString());
    } catch (JsonMappingException e) {
        log.warn("Caught an exception during FetchPisteTask :" + e.toString());
    } catch (IOException e) {
        log.warn("Caught an exception during FetchPisteTask :" + e.toString());
    }
  } 

  /*
   * asPiste - Méthode permettant de générer un objet de classe Piste à
   * partir d'un objet "Feature" obtenu par désérialisation 
   *
   * @param f   L'objet de classe "Feature"
   * @return    L'objet de classe "Piste" correspondant
   */
  private Piste asPiste(Feature f) {

    PisteProperties p = f.properties;
    Geo g = f.geometry;
      
    return new Piste(p.id, p.id_trc_geo, p.type_voie, p.type_voie2, p.longueur,
                    p.nbr_voie, p.separateur, p.saisons4, p.protege_4s, p.ville_mtl,
                    p.nom_arr_ville, g.coordinates);
  }
}

/*
 * Classes JsonPisteResponse, Feature, PisteProperties et Geo pour la
 * désérialisation
 */
class JsonPisteResponse {
  @JsonProperty("type")     String type;
  @JsonProperty("crs")      Object crs;
  @JsonProperty("features") Feature[] features;
}

class Feature {
  @JsonProperty("type")       String type;
  @JsonProperty("properties") PisteProperties properties;
  @JsonProperty("geometry")   Geo geometry;
}

class PisteProperties {
  @JsonProperty("ID")         Double id;
  @JsonProperty("ID_TRC_GEO") Double id_trc_geo;
  @JsonProperty("TYPE_VOIE")  Double type_voie;
  @JsonProperty("TYPE_VOIE2") Double type_voie2;
  @JsonProperty("LONGUEUR")   Double longueur;
  @JsonProperty("NBR_VOIE")   Double nbr_voie;
  @JsonProperty("SEPARATEUR") String separateur;
  @JsonProperty("SAISONS4")   String saisons4;
  @JsonProperty("PROTEGE_4S") String protege_4s;
  @JsonProperty("Ville_MTL")  String ville_mtl;
  @JsonProperty("NOM_ARR_VI") String nom_arr_ville;
}

class Geo {
  @JsonProperty("type")        String type;
  @JsonProperty("coordinates") Double[][][] coordinates;
}
