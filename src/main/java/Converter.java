package main.java;

public class Converter {

    private final DPBCD dpbcd;

    float decimal;
    int exponent;
    int mostSigBit;
    String significantBitBinary;

    String signBit;
    String ePrimeBinary;
    String combiField;
    String expContinuation;

    public Converter() {
        dpbcd = new DPBCD();
    }

    void normalize() {
        //normalize + rounding here??
    }

    void convert(float decimal, int exponent) {
        this.decimal = decimal;
        this.exponent = exponent;
        //normalize();

        //signBit
        if (this.decimal > 0) {
            signBit = "0";
        }
        else {signBit = "1";}

        msbToBinary(this.decimal);

        computeEPrime(this.exponent);
        determineCombiField();
        expContinuation = ePrimeBinary.substring(2);
        /* do coefficient continuation here*/
        //dpbcd.computeDPBCD(Integer.toString((int)decimal % 1000000));

        System.out.println(signBit + " " + combiField + " " + expContinuation);
    }

    void msbToBinary(float decimal) {
        mostSigBit = Math.abs((int)decimal);
        while (mostSigBit > 9) {
            mostSigBit /= 10;
        }

        significantBitBinary = dpbcd.DecimalToBinary(Integer.toString(mostSigBit));
    }

    void computeEPrime(int exp) {
        ePrimeBinary = Integer.toBinaryString(exp + 101);
        while (ePrimeBinary.length() < 8) { ePrimeBinary = "0" + ePrimeBinary;}
    }

    void determineCombiField() {
        if (mostSigBit >= 0 && mostSigBit <= 7) {
            combiField = ePrimeBinary.substring(0,2) + significantBitBinary.substring(1,4);
        }
        else {
            combiField = "11" + ePrimeBinary.substring(0,2) + significantBitBinary.charAt(3);
        }
    }

    void binaryToHexadecimal() {

    }

    /*
    public static void main(String[] args) {
        Converter abc = new Converter();
        //only works on already normalized inputs
        abc.convert(-7123456, 20);
        abc.convert(-8765432, -20);
        abc.convert(-1234567, 9);
    }
    */
}
