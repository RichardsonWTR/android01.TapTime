package ifsudestemg.tsi.richardson.taptime;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;

import ifsudestemg.tsi.richardson.database.Jogador;

public class ListaDeRecordes extends AppCompatActivity {
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_de_recordes);
        listView = (ListView) findViewById(R.id.listView_listaRecordes);


        //Recebendo o Intent e obtendo os dados da Activity principal
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Log.d("bundle empty", String.valueOf(bundle.isEmpty()));
        ArrayList<Jogador> jogadores = bundle.getParcelableArrayList("Jogadores");
        criarLista(jogadores);
    }

    private void criarLista(ArrayList<Jogador> jogadores){
        ArrayAdapter<Jogador> adapter;
        adapter = new ArrayAdapter<Jogador>(this,
                android.R.layout.simple_list_item_1,jogadores);
        listView.setAdapter(adapter);
    }
}
