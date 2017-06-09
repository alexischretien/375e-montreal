/*
 * UQAM - Été 2017 - INF4375 - Groupe 30 - Projet de session
 *
 * Piste.java - Fichier source .java de la classe Piste
 *
 * @Auteur  Alexis Chrétien (CHRA25049209)
 * @Version 8 juin 2017
 */

package ca.uqam.projet.resources;

import com.fasterxml.jackson.annotation.*;

public class Piste {

  /*
   * Attributs
   */
  private  double  id;
  private  double  id_trc_geobase;
  private  int     type_voie;
  private  int     type_voie2;
  private  int     longueur;
  private  int     nbr_voie;
  private  String  separateur;
  private  String  saisons4;
  private  String  protege_4s;
  private  String  ville_mtl;
  private  String  nom_arr_ville;
  private  String  coordinates;


  /*
   * Constructeur
   */
  public Piste (double id, double id_trc_geobase, int type_voie, int type_voie2, int longueur,
                int nbr_voie, String separateur, String saisons4, String protege_4s, 
                String ville_mtl, String nom_arr_ville, String coordinates) {
    this.id             = id;
    this.id_trc_geobase = id_trc_geobase;
    this.type_voie      = type_voie;
    this.type_voie2     = type_voie2;
    this.longueur       = longueur;
    this.nbr_voie       = nbr_voie;
    this.separateur     = separateur;
    this.saisons4       = saisons4;
    this.protege_4s     = protege_4s;
    this.ville_mtl      = ville_mtl;
    this.nom_arr_ville  = nom_arr_ville;
    this.coordinates    = coordinates;
  }

  /*
   * Getters
   */
  @JsonProperty public  double       getId()             { return id;  }
  @JsonProperty public  double       getId_trc_geobase() { return id_trc_geobase; }
  @JsonProperty public  int          getType_voie()      { return type_voie; }
  @JsonProperty public  int          getType_voie2()     { return type_voie2; }
  @JsonProperty public  int          getLongueur()       { return longueur; }
  @JsonProperty public  int          getNbr_voie()       { return nbr_voie; }
  @JsonProperty public  String       getSeparateur()     { return separateur; }
  @JsonProperty public  String       getSaisons4()       { return saisons4; }
  @JsonProperty public  String       getProtege_4s()     { return protege_4s; }
  @JsonProperty public  String       getVille_mtl()      { return ville_mtl; }
  @JsonProperty public  String       getNom_arr_ville()  { return nom_arr_ville; }
  @JsonProperty public  String       getCoordinates()    { return coordinates; }

  /*
   * Méthodes
   */
  @Override public String toString() {
    return String.format("«%s» -- %s -- %s -- %s -- %s -- %s -- %s -- %s -- %s -- %s -- %s -- %s",
                         id, id_trc_geobase, type_voie, type_voie2, longueur, nbr_voie, 
                         separateur, saisons4, protege_4s, ville_mtl, nom_arr_ville, coordinates );
  }
}
