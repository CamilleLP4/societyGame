# Plateforme de jeux

Auteur : Camille

Date de début : 24/10/19

Contexte : Projet Java 2 Faire un plateau de jeu

Développé en Java

## Description générale

### La Plateforme permet de jouer à différents jeux.

![](https://zupimages.net/up/19/46/ogto.png)

Les jeux se déroulent dans la console.

### 1 Puissance 4

![](https://zupimages.net/up/19/46/ftj6.png)

Le Puissance 4 se joue Joueur contre Joueur ou Joueur contre Ordinateur (Ordinateur contre Ordinateur aussi mais pas passionnant).

Le but du jeu est d'aligner 4 pions identiques d'affilés.
Il est possible de gagner en les alignant en verticale, horizontale et diagonale

### 2 Morpion

![](https://zupimages.net/up/19/46/e5cu.png)

Le Morpion se joue Joueur contre Joueur ou Joueur contre Ordinateur (Ordinateur contre Ordinateur).
Le but du jeu est d'aligner 3 pions identique d'affilés. Il est possible de gagner en les alignant en verticale, horizontale et diagonale

### 3 Pendu

![](https://zupimages.net/up/19/46/qfve.png)

Le Pendu se joue en solo.
Le but du jeu est de trouver le Mot.
Pour cela le joueur dispose de 12 lettres erronés.

## Description des Classes

La plateforme est constitué de 4 Classes.

### Classe GestionJeu

Classe principale de la Plateforme.

Elle gère le menu, la gestion général du jeu et l'appel aux autres classes.

Sélectionne le jeu, initialise les joueurs, gère les entrées du joueur et vérifie les conditions de victoire.

### Classe Plateau

Classe initialisant les différents plateau de jeu.

Elle gère complètement les plateaux Puissance 4 et Morpion. Crée le plateau, inscrit les coups dans le plateau, affiche le plateau et vérifie les conditions de victoire.

Elle fait appel à la Classe Pendu pour le gestion du pendu.

### Classe Pendu

Classe gérant le Pendu.

Elle gère les lettres entrées par le joueur, met à jour le mot si nécessaire, vérifie les conditions de victoire et affiche le Pendu.

### Classe IA

Classe gérant l'IA du Puissance 4 et du Morpion.

Elle analyse le plateau de jeu et détermine si un coup empêchant le joueur de gagner est à jouer sinon elle joue au hasard.

## Evolution future

Ajout d'une bataille navale.

Optimisation IA.