package nelson.aparecido.estudaki;

import android.os.Parcel;
import android.os.Parcelable;

public class Respostas implements Parcelable {

    private String tipo;
    private String materia;
    private String turma;
    private String alunoID;
    private String url;
    private long timestamp;
    private String nota;
    private String tituloProvaAtividade;

    public Respostas() {
    }

    public Respostas(String tipo, String materia, String turma, String alunoID, String url, long timestamp, String nota, String tituloProvaAtividade) {
        this.tipo = tipo;
        this.materia = materia;
        this.turma = turma;
        this.alunoID = alunoID;
        this.url = url;
        this.timestamp = timestamp;
        this.nota = nota;
        this.tituloProvaAtividade = tituloProvaAtividade;
    }

    protected Respostas(Parcel in) {
        tipo = in.readString();
        materia = in.readString();
        turma = in.readString();
        alunoID = in.readString();
        url = in.readString();
        timestamp = in.readLong();
        nota = in.readString();
        tituloProvaAtividade = in.readString();
    }

    public static final Creator<Respostas> CREATOR = new Creator<Respostas>() {
        @Override
        public Respostas createFromParcel(Parcel in) {
            return new Respostas(in);
        }

        @Override
        public Respostas[] newArray(int size) {
            return new Respostas[size];
        }
    };

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public String getTurma() {
        return turma;
    }

    public void setTurma(String turma) {
        this.turma = turma;
    }

    public String getAlunoID() {
        return alunoID;
    }

    public void setAlunoID(String alunoID) {
        this.alunoID = alunoID;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public String getTituloProvaAtividade() {
        return tituloProvaAtividade;
    }

    public void setTituloProvaAtividade(String tituloProvaAtividade) {
        this.tituloProvaAtividade = tituloProvaAtividade;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tipo);
        dest.writeString(materia);
        dest.writeString(turma);
        dest.writeString(alunoID);
        dest.writeString(url);
        dest.writeLong(timestamp);
        dest.writeString(nota);
        dest.writeString(tituloProvaAtividade);
    }
}
