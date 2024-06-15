
import java.util.ArrayList;
import java.util.Arrays;

public class DecodeDraft extends PICGUI {

    /*
     * name : binaryCode? ganze ram implementieren? NOP_befehl? quarzfreuez zur
     * einstellung der laufzeu flags nur bei arimetischen PCLuPCLATH
     * 
     * 
     * indirect SwitchCase
     */

    static int wRegister = 0;
    static int[][] ram = new int[2][80];
    static int[] EEROM;
    static int pC_Next;
    static int pC_Current;
    static int[] stack = new int[8];
    static int stackpointer;
    static int carrybit;
    static int digitcarrybit;
    static int zerobit;
    static int rb0;
    static int rb0Check;
    static int fsr = -1;
    static boolean resetValue;
    static double runtime;
    static int endOfProgrammCheck;
    static ArrayList<Integer> execute;
    static boolean t0Intcheck;
    static boolean t0IntJumpcheck;

    

    public static void decode(int instructionCode) {
        rb0Check = rb0;
        setInterrupt();
        literalInstructions(instructionCode);
        bitorientated(instructionCode);
        byteorientated(instructionCode);
        setRegister();
        runtime += (4.0 / quartz);
    }

    public static void setInterrupt() {
        if( ram[rb0][11]== 0b1010_0000 ) {
            if(ram[0][1] == 0b1111_1111) {
                t0Intcheck = true;
            }
            if(t0Intcheck &&  ram[rb0][1] == 0 ) {
                t0Intcheck = false;
                ram[rb0][11] &= 0b0111_1111; 
                ram[rb0][11] |= 0b0000_0100; 
                
                t0IntJumpcheck = true;
            }
        }
    }
    // ADDLW,ANDLW, IORLW,SUBLW,MOVLW, XORLW
    public static void literalInstructions(int instructionCode) {
        int opCode = instructionCode & 0b0011_1111_0000_0000;
        int literalCode = instructionCode & 0b0000_0000_1111_1111;

        rb0 = (ram[rb0][3] >> 5) & 1;

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
            runtime += (4.0 / quartz);
            break;
        case 0b0000_0000_0000_0000: // RETURN
            if (instructionCode == 0b0000_0000_0000_1001) {
                Instructions.return0();
                ram[rb0][11] |= 0b1000_0000; 
                runtime += (4.0 / quartz);
                break;
            }
            
            if (instructionCode == 0b0000_0000_0000_1000) {
                Instructions.return0();
                runtime += (4.0 / quartz);
                break;
            }
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
            runtime += (4.0 / quartz);
            if( ram[rb0][11] != 0b0010_0100 ) {
                timer0++;
              }
            
            break;

        case 0b0010_0000_0000_0000: // CALL
            Instructions.call(literalCode);
            runtime += (4.0 / quartz);
            if( ram[rb0][11] != 0b0010_0100 ) {
                timer0++;
              }
            break;

        default:

        }

    }

    public static void byteorientated(int instructionCode) {

        int opCode = instructionCode & 0b0011_1111_0000_0000;
        int destBit = instructionCode & 0b0000_0000_1000_0000;
        int fileCode = instructionCode & 0b0000_0000_0111_1111;
        if (fileCode == 0) {
            fileCode = ram[rb0][4];
        }
       
        switch (opCode) {

        case 0b0000_1000_0000_0000: // MOVF
            Instructions.movf(fileCode, destBit);

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
               if( fileCode == 1) {
                   timer0=0;
                   prescaler =2;
                   PSAone = true;;
               }
                
               
            } else if (instructionCode == 0b0000_0000_0000_0000) {// NOP
                Instructions.nop();

            }
            break;

        case 0b0000_0001_0000_0000: // CLRW //CLRF

            if (destBit == 0b0000_0000_0000_0000) {// CLRW
                Instructions.clrw();

            } else if (destBit == 0b0000_0000_1000_0000) {// CLRF
                Instructions.clrf(fileCode);

            }
            break;

        case 0b0000_1001_0000_0000: // COMF
            Instructions.comf(fileCode, destBit);

            break;

        case 0b0000_0011_0000_0000: // DECF
            Instructions.decf(fileCode, destBit);

            break;

        case 0b0000_1010_0000_0000: // INCF
            Instructions.incf(fileCode, destBit);

            break;

        case 0b0000_0100_0000_0000: // IORWF
            Instructions.iorwf(fileCode, destBit);

            break;

        case 0b0000_0010_0000_0000: // SUBWF
            Instructions.subwf(fileCode, destBit);

            break;

        case 0b0000_1110_0000_0000: // SWAPF
            Instructions.swapf(fileCode, destBit);

            break;

        case 0b0000_0110_0000_0000: // XORWF
            Instructions.xorwf(fileCode, destBit);

            break;

        case 0b0000_1101_0000_0000: // RLF
            Instructions.rlf(fileCode, destBit);

            break;
        case 0b0000_1100_0000_0000: // RRF
            Instructions.rrf(fileCode, destBit);

            break;
        default:

        }

    }

    public static void bitorientated(int instructionCode) {

        int opCode = instructionCode & 0b0011_1100_0000_0000;
        int bitCode = (instructionCode & 0b0000_0011_1000_0000) >> 7;
        int fileCode = instructionCode & 0b0000_0000_0111_1111;
        if (fileCode == 0) {
            fileCode = ram[rb0][4];
        }
        
        
        
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
    /*
     * public static void digitcarry(int result, int halfbyte) {
     * 
     * if ((halfbyte & 0b1111_0000) != 0) {
     * 
     * digitcarrybit = 1; ram[rb0][3] = (ram[rb0][3]) | (digitcarrybit << 1);
     * 
     * } else { digitcarrybit = 0; ram[rb0][3] = (ram[rb0][3]) & (digitcarrybit <<
     * 1);
     * 
     * } }
     */

    public static void digitcarry(int register1, int register2, int isSub) {

        int bottomNibbleRegister1 = register1 & 0b0000_1111;
        int bottomNibbleRegister2 = register2 & 0b0000_1111;

        int carryout = bottomNibbleRegister1 + bottomNibbleRegister2;
        int upperNibbleCarryout = carryout & 0b1111_0000;

        boolean cond;
        if (isSub == 1) {
            cond = register2 == 0;
        } else {
            cond = false;
        }

        if (upperNibbleCarryout != 0 || cond) {
            digitcarrybit = 1;
            ram[rb0][3] = (ram[rb0][3]) | (digitcarrybit << 1);
        } else {
            digitcarrybit = 0;
            ram[rb0][3] = (ram[rb0][3]) & (digitcarrybit << 1);
        }

        /*
         * if ((halfbyte<=0b0000_1111 && result >0b0000_1111)||(halfbyte>0b0000_1111 &&
         * result <= 0b0000_1111)) {
         * 
         * digitcarrybit = 1; ram[rb0][3] = (ram[rb0][3]) | (digitcarrybit << 1);
         * 
         * } else { digitcarrybit = 0; ram[rb0][3] = (ram[rb0][3]) & (digitcarrybit <<
         * 1);
         * 
         * }
         * 
         * 
         * int guess; Boolean c; if (isSub == 1) { guess = Instructions.lz(-(15 -
         * register1)); c = register2 >= guess; } else { if(register1==0) { guess =
         * register1; } else { guess = Instructions.lz((16 - register1)); } c =
         * register2 >= guess; } if (c) {
         * 
         * digitcarrybit = 1; ram[rb0][3] = (ram[rb0][3]) | (digitcarrybit << 1);
         * 
         * } else { digitcarrybit = 0; ram[rb0][3] = (ram[rb0][3]) & (digitcarrybit <<
         * 1); }
         */
    }
    /*
     * public static void carry(int result, int register) {
     * 
     * if (((register <= 0b1111_1111) && (result > 0b1111_1111)) || ((register >=
     * 0b0000_0000) && (result < 0b0000_0000))) { carrybit = 1; ram[rb0][3] =
     * (ram[rb0][3]) | (carrybit);
     * 
     * } else { carrybit = 0; ram[rb0][3] = (ram[rb0][3]) & (carrybit);
     * 
     * }
     * 
     * }
     */

    public static void carry(int register1, int register2, int isSub) {

        int addRegisters = register1 + register2;

        boolean cond;
        if (isSub == 1) {
            cond = register2 == 0;
        } else {
            cond = false;
        }

        if (addRegisters > 0b1111_1111 || cond) {
            carrybit = 1;
            ram[rb0][3] = (ram[rb0][3]) | (carrybit);

        } else {
            carrybit = 0;
            ram[rb0][3] = (ram[rb0][3]) & (carrybit);

        }

        /*
         * if ( register<ram0) { carrybit = 1; ram[rb0][3] = (ram[rb0][3]) | (carrybit);
         * 
         * } else { carrybit = 0; ram[rb0][3] = (ram[rb0][3]) & (carrybit);
         * 
         * }
         * 
         * 
         * int guess; Boolean c; if (isSub == 1) { guess = Instructions.lz(-(255-
         * register1)); c=register2 >= guess; } else { if(register1==0) { guess =
         * register1; } else { guess = Instructions.lz(( -register1)); } c=register2 >=
         * guess;
         * 
         * }
         * 
         * if (c) { carrybit = 1; ram[rb0][3] = (ram[rb0][3]) | (carrybit);
         * 
         * } else { carrybit = 0; ram[rb0][3] = (ram[rb0][3]) & (carrybit);
         * 
         * }
         */
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

    public static void setRegister() {
        fsr = ram[0][4];
        if (rb0Check != 0) {
            ram[1][0] = ram[1][fsr];
            ram[0][0] = ram[1][0];
            // ram[1][fsr] = ram[1][0];
            ram[0][2] = ram[1][2];
            ram[0][3] = ram[1][3];
            ram[0][4] = ram[1][4];
            ram[0][10] = ram[1][10];
            ram[0][11] = ram[1][11];
           
        } else {
            ram[0][0] = ram[0][fsr];
            ram[1][0] = ram[0][0];
            // ram[0][fsr] = ram[0][0];
            ram[1][2] = ram[0][2];
            ram[1][3] = ram[0][3];
            ram[1][4] = ram[0][4];
            ram[1][10] = ram[0][10];
            ram[1][11] = ram[0][11];

        }

    }
}
