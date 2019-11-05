import java.util.List;

public class IA{
    // Variables
    private int typePlateau;
    private List<Integer>[] puissance;
    private int[] morpion;
    private int coupAJouer;
    private int[] coupMorpion = {10, 10, 10, 10, 10, 10, 10, 10, 10};
    private int pionIA;

    /**
     * Constructeur
     */
    public IA(int typePlateau){
        this.typePlateau = typePlateau;
    }

    public int coupIA(int tour, List<Integer>[] puissance, int derniereColonne){
        this.puissance = puissance;
        if (tour < 3) {
            if (tour == 1) {
                pionIA = 10;
            } else {
                pionIA = 1;
            }
            return 4;           
        } else {
            if (this.verifPlateau() && this.puissance[derniereColonne].size() < 6){
                return this.coupAJouer + 1;
            }
            if (this.puissance[derniereColonne].size() < 6) {
                return derniereColonne + 1;
            } else {
                return this.randomP4();
            }
        }
    }

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

    public boolean verifPlateau(){
        this.coupAJouer = 0;
        return this.vertical() || this.horizon() || this.diagonal();
    }

    public boolean vertical(){
        int calculVictory;
        int position = -1;
        if (this.typePlateau == 1) {
            //a faire
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

    public boolean horizon(){
        int calculVictory;
        int position = -1;
        if (this.typePlateau == 1) {
            //a faire
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

    public boolean diagonal(){
        int calculVictory = 0;
        if (this.typePlateau == 1) {
            //a faire            
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

    public int randomP4(){
        int randomP4 = (int)(Math.random() * (100-1)) + 1;
 
        if(randomP4 < 12){ // 11% de renvoyé 1
            return 1;
        } else if(randomP4 < 26){ // 14% de renvoyé 2
            return 2;
        } else if(randomP4 < 42){ // 16% de renvoyé 3
            return 3;
        } else if(randomP4 < 60){ // 18% de renvoyé 4
            return 4;
        } else if(randomP4 < 76){ // 16% de renvoyé 5
            return 5;
        } else if(randomP4 < 90){ // 14% de renvoyé 6
            return 6;
        } else { // 11% de renvoyé 7
            return 7;
        }
    }
}