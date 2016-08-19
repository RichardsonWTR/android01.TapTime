package ifsudestemg.tsi.richardson.taptime;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Integer> inativos;
    private int movimento;
    private List<Integer> ordem;
    private List<Integer> cores;
    private ProgressBar progressBar;
    private Vibrator vibrator;
    private TextView textViewAttempts;
    private Button restart_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        vibrator =  (Vibrator) getSystemService(VIBRATOR_SERVICE);
        textViewAttempts = (TextView) findViewById(R.id.textView_attempts);
        restart_btn = (Button) findViewById(R.id.button_restart);
        start();
    }

    private void start() {
        inativos = new ArrayList<Integer>();
        ordem = getNumbers();
        movimento = 0;
        cores = setButtonColors();
        progressBar.setProgress(0);
        //System.out.println("Numeros: " + ordem.toString());
        restart_btn.setVisibility(View.INVISIBLE);
        textViewAttempts.setVisibility(View.INVISIBLE);

    }

    public void restart(View view){

        findViewById(R.id.textView_congratulations).setVisibility(View.INVISIBLE);
        for (Integer n : inativos) {
            findViewById(n).setVisibility(View.VISIBLE);
        }
        inativos = new ArrayList<Integer>();
        ordem = getNumbers();
        cores = setButtonColors();
        progressBar.setProgress(0);
        findViewById(R.id.layout).setBackgroundColor(0xFFFFFFFF);
        restart_btn.setVisibility(View.INVISIBLE);
        textViewAttempts.setVisibility(View.INVISIBLE);
    }

    public void attempt(View view) {
        Button button = (Button) view;
        int number = Integer.parseInt(button.getText().toString());

        if (number == ordem.get(inativos.size())) { // Botao certo clicaodEsconder botao

            inativos.add(button.getId());
            view.setVisibility(View.INVISIBLE);

            findViewById(R.id.layout).setBackgroundColor(((ColorDrawable)view.getBackground()).getColor());
            movimento++;

            vibrator.vibrate(50);

            progressBar.setProgress(100/6 * inativos.size());

            if(inativos.size() == 6){ // Congratulations!
                findViewById(R.id.textView_congratulations).setVisibility(View.VISIBLE);
                progressBar.setProgress(100);

                textViewAttempts.setText("Finalizado com " + movimento + " movimentos!");
                textViewAttempts.setVisibility(View.VISIBLE);

                restart_btn.setVisibility(View.VISIBLE);

                long[] pattern = {0,200,200,200};
                vibrator.vibrate(pattern,-1);
            }

        } else { // Botao errado clicado

            for (Integer n : inativos) {
                findViewById(n).setVisibility(View.VISIBLE);
            }
            inativos = new ArrayList<Integer>();
            findViewById(R.id.layout).setBackgroundColor(0xFFFFFFFF);

            progressBar.setProgress(0);
        }

    }

    private List<Integer> getNumbers() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);
        Collections.shuffle(numbers);
        return numbers;
    }

    private List<Integer> getCores() {
        List<Integer> colors = Arrays.asList(0xFFe5e500, 0xFF757575, 0xFFE64A19, 0xFF4CAF50, 0xFF1E90FF, 0xFF607D8B);
        Collections.shuffle(colors);
        return colors;
    }

    private List<Integer> setButtonColors(){
        List<Integer> colors = getCores();

        findViewById(R.id.button_1).setBackgroundColor(colors.get(0));
        findViewById(R.id.button_2).setBackgroundColor(colors.get(1));
        findViewById(R.id.button_3).setBackgroundColor(colors.get(2));
        findViewById(R.id.button_4).setBackgroundColor(colors.get(3));
        findViewById(R.id.button_5).setBackgroundColor(colors.get(4));
        findViewById(R.id.button_6).setBackgroundColor(colors.get(5));
        return colors;
    }
}
