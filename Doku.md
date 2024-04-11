
<h1>Doku<h1> 

<h2>Einleitung</h2>

<p>Im Rahmen des Labor Rechnerarchitektur wurde die Entwicklung eines PIC16F84 Simulators mit der Programmiersprache Java  und dem Framework Java Swing  für die Grafische Oberfläche durchgeführt um ein besseres Verständnis für PIC zu gewinnen sowie die erste Schritte mit Java Swing zu machen.</p>

<p>
PIC steht für "Peripheral Interface Controller" und  beschreibt eine Familie von Mikrocontrollern. 
Das Arbeitsprinzip des Mikrocontrollers kann in Folgenden Schritten unterteilt werden:
</p>


<b style="color:red">Schritte hinzufügen</b>



<p>PIC-Mikrocontroller werden in vielen Anwendungen verwendet, die täglich benutzt werden. Darunter zählen unter anderem Haushaltsgeräte, Automobiltechnik und Spielzeug.
Ziel des Projektes war es,  das Verhalten der PIC Hardwarekomponente mithilfe der GUI nachzuahmen. Die Simulation hat zahlreiche Vorteile wie zum Beispiel  Kostenersparnis und Sicherheit aufgrund des nicht reallen Aspekts.
Man sollte aber ebenfalls bedenken dass eine Simulation nicht perfekt ist, da es zu der physischen Komponenten Abweichungen geben kann, weil sich nicht alles nachbilden lässt wie zum Beispiel das Material das mit der Zeit nach lässt.</p>


<p>Der PIC16F84 hat gegenüber den anderen Modellen der Familie den Vorteil dass er einer älteren Modelle  ist und bietet dadurch eine leichtere Komplexität. Der Befehlssatz ist im Vergleich der anderen PIC Mikrocontroller gut verständlich und bietet sich dementsprechend ideal für das Labor an.</p>










<h2>Anleitung Interaktion mit der GUI</h2>

<p>Um Den Simulator besser zu verstehen werden hier alle Interaktionsmöglichkeiten mit der GUI beschrieben, sowie die Abläufe die dahinter stecken.</p>

Nach dem erfolgreichen einlesen einer LST Datei über den File Button, wird 



Buttons:

<h3>File:</h3> 
<p>Beschribung</p>
<p>Was passiert auf der GUI: Öffnet Explorer wo man eine LST Datei auswählen muss. Bei dem Erfolgreichen einlesen der LST Datei erscheint eine Erfolgsmeldung in der Konsole und eine Fehlermeldung falls das einlesen nicht erfolgreich war</p> 
<p>Was passiert intern: Intern wird nach dem erfolgreichen einlesen die Methoden displayToTable()</p>

<h3>Run:</h3> 
<p>Beschribung: Soll Programm durchlaufen</p>
<p>Was passiert auf der GUI: Wenn der Button gedrückt wird, soll Programm bis zum letzten Befehl durchelaufen und die Abhängigne Werte sollen nach jedem Befehl verändert werden</p> 
<p>Was passiert intern: Es soll mit einer For Schleife die Länge des Arrays durchgehen und alle Befehle executen</p>


<h3>Stop:</h3> 
<p>Beschribung</p>
<p>Was passiert auf der GUI:Macht im Prinzip alles was Step macht bis alle Befehle abgearbeitet werden oder auf Stop gedrückt wird</p> 
<p>Was passiert intern:</p>


<h3>Step:</h3> 
<p>Beschribung</p>
<p>Was passiert auf der GUI:Macht im Prinzip alles was Step macht bis alle Befehle abgearbeitet werden oder auf Stop gedrückt wird</p> 
<p>Was passiert intern:</p>


<h3>Reset:</h3> 
<p>Beschribung</p>
<p>Was passiert auf der GUI:Macht im Prinzip alles was Step macht bis alle Befehle abgearbeitet werden oder auf Stop gedrückt wird</p> 
<p>Was passiert intern:</p>


<h3>Slider:</h3>
<p>Beschribung</p>
<p>Was passiert auf der GUI:Macht im Prinzip alles was Step macht bis alle Befehle abgearbeitet werden oder auf Stop gedrückt wird</p> 
<p>Was passiert intern:</p>


<h3>IO-Pins:</h3>
<p>Beschribung</p>
<p>Was passiert auf der GUI:Macht im Prinzip alles was Step macht bis alle Befehle abgearbeitet werden oder auf Stop gedrückt wird</p> 
<p>Was passiert intern:</p>


<h3>Breakpoints:</h3>
<p>Beschribung</p>
<p>Was passiert auf der GUI:Macht im Prinzip alles was Step macht bis alle Befehle abgearbeitet werden oder auf Stop gedrückt wird</p> 
<p>Nur da wo Befehle, soll nach setzten run soll bis zu diesem punkt gehen, Step in Run bis Breakpoint
 
Run Button: laufe solange bis du ein Breakpoint findest. Dann gib Ruhe
Step in: 
 
Breakpoint für button</p>
<p>Was passiert intern:</p>



<h2>Struktur des Projektes<h2>

<b>Struktur</b>





<p>Am Anfang 
Bevor mit der Implementierung angefangen wurde, war eine umfassende Planung notwending, um sicherzustellen, dass alle Anforderungen erfüllt werden und das Projekt so effizient wie möglich abläuft. 
Die Planung umfasste folgende Schritte:
Erstellen eines Wireframes für die GUI. Einfachheitshalber wurde entschieden die GUI aus dem "GUI-Themenblatt" nachzubauen um sicherzustellen das alle wichtigen Anforderungen erfüllt werden. Im Laufe des Projektes würde die GUI nochmal umgestaltet um den Punkt "Hardwareansteuerung" zu erfüllen.
Assember....</p>


Bei dem GUI Setup 

Frontend:
Hier wurde 

Für den 








<h2>Fazit</h2> 
<p>Die Implementierung des PIC16F84 Simulators war eine umfangreiche, aber lehrreiche Aufgabe um zu verstehen wie der Mikrocontroller tatsächlich funktioniert und ein tieferes Verständnis für Java zu gewinnen.</p>
<p>
 Neben diesen technischen Fähigkeiten wurde ebenfalls die Wichtigkeit des Software Engineerings bedeutsam geworden ins besondere die Strukturierung des Projektes und Zusammenarbeit im Team. Ein Beispiel Für die  wäre es gut im voraus die Struktur zu planen schon von Anfang an anstatt mitten im Projekt zu realisieren dass man die Übersicht verloren hat und es erst dann zu strukturieren. Außerdem vermeidet man ebenfalls die Fehler die bei der Umstrukturierung entstehen. 

und Zusammenarbeit im Team um möglichst effektiv das Projekt zu bearbeiten.

Aber auch die Kommunikation zum Beispiel war es wichtig zu wissen wie die andre Person etwas implementiert hat damit das Frontend und backend gut kombiniert
</p>


<h3>Was schief gelaufen ist</h3> 
<p>Trotz ausführlicher Planung am Anfang des Projektes, wäre es wichtig gewesen ebenfalls die Architektur des ganzen zu planen damit man im Laufe des Projektes nicht den Pfaden verliert. Beim nächsten Mal sollten ebenfalls die Design Patterns direkt am Anfang berücksichtigt werden.</p>

<h3>Was man am Projekkt noch verbessern kann</h3> 
<p>Wäre mehr zeit für das Projekt zur Verfügung gewesen, wären könnte man weitere Funktionalitäten einbauen wie z. B. Funktionalitäten die man hinzufügen könnte und was die machen</p>
 
