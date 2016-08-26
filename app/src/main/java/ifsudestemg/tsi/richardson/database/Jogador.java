package ifsudestemg.tsi.richardson.database;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by richardson on 8/26/16.
 */
public class Jogador implements Parcelable{
    private String nome;
    private int pontuacao;

    public Jogador(){
        pontuacao = 0;
    }

    public Jogador(String nome, int pontuacao){
        this.nome = nome;
        this.pontuacao = pontuacao;
    }

    public Jogador(Parcel parcel){
        this.nome = parcel.readString();
        this.pontuacao = parcel.readInt();
    }

    @Override
    public String toString() {
        return nome + ", " + pontuacao + " movimentos";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(nome);
        parcel.writeInt(pontuacao);
    }

    static final Parcelable.Creator<Jogador> CREATOR
            = new Parcelable.Creator<Jogador>() {

        public Jogador createFromParcel(Parcel in) {
            return new Jogador(in);
        }

        public Jogador[] newArray(int size) {
            return new Jogador[size];
        }
    };

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(int pontuacao) {
        this.pontuacao = pontuacao;
    }
}//class
