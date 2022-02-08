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

    void normalize(float decimal) {
        while (decimal < 1000000 && decimal > -1000000) {
            decimal *= 10f;
            exponent -= 1;
        }
        while (decimal > 9999999 || decimal < -9999999) {
            decimal /= 10f;
            exponent += 1;
        }

    }

    void convert(float decimal, int exponent) {
        this.decimal = decimal;
        this.exponent = exponent;
        normalize(this.decimal);
        System.out.println(this.decimal);

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
        //round up
        //separate the msb to the rest of the normalized/rounded up number
        //split the 6-digit number into two
        //get the bcd of the first half and second half, combine strings together

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


    public static void main(String[] args) {
        Converter abc = new Converter();
        //only works on already normalized inputs
        abc.convert(7123456f, 20);
        abc.convert(71234560000f, 16);
        abc.convert(-8765432f, -20);
        abc.convert(-1234567f, 9);
        abc.convert(-1.234567f, 15);
    }

}
