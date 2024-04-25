# PIC

## TODO GUI

Datei löschen nach einsesen von anderer Datei




 DecodeDraft m = new DecodeDraft();

  
Werte von Backend in tabellen 
String indirect= Integer.toHexString( m.getRam(0, 0)); //param 1 Bank 0/1, param 2 Bezeichnungs

Step in aktueller befehl visualisieren 

### Fragen
Wie Soll die Range für Slider sein?<br/><br/>
Hardwareansteuerung?


Jedes 4 Mega Hz = 1 mmico secunde <br/>
Bei 1 MHz benötigt Befehl 4 psek<br/>
Laufzeit bei jedem befehl um 1 erhöhen ausser bei call und goto<br/>
[NIDI SUPPORT: nein, ist abhängik von ANzahl der Ausgeführten Befehle UND Quarzfrequenz (bei 4MHz ist Laufzeit = 1ms Pro Befehlszyklus ) ab hier Dreisatz (je höcher die Frequenz desto kleiner die Laufzeit)] <br/>

### FRONTEND
### Setup GUI on Eclipse: https://www.youtube.com/watch?v=lctZ-NAWgDU

GPR WIE 1.2 Die Registerstruktur des PICs zUSÄTZLICH NOCH SPALTE bEZEICHNUNGEN<br/>
lINKS NEBEN DRAN W; PC; PCL ETC KONSTANT ´ + ANDEREN FLAGS UND NUR FLAGS IN EINER SECTION: ZAHLEN WERDEN VOM AKTUELLEN ZUSTAND ENTNOMMEN <br/>
WAS IST PICO SEKUNDEN<br/>
HERZ NORMAL QUARZFREQUENZ<br/>
Stack: Zeile des AKtuellen Programmcodes, und bis zu 7/8 return adressen (Achtung ProgrammCounter ist die adresse des Nächsten Codes)<br/>
SFR: GRUNDLAGEN ZUR PIC PROGRAMMIERUNG UND EINFACHE CODESEQUENZ TABELLE AUF SEITE 5<br/>
IO PINS WIE KLEINE TABELLE BEI PROGRAMM ABER UM 90 GRAD DREHEN<br/>
CONSOLE; SIMULATOR STATE ?<br/>

<I>PROGRAMMZÄHLER : ZEIGT AUF AKTUELLES ODER NÄCHSTES BEFEHL? WIE IMPLEMENTIEREN WENN NÄCHSTES?</I><br/>
[NIDI SUPPORT: einfach PC++ wird auch im PIC so realisiert (wenn es ein Springbefehl ist dann wird im ersten zyklus der ProgrammCounter geändert und im zweiten erst dahingesprungen) [MACHT DAS NICHT SO SONDERN AUF DIE EINFACHE ART UND WEISE] aber deshalb dauern <br/>Sprungbefehle auch im gegensatz zu normalen befehlen immer 2 zyklen] 
<I>BANKEN IMPLEMENTIERN: NUR 2 ODER ALLE BANKEN</I><br/>
[NIDI SUPPORT:Nur 2]<br/>
## Backend
### Befehle 
- MOVLW, ADDLW, SUBLW ( einfachen Literalbefehle)
   </br>
   </br>
  <b>MOVLW</b>
  <pRE>
    BEWEGT LITERALWERT(Z.B: 0b1111_1111) IN DEN W-REGISTER
    PSEUDOCODE:
    W-REGISTER = 0;
    MOVLW K(0b1111_1111); <I> </I>
    FETCH
   -> PROGRAMMZÄHLER ZEIGT AUF BEFEHL
    DECODE
   -> BEFEHLSCODE MIT MASKE FILTERN
    LITRALCODE MIT MASKE FILTERN
    EXECUTE
    -> W-REGISTER=LITERALCODE
  </pRE>
  </br>
  <b>ADDLW</b>
<pRE>
    ADDIERT LITERALWERT(Z.B: 0b1111_1111) IN DEN W-REGISTER
    PSEUDOCODE:
    W-REGISTER = 0;
    ADDLW K(0b1111_1111); <I> 0 ≤ k ≤ 255</I>
    FETCH
   -> PROGRAMMZÄHLER ZEIGT AUF BEFEHL
    DECODE
   -> BEFEHLSCODE MIT MASKE FILTERN
    LITRALCODE MIT MASKE FILTERN
    EXECUTE
  ERGEBNIS=W-REGISTER+LITERALCODE
  -> IST CARRYBIT;DIGITCARRYBIT;ZEROFLAG BEEINFLUSST? WENN JA DANN BIT SETZEN WENN NICHT DANN 0 SETZEN  <I> WAS WENN ADDLW UND DANN NACH MOVLW </I>
     W-REGISTER=ERGEBNIS
  </pRE>
 </br>
  </br>
  <b>SUBLW</b>
 <pRE>
    SUBTRAHIEREN LITERALWERT(Z.B: 0b1111_1111) IN DEN W-REGISTER
    PSEUDOCODE:
    W-REGISTER = 0;
    SUBLW K(0b1111_1111); <I>0 ≤ k ≤ 255 </I>
    FETCH
   -> PROGRAMMZÄHLER ZEIGT AUF BEFEHL
    DECODE
   -> BEFEHLSCODE MIT MASKE FILTERN
    LITRALCODE MIT MASKE FILTERN
    EXECUTE
  ERGEBNIS=W-REGISTER-LITERALCODE
  -> IST CARRYBIT;DIGITCARRYBIT;ZEROFLAG BEEINFLUSST? WENN JA DANN BIT SETZEN WENN NICHT DANN 0 SETZEN  
     W-REGISTER=ERGEBNIS
  </pRE>
  </br>
