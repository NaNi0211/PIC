/*
 * Sunlw test1
 * 
 * Bank test3
 * 
 * PD TO Status
 * 
 * CarryFlag test4
 */
public class Instructions extends DecodeDraft {

    // Literal
    public static void addlw(int literalCode) {
        int result = wRegister + literalCode;
        // int bottomHalfbyte = (wRegister & 0b0000_1111) + (literalCode & 0b0000_1111);
        zero(result);
        carry(literalCode, wRegister, 0);
        digitcarry(literalCode, wRegister, 0);

        wRegister = result % 256;
        pC_Next++;
    }

    public static void andlw(int literalCode) {
        int result = wRegister & literalCode;
        zero(wRegister);
        wRegister = result % 256;
        pC_Next++;
    }

    public static void iorlw(int literalCode) {
        int result = wRegister | literalCode;
        zero(result);
        wRegister = result % 256;
        pC_Next++;
    }

    
    public static void sublw(int literalCode) {
        int newW_Reg = lz(-wRegister);
        int result = literalCode + newW_Reg;

        // int bottomHalfbyte = (wRegister & 0b0000_1111) - ( result & 0b0000_1111);
        zero(result);
        carry(literalCode, newW_Reg, 1);
        digitcarry(literalCode, newW_Reg, 1);

        wRegister = result % 256;
        pC_Next++;
    }

    public static int lz(int comp) {

        while (comp < 0) {
            comp += 256;
        }
        /*
         * if(comp<0) {
         * 
         * comp =(comp ^ 0b1111_1111) ;
         * System.out.println(Integer.toBinaryString(comp)); comp = (comp +1)|
         * 0b1000_0000; System.out.println(Integer.toBinaryString(comp)); comp = comp
         * &0b1111_1111; }
         */
        return comp;
    }

    public static void movlw(int literalCode) {
        int result = literalCode;
        wRegister = result % 256;
        pC_Next++;
    }

    public static void xorlw(int literalCode) {
        int result = wRegister ^ literalCode;
        zero(result);
        wRegister = result % 256;
        pC_Next++;
    }

    public static void retlw(int literalCode) {
        wRegister = literalCode;
        return0();
    }

    // Jump
    public static void goto0(int literalCode) {
        pC_Next = literalCode;
    }

    public static void call(int literalCode) {
        setCallnReturn(0);
        pC_Next = literalCode;
    }

    // onlyOp
    public static void nop() {
        pC_Next++;
    }

    public static void clrw() {
        wRegister = 0;
        zero(wRegister);
        pC_Next++;
    }

    public static void return0() {
        setCallnReturn(1);
    }

    public static void setCallnReturn(int value) {
        if (value == 0) {
            stack[stackpointer++] = ++pC_Next;
            stackpointer %=8;
        } else {
            pC_Next = stack[--stackpointer];
        }

    }

    // byteorientated
    public static void movf(int fileCode, int destBit) {

        int result = ram[rb0][fileCode] % 256;
        zero(result);
        if (destBit != 0) {
            ram[rb0][fileCode] = result;
        } else {
            wRegister = result;
        }
        pC_Next++;
    }

    public static void clrf(int fileCode) {
        ram[rb0][fileCode] = 0;
        zero(ram[rb0][fileCode]);
        pC_Next++;
    }

    public static void comf(int fileCode, int destBit) {
        int result = (ram[rb0][fileCode] ^ 0b1111_1111) % 256;
        zero(result);
        if (destBit != 0) {
            ram[rb0][fileCode] = result;
        } else {
            wRegister = result;
        }
        pC_Next++;
    }

    public static void decf(int fileCode, int destBit) {
        int result = lz(ram[rb0][fileCode] - 1) % 256;
        zero(result);
        if (destBit != 0) {
            ram[rb0][fileCode] = result;
        } else {
            wRegister = result;
        }
        pC_Next++;
    }

    public static void incf(int fileCode, int destBit) {
        int result = ++ram[rb0][fileCode] % 256;
        zero(result);
        if (destBit != 0) {
            ram[rb0][fileCode] = result;
        } else {
            wRegister = result;
        }
        pC_Next++;
    }

    public static void iorwf(int fileCode, int destBit) {
        int result = (ram[rb0][fileCode] | wRegister) % 256;
        zero(result);
        if (destBit != 0) {
            ram[rb0][fileCode] = result;
        } else {
            wRegister = result;
        }
        pC_Next++;
    }

    public static void xorwf(int fileCode, int destBit) {
        int result = (ram[rb0][fileCode] ^ wRegister) % 256;
        zero(result);
        if (destBit != 0) {
            ram[rb0][fileCode] = result;
        } else {
            wRegister = result;
        }
        pC_Next++;
    }

