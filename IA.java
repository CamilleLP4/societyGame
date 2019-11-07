import java.util.List;
import java.io.IOException;

public class IA{
    // Variables
    private int typePlateau;
    private List<Integer>[] puissance;
    private int[] morpion;
    private int coupAJouer;
    private int[] coupMorpion = {10, 10, 10, 10, 10, 10, 10, 10, 10};
    private int pionIA;
    private int derniereColonne;
    private int coupInterdit;

    /**
     * Constructeur
     */
    public IA(int typePlateau){
        this.typePlateau = typePlateau;
    }

    /**
     * Methode récuperant les informations du jeu puissance 4 pour etre traité et renvoyé le coup a jouer 
     * @param tour entier tour en cours
     * @param puissance tableau de List d'entier est l'etat actuel du plateau
     * @param derniereColonne un entier etant le coup precedent
     * @return un entier le coup a jouer
     */
    public int coupIA(int tour, List<Integer>[] puissance, int derniereColonne){
        this.puissance = puissance;
        this.derniereColonne = derniereColonne;
        if (tour < 3) {
            if (tour == 1) {
                pionIA = 10;
            } else {
                pionIA = 1;
            }
            return 4;           
        } else {
            System.out.println("else 1");
            System.out.println(this.horizon());
            if (this.verifPlateau() /*&& this.puissance[this.derniereColonne].size() < 6*/){
                System.out.println("retour valid");
                return this.coupAJouer + 1;
            }
            if (this.puissance[this.derniereColonne].size() < 6) {
                //return this.derniereColonne + 1;
                System.out.println("jeu enfant");
                return this.randomP4();
            } else {
                int retour = 0;
                while (retour == 0) {
                    System.out.println("random");
                    retour = this.randomP4();
                }
                return retour;
            }
        }
    }

    /**
     * Methode récuperant les informations du jeu Morpion pour etre traité et renvoyé le coup a jouer 
     * @param tour entier tour en cours
     * @param morpion tableau d'entiers representant l'etat actuel du morpion
     * @param derniereColonne un entier etant le coup precedent
     * @return un entier le coup a jouer
     */
    public int coupIA(int tour, int[] morpion, int dernierCoup){
        this.morpion = morpion;
        boolean ok = false;
        int coup = -1;
        if (tour < 3) {
            if (this.morpion[4]== 0) {
                this.enregistrementCoup(5);
                pionIA = 10;
                return 5;
            } else {
                this.enregistrementCoup(dernierCoup + 1);
                pionIA = 1;
                while (!ok) {
                    coup = ((int)(Math.random() * this.morpion.length));
                    ok = this.enregistrementCoup(coup + 1);
                }
                return coup + 1;
            }
        } else {
            this.enregistrementCoup(dernierCoup + 1);
            if (this.verifPlateau()) {
                this.enregistrementCoup(this.coupAJouer + 1);
                return this.coupAJouer + 1;
            } else {
                while (!ok) {
                    coup = ((int)(Math.random() * this.morpion.length));
                    ok = this.enregistrementCoup(coup + 1);
                }
                return coup + 1;
            }
        }
    }

    /**
     * execute les fonctions verifiant si un joueur est proche de la victoire
     * @return true si proche de la victoire sinon false
     */
    public boolean verifPlateau(){
        this.coupAJouer = 0;
        return this.vertical() || this.horizon() || this.diagonal();
    }

