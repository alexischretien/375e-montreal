/*
 * UQAM - Été 2017 - INF4375 - Groupe 30 - Projet de session
 *
 * Piste.java - Fichier source .java de la classe Piste
 *
 * @Auteur  Alexis Chrétien (CHRA25049209)
 * @Version 21 juillet 2017
 */

package ca.uqam.projet.resources;

public class Piste {

  /*
   * Attributs
   */
  private  double  id;
  private  double  id_trc_geobase;
  private  double  type_voie;
  private  double  type_voie2;
  private  double  longueur;
  private  double  nbr_voie;
  private  String  separateur;
  private  String  saisons4;
  private  String  protege_4s;
  private  String  ville_mtl;
  private  String  nom_arr_ville;
  private  Double[][][] coordinates;

  /*
   * Constructeur
   */
  public Piste (double id, double id_trc_geobase, double type_voie, double type_voie2, double longueur,
                double nbr_voie, String separateur, String saisons4, String protege_4s, 
                String ville_mtl, String nom_arr_ville, Double[][][] coordinates) {
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

  public double getId() {
    return id;
  }
  public double getId_trc_geobase() {
    return id_trc_geobase;
  }
  public double getType_voie() {
    return type_voie;
  }
  public double getType_voie2() {
    return type_voie2;
  }
  public double getLongueur() {
    return longueur;
  }
  public double getNbr_voie() {
    return nbr_voie;
  }
  public String getSeparateur() {
    return separateur;
  }
  public String getSaisons4() {
    return saisons4;
  }
  public String getProtege_4s() {
    return protege_4s;
  }
  public String getVille_mtl() {
    return ville_mtl;
  }
  public String getNom_arr_ville() {
    return nom_arr_ville;
  }
  public Double[][][] getCoordinates() {
    return coordinates;
  }

  /*
   * printCoordinates - Méthode permettant de retourner la représentation 
   * de l'attribut "coordinates" sous forme d'une chaine de caracteres.
   *
   * @param  asArray    si "true", crée la représentation sous forme d'un array
   *                    sinon, crée la représentation sous forme d'un MultiLineString
   * @return            la chaine correspondante
   */ 
  public String printCoordinates (boolean asArray) {

    String title;
    String open;
    String close;
    String separ;

    title = (asArray ? "{" : "MULTILINESTRING(");
    open  = (asArray ? "{" : "(");
    close = (asArray ? "}" : ")");
    separ = (asArray ? "," : " ");
    
    int i, j, k;
    String s = (asArray ? "{" : "MULTILINESTRING(");

    for (i = 0 ; i < coordinates.length ; ++i) {
      s += (asArray ? "{" : "("); 

      for (j = 0 ; j < coordinates[i].length ; ++j) {
        s += (asArray ? "{" : "");

        for (k = 0 ; k < coordinates[i][j].length ; ++k) {
          s += coordinates[i][j][k] + (asArray ? "," : " ");
        }    
        if (k != 0) s = s.substring(0, s.length() - 1); 
        s += (asArray ? "}," : ",");
    } 
    if (j != 0) s = s.substring(0, s.length() - 1);
    s += (asArray ? "}," :"),");
  }
  if (i != 0)  s = s.substring(0, s.length() - 1);
  s += (asArray ? "}" : ")");
  return s; 
} 

  /*
   * setCoordinates - met à jour l'attribut "coordinates" en
   * fonction d'une chaine de caractère représentant un array 3D
   * contenant des elements de type Double
   *
   * @param multiLinestring    La représentation de l'array 3D
   */
  public void setCoordinates(String multiLineString) {

    Double[][][] array;

    multiLineString = multiLineString.replaceAll("((\\{\\{\\{)|(\\}\\}\\}))", "");
    String[] lineStrings = multiLineString.split("\\}\\},\\{\\{", -1);
    array = new Double[lineStrings.length][][];


    for (int i = 0 ; i < lineStrings.length ; ++i) {
      String[] points = lineStrings[i].split("\\},\\{", -1);
      array[i] = new Double[points.length][];

      for (int j = 0 ; j < points.length ; ++j) {
        String[] coordinates = points[j].split(",", -1);
        array[i][j] = new Double[coordinates.length];

        for (int k = 0 ; k < coordinates.length ; ++k) {
          array[i][j][k] = Double.parseDouble(coordinates[k]);
        }
      }
    }
    this.coordinates = array;
  }

  @Override public String toString() {
    return String.format("«%s» -- %s -- %s -- %s -- %s -- %s -- %s -- %s -- %s -- %s -- %s -- %s",
                         id, id_trc_geobase, type_voie, type_voie2, longueur, nbr_voie, 
                         separateur, saisons4, protege_4s, ville_mtl, nom_arr_ville, printCoordinates(true));
  }
}
