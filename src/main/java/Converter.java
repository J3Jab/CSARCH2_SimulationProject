package main.java;

import static java.lang.Double.NaN;

public class Converter {

    private final DPBCD dpbcd;

    double decimal;
    int exponent;
    int mostSigBit;
    String significantBitBinary;

    int roundOffMethod;

    String signBit;
    String ePrimeBinary;
    String combiField;
    String expContinuation;

    String first3digitsDPBCD;
    String last3digitsDPBCD;
    String binToHexa;


    public Converter() {
        dpbcd = new DPBCD();

    }

    void normalize() {
        System.out.println(this.decimal);
        if(this.decimal < 1000000.0 && this.decimal > -1000000.0){
            while (this.decimal < 1000000.0 && this.decimal > -1000000.0) {
                this.decimal *= 10;
                exponent -= 1;
            }
        }
        else{
            while (this.decimal > 9999999 || this.decimal < -9999999) {
                this.decimal /= 10;
                exponent += 1;
            }
        }
    }

    void convert(double decimal, int exponent) {
        this.decimal = decimal;
        this.exponent = exponent;
        normalize();
        roundOff();

        while (this.decimal > 9999999 || this.decimal < -9999999) {
            this.decimal /= 10;
            this.exponent += 1;
        }

        //signBit
        if (this.decimal > 0) {
            signBit = "0";
        }
        else {signBit = "1";}

        msbToBinary(this.decimal);

        computeEPrime(this.exponent);
        determineCombiField();
        expContinuation = ePrimeBinary.substring(2);
        determineDPBCD();
        /* do coefficient continuation here*/
        //dpbcd.computeDPBCD(Integer.toString((int)decimal % 1000000));
        //round up
        //separate the msb to the rest of the normalized/rounded up number
        //split the 6-digit number into two
        //get the bcd of the first half and second half, combine strings together
        System.out.println("before rounding: "+ this.decimal);


        System.out.println("rounded off: " + this.decimal);
        System.out.println("exponent: " + this.exponent);
        System.out.println(signBit + " " + combiField + " " + expContinuation + " "
                + first3digitsDPBCD + " " + last3digitsDPBCD);
        binaryToHexadecimal();
    }

    public static long roundEven(double d) {
        return Math.round(d / 2) * 2;
    }

    void roundOff(){
        switch (roundOffMethod){
            // truncate
            case 1:
                int temp;
                temp = (int) this.decimal;
                this.decimal = temp;
                break;
            // round down
            case 2:
                this.decimal = Math.floor(this.decimal);
                break;
            // round up
            case 3:
                this.decimal = Math.ceil(this.decimal);
                System.out.println(this.decimal);
                break;
            // round to nearest (ties to even)
            case 4:
                this.decimal = roundEven(this.decimal);
                break;
        }
    }
    void msbToBinary(double decimal) {
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

    void determineDPBCD(){
        int value = (int) this.decimal;
        value = Math.abs(value);
        String stringVal = String.valueOf(value);
        System.out.println(stringVal);
        String last3digits = stringVal.substring(4, 7);
        String first3digits = stringVal.substring(1, 4);

        System.out.println(first3digits);
        System.out.println(last3digits);

        first3digitsDPBCD = dpbcd.computeDPBCD(first3digits);
        last3digitsDPBCD = dpbcd.computeDPBCD(last3digits);

        System.out.println(first3digitsDPBCD);
        System.out.println(last3digitsDPBCD);
    }

    public String HexaTable(String binary){

        String pattern;
        pattern = switch (binary) {
            case "0000" -> "0";
            case "0001" -> "1";
            case "0010" -> "2";
            case "0011" -> "3";
            case "0100" -> "4";
            case "0101" -> "5";
            case "0110" -> "6";
            case "0111" -> "7";
            case "1000" -> "8";
            case "1001" -> "9";
            case "1010" -> "A";
            case "1011" -> "B";
            case "1100" -> "C";
            case "1101" -> "D";
            case "1110" -> "E";
            case "1111" -> "F";

            default -> "0";
        };

        return pattern;
    }

    void binaryToHexadecimal() {
        String fullBin = signBit + combiField + expContinuation + first3digitsDPBCD + last3digitsDPBCD;
        StringBuilder hexa = new StringBuilder("0x");
        int cutoff;
        for(int i = 1; i <= 8; i++){
            cutoff = i * 4;
            String substring_bin = fullBin.substring(cutoff - 4, cutoff);
            hexa.append(HexaTable(substring_bin));
        }

        System.out.println(hexa.toString());

    }

    public static void main(String[] args) {
        Converter abc = new Converter();

        abc.roundOffMethod = 2;
        //only works on already normalized inputs
//        abc.convert(1.0, 10);
        abc.convert(7123456.0, 20);
//        abc.convert(71234560000.0, 16);
//        abc.convert(-9.9999999, -20);
//        abc.convert(-1234567, 9);
//        abc.convert(-1.234567, 15);
//        abc.convert(9.9999999, 15);
        //abc.convert(NaN, 15);

    }

}
