/*  
 * UQAM - Été 2017 - INF4375 - Groupe 30 - Projet de session
 *
 * MethodArgumentTypeMismatchExceptionHandler.java - Fichier source .java de 
 * la classe MethodArgumentTypeMismatchExceptionHandler permettant de traiter
 * les cas où une requête client vers l'une ou l'autre des API de l'application
 * génère un MethodArgumentTypeMismatchException. Permet de répondre au client
 * avec une réponse sur mesure. 
 *               
 * 
 * @Auteur Alexis Chrétien (CHRA25049209)
 * @Version 21 juillet 2017
 */


package ca.uqam.projet.controllers;

import ca.uqam.projet.resources.*;

import java.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.*;
import org.springframework.http.*;


@ControllerAdvice
class MethodArgumentTypeMismatchExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class) 
    public ResponseEntity handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException e) {

        ResponseEntity response;
        List<String> errors = new ArrayList<String>();

        errors.add("Type du paramètre '" + e.getParameter().getParameterName() + "' invalide");
        response = new ResponseEntity(new Erreur(400, errors), null, HttpStatus.valueOf(400));
        
        return response;
    }
}