#### vereinfacht, ohne Rücksicht auf PCLATH
- CALL, GOTO
   </br>
   </br>
  <b>CALL</b> <I>FRAGE: LITERAL=PROGRAMMZÄHLER; WIE AUFRUF DURCH LABEL(VARIABLE?)</I>
 </pRE>
 </br>
 <pRE>
   SPRINGT AUF ANDERE BEFEHL IM PROGRAMM UND DER PROGRAMZÄHLER WIRD IM *STACK* GESPEICHERT UND *RETURN* RUFT PROGRAMMZÄHLER IM STACK AUF<I>SPÄTER WEITER</I>
   <BR>
   PSEUDOCODE:
   CALL K(0b1111_1111) <I>0 ≤ k ≤ 2047</I>
  FETCH
   -> PROGRAMMZÄHLER ZEIGT AUF BEFEHL
    DECODE
   -> BEFEHLSCODE MIT MASKE FILTERN
    LITRALCODE MIT MASKE FILTERN
    EXECUTE
   ->RAM[GESPRUNGENER PC] WIRD AUSGEFÜHRT
   
    PSEUDOCODE:
    W-REGISTER = 0;
    SUBLW K(0b1111_1111); <I>0 ≤ k ≤ 255 </I>
    FETCH
   -> PROGRAMMZÄHLER ZEIGT AUF BEFEHL
    DECODE
   -> BEFEHLSCODE MIT MASKE FILTERN
    LITRALCODE MIT MASKE FILTERN
    EXECUTE
   CALL 
  ERGEBNIS=W-REGISTER-LITERALCODE
  -> IST CARRYBIT;DIGITCARRYBIT;ZEROFLAG BEEINFLUSST? WENN JA DANN BIT SETZEN WENN NICHT DANN 0 SETZEN  
     W-REGISTER=ERGEBNIS
  </pRE>
  </br>
   </br>
   </br>
  <b>GOTO</b>
  <p>
    BEWEGT LITERALWERT(Z.B: 0b1111_1111) IN DEN W-REGISTER
    PSEUDOCODE:
    GOTO
    
  </p>
  </br>
  #### nur direkte Adressierung, aber mit d-Bit Auswert.
-  MOVWF, MOVF, SUBWF
   </br>
   </br>
  <b>MOVWF</b>
  <p>
    BEWEGT LITERALWERT(Z.B: 0b1111_1111) IN DEN W-REGISTER
    PSEUDOCODE:
    MOVWF
    
  </p>
  </br>
   </br>
   </br>
  <b>MOVF</b>
  <p>
    BEWEGT LITERALWERT(Z.B: 0b1111_1111) IN DEN W-REGISTER
    PSEUDOCODE:
    MOVF
    
  </p>
  </br>
   </br>
   </br>
  <b>SUBWF</b>
  <p>
    BEWEGT LITERALWERT(Z.B: 0b1111_1111) IN DEN W-REGISTER
    PSEUDOCODE:
    SUBWF
    
  </p>
  </br>
  
  #### nur direkte Adressierung, aber mit d-Bit Auswert
   </br>
   </br>
  <b>DCFSZ</b>
  <p>
    BEWEGT LITERALWERT(Z.B: 0b1111_1111) IN DEN W-REGISTER
    PSEUDOCODE:
    DCFSZ
    
  </p>
  </br>
   </br>
   </br>
  <b>MOVLW</b>
  <p>
    BEWEGT LITERALWERT(Z.B: 0b1111_1111) IN DEN W-REGISTER
    PSEUDOCODE:
    W-REGISTER = 0;
    MOVLW K(0b1111_1111); <I></I>
    
  </p>
  </br>
-  DCFSZ, INCFSZ, RLF, RRF

  #### direkt und indirekt Adressierung
  -(BYTEBEFEHLE)
-  BSF, BCF, BTFSC, BTFSS
-  (Bytebefehle)
