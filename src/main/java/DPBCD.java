package main.java;

public class DPBCD {

    public DPBCD(){

    }

    public String DecimalToBinary(String Decimal){

        String bin;
        System.out.println(Decimal);
        bin = switch (Decimal) {
            case "0" -> "0000";
            case "1" -> "0001";
            case "2" -> "0010";
            case "3" -> "0011";
            case "4" -> "0100";
            case "5" -> "0101";
            case "6" -> "0110";
            case "7" -> "0111";
            case "8" -> "1000";
            case "9" -> "1001";
            default -> "0000";
        };

        return bin;
    }

    public String PatternTable(String msb){

        String pattern;
        System.out.println(msb);
        pattern = switch (msb) {
            case "000" -> "bcdfgh0jkm";
            case "001" -> "bcdfgh100m";
            case "010" -> "bcdjkh101m";
            case "011" -> "bcd10h111m";
            case "100" -> "jkdfgh110m";
            case "101" -> "fgd01h111m";
            case "110" -> "jkd00h111m";
            case "111" -> "00d11h111m";
            default -> "0000";
        };

        return pattern;
    }

    public String keyDPBCD(String[][] digitBCD, String key){

        String bitDPBCD;
        System.out.println(key);
        bitDPBCD = switch (key) {
            case "a" -> digitBCD[0][0];
            case "b" -> digitBCD[0][1];
            case "c" -> digitBCD[0][2];
            case "d" -> digitBCD[0][3];
            case "e" -> digitBCD[1][0];
            case "f" -> digitBCD[1][1];
            case "g" -> digitBCD[1][2];
            case "h" -> digitBCD[1][3];
            case "i" -> digitBCD[2][0];
            case "j" -> digitBCD[2][1];
            case "k" -> digitBCD[2][2];
            case "m" -> digitBCD[2][3];
            case "1" -> "1";
            case "0" -> "0";
            default -> "0000";
        };

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
