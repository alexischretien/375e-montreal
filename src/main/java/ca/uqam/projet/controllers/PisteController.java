/*  
 * UQAM - Été 2017 - INF4375 - Groupe 30 - Projet de session
 *
 * PisteController.java - Fichier source .java de la classe 
 *                        PisteController, offrant un API
 *                        pour traiter des requêtes relatives
 *                        aux pistes cyclables stockées dans la base
 *                        de données
 * 
 * @Auteur Alexis Chrétien (CHRA25049209)
 * @Version 21 juillet 2017
 */

package ca.uqam.projet.controllers;

import java.util.*;
import java.time.*;

import ca.uqam.projet.repositories.*;
import ca.uqam.projet.resources.*;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;


@RestController
public class PisteController {

  @Autowired PisteRepository repository;

  @RequestMapping(value = "/pistes-cyclables", method = RequestMethod.GET)
  public ResponseEntity<Map<String, Object>> findByRadius(@RequestParam(value = "rayon", 
                                                                        required = false,
                                                                        defaultValue = "200") Double rayon,
                                                          @RequestParam(value = "lat",
                                                                        required = true) Double lat,
                                                          @RequestParam(value = "lng", 
                                                                        required = true) Double lng) {

    List<Piste> pistes;
    ResponseEntity<Map<String, Object>> response;

    List<String> errors = GeographyValidator.validateGeography(rayon, lng, lat);

    if (errors.size() > 0) {
      response = new ResponseEntity(new Erreur(400, errors), null, HttpStatus.valueOf(400));
    }
    else {
      pistes = repository.findByRadius(rayon, lng, lat);
      response = new ResponseEntity(pistes, null, HttpStatus.valueOf(200));
    }
    return response;
  }
}

