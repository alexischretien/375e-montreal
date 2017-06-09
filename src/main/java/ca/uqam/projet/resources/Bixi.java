/*
 * UQAM - Été 2017 - INF4375 - Groupe 30 - Projet de session
 *
 * Bixi.java - Fichier source .java de la classe Bixi
 *
 * @Auteur  Alexis Chrétien (CHRA25049209)
 * @Version 8 juin 2017
 */

package ca.uqam.projet.resources;

import com.fasterxml.jackson.annotation.*;

public class Bixi {

  /*
   * Attributs
   */
  private  int     id;
  private  String  nom;
  private  String  idTerminal;
  private  int     etat;
  private  boolean estBloquee;
  private  boolean estSuspendue;
  private  boolean estHorsService;
  private  long    derniereMiseAJour;
  private  long    derniereCommunication;
  private  boolean bk;
  private  boolean bl;
  private  double  latitude;
  private  double  longitude; 
  private  int     nbBornesDisponibles;
  private  int     nbBornesIndisponibles;
  private  int     nbVelosDisponibles;
  private  int     nbVelosIndisponibles;

  /*
   * Constructeur
   */
  public Bixi (int id, String s, String n, int st, boolean b, boolean su, boolean m, 
               long lu, long lc, boolean bk, boolean bl, double la, double lo, 
               int da, int dx, int ba, int bx) {
    this.id                    = id;
    this.nom                   = s ;
    this.idTerminal            = n ;
    this.etat                  = st;
    this.estBloquee            = b ;
    this.estSuspendue          = su;
    this.estHorsService        = m ;
    this.derniereMiseAJour     = lu;
    this.derniereCommunication = lc;
    this.bk                    = bk;
    this.bl                    = bl;
    this.latitude              = la;
    this.longitude             = lo;
    this.nbBornesDisponibles   = da;
    this.nbBornesIndisponibles = dx;
    this.nbVelosDisponibles    = ba;
    this.nbVelosIndisponibles  = bx;
  }

  /*
   * Getters 
   */
  @JsonProperty public  int     getId()                    { return id;  }
  @JsonProperty public  String  getNom()                   { return nom; }
  @JsonProperty public  String  getIdTerminal()            { return idTerminal; }
  @JsonProperty public  int     getEtat()                  { return etat; }
  @JsonProperty public  boolean getEstBloquee()            { return estBloquee; }
  @JsonProperty public  boolean getEstSuspendue()          { return estSuspendue; }
  @JsonProperty public  boolean getEstHorsService()        { return estHorsService; }
  @JsonProperty public  long    getDerniereMiseAJour()     { return derniereMiseAJour; }
  @JsonProperty public  long    getDerniereCommunication() { return derniereCommunication; }
  @JsonProperty public  boolean getBk()                    { return bk; }
  @JsonProperty public  boolean getBl()                    { return bl; }
  @JsonProperty public  double  getLatitude()              { return latitude; }
  @JsonProperty public  double  getLongitude()             { return longitude; }
  @JsonProperty public  int     getNbBornesDisponibles()   { return nbBornesDisponibles; }
  @JsonProperty public  int     getNbBornesIndisponibles() { return nbBornesIndisponibles; }
  @JsonProperty public  int     getNbVelosDisponibles()    { return nbVelosDisponibles; }
  @JsonProperty public  int     getNbVelosIndisponibles()  { return nbVelosIndisponibles; }

  /*
   * Méthodes
   */
  @Override public String toString() {
    return String.format("«%s» -- %s -- %s -- %s -- %s -- %s -- %s -- %s -- %s" +
                             " -- %s -- %s -- %s -- %s -- %s -- %s -- %s -- %s", 
                         id, nom, idTerminal, etat, estBloquee, estSuspendue, estHorsService, 
                         derniereMiseAJour, derniereCommunication, bk, bl, latitude, longitude, 
                         nbBornesDisponibles, nbBornesIndisponibles, nbVelosDisponibles, 
                         nbVelosIndisponibles );
  }
}
