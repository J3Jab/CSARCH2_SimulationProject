package converter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Double.NaN;

public class Converter {
    Stage stage;
    private Parent root;
    private Scene scene;
    private FXMLLoader loader;
    private final DPBCD dpbcd;

    @FXML
    private Button nan_btn;
    @FXML
    private Button convert_btn;
    @FXML
    private Button exit_btn;
    @FXML
    private Button copy_btn;
    @FXML
    private Button trunc_btn;
    @FXML
    private Button down_btn;
    @FXML
    private Button up_btn;
    @FXML
    private Button near_btn;
    @FXML
    private TextField decinput_tf;
    @FXML
    private TextField expinput_tf;
    @FXML
    private TextField binoutput_ta;
    @FXML
    private TextField hexoutput_ta;
    @FXML
    private ImageView bg_img;
    @FXML
    private Image bg_trunc = new Image(getClass().getResource("/img/mainTrunc.png").toExternalForm());
    @FXML
    private Image bg_down = new Image(getClass().getResource("/img/mainDown.png").toExternalForm());
    @FXML
    private Image bg_up = new Image(getClass().getResource("/img/mainUp.png").toExternalForm());
    @FXML
    private Image bg_near = new Image(getClass().getResource("/img/mainNear.png").toExternalForm());

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
    String binOutput;
    String hexOutput;

    public Converter() {
        dpbcd = new DPBCD();
    }

    private void normalize() {
        //System.out.println(this.decimal);

        if (this.decimal < 1000000.0 && this.decimal > -1000000.0) {
            while (this.decimal < 1000000.0 && this.decimal > -1000000.0) {
                this.decimal *= 10;
                exponent -= 1;
            }
        } else {
            while (this.decimal > 9999999 || this.decimal < -9999999) {
                this.decimal /= 10;
                exponent += 1;
            }
        }
    }

    private void convert(double decimal, int exponent) {
        this.decimal = decimal;
        this.exponent = exponent;
        normalize();
        roundOff();
        normalize();

        //signBit
        if (this.decimal > 0) { signBit = "0"; }
            else { signBit = "1"; }

        if (exponent > 90 || exponent < -101) {
            combiField = "11110";
            expContinuation = "000000";
            first3digitsDPBCD = "0000000000";
            last3digitsDPBCD = "0000000000";
        } else if (Double.isNaN(this.decimal)) {
            combiField = "11111";
            expContinuation = "000000";
            first3digitsDPBCD = "0000000000";
            last3digitsDPBCD = "0000000000";
        } else {
            msbToBinary(this.decimal);
            computeEPrime(this.exponent);
            determineCombiField();
            expContinuation = ePrimeBinary.substring(2);
            determineDPBCD();
        }

        binOutput = signBit + " " + combiField + " " + expContinuation + " "
                + first3digitsDPBCD + " " + last3digitsDPBCD;
        System.out.println("bin: " + binOutput);
        binaryToHexadecimal();
    }

    public static long roundEven(double d) {
        return Math.round(d / 2) * 2;
    }

    private void roundOff(){
        switch (roundOffMethod) {
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
                //System.out.println(this.decimal);
                break;
            // round to nearest (ties to even)
            case 4:
                this.decimal = roundEven(this.decimal);
                break;
        }
    }

    private void msbToBinary(double decimal) {
        mostSigBit = Math.abs((int)decimal);

        while (mostSigBit > 9) { mostSigBit /= 10; }

        significantBitBinary = dpbcd.DecimalToBinary(Integer.toString(mostSigBit));
    }

    private void computeEPrime(int exp) {
        ePrimeBinary = Integer.toBinaryString(exp + 101);
        while (ePrimeBinary.length() < 8) { ePrimeBinary = "0" + ePrimeBinary;}
    }

    private void determineCombiField() {
        if (mostSigBit >= 0 && mostSigBit <= 7) {
            combiField = ePrimeBinary.substring(0,2) + significantBitBinary.substring(1,4);
        } else {
            combiField = "11" + ePrimeBinary.substring(0,2) + significantBitBinary.charAt(3);
        }
    }

    private void determineDPBCD() {
        int value = (int) this.decimal;
        value = Math.abs(value);
        String stringVal = String.valueOf(value);

        //System.out.println(stringVal);

        String last3digits = stringVal.substring(4, 7);
        String first3digits = stringVal.substring(1, 4);

        //System.out.println(first3digits);
        //System.out.println(last3digits);

        first3digitsDPBCD = dpbcd.computeDPBCD(first3digits);
        last3digitsDPBCD = dpbcd.computeDPBCD(last3digits);

        //System.out.println(first3digitsDPBCD);
        //System.out.println(last3digitsDPBCD);
    }

