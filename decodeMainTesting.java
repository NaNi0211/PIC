
public class decodeMainTesting {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
DecodeDraft m =new DecodeDraft();
/*
m.literalbefehl(0x3011);
System.out.println("W: "+Integer.toHexString(m.getwRegister())+ " C: "+m.getCarrybit() +" DC: "+m.getDigitcarrybit()+" Z: "+m.getZerobit()+" PC: "+m.getpC()+" Status: "+Integer.toBinaryString(m.getRam()[3]));
m.literalbefehl(0x3930);
System.out.println("W: "+Integer.toHexString(m.getwRegister())+ " C: "+m.getCarrybit() +" DC: "+m.getDigitcarrybit()+" Z: "+m.getZerobit()+" PC: "+m.getpC()+" Status: "+Integer.toBinaryString(m.getRam()[3]));
m.literalbefehl(0x380D );
System.out.println("W: "+Integer.toHexString(m.getwRegister())+ " C: "+m.getCarrybit() +" DC: "+m.getDigitcarrybit()+" Z: "+m.getZerobit()+" PC: "+m.getpC()+" Status: "+Integer.toBinaryString(m.getRam()[3]));
m.literalbefehl(0x3C3D);
System.out.println("W: "+Integer.toHexString(m.getwRegister())+ " C: "+m.getCarrybit() +" DC: "+m.getDigitcarrybit()+" Z: "+m.getZerobit()+" PC: "+m.getpC()+" Status: "+Integer.toBinaryString(m.getRam()[3]));
m.literalbefehl(0x3A20);
System.out.println("W: "+Integer.toHexString(m.getwRegister())+ " C: "+m.getCarrybit() +" DC: "+m.getDigitcarrybit()+" Z: "+m.getZerobit()+" PC: "+m.getpC()+" Status: "+Integer.toBinaryString(m.getRam()[3]));
m.literalbefehl(0x3E25);
System.out.println("W: "+Integer.toHexString(m.getwRegister())+ " C: "+m.getCarrybit() +" DC: "+m.getDigitcarrybit()+" Z: "+m.getZerobit()+" PC: "+m.getpC()+" Status: "+Integer.toBinaryString(m.getRam()[3]));
m.literalbefehl(0x2806);
System.out.println("W: "+Integer.toHexString(m.getwRegister())+ " C: "+m.getCarrybit() +" DC: "+m.getDigitcarrybit()+" Z: "+m.getZerobit()+" PC: "+m.getpC()+" Status: "+Integer.toBinaryString(m.getRam()[3]));
 */ 

int[] arr= new int[] {0x3011,0x3930,0x380D,0x3C3D,0x3A20,0x3E25,0x2806};
while(m.getpC()!=7) {
    m.literalbefehl(arr[m.getpC()]);
    System.out.println("W: "+Integer.toHexString(m.getwRegister())+ " C: "+m.getCarrybit() +" DC: "+m.getDigitcarrybit()+" Z: "+m.getZerobit()+" PC: "+m.getpC()+" Status: "+Integer.toBinaryString(m.getRam()[3]));    

}
    }

}
