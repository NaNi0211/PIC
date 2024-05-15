
public class Instructions extends DecodeDraft {

    // Literal
    public static void addlw(int literalCode) {
        int result = wRegister + literalCode;
        int bottomHalfbyte = (wRegister & 0b0000_1111) + (literalCode & 0b0000_1111);
        zero(result);
        carry(result, wRegister);
        digitcarry(result, bottomHalfbyte);

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
        int result = wRegister - literalCode;
        int zweierKom = (((literalCode ^ (0b1111_1111)) + 1) & 0b0000_1111);
        int bottomHalfbyte = (wRegister & 0b0000_1111) + zweierKom;
        zero(result);
        carry(result, wRegister);
        digitcarry(result, bottomHalfbyte);
        result *= -1;
        wRegister = result % 256;
        pC_Next++;
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
        pC_Next++;
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
        pC_Next++;
    }

    public static void return0() {
        setCallnReturn(1);
    }

    public static void setCallnReturn(int value) {
        if (value == 0) {
            stack[stackpointer++] = ++pC_Next;
        } else {
            pC_Next = stack[--stackpointer];
        }
    }

    // byteorientated
    public static void movf(int destBit, int fileCode) {
        if (destBit == 0b0000_0000_1000_0000) {
            ram[rb0][fileCode] = ram[rb0][fileCode];

        } else {

            wRegister = ram[rb0][fileCode];
        }
    }
    // Ant

    public static void movwf(int fileCode) {

        ram[rb0][fileCode] = wRegister;
        // zero(wRegister);

    }

    public static void addwf(int fileCode, int destBit) {

        int fileValue = ram[rb0][fileCode];
        int result = wRegister + fileValue;
        zero(result);
        carry(result, wRegister);
        digitcarry(result, (wRegister & 0b0000_1111) + (fileValue & 0b0000_1111));
        if (destBit == 0) {
            ram[rb0][fileCode] = result;
        } else {
            wRegister = result;
        }
        pC_Next++;
    }

    public static void andwf(int fileCode, int destBit) {

        int fileValue = ram[rb0][fileCode];
        int result = wRegister & fileValue;
        zero(result);
        if (destBit == 0) {
            ram[rb0][fileCode] = result;
        } else {
            wRegister = result;
        }

        pC_Next++;
    }

    public static void decfsz(int fileCode) {

        ram[rb0][fileCode]--;
        if (ram[rb0][fileCode] == 0) {
            pC_Next += 2; // Skip next instruction
        } else {
            pC_Next++;
        }
    }

    public static void incfsz(int fileCode) {

        ram[rb0][fileCode]++;
        if (ram[rb0][fileCode] == 0) {
            pC_Next += 2; // Skip next instruction
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

            // Skip next instruction
        } else {
            pC_Next++;
        }
    }

    public static void btfss(int fileCode, int bitCode) {

        if ((ram[rb0][fileCode] & (1 << bitCode)) != 0) {
            pC_Next += 2;

            // Skip next instruction
        } else {
            pC_Next++;
        }
    }
}
