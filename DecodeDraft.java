
import java.util.ArrayList;

public class DecodeDraft{

    /*
     * name : binaryCode? ganze ram implementieren? NOP_befehl? quarzfreuez zur
     * einstellung der laufzeu flags nur bei arimetischen PCLuPCLATH
     */

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

    public static void decode(int instructionCode) {
        literalInstructions(instructionCode);

    }

    // ADDLW,ANDLW, IORLW,SUBLW,MOVLW, XORLW
    public static void literalInstructions(int instructionCode) {
        int opCode = instructionCode & 0b0011_1111_0000_0000;
        int literalCode = instructionCode & 0b0000_0000_1111_1111;

        rb0 = (ram[rb0][3] >> 5) & 1;
        if (pC_Current != pC_Next) {
            pC_Current = pC_Next;
        }

        switch (opCode) {

        case 0b0011_1110_0000_0000:
            Instructions.addlw(literalCode);
            break;

        case 0b0011_1001_0000_0000:
            Instructions.andlw(literalCode);
            break;

        case 0b0011_1000_0000_0000:
            Instructions.iorlw(literalCode);
            break;

        case 0b0011_1100_0000_0000:
            Instructions.sublw(literalCode);
            break;

        case 0b0011_0000_0000_0000:
            Instructions.movlw(literalCode);
            break;

        case 0b0011_1010_0000_0000:
            Instructions.xorlw(literalCode);
            break;

        case 0b0011_0100_0000_0000:
            Instructions.retlw(literalCode);
            jump(0b0000_0000_0000_1000);
            break;
        case 0b0000_0000_0000_1000: // RETURN
            Instructions.return0();
            break;
        default:
            jump(instructionCode);
        }
    }

    // Goto,call
    public static void jump(int instructionCode) {

        int opCode = instructionCode & 0b0011_1000_0000_0000;
        int literalCode = instructionCode & 0b0000_0111_1111_1111;

        switch (opCode) {
        case 0b0010_1000_0000_0000: // GOTO
            Instructions.goto0(literalCode);
            break;

        case 0b0010_0000_0000_0000: // CALL
            Instructions.call(literalCode);
            break;

        default:

        }

    }

    public static void byteorientated(int instructionCode) {

        int opCode = instructionCode & 0b0000_1111_0000_0000;
        int destBit = instructionCode & 0b0000_0000_1000_0000;
        int fileCode = instructionCode & 0b0000_0000_0111_1111;

        switch (opCode) {

        case 0b0000_1000_0000_0000: // MOVF
            // TODO Instructions.movf(destBit, fileCode);
            break;

        case 0b0000_0111_0000_0000:// ADDWF
            Instructions.addwf(fileCode, destBit);
            break;

        case 0b0000_0101_0000_0000: // ANDWF
            Instructions.andwf(fileCode, destBit);
            break;

        case 0b0000_1011_0000_0000: // DECFSZ
            Instructions.decfsz(fileCode);
            break;

        case 0b0000_1111_0000_0000:// INCFSZ
            Instructions.incfsz(fileCode);
            break;

        case 0b0000_0000_0000_0000: // MOVWF //NOP

            if (destBit == 0b0000_0000_1000_0000) {// MOVWF
                Instructions.movwf(fileCode);

            } else if (destBit == 0b0000_0000_0000_0000) {// NOP
                Instructions.nop();
            }
            break;

        case 0b0000_0001_0000_0000: // CLRW //CLRF

            if (destBit == 0b0000_0000_0000_0000) {// CLRW
                Instructions.clrw();

            } else if (destBit == 0b0000_0000_1000_0000) {//CLRF
                // TODO Instructions.clrf(fileCode);

            }
            break;
            
        case 0b0000_1001_0000_0000: //COMF
            //TODO Instructions.comf(fileCode,destBit);
            break;
            
        case 0b0000_0011_0000_0000: //DECF
            //TODO Instructions.decf(fileCode,destBit);
            break; 
            
        case 0b0000_1010_0000_0000: //INCF
            //TODO Instructions.incf(fileCode,destBit);
            break; 
            
        case 0b0000_0100_0000_0000: //IORWF
            //TODO Instructions.iorwf(fileCode,destBit);
            break; 
            
        case 0b0000_0010_0000_0000: //SUBWF
            //TODO Instructions.subwf(fileCode,destBit);
            break; 
            
        case 0b0000_1110_0000_0000: //SWAPF
            //TODO Instructions.swapf(fileCode,destBit);
            break;  
            
        case 0b0000_0110_0000_0000: //XORWF
            //TODO Instructions.xorwf(fileCode,destBit);
            break;   
            
        case 0b0000_1101_0000_0000: //RLF
            //TODO Instructions.rlf(fileCode,destBit);
            break;   
        case 0b0000_1100_0000_0000: //RRF
            //TODO Instructions.rrf(fileCode,destBit);
            break;    
        default:

        }
    }

    public static void bitorientated(int instructionCode) {

        int opCode = instructionCode & 0b0001_1100_0000_0000;
        int bitCode = (instructionCode & 0b0000_0011_1000_0000) >> 7;
        int fileCode = instructionCode & 0b0000_0000_0111_0000;

        switch (opCode) {

        case 0b0001_0000_0000_0000: // BCF
            Instructions.bcf(fileCode, bitCode);
            break;

        case 0b0001_0100_0000_0000:// BSF
            Instructions.bsf(fileCode, bitCode);
            break;

        case 0b0001_1000_0000_0000:// BTFSC
            Instructions.btfsc(fileCode, bitCode);
            break;

        case 0b0001_1100_0000_0000:// BTFSS
            Instructions.btfss(fileCode, bitCode);
            break;

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

        return pC_Next;
    }

    public static int get_current_ProgrammCounter() {

        return pC_Current;
    }

    public static int getCurrentBank() {

        int rb0 = (ram[0][3] >> 5) & 0000_0001;

        return rb0;

    }

    public static int getValOnAddress(int bank, int adress) {

        return ram[bank][adress];

    }

    public static void setValOnAddress(int bank, int adress, int val) {

        ram[bank][adress] = val;

    }

    public static void setBITValOnAddress(int bank, int adress, int bitPos, int bitVal) {

        int maske = bitVal << bitPos;
        if (bitVal == 0) {
            ram[bank][adress] = ram[bank][adress] & maske;
        } else {

            ram[bank][adress] = ram[bank][adress] | maske;
        }

    }

    public static int getBITValOnAddress(int bank, int adress, int bitPos) {

        int bitShift = ram[bank][adress] >> bitPos;
        int maske = 0b0000_0001;
        return bitShift & maske;
    }

    public static void setup_with_LSTcode(ArrayList<Integer> list) {

        execute = list;

    }

    public static void runCompleteCode() {

        while (resetValue) {
            literalInstructions(execute.get(pC_Next));
        }
    }

    public static void do_cmd() {

        literalInstructions(execute.get(pC_Next));
    }

}


  
