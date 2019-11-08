import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Projet java créer un jeu de plateau en java
 * 
 * @author Camille Paudrat LP4 24/10/19 Une classe qui modélise le plateau.
 */
public class Plateau {
    // Variables
    private int typePlateau;
    private List<Integer> puissance[] = new List[7];
    private int morpion[] = new int[9];
    private int colEnCours;
    private Pendu pendu;
    private int dernierCoup;

    /**
     * Constructeur du plateau
     * 
     * @param typePlateau le parametre defini le type de plateau a construire 1
     *                    Puissance4 2 Morpion 3 Pendu 4 ...
     * @throws IOException
     */
    public Plateau(int typePlateau) throws IOException {
        this.typePlateau = typePlateau;
        switch (this.typePlateau) {
        case 1:
            for (int i = 0; i < 7; i++) {
                this.puissance[i] = new ArrayList<Integer>();
            }
            break;
        case 2:
            for (int i : this.morpion) {
                this.morpion[i] = 0;
            }
            break;
        case 3:
            this.pendu = new Pendu();
            break;
        }
    }

    /**
     * Rempli le tableau avec le joueur dans la caze jouée A chaque type de table
     * sont case
     * 
     * @param caze   l'index du tableau a remplir
     * @param joueur la valeur inserer dans le tableau
     * @return true si le remplassage s'effectue correctement sinon false voir le
     *         message d'erreur
     */
    public boolean tourDeJeu(int caze, int joueur, Character charPendu) {
        switch (this.typePlateau) {
        case 1:
            if (caze >= 0 && caze < 7) {
                this.colEnCours = caze;
                if (this.puissance[caze].size() < 6) {
                    this.puissance[caze].add(joueur);
                    this.afficherPlateau();
                    return true;
                } else {
                    System.out.println("Colonne pleine, veuillez rejouer.");
                    return false;
                }
            } else {
                System.out.println("Cette Colonne n'existe pas!!");
                return false;
            }
        case 2:
            if (caze >= 0 && caze < 9) {
                if (this.morpion[caze] == 0) {
                    this.morpion[caze] = joueur;
                    this.dernierCoup = caze;
                    this.afficherPlateau();
                    return true;
                } else {
                    System.out.println("Case deja joue, veuillez rejouer.");
                    return false;
                }
            } else {
                System.out.println("Cette case n'existe pas!!");
                return false;
            }
        case 3:
            boolean retour;
            retour = this.pendu.compare(charPendu);
            this.afficherPlateau();
            return retour;
        default:
            return false;
        }
    }

    /**
     * Verifie si l'une des conditions de victoire est true si c'est le cas elle
     * retourn true sinon false
     * 
     * @return true si l'une des conditions de victoire est true sinon false
     */
    public boolean verifVictoire() {
        return this.verifVertical() || this.verifHorizontal() || this.verifDiagonal();
    }

    /**
     * verifie les colonnes pour voir si la condition de victoire est atteinte
     * 
     * @return true si une colonne correspond sinon false
     */
    private boolean verifVertical() {
        int calculVictory;
        switch (this.typePlateau) {
        case 1:
            calculVictory = 0;
            int tailleLi = this.puissance[colEnCours].size() - 1;
            if (tailleLi > 2 && !(this.puissance[colEnCours].isEmpty())) {
                for (int i = tailleLi; i >= tailleLi - 3; i--) {
                    calculVictory += this.puissance[colEnCours].get(i);
                }
            }
            if (calculVictory == 4 || calculVictory == 40) {
                return true;
            }
            return false;
        case 2:
            for (int i = 0; i < 3; i++) {
                calculVictory = 0;
                for (int j = i; j <= i + 7; j += 3) {
                    calculVictory += this.morpion[j];
                }
                if (calculVictory == 3 || calculVictory == 30) {
                    return true;
                }
            }
            return false;
        case 3:
            return false;
        default:
            return false;
        }
    }