    public String HexaTable(String binary) {
        String pattern = "0";

        if ("0000".equals(binary)) { pattern = "0" ;}
        if ("0001".equals(binary)) { pattern = "1" ;}
        if ("0010".equals(binary)) { pattern = "2" ;}
        if ("0011".equals(binary)) { pattern = "3" ;}
        if ("0100".equals(binary)) { pattern = "4" ;}
        if ("0101".equals(binary)) { pattern = "5" ;}
        if ("0110".equals(binary)) { pattern = "6" ;}
        if ("0111".equals(binary)) { pattern = "7" ;}
        if ("1000".equals(binary)) { pattern = "8" ;}
        if ("1001".equals(binary)) { pattern = "9" ;}
        if ("1010".equals(binary)) { pattern = "A" ;}
        if ("1011".equals(binary)) { pattern = "B" ;}
        if ("1100".equals(binary)) { pattern = "C" ;}
        if ("1101".equals(binary)) { pattern = "D" ;}
        if ("1110".equals(binary)) { pattern = "E" ;}
        if ("1111".equals(binary)) { pattern = "F" ;}

        return pattern;
    }

    void binaryToHexadecimal() {
        String fullBin = signBit + combiField + expContinuation + first3digitsDPBCD + last3digitsDPBCD;
        StringBuilder hexa = new StringBuilder("0x");
        int cutoff;

        for(int i = 1; i <= 8; i++) {
            cutoff = i * 4;
            String substring_bin = fullBin.substring(cutoff - 4, cutoff);
            hexa.append(HexaTable(substring_bin));
        }

        hexOutput = hexa.toString();
        System.out.println("hex: " + hexOutput);
    }

    public static void main(String[] args) {
        /*Converter abc = new Converter();

        abc.roundOffMethod = 2;
        //only works on already normalized inputs
//        abc.convert(1.0, 10);
        abc.convert(7123456.0, 20);
        abc.convert(-8765432, -20);
//        abc.convert(71234560000.0, 16);
//        abc.convert(-9.9999999, -20);
//        abc.convert(-1234567, 9);
       abc.convert(-1.234567, 98);
//        abc.convert(9.9999999, 15);
        abc.convert(NaN, 15);*/
    }

    @FXML
    private void handleAction(ActionEvent e) throws IOException {
        String decInputStr;
        String expInputStr;
        Double decInput;
        int expInput;

        String doubleReg = "^-?\\d+(\\.\\d+)?$";
        String intReg = "^-?\\d+$";
        String NaNReg = "NaN";
        Pattern p = Pattern.compile(doubleReg);
        Pattern p1 = Pattern.compile(NaNReg);
        Pattern p2 = Pattern.compile(intReg);
        Matcher mDec;
        Matcher mDec1;
        Matcher mExp;

        if (e.getSource() == nan_btn) {
            decInput = NaN;
            decinput_tf.setText("NaN");
        }

        if (e.getSource() == convert_btn) {
            if (decinput_tf.getText() == null || expinput_tf.getText() == null ||
                    decinput_tf.getText().trim().isEmpty() || expinput_tf.getText().trim().isEmpty()) {
                Alert fail = new Alert(Alert.AlertType.ERROR);
                fail.setHeaderText("Empty Field");
                fail.setContentText("At least one of the input fields is empty.");
                fail.showAndWait();
            } else {
                mDec = p.matcher(decinput_tf.getText());
                mExp = p2.matcher(expinput_tf.getText());
                mDec1 = p1.matcher(decinput_tf.getText());

                if (mDec.matches() || mDec1.matches()) {
                    if (mExp.matches()) {
                        decInput = Double.parseDouble(decinput_tf.getText());
                        expInput = Integer.parseInt(expinput_tf.getText());
                        convert(decInput, expInput);

                        binoutput_ta.setText(binOutput);
                        hexoutput_ta.setText(hexOutput);
                    } else {
                        Alert fail = new Alert(Alert.AlertType.ERROR);
                        fail.setHeaderText("Invalid Input");
                        fail.setContentText("Exponent cannot be a float or equal to NaN.");
                        fail.showAndWait();
                    }
                } else {
                    Alert fail = new Alert(Alert.AlertType.ERROR);
                    fail.setHeaderText("Invalid Input");
                    fail.setContentText("Only NaN or numbers (0-9) are allowed.");
                    fail.showAndWait();
                }
            }
        }

        if (e.getSource() == copy_btn) {
            if (!(binoutput_ta.getText().trim().isEmpty() && hexoutput_ta.getText().trim().isEmpty())) {
                String clipboardText = binoutput_ta.getText() + "\n" + hexoutput_ta.getText();

                StringSelection stringSelection = new StringSelection(clipboardText);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringSelection, null);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Success");
                alert.setContentText("Copied \"" + clipboardText + "\" to clipboard!");
                alert.showAndWait();
            }
        }

        if (e.getSource() == exit_btn) { ((Stage)(((Button)e.getSource()).getScene().getWindow())).close(); }

        if (e.getSource() == trunc_btn) {
            roundOffMethod = 1;
            bg_img.setImage(bg_trunc);
        } if (e.getSource() == down_btn) {
            roundOffMethod = 2;
            bg_img.setImage(bg_down);
        } if (e.getSource() == up_btn) {
            roundOffMethod = 3;
            bg_img.setImage(bg_up);
        } if (e.getSource() == near_btn) {
            roundOffMethod = 4;
            bg_img.setImage(bg_near);
        }
    }
}
