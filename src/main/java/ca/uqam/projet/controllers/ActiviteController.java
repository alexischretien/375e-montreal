package ca.uqam.projet.controllers;

import java.util.*;
import java.time.LocalDate;

import ca.uqam.projet.repositories.*;
import ca.uqam.projet.resources.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

@RestController
//@RequestMapping("/activites")
//@EnableAutoConfiguration
public class ActiviteController {

  @Autowired ActiviteRepository repository;

  @RequestMapping("/activites")
  public List<Activite> findAll() {
    return repository.findAll();
  }
/*
  @RequestMapping(method = RequestMethod.GET)
  public List<Activite> findActivites(@RequestParam(value = "du", required = false) 
                                      @DateTimeFormat("yyyy-MM-dd") LocalDate du, 
                                      @RequestParam(value = "au", required = false) 
                                      @DateTimeFormat("yyyy-MM-dd") LocalDate au,
                                      @RequestParam(value = "rayon", 
                                                    required = false) Integer rayon,
                                      @RequestParam(value = "lat",
                                                    required = false,
                                                    defaultValue = 45.5089306) Double lat,
                                      @RequestParam(value = "lng",
                                                    required ⁼ false,
                                                     defaultValue = -73.5685676) Double lng) {

    if ((du != null && au == null) ||
        (du == null && au != null)) {
      System.out.println("Erreur, Si une date est spécifié, l'autre doit aussi l'être");
    }
    else if (au < du) {
      System.out.println("Erreur, date de fin plus petite que date du début");
    }
    else if ((lat != null && lng == null) ||
             (lat == null && lng != null)) {
      System.out.println("Erreur, si la long ou lat est spécifié, l'autre coordonnée doit aussi l'être");
    }
    else if((lng != null && lng > 180)  ||
            (lng != null && lng < -180) ||
            (lat != null && lat > 90)   ||
            (lat != null && lat < -90)) {
      System.out.println("Mauvais format de longitude et/ou latitude");
    }
    else if(rayon == null && lat == null && lng == null) {
      if (du != null && au != null) {
        repository.findByDates(du, au)
      }

    }
    
 }  */
/**
  @RequestMapping("/activites/contenu")
  public List<Activite> findByContenu(@RequestParam("term") String[] tsterms) {
    return (tsterms.length == 0) ? repository.findAll() : repository.findByNom(tsterms);
  }
*/
  @RequestMapping("/activites/{id}")
  public Activite findById(@PathVariable("id") int id) {
    return repository.findById(id);
  }
  /*
  private String dateNow() {
    return DateTimeFormatter.ofPattern("yyyy-MM-dd").format((new LocalDate()).now());
  
  }

  private String dateTomorrow() {
    return DateTimeFormatter.ofPattern("yyyy-MM-dd").format((new LocalDate()).now().plusDays(1));
  }
*/
}
