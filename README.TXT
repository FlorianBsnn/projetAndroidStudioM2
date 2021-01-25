Pour cette application, j'utilise un RaspberryPi avec une PiCamera et
2 capteurs de température one-wire (DS28B20). Sur le RaspberryPi il y a
un script Python3 qui est exécuté en continue.

La première activité est MainActivity, qui représente le menu principal.
Dans la toolbar, il y a une icône cliquable qui permet d'aller dans les
paramètres de l'application. De plus, 2 autres boutons sont disponibles,
l'un pour aller sur la caméra et l'autre pour aller sur les capteurs
de température.

L'activité OptionActivity permet de changer la langue de l'application,
l'adresse du RaspberryPi et la couleur du thème de l'application.
Il faut valider pour que les changements soient pris en compte.
Les paramètres sont stockés dans une Classe MySingletonClass, qui est
constante pour toutes les activités. Ce qui permet de facilement transmettre
les informations à travers l'application.

L'activité PicamActivity, affiche une vidéo de la PiCamera.
Il y a 2 boutons, un pour lancer et un autre pour stopper.
Le protocole Tcp est utilisé pour transférer une image à la fois.
Il n'y a pas réellement de vidéo, mais le changement dynamique d'images
donne l'impression d'en être une.
Dès que l'app se connecte au RaspberryPi il recoit une image qu'il affiche
et coupe ensuite la connexion.

L'activité TemperatureActivity, permet d'afficher la valeur de tous les
capteurs DS28B20 connectés aux RaspberryPi.
Les transferts de données sont effectués grâce au MQTT. Tous les transferts
sont sur le topic /topic/FB_Application
L'application envoie tout d'abord : {"order": "sensor","value": "init"}
pour demander les premières données, il reçoit ensuite du RaspberryPi :
{"order": "return","value": "init" , "len" : 2 , "sensor0" : "28-00XXX",
"sensor0val" : "25000", "sensor1" : "28-00XXX", "sensor1vak" : "27000"}
l'application récupère donc le nom et la valeur actuelle récupérée.
Le button Update permet de mettre à jour les données avec le même principe.
Il y a petit temps de délai entre l'envoi de la requête et l'affichage
qui est dû aux capteurs qui prennent du temps à récupérer les températures.

L'application peut être utilisée en paysage ou en portrait car des layouts
spécifiques sont créés pour ces deux cas.
