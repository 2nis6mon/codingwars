# LE SYSTEME DE NOS CARGOS

## Les nouveautées du Pilote Automatique

On a des super nouvelles ! Notre équipe d'armement a installé dans nos cargos un système de bouclier qui permet de traverser
les champs de météorites !
Par contre le bouclier ne permet que de traverser deux champs de météorites. Le bouclier est activé avec la fonctionnalité
BOUCLIER.
Ce gain de temps sur les voyages va nous permettre de gagner jusqu'à 1000 CG.

Notre cargo contient un nouveau système d'émission automatique de messages pour nous alerter plus rapidement.

Ces alertes seront émises pour chaque Alerte créée dans le journal de bord si la fonctionnalité EMETTRE est activée.
La vente de ces informations en temps réel à nos alliés pourra maintenant nous rapporter très gros, jusqu'à 2500 CG !

Par exemple, si le Journal de Bord contient une Alerte de type VAISSEAU dans le secteur X,Y avec un message N
on trouvera une autre Alerte de type MESSAGE_EMIS dans le même secteur X,Y avec le message N crypté.

Pour crypter les messages on va utiliser un algorithme de traduction par clé de tableau.

Chaque lettre est transcodée par sa position dans le tableau clé.

Nous utiliserons le carré suivant comme clé pour décrypter vos messages :

```
    0 1 2 3 4 5 6

0   A B C D E F G
1   H I J K L M N
2   O P Q R S T U
3   V W X Y Z 0 1
4   2 3 4 5 6 7 8
5   9
```

Exemple :

Texte d'origine : "I SAY HELLO 1"

Texte crypté : "11 420033 0140414102 63"

