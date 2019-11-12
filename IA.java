import java.util.List;

/**
 * Projet java creer un jeu de plateau en java
 * Class gerant l'IA Morpion et Puissance 4
 * 
 * @author Camille Paudrat LP4 4/11/19 Une classe qui gere l'IA.
 */
public class IA {
    // Variables
    private int typePlateau;
    private List<Integer>[] puissance;
    private int[] morpion;
    private int coupAJouer;
    private int[] coupMorpion = { 10, 10, 10, 10, 10, 10, 10, 10, 10 };
    private int pionIA;
    private int derniereColonne;
    private int coupInterdit;

    /**
     * Constructeur
     */
    public IA(int typePlateau) {
        this.typePlateau = typePlateau;
    }

    /**
     * recupere les informations du jeu puissance 4 pour etre traite et renvoi le
     * coup a jouer
     * 
     * @param tour            entier tour en cours
     * @param puissance       tableau de List d'entier est l'etat actuel du plateau
     * @param derniereColonne un entier etant le coup precedent
     * @return un entier le coup a jouer
     * @throws InterruptedException
     */
    public int coupIA(int tour, List<Integer>[] puissance, int derniereColonne) throws InterruptedException {
        this.puissance = puissance;
        this.derniereColonne = derniereColonne;
        Thread.sleep(1000); // temps d'arret d'une seconde
        if (tour == 1) {
            return 4; // joue au centre si premier tour
        } else if (tour < 4) {
            return this.randomP4(); // joue aleatoire
        } else {
            if (this.verifPlateau()) {
                return this.coupAJouer + 1; // joue pour bloquer le joueur
            }
            if (this.puissance[this.derniereColonne].size() < 6 && this.derniereColonne + 1 != this.coupInterdit) {
                return this.derniereColonne + 1; // joue la meme colonne que le joueur
            } else {
                return this.randomP4(); // joue aleatoire
            }
        }
    }

    /**
     * recupere les informations du jeu Morpion pour etre traite et renvoi le coup a
     * jouer
     * 
     * @param tour            entier tour en cours
     * @param morpion         tableau d'entiers representant l'etat actuel du
     *                        morpion
     * @param derniereColonne un entier etant le coup precedent
     * @return un entier le coup a jouer
     * @throws InterruptedException
     */
    public int coupIA(int tour, int[] morpion, int dernierCoup) throws InterruptedException {
        this.morpion = morpion;
        boolean ok = false;
        int coup = -1;
        Thread.sleep(1000); // arret d'une seconde
        if (tour < 3) {
            if (this.morpion[4] == 0) { // si centre vide
                this.enregistrementCoup(5);
                pionIA = 10;
                return 5;
            } else {
                this.enregistrementCoup(dernierCoup + 1);
                pionIA = 1;
                while (!ok) {
                    coup = ((int) (Math.random() * this.morpion.length));
                    ok = this.enregistrementCoup(coup + 1);
                }
                return coup + 1;
            }
        } else {
            this.enregistrementCoup(dernierCoup + 1);
            if (this.verifPlateau()) { // verifie si un joueur est a 1 pion de la victoire
                this.enregistrementCoup(this.coupAJouer + 1);
                return this.coupAJouer + 1;
            } else {
                while (!ok) {
                    coup = ((int) (Math.random() * this.morpion.length));
                    ok = this.enregistrementCoup(coup + 1);
                }
                return coup + 1;
            }
        }
    }

    /**
     * execute les fonctions verifiant si un joueur est proche de la victoire
     * 
     * @return true si proche de la victoire sinon false
     */
    public boolean verifPlateau() {
        this.coupAJouer = 0;
        return this.vertical() || this.horizon() || this.diagonal();
    }

