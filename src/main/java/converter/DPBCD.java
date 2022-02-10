package converter;

public class DPBCD {
    public DPBCD() { }

    public String DecimalToBinary(String Decimal) {
        String bin = "0000";

        /* bin = switch (Decimal) {
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
        };*/

        if ("0".equals(Decimal)) { bin = "0000" ;}
        if ("1".equals(Decimal)) { bin = "0001" ;}
        if ("2".equals(Decimal)) { bin = "0010" ;}
        if ("3".equals(Decimal)) { bin = "0011" ;}
        if ("4".equals(Decimal)) { bin = "0100" ;}
        if ("5".equals(Decimal)) { bin = "0101" ;}
        if ("6".equals(Decimal)) { bin = "0110" ;}
        if ("7".equals(Decimal)) { bin = "0111" ;}
        if ("8".equals(Decimal)) { bin = "1000" ;}
        if ("9".equals(Decimal)) { bin = "1001" ;}

        return bin;
    }

    public String PatternTable(String msb) {
        String pattern = "0000";

        /*pattern = switch (msb) {
            case "000" -> "bcdfgh0jkm";
            case "001" -> "bcdfgh100m";
            case "010" -> "bcdjkh101m";
            case "011" -> "bcd10h111m";
            case "100" -> "jkdfgh110m";
            case "101" -> "fgd01h111m";
            case "110" -> "jkd00h111m";
            case "111" -> "00d11h111m";
            default -> "0000";
        };*/

        if ("000".equals(msb)) { pattern = "bcdfgh0jkm" ;}
        if ("001".equals(msb)) { pattern = "bcdfgh100m" ;}
        if ("010".equals(msb)) { pattern = "bcdjkh101m" ;}
        if ("011".equals(msb)) { pattern = "bcd10h111m" ;}
        if ("100".equals(msb)) { pattern = "jkdfgh110m" ;}
        if ("101".equals(msb)) { pattern = "fgd01h111m" ;}
        if ("110".equals(msb)) { pattern = "jkd00h111m" ;}
        if ("111".equals(msb)) { pattern = "00d11h111m" ;}

        return pattern;
    }

    public String keyDPBCD(String[][] digitBCD, String key) {
        String bitDPBCD = "0000";

        /* bitDPBCD = switch (key) {
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
        }; */

        if ("a".equals(key)) { bitDPBCD = digitBCD[0][0] ;}
        if ("b".equals(key)) { bitDPBCD = digitBCD[0][1] ;}
        if ("c".equals(key)) { bitDPBCD = digitBCD[0][2] ;}
        if ("d".equals(key)) { bitDPBCD = digitBCD[0][3] ;}

        if ("e".equals(key)) { bitDPBCD = digitBCD[1][0] ;}
        if ("f".equals(key)) { bitDPBCD = digitBCD[1][1] ;}
        if ("g".equals(key)) { bitDPBCD = digitBCD[1][2] ;}
        if ("h".equals(key)) { bitDPBCD = digitBCD[1][3] ;}

        if ("i".equals(key)) { bitDPBCD = digitBCD[2][0] ;}
        if ("j".equals(key)) { bitDPBCD = digitBCD[2][1] ;}
        if ("k".equals(key)) { bitDPBCD = digitBCD[2][2] ;}
        if ("m".equals(key)) { bitDPBCD = digitBCD[2][3] ;}

        if ("1".equals(key)) { bitDPBCD = "1" ;}
        if ("0".equals(key)) { bitDPBCD = "0" ;}

        return bitDPBCD;
    }


    public void storeBinary(String[][] binaryArray, String binary, int i) {
        for(int j = 0; j < 4; j++) {binaryArray[i][j] = String.valueOf(binary.charAt(j));}
    }

    public String computeDPBCD(String input) {
        String temp;
        String[][] digitBCD = new String[3][4];

        for (int i = 0; i < 3; i++) {
            temp = DecimalToBinary(String.valueOf(input.charAt(i)));
            storeBinary(digitBCD, temp, i);
        }

        String msb = digitBCD[0][0] + digitBCD[1][0] + digitBCD[2][0];
        String pattern = PatternTable(msb);

        StringBuilder answer = new StringBuilder();
        for (int i = 0; i < pattern.length(); i++) {
            answer.append(keyDPBCD(digitBCD, String.valueOf(pattern.charAt(i))));
        }

        return answer.toString();
    }

    public static void main(String[] args) {
        DPBCD object = new DPBCD();
        object.computeDPBCD("945");
    }
}