    /**
     * verifie sur la vertical si un joueur est proche de la victoire
     * @return true si proche de la victoire sinon false
     */
    public boolean vertical(){
        int calculVictory;
        int position = -1;
        if (this.typePlateau == 1) {
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
        } else if (this.typePlateau == 2){
            for (int i = 0; i < 3; i++) {
                calculVictory = 0;
                for (int j = i; j <= i + 7; j += 3) {
                    calculVictory += this.morpion[j];
                }
                if (calculVictory == pionIA * 2) {
                    position = i;
                    break;
                } else if (calculVictory == 2 || calculVictory == 20) {
                    position = i;
                    break;
                }
            }
            if (position >= 0) {
                for (int i = position; i < position + 7; i += 3) {
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
     * @return true si proche de la victoire sinon false
     */
    public boolean horizon(){
        int calculVictory;
        int position = -1;
        if (this.typePlateau == 1) {
            int hauteurList = this.puissance[this.derniereColonne].size() - 1;
            calculVictory = this.puissance[this.derniereColonne].get(hauteurList);
            int compteur = 3;
            for (int i = this.derniereColonne + 1; i < this.puissance.length; i++) {
                if (this.puissance[i].size() > hauteurList && !(this.puissance[i].isEmpty())
                        && i < this.derniereColonne + 4) {
                    if (this.puissance[i].get(hauteurList) == this.puissance[this.derniereColonne].get(hauteurList)) {
                        calculVictory += this.puissance[i].get(hauteurList); // verifie les cases vers la droite
                        compteur--;
                    } else {
                        break;
                    }
                }
            }
            for (int i = this.derniereColonne - 1; i >= this.derniereColonne - compteur; i--) {
                if (i >= 0) {
                    if (this.puissance[i].size() > hauteurList && !(this.puissance[i].isEmpty())) {
                        calculVictory += this.puissance[i].get(hauteurList); // verifie les cases vers la gauche
                    }
                }
            }
            if (calculVictory == 3 || calculVictory == 30) {
                System.out.println("presque");
                System.out.println(hauteurList);
                for (int i = this.derniereColonne + 1; i < this.puissance.length; i++) {
                    System.out.println("for");
                    if (this.puissance[i].size() > hauteurList) {
                        if (this.puissance[i].get(hauteurList) != this.puissance[this.derniereColonne].get(hauteurList)) {
                            System.out.println("break");
                            break;
                        }
                    } else {
                        System.out.println(this.puissance[i].size());
                        if (this.puissance[i].size() == hauteurList) {
                            this.coupAJouer = i;
                            System.out.println("coup a jouer");
                            return true;
                        } else if (this.puissance[i].size() == hauteurList - 1){
                            System.out.println("coup interdit");
                            this.coupInterdit = i;
                            System.out.println(this.coupInterdit);
                            return false;
                        }
                    }
                }
                for (int i = this.derniereColonne - 1; i >= this.derniereColonne - compteur; i--) {
                    if (i >= 0) {
                        if (this.puissance[i].size() > hauteurList) {
                            if (this.puissance[i].get(hauteurList) != this.puissance[this.derniereColonne].get(hauteurList)) {
                                System.out.println("break retour");
                                break;
                            }
                        } else {
                            if (this.puissance[i].size() == hauteurList) {
                                this.coupAJouer = i;
                                System.out.println("coup a jouer retour");
                                return true;
                            } else if (this.puissance[i].size() == hauteurList - 1){
                                System.out.println("coup interdit retour");
                                this.coupInterdit = i;
                                System.out.println(this.coupInterdit);
                                return false;
                            }
                        }
                    }
                }
            }
            return false;
        } else if (this.typePlateau == 2){
            for (int i = 0; i < 7; i += 3) {
                calculVictory = 0;
                for (int j = i; j < i + 3; j++) {
                    calculVictory += this.morpion[j];
                }
                if (calculVictory == pionIA * 2) {
                    position = i;
                    break;
                } else if (calculVictory == 2 || calculVictory == 20) {
                    position = i;
                    break;
                }
            }
            if (position >= 0) {
                for (int i = position; i < position + 3; i++) {
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
     * verifie sur la diagonal si un joueur est proche de la victoire
     * @return true si proche de la victoire sinon false
     */
    public boolean diagonal(){
        int calculVictory = 0;
        if (this.typePlateau == 1) {
            int hauteurList = this.puissance[this.derniereColonne].size() - 1;
            calculVictory = this.puissance[this.derniereColonne].get(hauteurList);
            int compteur = 3;
            int j = hauteurList + 1;
            for (int i = this.derniereColonne + 1; i < this.puissance.length; i++) {
                if (this.puissance[i].size() > j && !(this.puissance[i].isEmpty()) && j < hauteurList + 4) {
                    if (this.puissance[i].get(j) == this.puissance[this.derniereColonne].get(hauteurList)) {
                        calculVictory += this.puissance[i].get(j); // verifie les case en haut a droite de la precedente
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
            for (int i = this.derniereColonne - 1; i >= this.derniereColonne - compteur; i--) {
                if (i >= 0 && j >= 0) {
                    if (this.puissance[i].size() > j && !(this.puissance[i].isEmpty())) {
                        calculVictory += this.puissance[i].get(j); // verifie les case en bas a gauche de la precedente
                        j--;
                    }
                } else {
                    break;
                }
            }
            //ici
            if (calculVictory == 3 || calculVictory == 30) {
                j = hauteurList + 1;
                for (int i = this.derniereColonne + 1; i < this.puissance.length; i++) {
                    if (this.puissance[i].size() > j && j < hauteurList + 4) {
                        if (this.puissance[i].get(j) == this.puissance[this.derniereColonne].get(hauteurList)) {
                            calculVictory += this.puissance[i].get(j); // verifie les case en haut a droite de la precedente
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
                for (int i = this.derniereColonne - 1; i >= this.derniereColonne - compteur; i--) {
                    if (i >= 0 && j >= 0) {
                        if (this.puissance[i].size() > j && !(this.puissance[i].isEmpty())) {
                            calculVictory += this.puissance[i].get(j); // verifie les case en bas a gauche de la precedente
                            j--;
                        }
                    } else {
                        break;
                    }
                }
            }
            //la
            calculVictory = this.puissance[this.derniereColonne].get(hauteurList);
            compteur = 3;
            j = hauteurList - 1;
            for (int i = this.derniereColonne + 1; i < this.puissance.length; i++) {
                if (this.puissance[i].size() > j && !(this.puissance[i].isEmpty()) && j > hauteurList - 4 && j >= 0) {
                    if (this.puissance[i].get(j) == this.puissance[this.derniereColonne].get(hauteurList)) {
                        calculVictory += this.puissance[i].get(j); // verifie les case en bas a droite de la precedente
                        compteur--;
                        j--;
                    }
                } else {
                    break;
                }
            }
            j = hauteurList + 1;
            for (int i = derniereColonne - 1; i >= derniereColonne - compteur; i--) {
                if (i >= 0 && j < 6) {
                    if (this.puissance[i].size() > j && !(this.puissance[i].isEmpty())) {
                        calculVictory += this.puissance[i].get(j); // verifie les case en haut a gauche de la precedente
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
        } else if (this.typePlateau == 2){
            for (int i = 0; i < 9; i += 4) {
                calculVictory += this.morpion[i];
                if (calculVictory == pionIA * 2) {
                    for (int j = 0; j < 9; j += 4) {
                        if (this.morpion[j] == 0) {
                            this.coupAJouer = j;
                            return true;
                        }
                    }
                } else if (calculVictory == 2 || calculVictory == 20) {
                    for (int j = 0; j < 9; j += 4) {
                        if (this.morpion[j] == 0) {
                            this.coupAJouer = j;
                            return true;
                        }
                    }
                }
            }
            for (int i = 2; i < 7; i += 2) {
                calculVictory += this.morpion[i];
                if (calculVictory == pionIA * 2) {
                    for (int j = 2; j < 7; j += 2) {
                        if (this.morpion[j] == 0) {
                            this.coupAJouer = j;
                            return true;
                        }
                    }
                } else if (calculVictory == 2 || calculVictory == 20) {
                    for (int j = 2; j < 7; j += 2) {
                        if (this.morpion[j] == 0) {
                            this.coupAJouer = j;
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * enregistre les coups joués au morpion
     * @param coup un entier le coup a enregistrer
     * @return true si le coup n'est pas deja et enregistré sinon false
     */
    public boolean enregistrementCoup(int coup){
        for (int i = 0; i < 9; i++) {
            if (this.coupMorpion[i] == coup){
                return false;
            }
            if (this.coupMorpion[i] == 10) {
                this.coupMorpion[i] = coup;
                return true;
            }
        }
        return false;
    }

    /**
     * random avec des pourcentages de chances pour le puissance 4
     * @return
     */
    public int randomP4(){
        int randomP4 = (int)(Math.random() * (100-1)) + 1;
        int retour;
        if(randomP4 < 12){ // 11% de renvoyé 1
            retour = 1;
        } else if(randomP4 < 26){ // 14% de renvoyé 2
            retour = 2;
        } else if(randomP4 < 42){ // 16% de renvoyé 3
            retour = 3;
        } else if(randomP4 < 60){ // 18% de renvoyé 4
            retour = 4;
        } else if(randomP4 < 76){ // 16% de renvoyé 5
            retour = 5;
        } else if(randomP4 < 90){ // 14% de renvoyé 6
            retour = 6;
        } else { // 11% de renvoyé 7
            retour = 7;
        }
        if (retour == this.coupInterdit){
            return 0;
        } else {
            return retour;
        }
    }

    public static void main(String[] args) throws IOException {
        Plateau test = new Plateau(1);
        IA ordi = new IA(1);
        test.tourDeJeu(5, 10, 'a');
        test.tourDeJeu(3, 1, 'a');
        test.tourDeJeu(6, 10, 'a');
        test.tourDeJeu(4, 10, 'a');
        test.tourDeJeu(5, 1, 'a');
        test.tourDeJeu(3, 10, 'a');
        test.tourDeJeu(6, 1, 'a');
        test.tourDeJeu(4, 1, 'a');
        test.tourDeJeu(5, 10, 'a');
        test.tourDeJeu(3, 1, 'a');
        test.tourDeJeu(6, 10, 'a');
        test.tourDeJeu(4, 10, 'a');
        test.tourDeJeu(5, 10, 'a');
        test.tourDeJeu(3, 1, 'a');
        //test.tourDeJeu(6, 10, 'a');
        test.tourDeJeu(2, 10, 'a');
        test.tourDeJeu(2, 10, 'a');
        test.tourDeJeu(2, 1, 'a');
        test.tourDeJeu(2, 1, 'a');
        test.tourDeJeu(1, 10, 'a');
        test.tourDeJeu(1, 1, 'a');
        test.tourDeJeu(4, 1, 'a');
        System.out.println(ordi.coupIA(3, test.getPlateauP4(), test.getColEnCours()));
        System.out.println(test.getColEnCours());
    }
}