
public class DecodeDraft {

    /*
     * name : binaryCode? ganze ram implementieren? NOP_befehl? quarzfreuez zur
     * einstellung der laufzeu flags nur bei arimetischen PCLuPCLATH
     */
    private int wRegister = 0;
    private int[][] ram = new int[2][12];
   
    private int[] EEROM;
    private int pC;
    private int pCL;
    private int[] stack = new int[8];
    private int stackpointer;
    private int carrybit;
    private int digitcarrybit;
    private int zerobit;
    private int rb0;

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
        return pC;
    }

    public int getpCL() {
        return pCL;
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

    public void literalbefehl(int instructionCode) {

        int opCode = instructionCode & 0b0011_1111_0000_0000;
        int literalCode = instructionCode & 0b0000_0000_1111_1111;
        int result = 0;
        int bottomHalfbyte = 0;
        rb0= (ram[rb0][3]>>5)&1;
        if (pCL != pC) {
            pCL = pC;
        }
        
        switch (opCode) {
        case 0b0011_1110_0000_0000: // ADDLW
            result = wRegister + literalCode;
            bottomHalfbyte = (wRegister & 0b0000_1111) + (literalCode & 0b0000_1111);
            zero(result);
            carry(result, wRegister);
            digitcarry(result, bottomHalfbyte);
            wRegister = result % 256;

            pC++;
            break;
        case 0b0011_1001_0000_0000:// ANDLW
            result = wRegister & literalCode;
            zero(wRegister);
            wRegister = result % 256;
            pC++;

            break;
        case 0b0011_1000_0000_0000:// IORLW
            result = wRegister | literalCode;
            zero(result);
            wRegister = result % 256;
            pC++;

            break;
        case 0b0011_1100_0000_0000:// SUBLW
            result = wRegister - literalCode;
            int zweierKom = (((literalCode ^ (0b1111_1111)) + 1) & 0b0000_1111);
            bottomHalfbyte = (wRegister & 0b0000_1111) + zweierKom;
            zero(result);
            carry(result, wRegister);
            digitcarry(result, bottomHalfbyte);
            result *= -1;
            wRegister = result % 256;
            pC++;

            break;
        case 0b0011_0000_0000_0000:// MOVLW
            result = literalCode;
            wRegister = result % 256;
            pC++;

            break;
        case 0b0011_1010_0000_0000:// XORLW
            result = wRegister ^ literalCode;
            zero(result);
            wRegister = result % 256;
            pC++;

            break;
        case 0b0011_0100_0000_0000: // RETLW
            wRegister = literalCode;
            jump(0b0000_0000_0000_1000);

        default:
            jump(instructionCode);
        }

    }

    public void jump(int instructionCode) {

        int opCode = instructionCode & 0b0011_1000_0000_0000;
        int literalCode = instructionCode & 0b0000_0111_1111_1111;
        switch (opCode) {
        case 0b0010_1000_0000_0000: // GOTO
            pC = literalCode;

            break;
        case 0b0010_0000_0000_0000: // CALL
            stack[stackpointer++] = ++pC;
            pC = literalCode;

            break;

        default:
            onlyOp(instructionCode);

        }

    }

    public void onlyOp(int instructionCode) {
        switch (instructionCode) {
        case 0b0000_0000_0000_0000: // NOP
            pC++;
            break;
        case 0b0000_0001_0000_0000: // CLRW
            wRegister = 0;
            pC++;
            break;
        case 0b0000_0000_0000_1000: // RETURN
            pC = stack[--stackpointer];
            break;

        }
    }

    public void byteorientated(int instructionCode) {
        int opCode = instructionCode & 0b1111_1111_0000_0000;
        int destBit = instructionCode & 0b0000_0000_1000_0000;
        int fileCode = instructionCode & 0b0000_0000_0111_1111;
        switch (opCode) {
        case 0b0000_1000_0000_0000: // MOVF
          
                if (destBit == 0b0000_0000_1000_0000) {
                    ram[rb0][fileCode] = ram[rb0][fileCode];
                }
            else {

                wRegister = ram[rb0][fileCode];
            }
            
        default:

        }
    }

    private void digitcarry(int result, int halfbyte) {
        if ((halfbyte & 1111_0000) != 0) {

            digitcarrybit = 1;
            ram[rb0][3] = (ram[rb0][3]) | (digitcarrybit << 1);
           
        } else {
            digitcarrybit = 0;
            ram[rb0][3] = (ram[rb0][3]) & (digitcarrybit << 1);
           
        }
    }

    private void carry(int result, int register) {

        if (((register <= 0b1111_1111) && (result > 0b1111_1111))
                || ((register >= 0b0000_0000) && (result < 0b0000_0000))) {
            carrybit = 1;
            ram[rb0][3] = (ram[rb0][3]) | (carrybit);
            
        } else {
            carrybit = 0;
            ram[rb0][3] = (ram[rb0][3]) & (carrybit);
          
        }

    }

    private void zero(int result) {
        if (result == 0) {
            zerobit = 1;
            ram[rb0][3] = (ram[rb0][3]) | (zerobit << 2);
           
        } else {
            zerobit = 0;
            ram[rb0][3] = (ram[rb0][3]) & (zerobit << 2);
          
        }

    }
   
    }


