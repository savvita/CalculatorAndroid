package com.savita.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button resetBtn;
    private Button backspaceBtn;
    private Button squareBtn;
    private Button sqrtBtn;
    private Button openedBracket;
    private Button closedBracket;

    private Button divisionBtn;
    private Button multiplyBtn;
    private Button minusBtn;
    private Button plusBtn;
    private List<Button> operationsBtn;

    private Button signBtn;

    private Button zeroBtn;
    private Button oneBtn;
    private Button twoBtn;
    private Button threeBtn;
    private Button fourBtn;
    private Button fiveBtn;
    private Button sixBtn;
    private Button sevenBtn;
    private Button eightBtn;
    private Button nineBtn;
    private List<Button> digits;

    private Button equalsBtn;

    private TextView resultTxt;

    private String operations = "+-*/";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViews();
        setTextToViews();
        initializeButtons();
    }

    private void initializeViews() {
        resetBtn = findViewById(R.id.resetBtn);

        openedBracket = findViewById(R.id.openedBracketBtn);
        closedBracket = findViewById(R.id.closedBraceBtn);

        squareBtn = findViewById(R.id.squareBtn);
        sqrtBtn = findViewById(R.id.sqrtBtn);
        signBtn = findViewById(R.id.signBtn);
        backspaceBtn = findViewById(R.id.backspaceBtn);
        resultTxt = findViewById(R.id.resultTxt);
        equalsBtn = findViewById(R.id.equalsBtn);

        divisionBtn = findViewById(R.id.divisionBtn);
        multiplyBtn = findViewById(R.id.multiplyBtn);
        minusBtn = findViewById(R.id.minusBtn);
        plusBtn = findViewById(R.id.plusBtn);

        oneBtn = findViewById(R.id.oneBtn);
        twoBtn = findViewById(R.id.twoBtn);
        threeBtn = findViewById(R.id.threeBtn);
        fourBtn = findViewById(R.id.fourBtn);
        fiveBtn = findViewById(R.id.fiveBtn);
        sixBtn = findViewById(R.id.sixBtn);
        sevenBtn = findViewById(R.id.sevenBtn);
        eightBtn = findViewById(R.id.eightBtn);
        nineBtn = findViewById(R.id.nineBtn);
        zeroBtn = findViewById(R.id.zeroBtn);
    }

    private void setTextToViews() {
        squareBtn.setText(Html.fromHtml("x<sup><small>2</sup></small>"));
        sqrtBtn.setText(Html.fromHtml("&Sqrt;"));
        signBtn.setText(Html.fromHtml("&plusmn;"));
        backspaceBtn.setText(Html.fromHtml("&larr;"));
        divisionBtn.setText(Html.fromHtml("&div;"));
    }

    private void initializeButtons() {
        initializeDigits();
        initializeOperations();

        resetBtn.setOnClickListener(view -> resultTxt.setText("0"));
        backspaceBtn.setOnClickListener(view -> backspace());

        equalsBtn.setOnClickListener(view -> calculate());

        signBtn.setOnClickListener(view -> changeSign());
        openedBracket.setOnClickListener(view -> setOpenedBracket());
        closedBracket.setOnClickListener(view -> setClosedBracket());

        sqrtBtn.setOnClickListener(view -> setSqrt());
        squareBtn.setOnClickListener(view -> setSquare());
    }

    private void setSquare() {
        String txt = normalizeText(resultTxt.getText().toString());

        if(txt.charAt(txt.length() - 1) >= '0' && txt.charAt(txt.length() - 1) <= '9' || txt.charAt(txt.length() - 1) == ')') {
            resultTxt.setText(resultTxt.getText() + "^2");
        }
    }

    private void setSqrt() {
        String txt = resultTxt.getText().toString();

        if(txt.equals("0") || txt.equals("NaN")) {
            resultTxt.setText(Html.fromHtml("&Sqrt;").toString());
        } else if(operations.indexOf(txt.charAt(txt.length() - 1)) >= 0) {
            resultTxt.setText(resultTxt.getText() + Html.fromHtml("&Sqrt;").toString());
        }
    }

    private void changeSign() {
        String txt = resultTxt.getText().toString();

        try {
            Double.parseDouble(txt);
            resultTxt.setText(txt.charAt(0) != '-' ? "-" + txt : txt.substring(1));
        } catch (Exception ex) {
        }
    }

    private void backspace() {
        String txt = resultTxt.getText().toString().substring(0, resultTxt.getText().length() - 1);
        if(txt.isEmpty()) {
            resultTxt.setText("0");
        } else {
            resultTxt.setText(txt);
        }
    }

    private void calculate() {
        try {
            double res = MathExpression.calculate(normalizeText(resultTxt.getText().toString()));
            resultTxt.setText(String.valueOf(res));
        } catch(Exception ex) {
            resultTxt.setText("NaN");
        }
    }

    private void initializeOperations() {
        operationsBtn = new ArrayList<>();
        operationsBtn.add(divisionBtn);
        operationsBtn.add(multiplyBtn);
        operationsBtn.add(minusBtn);
        operationsBtn.add(plusBtn);

        for(Button btn : operationsBtn) {
            btn.setOnClickListener(view -> setOperation(btn));
        }
    }

    private void setOpenedBracket() {
        String txt = normalizeText(resultTxt.getText().toString());
        if(txt.equals("0") || txt.equals("NaN")) {
            resultTxt.setText("(");
        } else if(operations.indexOf(txt.substring(txt.length() - 1)) >= 0 ||
                txt.substring(txt.length() - 1).equals("(") ||
                txt.endsWith("q") ) {
            resultTxt.setText(resultTxt.getText() + "(");
        }
    }

    private void setClosedBracket() {
        String txt = normalizeText(resultTxt.getText().toString());
        if(operations.indexOf(txt.substring(txt.length() - 1)) < 0) {
            if(txt.chars().filter(x -> x == '(').count() > txt.chars().filter(x -> x == ')').count() && !txt.substring(txt.length() - 1).equals("(")) {
                resultTxt.setText(resultTxt.getText() + ")");
            }
        }
    }

    private void setOperation(Button btn) {
        String txt = normalizeText(resultTxt.getText().toString());

        if(txt.equals("0") && btn.getText().equals("-")) {
            input(btn);
        }

        else if(operations.indexOf(txt.substring(txt.length() - 1)) >= 0) {
            resultTxt.setText(resultTxt.getText().toString().substring(0, resultTxt.getText().length() - 1) + btn.getText());
        }

        else {
            input(btn);
        }
    }

    private void initializeDigits() {
        digits = new ArrayList<>(10);
        digits.add(oneBtn);
        digits.add(twoBtn);
        digits.add(threeBtn);
        digits.add(fourBtn);
        digits.add(fiveBtn);
        digits.add(sixBtn);
        digits.add(sevenBtn);
        digits.add(eightBtn);
        digits.add(nineBtn);
        digits.add(zeroBtn);

        for(Button btn : digits) {
            btn.setOnClickListener(view -> input(view));
        }
    }

    private String normalizeText(String text) {
        return text
                .replaceAll(Html.fromHtml("&div;").toString(), "/")
                .replaceAll(Html.fromHtml("&Sqrt;").toString(), "q")
                .replaceAll("\\^2", "s");
    }

    public void input(View view) {
        String txt = resultTxt.getText().toString();

        resultTxt.setText((txt.equals("0") || txt.equals("NaN") ? "" : txt) + ((Button)view).getText().toString());
    }
}