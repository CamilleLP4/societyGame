import java.io.IOException;
import java.util.Scanner;

/**
 * Projet java creer un jeu de plateau en java
 * 
 * @author Camille Paudrat LP4 24/10/19 Une classe qui gere le jeu en general.
 */
public class GestionJeu {
    // Variables
    private int tour;
    private int joueurX = 1;
    private int joueurO = 10;
    private Plateau plateau;
    private static Scanner saisie;
    private boolean iaActive = false;
    private String[] tourJeu = new String[2];
    private int typePlateau;
    private IA ia;

    /**
     * Construit l'objet gerant les fonctions du jeu
     */
    public GestionJeu() {

    }

    /**
     * Verifie si le contenu saisie par les joueurs est un entier
     * 
     * @param choix chaine de caractere saisi par les joueurs
     * @return choix transforme en entier, renvoie 0 si choix n'est pas
     *         transformable en entier
     */
    private int humanChoice(String choix) {
        try {
            return Integer.parseInt(choix); // verifie si c'est un entier
        } catch (NumberFormatException e) {
            System.out.println("Mauvais choix, Merci d'entree une valeur correcte");
            return 0;
        }
    }

    /**
     * Selectionne le jeu et defini le type de plateau a creer
     * 
     * @throws IOException
     */
    private void choixDuJeu() throws IOException {
        int choixInt = 0;
        String choix;
        System.out.println("Selection du jeu :\n1 Puissance 4\n2 Morpion\n3 Pendu\nMerci de saisir le chiffre associe");
        while (choixInt == 0) {
            choix = saisie.next();
            choixInt = this.humanChoice(choix); // test le choix saisie
            if (choixInt > 3 || choixInt < 0) {
                choixInt = 0;
                System.out.println("Mauvais choix, Merci d'entree une valeur correcte");
            }
        }
        this.typePlateau = choixInt;
        switch (choixInt) {
        case 1:
            System.out.println("Lancement Puissance 4");
            this.plateau = new Plateau(choixInt);
            break;
        case 2:
            System.out.println("Lancement Morpion");
            this.plateau = new Plateau(choixInt);
            break;
        case 3:
            System.out.println("Lancement Pendu");
            this.plateau = new Plateau(choixInt);
            break;
        }
    }

    /**
     * Initialise les joueurs et selectionne au hasard le joueur commencant
     */
    private void initPlayer() {
        String joueur1;
        String joueur2;
        System.out.print("Nom du joueur 1 : ");
        joueur1 = saisie.next();
        if (this.typePlateau < 3) {
            System.out.print("\nNom du joueur 2 (Pour affronter l'IA inscrire IA) : ");
            joueur2 = saisie.next();
            if (joueur2.equalsIgnoreCase("IA")) {
                this.iaActive = true; // active l'IA
                this.ia = new IA(this.typePlateau); // cree une instance d'ia
            }
            double ordre = Math.random();
            if (ordre < 0.5) {
                this.tourJeu[0] = joueur1;
                this.tourJeu[1] = joueur2;
            } else {
                this.tourJeu[0] = joueur2;
                this.tourJeu[1] = joueur1;
            }
        } else {
            this.tourJeu[0] = joueur1;
            this.tourJeu[1] = joueur1;
        }
        this.plateau.afficherPlateau();
    }

    /**
     * verifie les conditions de victoire
     * 
     * @return retourne true si un joueur gagne sinon retourne true quand le nombre
     *         de tour maximum est atteint
     */
    private boolean victoryOrDraw() {
        if (this.plateau.verifVictoire()) {
            if (this.typePlateau < 3) {
                System.out.println("\nVictoire de " + this.tourJeu[(this.tour - 1) % 2] + ".\n");
            } else {
                System.out.println("\nMot trouve bravo " + this.tourJeu[this.tour % 2] + ".\n");
            }
            return true;
        } else {
            if (this.plateau.verifDraw(this.tour)) {
                if (typePlateau < 3) {
                    System.out.println("\nEgalite. Fin de Partie.\n");
                } else {
                    System.out.println("\nFin de Partie.\n");
                }
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Gere la partie remplissage du plateau avec les saisies joueurs ou les choix
     * ia
     * 
     * @throws InterruptedException
     */
    private void chacunSonTour() throws InterruptedException {
        int choixInt;
        String choix = "";
        boolean valid = false;
        if (this.typePlateau < 3) {
            System.out.println("Au tour de " + this.tourJeu[this.tour % 2] + "\nChoix entre 1 et "
                    + this.plateau.getTaillePlateau());
            while (!valid) {
                choixInt = 0;
                while (choixInt == 0) {
                    if (this.iaActive == true && this.tourJeu[this.tour % 2].equalsIgnoreCase("IA")) { // si ia active
                                                                                                       // et son tour de
                                                                                                       // jeu
                        if (this.typePlateau == 1) {
                            choixInt = this.ia.coupIA(this.tour, this.plateau.getPlateauP4(),
                                    this.plateau.getColEnCours());
                        }
                        if (this.typePlateau == 2) {
                            choixInt = this.ia.coupIA(this.tour, this.plateau.getPlateauMorpion(),
                                    this.plateau.getDernierCoup());
                        }
                    } else {
                        choix = saisie.next();
                        choixInt = this.humanChoice(choix);
                        if (choixInt > this.plateau.getTaillePlateau()) {
                            choixInt = 0;
                            System.out.println("Mauvais choix, Merci d'entree une valeur correcte");
                        }
                    }
                }
                if (this.tour % 2 == 0) {
                    valid = this.plateau.tourDeJeu(choixInt - 1, joueurX, ' ');
                } else {
                    valid = this.plateau.tourDeJeu(choixInt - 1, joueurO, ' ');
                }
            }
        } else {
            while (!valid) {
                System.out.println("\nMerci de saisir une lettre.");
                boolean lettre = false;
                while (!lettre) {
                    choix = saisie.next();
                    if (Character.isLetter(choix.charAt(0))) { // verifie si le premier caractere est une lettre
                        lettre = true;
                    } else {
                        System.out.println("Mauvais choix, Merci d'entree une lettre");
                    }
                }
                valid = this.plateau.tourDeJeu(0, joueurO, choix.charAt(0));
            }
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        saisie = new Scanner(System.in); // initialise le scanner
        System.out.println("Bienvenue sur votre console de jeu");
        boolean play = true;
        while (play) {
            GestionJeu jeu = new GestionJeu();
            jeu.tour = 1;
            jeu.choixDuJeu();
            jeu.initPlayer();
            do {
                jeu.chacunSonTour();
                jeu.tour++;
            } while (!jeu.victoryOrDraw());
            System.out.println("Voulez vous rejouez ? (Oui pour rejouer)");
            String response = saisie.next();
            if (!(response.equalsIgnoreCase("Oui"))) { // si la reponse est differente de oui fin du jeu
                play = false;
            }
        }
        System.out.println("Bye Bye");
        saisie.close(); // ferme le scanner
    }
}