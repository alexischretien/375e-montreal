package ca.uqam.projet.controllers;

import java.util.*;
import java.time.LocalDate;

import ca.uqam.projet.repositories.*;
import ca.uqam.projet.resources.*;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.hibernate.validator.constraints.*;

@RestController
public class PisteController {

  @Autowired PisteRepository repository;

  @RequestMapping(value = "/pistes-cyclables", method = RequestMethod.GET)
  public ResponseEntity<Map<String, Object>> findByRadius(@RequestParam(value = "rayon", 
                                                                        required = false,
                                                                        defaultValue = "200") double rayon,
                                                          @RequestParam(value = "lat",
                                                                        required = true) double lat,
                                                          @RequestParam(value = "lng", 
                                                                        required = true) double lng) {

    ResponseEntity<Map<String, Object>> response;
    Map<String, Object> body = new HashMap<String, Object>();
    
    if (rayon < 0   ||
          lat   < -90  || lat > 90 ||
          lng   < -180 || lng > 180) {
        
        body.put("code", 400);
        body.put("error", "Invalid request parameters");
        response = new ResponseEntity(body, null, HttpStatus.valueOf(400));
    }
    else {
        body.put("code", 200);
        body.put("message", "OK");
        body.put("results", repository.findByRadius(rayon, lng, lat));
        response = new ResponseEntity(body, null, HttpStatus.valueOf(200));
         
        for (Piste p : repository.findByRadius(rayon, lng, lat)) {
            System.out.println(p.toString());
        }
    }
    return response;
  }
}

