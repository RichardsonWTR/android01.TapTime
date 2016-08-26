// Armazenando utilizando SQLite
package ifsudestemg.tsi.richardson.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by richardson on 8/26/16.
 */
public class Database {
    private static final String DATABASE_NAME = "bd1";

    // Somente esta aplicação terá acesso ao BD
    private static final int DATABASE_ACCESS = 0;

    // Consultas SQL

    private static final String NOME_TABELA = "jogadores";
    private static final String SQL_STRUCT = "CREATE TABLE IF NOT " +
            "EXISTS " + NOME_TABELA + "(" +
            "id_ INTEGER PRIMARY KEY AUTOINCREMENT," +
            "nome TEXT NOT NULL," +
            "pontuacao INTEGER NOT NULL);";
    private static final String SQL_INSERT = "INSERT INTO " + NOME_TABELA +
            " (nome, pontuacao) VALUES ('%s', '%d');";
    private static final String SQL_SELECT_ALL = "SELECT * FROM " + NOME_TABELA +
            " ORDER BY nome;";
    private static final String SQL_CLEAR = "DROP TABLE IF EXISTS " + NOME_TABELA + ";";

    private SQLiteDatabase database;
    private Cursor cursor;
    private int indexID, indexNome, indexPontuacao;

    // Construtor
    public Database(Context context){
        // Utiliza o contexto da activity que vai manipular o BD
        database = context.openOrCreateDatabase(DATABASE_NAME, DATABASE_ACCESS, null);
        database.execSQL(SQL_STRUCT);
    }

    public void clear(){
        database.execSQL(SQL_CLEAR);
    }

    public void close(){
        database.close();
    }

    public void insert(Jogador jogador){
        String query = String.format(SQL_INSERT, jogador.getNome(), jogador.getPontuacao());
        database.execSQL(query);
    }

    public List<Jogador> all(){
        List<Jogador> jogadores = new ArrayList<Jogador>();
        Jogador jogador;

        cursor = database.rawQuery(SQL_SELECT_ALL,null);

        if(cursor.moveToFirst()){
            indexID = cursor.getColumnIndex("id_");
            indexNome = cursor.getColumnIndex("nome");
            indexPontuacao = cursor.getColumnIndex("pontuacao");

            do{
                jogador = new Jogador(cursor.getString(indexNome),cursor.getInt(indexPontuacao));
                jogadores.add(jogador);
            }while(cursor.moveToNext());

            cursor.close();
        }
        return jogadores;
    }//all()
}// class