    /**
     * verifie sur la vertical si un joueur est proche de la victoire
     * 
     * @return true si proche de la victoire sinon false
     */
    public boolean vertical() {
        int calculVictory;
        int position = -1;
        if (this.typePlateau == 1) { // puissance 4
            calculVictory = 0;
            int tailleLi = this.puissance[this.derniereColonne].size() - 1;
            if (tailleLi > 1 && !(this.puissance[this.derniereColonne].isEmpty())) {
                for (int i = tailleLi; i >= tailleLi - 2; i--) {
                    calculVictory += this.puissance[this.derniereColonne].get(i);
                }
            }
            if (calculVictory == 3 || calculVictory == 30) {
                this.coupAJouer = this.derniereColonne;
                return true;
            }
            return false;
        } else if (this.typePlateau == 2) { // morpion
            for (int i = 0; i < 3; i++) {
                calculVictory = 0;
                for (int j = i; j <= i + 7; j += 3) {
                    calculVictory += this.morpion[j];
                }
                if (calculVictory == pionIA * 2) { // si IA proche de la victoire
                    position = i; // colonne proche de la victoire
                    break;
                } else if (calculVictory == 2 || calculVictory == 20) {
                    position = i; // colonne proche de la victoire
                    break;
                }
            }
            if (position >= 0) {
                for (int i = position; i < position + 7; i += 3) { // test la colonne proche de la victoire pour
                                                                   // determiner la case a jouer
                    if (this.morpion[i] == 0) {
                        this.coupAJouer = i;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * verifie sur l'horizontal si un joueur est proche de la victoire
     * 
     * @return true si proche de la victoire sinon false
     */
    public boolean horizon() {
        int calculVictory;
        int position = -1;
        if (this.typePlateau == 1) { // puissance 4
            int hauteurList = this.puissance[this.derniereColonne].size() - 1;
            calculVictory = this.puissance[this.derniereColonne].get(hauteurList);
            int compteur = 3;
            for (int i = this.derniereColonne + 1; i < this.puissance.length; i++) {
                if (this.puissance[i].size() > hauteurList && i < this.derniereColonne + 4) {
                    if (this.puissance[i].get(hauteurList) == this.puissance[this.derniereColonne].get(hauteurList)
                            && position == -1) {
                        calculVictory += this.puissance[i].get(hauteurList); // verifie les cases vers la droite
                        compteur--;
                    } else {
                        break; // si pion different arret de la boucle
                    }
                } else {
                    if (position != -1) {
                        break; // si case vide deja enregistre
                    }
                    position = i; // enregistre la colonne vide
                }
            }
            for (int i = this.derniereColonne - 1; i >= this.derniereColonne - compteur; i--) {
                if (i >= 0) {
                    if (this.puissance[i].size() > hauteurList) {
                        if (this.puissance[i].get(hauteurList) == this.puissance[this.derniereColonne]
                                .get(hauteurList)) {
                            calculVictory += this.puissance[i].get(hauteurList); // verifie les cases vers la gauche
                        } else {
                            break; // si pion different arret de la boucle
                        }
                    } else {
                        if (position != -1) {
                            break; // si case vide deja enregistre
                        }
                        position = i; // enregistre la colonne vide
                    }
                }
            }
            if ((calculVictory == 3 || calculVictory == 30) && position != -1) {
                if (this.puissance[position].size() == hauteurList) {
                    this.coupAJouer = position; // colonne a jouer
                    return true;
                } else if (this.puissance[position].size() == hauteurList - 1) {
                    this.coupInterdit = position; // colonne a ne pas jouer
                    return false;
                }
            }
            return false;
        } else if (this.typePlateau == 2) { // morpion
            for (int i = 0; i < 7; i += 3) {
                calculVictory = 0;
                for (int j = i; j < i + 3; j++) {
                    calculVictory += this.morpion[j];
                }
                if (calculVictory == pionIA * 2) { // si ia proche de la victoire
                    position = i; // colonne proche de la victoire
                    break;
                } else if (calculVictory == 2 || calculVictory == 20) {
                    position = i; // colonne proche de la victoire
                    break;
                }
            }
            if (position >= 0) {
                for (int i = position; i < position + 3; i++) { // test la ligne et enregistre la case a jouer
                    if (this.morpion[i] == 0) {
                        this.coupAJouer = i;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * verifie sur les diagonales si un joueur est proche de la victoire
     * 
     * @return true si proche de la victoire sinon false
     */
    public boolean diagonal() {
        int calculVictory = 0;
        int position = -1;
        int hauteur = -1;
        if (this.typePlateau == 1) { // puissance 4
            int hauteurList = this.puissance[this.derniereColonne].size() - 1;
            calculVictory = this.puissance[this.derniereColonne].get(hauteurList);
            int compteur = 3;
            int j = hauteurList + 1;
            for (int i = this.derniereColonne + 1; i < this.puissance.length; i++) {
                if (this.puissance[i].size() > j && j < 6) {
                    if (this.puissance[i].get(j) == this.puissance[this.derniereColonne].get(hauteurList)
                            && position == -1) {
                        calculVictory += this.puissance[i].get(j); // verifie les cases en haut a droite de la
                                                                   // precedente
                        compteur--;
                    } else {
                        break; // si pion different arret de la boucle
                    }
                } else {
                    if (position != -1) {
                        break; // si case vide deja enregistre
                    }
                    position = i; // enregistre les coordonnées de la case vide
                    hauteur = j;
                }
                j++;
            }
            j = hauteurList - 1;
            for (int i = this.derniereColonne - 1; i >= this.derniereColonne - compteur; i--) {
                if (i >= 0 && j >= 0) { // si dans les limites du puissance 4
                    if (this.puissance[i].size() > j) {
                        if (this.puissance[i].get(j) == this.puissance[this.derniereColonne].get(hauteurList)) {
                            calculVictory += this.puissance[i].get(j); // verifie les cases en bas a gauche de la
                                                                       // precedente
                        } else {
                            break; // si pion different arret de la boucle
                        }
                    } else {
                        position = i; // enregistre les coordonnées de la case vide
                        hauteur = j;
                    }
                } else {
                    break; // si hors tableau arret de la boucle
                }
                j--;
            }
            if ((calculVictory == 3 || calculVictory == 30) && position != -1) { // si proche victoire et case vide
                                                                                 // trouve
                if (this.puissance[position].size() == hauteur) {
                    this.coupAJouer = position; // coup a jouer
                    return true;
                } else if (this.puissance[position].size() == hauteur - 1) {
                    this.coupInterdit = position; // coup a ne pas jouer
                    return false;
                }
            }
            calculVictory = this.puissance[this.derniereColonne].get(hauteurList);
            compteur = 3;
            position = -1;
            j = hauteurList - 1;
            for (int i = this.derniereColonne + 1; i < this.puissance.length; i++) {
                if (this.puissance[i].size() > j && j > hauteurList - 4 && j >= 0) {
                    if (this.puissance[i].get(j) == this.puissance[this.derniereColonne].get(hauteurList)
                            && position == -1) {
                        calculVictory += this.puissance[i].get(j); // verifie les cases en bas a droite de la precedente
                        compteur--;
                    } else {
                        break; // si pion different arret de la boucle
                    }
                } else {
                    if (position != -1) {
                        break; // si case vide deja enregistre
                    }
                    position = i; // enregistre les coordonnées de la case vide
                    hauteur = j;
                }
                j--;
            }
            j = hauteurList + 1;
            for (int i = this.derniereColonne - 1; i >= this.derniereColonne - compteur; i--) {
                if (i >= 0 && j < 6) { // si dans les limites du plateau
                    if (this.puissance[i].size() > j) {
                        if (this.puissance[i].get(j) == this.puissance[this.derniereColonne].get(hauteurList)) {
                            calculVictory += this.puissance[i].get(j); // verifie les cases en haut a gauche de la
                                                                       // precedente
                        } else {
                            break; // si pion different arret de la boucle
                        }
                    } else {
                        position = i; // enregistre les coordonnées de la case vide
                        hauteur = j;
                    }
                    j++;
                } else {
                    break; // si hors tableau arret de la boucle
                }
            }
            if ((calculVictory == 3 || calculVictory == 30) && position != -1) { // si proche victoire et case vide
                                                                                 // trouve
                if (this.puissance[position].size() == hauteur) {
                    this.coupAJouer = position; // coup a jouer
                    return true;
                } else if (this.puissance[position].size() == hauteur - 1) {
                    this.coupInterdit = position; // coup a ne pas jouer
                    return false;
                }
            }
            return false;
        } else if (this.typePlateau == 2) { // morpion
            for (int i = 0; i < 9; i += 4) { // diagonale gauche/haut bas/droite
                calculVictory += this.morpion[i];
                if (calculVictory == pionIA * 2) { // si ia proche victoire
                    for (int j = 0; j < 9; j += 4) {
                        if (this.morpion[j] == 0) {
                            this.coupAJouer = j; // coup a jouer
                            return true;
                        }
                    }
                } else if (calculVictory == 2 || calculVictory == 20) { // si joueur proche victoire
                    for (int j = 0; j < 9; j += 4) {
                        if (this.morpion[j] == 0) {
                            this.coupAJouer = j; // coup a jouer
                            return true;
                        }
                    }
                }
            }
            for (int i = 2; i < 7; i += 2) { // diagonale droite/haut bas/gauche
                calculVictory += this.morpion[i];
                if (calculVictory == pionIA * 2) { // si ia proche victoire
                    for (int j = 2; j < 7; j += 2) {
                        if (this.morpion[j] == 0) {
                            this.coupAJouer = j; // coup a jouer
                            return true;
                        }
                    }
                } else if (calculVictory == 2 || calculVictory == 20) { // si joueur proche victoire
                    for (int j = 2; j < 7; j += 2) {
                        if (this.morpion[j] == 0) {
                            this.coupAJouer = j; // coup a jouer
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * enregistre les coups joues au morpion
     * 
     * @param coup un entier le coup a enregistrer
     * @return true si le coup est enregistre sinon false si deja enregistre
     */
    public boolean enregistrementCoup(int coup) {
        for (int i = 0; i < 9; i++) {
            if (this.coupMorpion[i] == coup) { // compare le tableau avec le coup joue
                return false;
            }
            if (this.coupMorpion[i] == 10) { // parcourt le tableau jusqu'a trouver 10 pour le remplacer
                this.coupMorpion[i] = coup;
                return true;
            }
        }
        return false;
    }

    /**
     * random avec des pourcentages de chances pour le puissance 4 et verifie si le
     * coup est possible
     * 
     * @return retourne le coup a jouer
     * @throws InterruptedException
     */
    public int randomP4() throws InterruptedException {
        int retour = 10;
        int randomP4;
        int tentative = 7;
        while (retour == 10) {
            randomP4 = (int) (Math.random() * (98)) + 1;
            if (randomP4 < 12) { // 11% de renvoye 1
                retour = 1;
            } else if (randomP4 < 26) { // 14% de renvoye 2
                retour = 2;
            } else if (randomP4 < 42) { // 16% de renvoye 3
                retour = 3;
            } else if (randomP4 < 60) { // 18% de renvoye 4
                retour = 4;
            } else if (randomP4 < 76) { // 16% de renvoye 5
                retour = 5;
            } else if (randomP4 < 90) { // 14% de renvoye 6
                retour = 6;
            } else { // 11% de renvoye 7
                retour = 7;
            }
            if (tentative > 0 && retour - 1 == this.coupInterdit) { // rend possible au bout de 7 essais de jouer le
                                                                    // coup interdit
                retour = 10;
            } else {
                if (this.puissance[retour - 1].size() > 5) { // verifie si la colonne n'est pas deja pleine
                    retour = 10;
                }
            }
            tentative--;
        }
        return retour;
    }
}