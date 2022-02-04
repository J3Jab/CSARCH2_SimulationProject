package main.java;

import java.util.ArrayList;
import java.util.Scanner;

public class DPBCD {

    public DPBCD(){

    }

    public String DecimalToBinary(String Decimal){

        String bin;
        System.out.println(Decimal);
        switch(Decimal){
            case "0":
                bin = "0000";break;
            case "1":
                bin = "0001";break;
            case "2":
                bin = "0010";break;
            case "3":
                bin = "0011";break;
            case "4":
                bin = "0100";break;
            case "5":
                bin = "0101";break;
            case "6":
                bin = "0110";break;
            case "7":
                bin = "0111";break;
            case "8":
                bin = "1000";break;
            case "9":
                bin = "1001";break;
            default:
                bin = "0000";
        }

        return bin;
    }

    public String PatternTable(String msb){

        String pattern;
        System.out.println(msb);
        switch(msb){
            case "000":
                pattern = "bcdfgh0jkm";break;
            case "001":
                pattern = "bcdfgh100m";break;
            case "010":
                pattern = "bcdjkh101m";break;
            case "011":
                pattern = "bcd10h111m";break;
            case "100":
                pattern = "jkdfgh110m";break;
            case "101":
                pattern = "fgd01h111m";break;
            case "110":
                pattern = "jkd00h111m";break;
            case "111":
                pattern = "00d11h111m";break;
            default:
                pattern = "0000";
        }

        return pattern;
    }

    public String keyDPBCD(String[][] digitBCD, String key){

        String bitDPBCD;
        System.out.println(key);
        switch(key){
            case "a":
                bitDPBCD = digitBCD[0][0];break;
            case "b":
                bitDPBCD = digitBCD[0][1];break;
            case "c":
                bitDPBCD = digitBCD[0][2];break;
            case "d":
                bitDPBCD = digitBCD[0][3];break;
            case "e":
                bitDPBCD = digitBCD[1][0];break;
            case "f":
                bitDPBCD = digitBCD[1][1];break;
            case "g":
                bitDPBCD = digitBCD[1][2];break;
            case "h":
                bitDPBCD = digitBCD[1][3];break;
            case "i":
                bitDPBCD = digitBCD[2][0];break;
            case "j":
                bitDPBCD = digitBCD[2][1];break;
            case "k":
                bitDPBCD = digitBCD[2][2];break;
            case "m":
                bitDPBCD = digitBCD[2][3];break;
            case "1":
                bitDPBCD = "1";break;
            case "0":
                bitDPBCD = "0";break;

            default:
                bitDPBCD = "0000";
        }

        return bitDPBCD;
    }


    public void storeBinary(String[][] binaryArray, String binary, int i){
        for(int j = 0; j < 4; j++){
            binaryArray[i][j] = String.valueOf(binary.charAt(j));
        }

    }
    public void computeDPBCD(String input){
//        int hundreds, tens, ones;
//        hundreds = Integer.parseInt(String.valueOf(input.charAt(0)));
//        tens = Integer.parseInt(String.valueOf(input.charAt(1)));
//        ones = Integer.parseInt(String.valueOf(input.charAt(2)));

        String temp;
        String[][] digitBCD = new String[3][4];
        for(int i = 0; i < 3; i++){
            temp = DecimalToBinary(String.valueOf(input.charAt(i)));
            storeBinary(digitBCD, temp, i);
        }

        String msb = digitBCD[0][0] + digitBCD[1][0] + digitBCD[2][0];
        System.out.println(msb);
        String pattern = PatternTable(msb);
        System.out.println(pattern.length());

        String answer = "";
        for(int i = 0; i < pattern.length(); i++){
            answer += keyDPBCD(digitBCD, String.valueOf(pattern.charAt(i)));
        }

        System.out.println(answer);

    }

    public static void main(String[] args) {
        DPBCD object = new DPBCD();
        object.computeDPBCD("956");
    }

}