    /**
     * verifie les lignes pour voir si la condition de victoire est atteinte
     * 
     * @return true si une ligne correspond sinon false
     */
    private boolean verifHorizontal() {
        int calculVictory;
        switch (this.typePlateau) {
        case 1:
            int hauteurList = this.puissance[this.colEnCours].size() - 1;
            calculVictory = this.puissance[this.colEnCours].get(hauteurList);
            int compteur = 3;
            for (int i = this.colEnCours + 1; i < this.puissance.length; i++) {
                if (this.puissance[i].size() > hauteurList && !(this.puissance[i].isEmpty())
                        && i < this.colEnCours + 4) {
                    if (this.puissance[i].get(hauteurList) == this.puissance[this.colEnCours].get(hauteurList)) {
                        calculVictory += this.puissance[i].get(hauteurList); // verifie les cases vers la droite
                        compteur--;
                    } else {
                        break;
                    }
                }
            }
            for (int i = this.colEnCours - 1; i >= this.colEnCours - compteur; i--) {
                if (i >= 0) {
                    if (this.puissance[i].size() > hauteurList && !(this.puissance[i].isEmpty())) {
                        calculVictory += this.puissance[i].get(hauteurList); // verifie les cases vers la gauche
                    }
                }
            }
            if (calculVictory == 4 || calculVictory == 40) {
                return true;
            }
            return false;
        case 2:
            for (int i = 0; i < 7; i += 3) {
                calculVictory = 0;
                for (int j = i; j < i + 3; j++) {
                    calculVictory += this.morpion[j];
                }
                if (calculVictory == 3 || calculVictory == 30) {
                    return true;
                }
            }
            return false;
        case 3:
            return this.pendu.verifChaine();
        default:
            return false;
        }
    }

