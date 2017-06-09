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

@Component
public class FetchBixiTask {

  private static final Logger log = LoggerFactory.getLogger(FetchBixiTask.class);
  private static final String URL = "http://secure.bixi.com/data/stations.json";
  @Autowired private BixiRepository repository;
  
  // Scheduled for every 10 minutes
  @Scheduled(cron="0 */10 * * * ?") 
  public void execute() {

    try {
      JsonBixiResponse response = new RestTemplate().getForObject(URL, JsonBixiResponse.class);
      Arrays.asList(response.jsonBixis).stream()
        .map(this::asBixi)
        .peek(c -> log.info(c.toString()))
        .forEach(repository::insert)
        ;
      
      } catch (EmptyResultDataAccessException e) {
          log.warn("Caught an exception during FetchBixiTask : " + e.toString()); 
      } catch (HttpClientErrorException e) {
          log.warn("Caught an exception during FetchBixiTask : " + e.toString());
      } catch (HttpServerErrorException e) {
          log.warn("Caught an exception during FetchBixiTask : " + e.toString());
      }
  }
  private Bixi asBixi(JsonBixi b) {
    return new Bixi(b.id, b.s,  b.n,  b.st, b.b,  b.su, b.m,  b.lu, b.lc, 
                    b.bk, b.bl, b.la, b.lo, b.da, b.dx, b.ba, b.bx);
  }
}

class JsonBixiResponse {
  @JsonProperty("stations")        JsonBixi[] jsonBixis;
  @JsonProperty("schemeSuspended") String     schemeSuspended;
  @JsonProperty("timestamp")       long       timestamp;
}

class JsonBixi {
  @JsonProperty("id") int     id;
  @JsonProperty("s")  String  s;
  @JsonProperty("n")  String  n;
  @JsonProperty("st") int     st;
  @JsonProperty("b")  boolean b;
  @JsonProperty("su") boolean su;
  @JsonProperty("m")  boolean m;
  @JsonProperty("lu") long    lu;
  @JsonProperty("lc") long    lc;
  @JsonProperty("bk") boolean bk;
  @JsonProperty("bl") boolean bl;
  @JsonProperty("la") double  la;
  @JsonProperty("lo") double  lo;
  @JsonProperty("da") int     da;
  @JsonProperty("dx") int     dx;
  @JsonProperty("ba") int     ba;
  @JsonProperty("bx") int     bx;
}

