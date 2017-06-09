package ca.uqam.projet.tasks;
 
import ca.uqam.projet.resources.*;
 
import java.util.*;
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

@Component
public class FetchGeoActiviteTask {

  private static final String url_debut = "https://maps.googleapis.com/maps/api/geocode/json?address=";
  private static final String url_fin = "+Quebec,+Canada&key=AIzaSyA7s636RtJhED5ikxVatVnKbKTqeE3I5_g";
  private static final Logger log = LoggerFactory.getLogger(FetchGeoActiviteTask.class);
    
  @Autowired private ActiviteRepository repository;
  
  // Scheduled for every 15 minutes
  @Scheduled(cron="0 */15 * * * ?") 
  public void execute() {

    List<Activite> activitesChangees;
    Double lng;
    Double lat;

    try {
      List<Activite> activites = repository.findForGeoUpdate();

      for (Activite a : activites) {
        String url = url_debut + a.getLieux().replaceAll(" ", "+") + url_fin;

        try {                   
          RestTemplate restTemplate = new RestTemplate();  
          GoogleReply response = restTemplate.getForObject(url, GoogleReply.class);

          // Verifying if GET yielded any result
          if (response.results.isEmpty()) {
            log.warn(String.format("Google GET request yielded no result for «%s» -- %s\n"
                                 + "Requested URL was : %s", a.getId(), a.getLieux(), url));
            repository.update_noGoogleResult(a.getId()); 
          }
          else {
            lng = response.results.get(0).geometry.location.lng;
            lat = response.results.get(0).geometry.location.lat;

           // Verifying if coordinates are within reasonnable range of Montreal
            if (lng < -75.0 || lng > -72.5 ||
                lat <  45.1 || lat >  46.1) {
              log.warn(String.format("Google GET request yielded improbable results for "
                                   + "«%s» -- %s\n The coordinates were too far away from "
                                   + "Montréal and were thus discarded.\n Received coordinates "
                                   + "were : [%s, %s]", a.getId(), a.getLieux(), lng, lat));
              repository.update_noGoogleResult(a.getId());
            }     
            // GET request yielded good results
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
    catch (EmptyResultDataAccessException e) {
      log.warn("Caught an exception during FetchGeoActiviteTask : " + e.toString());
    }
  }
}

//sonIgnoreProperties(ignoreUnknown = true)
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
