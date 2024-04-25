
public class Instructions {

    // Literal
    public static int addlw(int wRegister, int literalCode) {
        int result = wRegister + literalCode;
        int bottomHalfbyte = (wRegister & 0b0000_1111) + (literalCode & 0b0000_1111);
        DecodeDraft.zero(result);
        DecodeDraft.carry(result, wRegister);
        DecodeDraft.digitcarry(result, bottomHalfbyte);

        return result % 256;
    }

    public static int andlw(int wRegister, int literalCode) {
        int result = wRegister & literalCode;
        DecodeDraft.zero(wRegister);
        return result % 256;
    }

    public static int iorlw(int wRegister, int literalCode) {
        int result = wRegister | literalCode;
        DecodeDraft.zero(result);
        // Implement zero check as needed
        return result % 256;
    }

    public static int sublw(int wRegister, int literalCode) {
        int result = wRegister - literalCode;
        int zweierKom = (((literalCode ^ (0b1111_1111)) + 1) & 0b0000_1111);
        int bottomHalfbyte = (wRegister & 0b0000_1111) + zweierKom;
        DecodeDraft.zero(result);
        DecodeDraft.carry(result, wRegister);
        DecodeDraft.digitcarry(result, bottomHalfbyte);
        result *= -1;
        return result % 256;
    }

    public static int movlw(int literalCode) {
        int result = literalCode;
        return result % 256;
    }

    public static int xorlw(int wRegister, int literalCode) {
        int result = wRegister ^ literalCode;
        DecodeDraft.zero(result);
        return result % 256;
    }

    public static int retlw(int literalCode) {
        return literalCode;
    }

    // Jump
    public static int goto0(int literalCode) {
        return literalCode;
    }

    public static int call(int literalCode) {
      setCallnReturn(0);
        return literalCode;
    }

    // onlyOp
    public static void nop() {
    }

    public static int clrw() {
        return 0;
    }

    public static void return0() {
       setCallnReturn(1);
    }
    public static void setCallnReturn(int value) {
        if (value == 0) {
            DecodeDraft.stack[DecodeDraft.stackpointer++] = ++DecodeDraft.pC_Next;
        } else {
            DecodeDraft.pC_Next = DecodeDraft.stack[--DecodeDraft.stackpointer];
        }
    }
    // byteorientated
    public static void movf(int destBit, int fileCode) {
        if (destBit == 0b0000_0000_1000_0000) {
            DecodeDraft.ram[DecodeDraft.rb0][fileCode] = DecodeDraft.ram[DecodeDraft.rb0][fileCode];

        } else {

            DecodeDraft.wRegister = DecodeDraft.ram[DecodeDraft.rb0][fileCode];
        }
    }