    public static void subwf(int fileCode, int destBit) {
        int newW_Reg = lz(-wRegister);
        int result = lz(ram[rb0][fileCode] -wRegister) ;
        zero(result);

        if (destBit != 0) {
            carry(ram[rb0][fileCode], newW_Reg, 1);
            digitcarry(ram[rb0][fileCode], newW_Reg, 1);
            ram[rb0][fileCode] = result;

        } else {
            carry(ram[rb0][fileCode], newW_Reg, 1);
            digitcarry(ram[rb0][fileCode], newW_Reg, 1);
            wRegister = result;

        }
        pC_Next++;
    }

    public static void rlf(int fileCode, int destBit) {

        int oldCarry = carrybit;

        carrybit = (ram[rb0][fileCode] & 0b1000_0000) >> 7;

        if (destBit != 0) {
            ram[rb0][fileCode] = ((ram[rb0][fileCode] << 1) | oldCarry) % 256;
        } else {
            wRegister = ((ram[rb0][fileCode] << 1) | oldCarry) % 256;
        }

        ram[rb0][3] = ram[rb0][3] | carrybit;

        pC_Next++;
    }
    public static void rrf(int fileCode, int destBit) {

        int oldCarry = carrybit;

        carrybit = (ram[rb0][fileCode] & 0b0000_0001);

        if (destBit != 0) {
            ram[rb0][fileCode] = ((ram[rb0][fileCode] >> 1) |(oldCarry<<7)) % 256;
        } else {
            wRegister = ((ram[rb0][fileCode] >> 1) | (oldCarry<<7)) % 256;
        }

        ram[rb0][3] = ram[rb0][3] | carrybit;

        pC_Next++;
    }
    
   
    
    // Ant

    public static void movwf(int fileCode) {

        ram[rb0][fileCode] = wRegister % 256;
        pC_Next++;

    }

    public static void addwf(int fileCode, int destBit) {

        int fileValue = ram[rb0][fileCode];
        int result = (wRegister + fileValue) % 256;
        zero(result);

        if (destBit != 0) {
            carry( fileValue,wRegister, 0);
            digitcarry( fileValue,wRegister,  0);
            ram[rb0][fileCode] = result;
        } else {
            carry( fileValue,wRegister, 0);
            digitcarry( fileValue,wRegister,  0);
            wRegister = result;
        }
        pC_Next++;
    }

    public static void andwf(int fileCode, int destBit) {

        int fileValue = ram[rb0][fileCode];
        int result = (wRegister & fileValue) % 256;
        zero(result);
        if (destBit != 0) {
            ram[rb0][fileCode] = result;
        } else {
            wRegister = result;
        }

        pC_Next++;
    }

    public static void swapf(int fileCode, int destBit) {
        int upperresult = (ram[rb0][fileCode] & 0b0000_1111) << 4;
        int lowerresult = (ram[rb0][fileCode] & 0b1111_0000) >> 4;

        int result = (upperresult | lowerresult) % 256;

        if (destBit != 0) {
            ram[rb0][fileCode] = result;
        } else {
            wRegister = result;
        }
        pC_Next++;
    }

    public static void decfsz(int fileCode) {

        ram[rb0][fileCode] = (lz(--ram[rb0][fileCode])) % 256;
        if (ram[rb0][fileCode] == 0) {
            pC_Next += 2; // Skip next instruction
            runtime += (4.0 / quartz);
        } else {
            pC_Next++;
        }
    }

    public static void incfsz(int fileCode) {

        ram[rb0][fileCode] = (++ram[rb0][fileCode]) % 256;
        if (ram[rb0][fileCode] == 0) {
            pC_Next += 2; // Skip next instruction
            runtime += (4.0 / quartz);
        } else {
            pC_Next++;
        }
    }

    public static void bsf(int fileCode, int bitCode) {

        ram[rb0][fileCode] |= (1 << bitCode);
        pC_Next++;
    }

    public static void bcf(int fileCode, int bitCode) {

        ram[rb0][fileCode] &= ~(1 << bitCode);
        pC_Next++;
    }

    public static void btfsc(int fileCode, int bitCode) {

        if ((ram[rb0][fileCode] & (1 << bitCode)) == 0) {
            pC_Next += 2;
            runtime += (4.0 / quartz);
            // Skip next instruction
        } else {
            pC_Next++;
        }
    }

    public static void btfss(int fileCode, int bitCode) {

        if ((ram[rb0][fileCode] & (1 << bitCode)) != 0) {
            pC_Next += 2;
            runtime += (4.0 / quartz);
            // Skip next instruction
        } else {
            pC_Next++;
        }
    }

}
