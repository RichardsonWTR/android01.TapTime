package ifsudestemg.tsi.richardson.taptime;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Parcelable;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import ifsudestemg.tsi.richardson.database.Database;
import ifsudestemg.tsi.richardson.database.Jogador;

public class MainActivity extends AppCompatActivity {
    private List<Integer> inativos;
    private int movimentos;
    private List<Integer> ordem;
    private ProgressBar progressBar;
    private Vibrator vibrator;
    private TextView textViewAttempts, textViewRecord;
    private EditText editTextNomeDoJogador;
    private Button restartBtn;
    private Button recordesBtn;
    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        vibrator =  (Vibrator) getSystemService(VIBRATOR_SERVICE);
        textViewAttempts = (TextView) findViewById(R.id.textView_attempts);
        restartBtn = (Button) findViewById(R.id.button_restart);
        editTextNomeDoJogador = (EditText) findViewById(R.id.editText_NomeJogador);
        textViewRecord= (TextView) findViewById(R.id.textView_Record);
        recordesBtn = (Button) findViewById(R.id.button_Recordes);

        editTextNomeDoJogador.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if(editTextNomeDoJogador.getText().toString().trim().equals("")){
                    Toast.makeText(getApplicationContext(), "Forneça um nome", Toast.LENGTH_SHORT).show();
                }else
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    salvarDados();
                    editTextNomeDoJogador.setVisibility(View.INVISIBLE);
                    textViewRecord.setVisibility(View.INVISIBLE);
                    editTextNomeDoJogador.setText("");
                    //handled = true;

                }
                return handled;
            }
        });
        start();

        db = new Database(this);
        for(Jogador j : db.all())
            Log.d("Jogador: ", j.toString());

    }//onCreate

    private void start() {
        inativos = new ArrayList<>();
        ordem = getNumbers();
        Log.d("Números corretos: ", ordem.toString());
        movimentos = 0;
        setButtonColors();
        progressBar.setProgress(0);
        //System.out.println("Numeros: " + ordem.toString());
        restartBtn.setVisibility(View.INVISIBLE);
        textViewAttempts.setVisibility(View.INVISIBLE);
    }

    public void restart(View view){

        findViewById(R.id.textView_congratulations).setVisibility(View.INVISIBLE);
        for (Integer n : inativos) {
            findViewById(n).setVisibility(View.VISIBLE);
        }
        inativos = new ArrayList<>();
        ordem = getNumbers();
        setButtonColors();
        progressBar.setProgress(0);
        findViewById(R.id.layout).setBackgroundColor(0xFFFFFFFF);
        restartBtn.setVisibility(View.INVISIBLE);
        textViewAttempts.setVisibility(View.INVISIBLE);
        editTextNomeDoJogador.setVisibility(View.INVISIBLE);
        textViewRecord.setVisibility(View.INVISIBLE);
        movimentos = 0;
    }

    public void tentativa(View view) {
        Button button = (Button) view;
        int number = Integer.parseInt(button.getText().toString());

        movimentos++;

        if (number == ordem.get(inativos.size())) { // Botao certo clicaodEsconder botao

            inativos.add(button.getId());
            view.setVisibility(View.INVISIBLE);
            findViewById(R.id.layout).setBackgroundColor(((ColorDrawable)view.getBackground()).getColor());
            vibrator.vibrate(50);
            progressBar.setProgress(100/6 * inativos.size());

            if(inativos.size() == 6){ // Fim de jogo!
                fimDeJogo();
            }

        } else { // Botao errado clicado

            for (Integer n : inativos) {
                findViewById(n).setVisibility(View.VISIBLE);
            }
            inativos = new ArrayList<>();
            findViewById(R.id.layout).setBackgroundColor(0xFFFFFFFF);

            progressBar.setProgress(0);
        }
    }//tentativa

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

    private void salvarDados(){
        Toast.makeText(MainActivity.this, "Recorde salvo!",Toast.LENGTH_SHORT).show();
        db.insert(new Jogador(editTextNomeDoJogador.getText().toString(),movimentos));
    }

    private void fimDeJogo(){
        findViewById(R.id.textView_congratulations).setVisibility(View.VISIBLE);
        progressBar.setProgress(100);

        textViewAttempts.setText(String.format(Locale.ENGLISH,"Finalizado com %d movimentos!", movimentos));
        textViewAttempts.setVisibility(View.VISIBLE);

        textViewRecord.setVisibility(View.VISIBLE);
        editTextNomeDoJogador.setVisibility(View.VISIBLE);

        restartBtn.setVisibility(View.VISIBLE);

        findViewById(R.id.layout).setBackgroundColor(0xFFFFFFFF);

        long[] pattern = {0,200,200,200};
        vibrator.vibrate(pattern,-1);
    }

    public void irParaActivityRecordes(View view){
        Intent intent = new Intent(this,ListaDeRecordes.class);
        Bundle bundle = new Bundle();

        List<Jogador> list =  db.all();

        Collections.sort(list, new Comparator<Jogador>() {
            @Override
            public int compare(Jogador j1, Jogador j2) {
                if (j1.getPontuacao() < j2.getPontuacao()) return -1;
                if (j1.getPontuacao() == j2.getPontuacao()) return 0;
                return 1;
            }
        });

        bundle.putParcelableArrayList("Jogadores", (ArrayList<? extends Parcelable>) list);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}//MainActivity
