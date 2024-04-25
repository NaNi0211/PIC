
import java.util.ArrayList;

public class DecodeDraft  {

    /*
     * name : binaryCode? ganze ram implementieren? NOP_befehl? quarzfreuez zur
     * einstellung der laufzeu flags nur bei arimetischen PCLuPCLATH
     */
  

    public int getStackpointer() {
        return stackpointer;
    }

    public int getRb0() {
        return rb0;
    }
    static int wRegister = 0;
    static int[][] ram = new int[2][12];
    static int[] EEROM;
    static int pC_Next;
    static int pC_Current;
    static int[] stack = new int[8];
    static int stackpointer;
    static int carrybit;
    static int digitcarrybit;
    static int zerobit;
    static int rb0;
    static boolean resetValue;
    static ArrayList<Integer> execute;
   

    public int getwRegister() {
        return wRegister;
    }

    public int[][] getRam() {
        return ram;
    }

    public int[] getEEROM() {
        return EEROM;
    }

    public int getpC() {
        return pC_Next;
    }

    public int getpCL() {
        return pC_Current;
    }

    public int[] getStack() {
        return stack;
    }

    public int getCarrybit() {
        return carrybit;
    }

    public int getDigitcarrybit() {
        return digitcarrybit;
    }

    public int getZerobit() {
        return zerobit;
    }

//ADDLW,ANDLW, IORLW,SUBLW,MOVLW, XORLW
    public static void executeliteralCode(int instructionCode) {
        int opCode = instructionCode & 0b0011_1111_0000_0000;
        int literalCode = instructionCode & 0b0000_0000_1111_1111;

        rb0 = (ram[rb0][3] >> 5) & 1;
        if (pC_Current != pC_Next) {
            pC_Current = pC_Next;
        }

        switch (opCode) {
        case 0b0011_1110_0000_0000:
            wRegister = Instructions.addlw(wRegister, literalCode);
            pC_Next++;
            break;
        case 0b0011_1001_0000_0000:
            wRegister = Instructions.andlw(wRegister, literalCode);
            pC_Next++;
            break;
        case 0b0011_1000_0000_0000:
            wRegister = Instructions.iorlw(wRegister, literalCode);
            pC_Next++;
            break;
        case 0b0011_1100_0000_0000:
            wRegister = Instructions.sublw(wRegister, literalCode);
            pC_Next++;
            break;
        case 0b0011_0000_0000_0000:
            wRegister = Instructions.movlw(literalCode);
            pC_Next++;
            break;
        case 0b0011_1010_0000_0000:
            wRegister = Instructions.xorlw(wRegister, literalCode);
            pC_Next++;
            break;
        case 0b0011_0100_0000_0000:
            wRegister = Instructions.retlw(literalCode);
            jump(0b0000_0000_0000_1000);
            break;
        default:
            jump(instructionCode);
        }
    }

//Goto,call
    public static void jump(int instructionCode) {

        int opCode = instructionCode & 0b0011_1000_0000_0000;
        int literalCode = instructionCode & 0b0000_0111_1111_1111;
        switch (opCode) {
        case 0b0010_1000_0000_0000: // GOTO
            pC_Next = Instructions.goto0(literalCode);
            break;
        case 0b0010_0000_0000_0000: // CALL
            pC_Next = Instructions.call(literalCode);
            break;

        default:
            onlyOp(instructionCode);

        }

    }

    public static void onlyOp(int instructionCode) {
        switch (instructionCode) {
        case 0b0000_0000_0000_0000: // NOP
            Instructions.nop();
            pC_Next++;
            break;
        case 0b0000_0001_0000_0000: // CLRW
            wRegister = Instructions.clrw();
            pC_Next++;
            break;
        case 0b0000_0000_0000_1000: // RETURN
            Instructions.return0();
            break;

        }
    }

    public static void byteorientated(int instructionCode) {
        int opCode = instructionCode & 0b1111_1111_0000_0000;
        int destBit = instructionCode & 0b0000_0000_1000_0000;
        int fileCode = instructionCode & 0b0000_0000_0111_1111;
        switch (opCode) {
        case 0b0000_1000_0000_0000: // MOVF
            Instructions.movf(destBit,fileCode);
        default:

        }
    }

    public static void digitcarry(int result, int halfbyte) {
        if ((halfbyte & 1111_0000) != 0) {

            digitcarrybit = 1;
            ram[rb0][3] = (ram[rb0][3]) | (digitcarrybit << 1);

        } else {
            digitcarrybit = 0;
            ram[rb0][3] = (ram[rb0][3]) & (digitcarrybit << 1);

        }
    }

    public static void carry(int result, int register) {

        if (((register <= 0b1111_1111) && (result > 0b1111_1111))
                || ((register >= 0b0000_0000) && (result < 0b0000_0000))) {
            carrybit = 1;
            ram[rb0][3] = (ram[rb0][3]) | (carrybit);

        } else {
            carrybit = 0;
            ram[rb0][3] = (ram[rb0][3]) & (carrybit);

        }

    }

    public static void zero(int result) {
        if (result == 0) {
            zerobit = 1;
            ram[rb0][3] = (ram[rb0][3]) | (zerobit << 2);

        } else {
            zerobit = 0;
            ram[rb0][3] = (ram[rb0][3]) & (zerobit << 2);

        }

    }

    
    public static int get_nxt_ProgrammCounter() {
        // TODO Auto-generated method stub
        return pC_Next;
    }

  
    public static int get_current_ProgrammCounter() {
        // TODO Auto-generated method stub
        return pC_Current;
    }

   
    public static int getCurrentBank() {
        // TODO Auto-generated method stub
        int rb0 = (ram[0][3] >> 5) & 0000_0001;

        return rb0;

    }

    public static int getValOnAdress(int bank, int adress) {
        // TODO Auto-generated method stub
        return ram[bank][adress];

    }

    public static void setValOnAdress(int bank, int adress, int val) {
        // TODO Auto-generated method stub
        ram[bank][adress] = val;

    }

    public static void setBITValOnAdress(int bank, int adress, int bitPos, int bitVal) {
        // TODO Auto-generated method stub
        int maske = bitVal << bitPos;
        if (bitVal == 0) {
            ram[bank][adress] = ram[bank][adress] & maske;
        } else {

            ram[bank][adress] = ram[bank][adress] | maske;
        }

    }

  
    public static int getBITValOnAdress(int bank, int adress, int bitPos) {
        // TODO Auto-generated method stub
        int bitShift = ram[bank][adress] >> bitPos;
        int maske = 0b0000_0001;
        return bitShift & maske;
    }


    public static void setup_with_LSTcode(ArrayList<Integer> list) {
        // TODO Auto-generated method stub
        execute = list;

    }


    public static void runCompleteCode() {
        // TODO Auto-generated method stub
       
while(resetValue) {
    executeliteralCode(execute.get(pC_Next));
}
    }


    public static void do_cmd() {
        // TODO Auto-generated method stub
        executeliteralCode(execute.get(pC_Next));
    }

}

  
