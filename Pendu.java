import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Projet java creer un jeu de plateau en java
 * Class gerant le pendu
 * 
 * @author Camille Paudrat LP4 30/10/19 Une classe qui gere le pendu.
 */
public class Pendu {
    // Variables
    private String leMotATrouver;
    private Character[][] tabDeComparaison;
    private Character[] tabDejaJouer = { ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
            ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' };
    private String[] dessinPendu = { "\n\n\n\n\n\n\n", "\n\n\n\n\n\n\n___", "\n |\n |\n |\n |\n |\n |\n_|__",
            "__________\n |\n |\n |\n |\n |\n |\n_|__", "__________\n | /\n |/\n |\n |\n |\n |\n_|__",
            "__________\n | /   |\n |/\n |\n |\n |\n |\n_|__", "__________\n | /   |\n |/    O\n |\n |\n |\n |\n_|__",
            "__________\n | /   |\n |/    O\n |     |\n |     |\n |\n |\n_|__",
            "__________\n | /   |\n |/    O\n |    /|\n |     |\n |\n |\n_|__",
            "__________\n | /   |\n |/    O\n |    /|\\\n |     |\n |\n |\n_|__",
            "__________\n | /   |\n |/    O\n |    /|\\\n |     |\n |    /\n |\n_|__",
            "__________\n | /   |\n |/    O\n |    /|\\\n |     |\n |    /`\\\n |\n_|__\tAhahah Tu est mort!!!"};
            //le dessin du pendu
    private int compteurVie = 12;

    /**
     * Constructeur du pendu
     * Recupere la liste de mot en choisi un au hasard et prepare le Pendu
     * @throws IOException necessaire pour lire mots.txt
     */
    public Pendu() throws IOException {
        String fileName = "mots.txt";
        List<String> listDeMots = Files.readAllLines(Paths.get(fileName)); //rempli la liste en lisant le fichier
        int numMot = (int)(Math.random() * listDeMots.size());
        this.leMotATrouver = listDeMots.get(numMot); //extrait le mot choisi au hasard
        this.tabDeComparaison = new Character[this.leMotATrouver.length()][this.leMotATrouver.length()];
        for (int i = 0; i < this.tabDeComparaison.length; i++) {
            this.tabDeComparaison[0][i] = this.leMotATrouver.charAt(i); //rempli le tableau avec le mot a trouver
            this.tabDeComparaison[1][i] = '_'; //rempli le tableau avec des _
        }
    }

    /**
     * Compare le caractere du joueur avec le mot a trouve et met a jour le tableau afficher au joueur
     * @param ch caractere du joueur
     * @return return true si tout se passe bien sinon false si le caractere est deja joue
     */
    public boolean compare(Character ch){
        Character comp = Character.toUpperCase(ch); //transforme le caractere en majuscule
        int echec = 1;
        for (Character verif : this.tabDejaJouer) {
            if(verif.equals(comp)){
                System.out.println("Caractere deja tente, veuillez rejouer.");
                return false;
            }
        }
        for (int i = 0; i < this.tabDeComparaison.length; i++) {
            if(this.tabDeComparaison[0][i].equals(comp)){
                this.tabDeComparaison[1][i] = this.tabDeComparaison[0][i]; //si le caractere est present mise a jour du tableau
                echec = 0;
            }
        }
        this.compteurVie -= echec; //mise a jour des tentatives restantes
        if (echec == 1) {
            for (int i = 0;i < this.tabDejaJouer.length; i++) {
                if (Character.isWhitespace(this.tabDejaJouer[i])) { 
                    this.tabDejaJouer[i] = comp; //rempli le debut du tableau des mauvaises lettres deja joue
                    return true;
                }
            }
        } else {
            for (int i = this.tabDejaJouer.length - 1; i >= 0; i--) {
                if (Character.isWhitespace(this.tabDejaJouer[i])) {
                    this.tabDejaJouer[i] = comp; //rempli la fin du tableau des bonnes lettres deja joue
                    return true;
                }
            } 
        }
        return false;
    }

    /**
     * compare le mot a trouver et le mot presente au joueur
     * @return true si les mots sont egaux sinon false pour pas egaux
     */
    public boolean verifChaine(){
        String leMot = "";
        for (Character characters : this.tabDeComparaison[1]) {
            leMot += characters;
        }
        return leMot.equals(this.leMotATrouver);
    }

    /**
     * renvoi le nombre de vie du joueur
     * @return renvoi un entier egal au nombres de vies restantes
     */
    public int getVieRestante(){
        return this.compteurVie;
    }

    /**
     * Renvoi le mot a trouver
     * @return un String contenant le mot a trouver
     */
    public String getMotATrouver(){
        return this.leMotATrouver;
    }

    /**
     * affiche le mot cote joueur suivi des lettres deja joues et le dessin du pendu
     */
    public void affichePendu(){
        System.out.println(this.dessinPendu[12 - this.compteurVie]);
        for (Character characters : this.tabDeComparaison[1]) {
            System.out.print(characters + " ");
        }
        System.out.print("\nLettre deja joue : ");
        for (Character characters : this.tabDejaJouer) {
            if (Character.isWhitespace(characters)) {
                break;
            }
            System.out.print(characters + " ");
        }
    }
}