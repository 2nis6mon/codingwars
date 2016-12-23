# LE SYSTEME DE NOS CARGOS

## Le Pilote Automatique

L'objectif principal de votre mission est de développer un système de pilotage automatique qui va guider le cargo allié d'un 
point de la galaxie vers un autre.
Malheureusement notre cargo auto-piloté n'est pas capable de naviguer entre les champs de météorites. Il est essentiel pour 
la survie du cargo que le pilote automatique esquive ces champs.

Un cargo se déplace de secteur en secteur sur deux axes, par contre il ne peut se déplacer que sur un seul axe à la fois.

A partir d'une origine et d'une destination, le pilote automatique calcule le chemin grâce à un sonar qui va alerter de la 
présence de météorites pour les esquiver.
 
Le sonar nous permet aussi lors de notre parcours d'identifier des vaisseaux ennemis et d'intercepter les communications ennemies.

Comme nos cargos sont parfois surveillés lors de leurs voyages, on peut décider d'activer ou pas ces fonctionnalités du sonar.
La détection des vaisseaux ennemis est activé par la fonctionnalité VAISSEAU.
L'interception de messages ennemis est activé par la fonctionnalité INTERCEPTER.


## Le Journal de Bord

Une fois le voyage achevé, le cargo émet un Journal de Bord dans lequel il a enregistré le parcours effectué et notifié différents 
types d'Alertes.

Il est important de savoir que plus court le parcours est, moins coûteux le voyage est, qu'un cargo ne peut pas traverser 
un champ de météorites, mais que comme il est non armé il peut passer sur le même secteur qu'un vaisseau de l'Empire.

Lorsque des vaisseaux de l'Empire se trouve à proximité de notre secteur notre pilote automatique doit enregistrer une Alerte de 
type VAISSEAU avec le nombre total de vaisseaux ennemis présents dans le secteur actuel et les secteurs adjacents. Lorsque le cargo 
continue de se déplacer, on continue à annoncer la présence des vaisseaux à proximité.

Quand le sonar intercepte un message ennemi il faut aussi le tracer et émettre une Alerte de type MESSAGE_INTERCEPTE avec le 
message ennemi et le secteur d'interception.

On sait que les messages ennemis sont cryptés. Si la machine peut déjà les déchiffrer cela permettra à l'alliance d'avoir un temps 
d'avance pour se déféndre.

Un de nos droïdes est arrivé a intercepter un message ennemi et le décrypter. Voici le message intercepté :

```
EPIVXI QSR GETMXEMRI
XVSYTIW VIFIPPIW HIXIGXIW E HERXSSMRI
IRZSCIV XSYX HI WYMXI PIW XVSYTIW TSYV MRWTIGXIV
```

Et voici le message décrytpé :

```
ALERTE MON CAPITAINE
TROUPES REBELLES DETECTES A DANTOOINE
ENVOYER TOUT DE SUITE LES TROUPES POUR INSPECTER
```

On sait que les messages ennemis contiennent seulement les caractères suivants : ABCDEFGHIJKLMNOPQRSTUVWXYZ

Un cargo qui ne bouge pas de secteur pour son voyage ne contient qu'un seul element dans son parcours, et il détecte les 
vaisseaux et intercepte des messages ennemis sur ce secteur.

Ah ! Et si le voyage est impossible, le Journal De Bord ne contient aucun parcours. Par contre il contient une Alerte d'émission 
de message avec le texte "Voyage impossible". Cette alerte est de type MESSAGE_EMIS et est émise depuis le secteur de départ du cargo.

Enfin, n'hésitez pas à poser des questions au client pour clarifier son besoin !

## Les gains

Comme vous le savez déjà, un voyage coûte 500 CG (crédits galactiques). En revanche les améliorations de votre système peuvent 
nous faire reduire ce coût voir gagner de l'argent.

Nos estimations sont les suivantes :

1. Un parcours optimisé nous fait gagner jusqu'à 500 CG.
2. Les alertes de vaisseau ennemi peuvent nous faire gagner jusqu'à 200 CG.
3. Intercepter et déchiffrer les messages ennemis peut nous rapporter jusqu'à 1000 CG.

Il faut savoir qu'une bonne qualité du produit évite des couts de maintenance sur le trajet et peut nous faire gagner le triple de CG.

## Quelques exemples de Journal de Bord

### Légende des cartes : 

* 'A'  : répresente le secteur de départ
* 'B'  : répresente le secteur de destination
* '-'  : répresente un secteur de l'espace VIDE
* '*'  : répresente un secteur de l'espace avec des météorites
* '@'  : répresente un secteur de l'espace avec des vaisseaux ennemies
* 'Mx' : représente un secteur de l'espace avec un message à intercepter (en étant x un numéro de 1 à 9)

### Exemple 1 :

```
         0    1    2    3    4    5    6   Positions X
     0   -    -    -    -    -    -    -
     1   -    -    -    -    -    -    -
     2   -    -    A    -    -    -    -
     3   -    -    *    *    *    -    -
     4   -    -    -    B    -    -    -
     5   -    -    -    -    -    -    -
     6   -    -    -    -    -    -    -
 Positions Y
```

Le journal de bord généré est le suivant :

```
 - Parcours [X,Y]
    2,2 > 1,2 > 1,3 > 1,4 > 2,4 > 3,4
  
 - Alertes [(X,Y) Type : Message] 
    (Aucune alerte pour ce Journal De Bord)
```

### Exemple 2 :

```
         0    1    2    3    4    5    6   Positions X
     0   -    -    -    -    -    -    -
     1   -    -    -    -    -    @    -
     2   -    -    A    -    @    -    B
     3   -    -    *    *    *    -    -
     4   -    -    -    -    -    -    -
     5   -    -    -    -    -    -    -
     6   -    -    -    -    -    -    -
 Positions Y
```

Le journal de bord généré est le suivant :

```
 - Parcours [X,Y]
    2,2 > 3,2 > 4,2 > 5,2 > 6,2
 
 - Alertes [(X,Y) Type : Message] 
     (3,2) VAISSEAU : 1 
     (4,2) VAISSEAU : 2 
     (5,2) VAISSEAU : 2 
     (6,2) VAISSEAU : 1
```

### Exemple 3 :

```
         0    1    2    3    4    5    6   Positions X
     0   -    A    -    -    -    -    -
     1   -    -    -    M1    -    B    -
     6   -    -    -    -    -    -    -
 Positions Y
 
 Et M1=EPIVXI QSR GETMXEMRI
```

Le journal de bord généré est le suivant :

```
 - Parcours [X,Y]
    1,0 > 1,1 > 2,1 > 3,1 > 4,1 > 5,1
     
 - Alertes [(X,Y) Type : Message] 
     (3,1) MESSAGE_INTERCEPTE : ALERTE MON CAPITAINE
```

