
//dient für die Schnittstelle zwischen UI und Daten/Steuerung 
//DecodeDraft wird das erben 
//bei anpassung/ änderung immer bescheid geben
public interface DataController {
	
	
	public void setup_with_LSTcode(int[] code); //{line1,line2,...}
	public int get_nxt_ProgrammCounter();// im RAM wird der pC als "adresse der Nächsten auszuführenden Code zeile" dargestellt 
	public int get_current_ProgrammCounter();// die adresse des Aktuell auszuführenden befehls (ist auch im Stack)
	public void do_cmd();// 1 Befehl wird ausgeführt, ProgrammCounter++, 
	public int getCurrentBank(); //= 0/1 //returned rb0
	public int getwRegister();
	public int[] getStack(); //Stack besteht aus: {aktueller_Programmcounter,n-te return adresse, n-1.te return adresse,...,1. return adresse} es gibt bis zu 8 return adressen (also n<=8)
	
	//---
		//entweder
	//public abstract int[][] getRam();// getRAM()[0]= Bank0; getRAM()[1]= Bank1;
	
		//oder (ist Schöner aber Schönheit wird nicht bewertet)
	public int getValOnAdress(int Bank,int adress);
	public int setValOnAdress(int Bank,int adress, int val);
	public int setBITValOnAdress(int Bank,int adress, int bitPos, int bitVal); // bitPos = 0...7; bitVal = 0/1
	//---
	
	

}
