/*
 * UQAM - Été 2017 - INF4375 - Groupe 30 - Projet de session
 *
 * FetchGeoActiviteTask.java - Fichier source .java de la classe FetchGeoActiviteTask.
 * Une méthode périodique  permet de récupérer et de sauvegarder les données 
 * géométriques des activités du 375e de la Ville de Montréal à toutes les 15 minutes
 * en effectuant une série d'appels à l'API de géocodage de Google. 
 *
 * Lors de chaque exécution de la méthode, un maximum de 25 requêtes sont envoyées à 
 * l'API de Google (la méthode FindForGeoUpdate retourne une liste de 25 activités ou moins)
 * La méthode est exécutée à toutes les 15 minutes. Ces restrictions
 * permettent de respecter les contraintes d'utilisation gratuire de l'API de Google,
 * les limites étant 50 requêtes par seconde et 2500 requêtes par jour.
 *
 *
 * @Auteur  Alexis Chrétien (CHRA25049209)
 * @Version 21 juillet 2017
 */

package ca.uqam.projet.tasks;
 
import ca.uqam.projet.resources.*;
import ca.uqam.projet.repositories.*;

import java.util.*;
import java.util.stream.*;

import com.fasterxml.jackson.annotation.*;
import org.jsoup.*;
import org.slf4j.*;
 
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.web.client.*;
import org.springframework.dao.*;
 
import java.io.*;
import javax.annotation.PostConstruct;

@Component
public class FetchGeoActiviteTask {

  private static final String url_debut = "https://maps.googleapis.com/maps/api/geocode/json?address=";
  private static final String url_fin = "+Quebec,+Canada&key=AIzaSyA7s636RtJhED5ikxVatVnKbKTqeE3I5_g";
  private static final Logger log = LoggerFactory.getLogger(FetchGeoActiviteTask.class);
    
  @Autowired private ActiviteRepository repository;
  
  // À toutes les 15 minutes
  @PostConstruct
  @Scheduled(cron="0 */15 * * * ?") 
  public void execute() {

    Double lng;
    Double lat;
  
    List<Activite> activites = repository.findForGeoUpdate();

    for (Activite a : activites) {
      String url = url_debut + a.getLieux().replaceAll(" ", "+") + url_fin;
      try {
                  
        RestTemplate restTemplate = new RestTemplate();  
        GoogleReply response = restTemplate.getForObject(url, GoogleReply.class);

        // Vérifier que la requête à retourné au moins un résultat
        if (response.results.isEmpty()) {
          log.warn(String.format("Google GET request yielded no result for «%s» -- %s\n"
                               + "Requested URL was : %s", a.getId(), a.getLieux(), url));
          repository.update_noGoogleResult(a.getId()); 
        }
        else {
          lng = response.results.get(0).geometry.location.lng;
          lat = response.results.get(0).geometry.location.lat;

          // Vérifier que les coordonnées renvoyées par Google sont dans les alentours
          // de Montréal
          if (lng < -75.0 || lng > -72.5 ||
              lat <  45.1 || lat >  46.1) {
            log.warn(String.format("Google GET request yielded improbable results for "
                                 + "«%s» -- %s\n The coordinates were too far away from "
                                 + "Montréal and were thus discarded.\n Received coordinates "
                                 + "were : [%s, %s]", a.getId(), a.getLieux(), lng, lat));
            repository.update_noGoogleResult(a.getId());
          }    
          else {
            log.info(String.format("«%s» -- [%s, %s]", a.getId(), lng, lat));
            repository.update_geo(a.getId(), lng, lat);
          }
        }
      } catch(EmptyResultDataAccessException e) {
          log.warn("Caught an exception during FetchGeoActiviteTask : " + e.toString());
      } catch (HttpClientErrorException e) {
          log.warn("Caught an exception during FetchGeoActiviteTask : " + e.toString() 
                 + "\nRequested URL was : " + url);
      } catch (HttpServerErrorException e) {
          log.warn("Caught an exception during FetchGeoActiviteTask : " + e.toString());
      }
    }
  }
}


/*
 * Classes GoogleReply, Result, Geometry et Location pour la déserialisation
 */
class GoogleReply {
  @JsonProperty("results") List<Result> results;

}

class Result {
  @JsonProperty("address_component") Object address_component;
  @JsonProperty("geometry") Geometry geometry;
  @JsonProperty("place_id") String place_id;
  @JsonProperty("types") String[] types; 
}
class Geometry {
  @JsonProperty("location") Location location;
  @JsonProperty("location_type") String location_type;
  @JsonProperty("viewport") Object viewport;
}

class Location {
  @JsonProperty("lat") double lat;
  @JsonProperty("lng") double lng;
}