    /**
     * verifie les diagonales pour voir si la condition de victoire est atteinte
     * 
     * @return true si une diagonales correspond sinon false
     */
    private boolean verifDiagonal() {
        int calculVictory = 0;
        switch (this.typePlateau) {
        case 1:
            int hauteurList = this.puissance[this.colEnCours].size() - 1;
            calculVictory = this.puissance[this.colEnCours].get(hauteurList);
            int compteur = 3;
            int j = hauteurList + 1;
            for (int i = this.colEnCours + 1; i < this.puissance.length; i++) {
                if (this.puissance[i].size() > j && !(this.puissance[i].isEmpty()) && j < hauteurList + 4) {
                    if (this.puissance[i].get(j) == this.puissance[this.colEnCours].get(hauteurList)) {
                        calculVictory += this.puissance[i].get(j); // verifie les cases en haut a droite de la precedente
                        compteur--;
                        j++;
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }
            j = hauteurList - 1;
            for (int i = this.colEnCours - 1; i >= this.colEnCours - compteur; i--) {
                if (i >= 0 && j >= 0) {
                    if (this.puissance[i].size() > j && !(this.puissance[i].isEmpty())) {
                        calculVictory += this.puissance[i].get(j); // verifie les cases en bas a gauche de la precedente
                        j--;
                    }
                } else {
                    break;
                }
            }
            if (calculVictory == 4 || calculVictory == 40) {
                return true;
            }
            calculVictory = this.puissance[this.colEnCours].get(hauteurList);
            compteur = 3;
            j = hauteurList - 1;
            for (int i = this.colEnCours + 1; i < this.puissance.length; i++) {
                if (this.puissance[i].size() > j && !(this.puissance[i].isEmpty()) && j > hauteurList - 4 && j >= 0) {
                    if (this.puissance[i].get(j) == this.puissance[this.colEnCours].get(hauteurList)) {
                        calculVictory += this.puissance[i].get(j); // verifie les cases en bas a droite de la precedente
                        compteur--;
                        j--;
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }
            j = hauteurList + 1;
            for (int i = colEnCours - 1; i >= colEnCours - compteur; i--) {
                if (i >= 0 && j < 6) {
                    if (this.puissance[i].size() > j && !(this.puissance[i].isEmpty())) {
                        calculVictory += this.puissance[i].get(j); // verifie les cases en haut a gauche de la precedente
                        j++;
                    }
                } else {
                    break;
                }
            }
            if (calculVictory == 4 || calculVictory == 40) {
                return true;
            }
            return false;
        case 2:
            for (int i = 0; i < 9; i += 4) {
                calculVictory += this.morpion[i];
            }
            if (calculVictory == 3 || calculVictory == 30) {
                return true;
            }
            calculVictory = 0;
            for (int i = 2; i < 7; i += 2) {
                calculVictory += this.morpion[i];
            }
            if (calculVictory == 3 || calculVictory == 30) {
                return true;
            }
            return false;
        case 3:
            return false;
        default:
            return false;
        }
    }

    /**
     * verifie si le nombre de tour atteint le maximum et donc declenche l'egalité
     * 
     * @param tour tour en cours
     * @return false tantque le nombre de tour maximum n'est pas atteint
     */
    public boolean verifDraw(int tour) {
        switch (this.typePlateau) {
        case 1:
            return tour > 42;
        case 2:
            return tour > 9;
        case 3:
            if (this.pendu.getVieRestante() == 1) {
                System.out.println("\nLe mot etait " + this.pendu.getMotATrouver()); // si echec a trouver le mot
                return true;
            }
            return false;
        default:
            return false;
        }
    }

    /**
     * Affiche le plateau dans la console
     */
    public void afficherPlateau() {
        switch (this.typePlateau) {
        case 1:
            System.out.println(" ___________________________ ");
            for (int i = 6; i > 0; i--) {
                for (List<Integer> li : this.puissance) {
                    if (li.size() >= i && !(li.isEmpty())) {
                        if (li.get(i - 1) == 1) {
                            System.out.printf("|%3S", "_X_");
                        } else if (li.get(i - 1) == 10) {
                            System.out.printf("|%3S", "_O_");
                        }
                    } else {
                        System.out.printf("|%3S", "___");
                    }
                }
                System.out.print("|\n");
            }
            for (int i = 1; i <= this.puissance.length; i++) {
                System.out.printf("%3d ", i);
            }
            System.out.println();
            break;
        case 2:
            System.out.println("\t\tAgencement");
            System.out.println("-------------");
            for (int i = 0; i < this.morpion.length; i++) {
                if (i == 3 || i == 6) {
                    System.out.println("|   " + (i - 2) + "   " + (i - 1) + "   " + (i) + "\n-------------");
                    if (this.morpion[i] == 1) {
                        System.out.printf("|%3S", " X ");
                    } else if (this.morpion[i] == 10) {
                        System.out.printf("|%3S", " O ");
                    } else {
                        System.out.printf("|%3S", "   ");
                    }

                } else {
                    if (this.morpion[i] == 1) {
                        System.out.printf("|%3S", " X ");
                    } else if (this.morpion[i] == 10) {
                        System.out.printf("|%3S", " O ");
                    } else {
                        System.out.printf("|%3S", "   ");
                    }
                }
            }
            System.out.println("|   " + 7 + "   " + 8 + "   " + 9 + "\n-------------");
            break;
        case 3:
            this.pendu.affichePendu();
            break;
        }
    }

    /**
     * retourne la taille du plateau
     * 
     * @return int egale a la taille du plateau
     */
    public int getTaillePlateau() {
        switch (this.typePlateau) {
        case 1:
            return this.puissance.length;
        case 2:
            return this.morpion.length;
        default:
            return 0;
        }
    }

    /**
     * renvoi le plateau du puissance 4 pour l'ia
     * 
     * @return retourne le plateau puissance 4
     */
    public List<Integer>[] getPlateauP4() {
        return this.puissance;
    }

    /**
     * renvoi le plateau du morpion pour l'ia
     * 
     * @return retourne le plateau morpion
     */
    public int[] getPlateauMorpion() {
        return this.morpion;
    }

    /**
     * retourne le dernier coup jouer
     * 
     * @return retourne le dernier coup jouer
     */
    public int getDernierCoup() {
        return this.dernierCoup;
    }

    /**
     * retourne la derniere colonne jouer
     * 
     * @return retourne la derniere colonne jouer
     */
    public int getColEnCours() {
        return this.colEnCours;
    }
}