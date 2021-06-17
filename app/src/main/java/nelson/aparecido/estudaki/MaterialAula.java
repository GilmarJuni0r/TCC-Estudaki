package nelson.aparecido.estudaki;

import android.os.Parcel;
import android.os.Parcelable;

public class MaterialAula implements Parcelable {

    private String  tipoArquivo;
    private String materia;
    private String turma;
    private String titulo;
    private String descricao;
    private String url;
    private long timestamp;

    public MaterialAula() {
    }

    public MaterialAula(String tipoArquivo, String materia, String turma, String titulo, String descricao, String url, long timestamp) {
        this.tipoArquivo = tipoArquivo;
        this.materia = materia;
        this.turma = turma;
        this.titulo = titulo;
        this.descricao = descricao;
        this.url = url;
        this.timestamp = timestamp;
    }

    protected MaterialAula(Parcel in) {
        tipoArquivo = in.readString();
        materia = in.readString();
        turma = in.readString();
        titulo = in.readString();
        descricao = in.readString();
        url = in.readString();
        timestamp = in.readLong();
    }

    public static final Creator<MaterialAula> CREATOR = new Creator<MaterialAula>() {
        @Override
        public MaterialAula createFromParcel(Parcel in) {
            return new MaterialAula(in);
        }

        @Override
        public MaterialAula[] newArray(int size) {
            return new MaterialAula[size];
        }
    };

    public String getTipoArquivo() {
        return tipoArquivo;
    }

    public void setTipoArquivo(String tipoArquivo) {
        this.tipoArquivo = tipoArquivo;
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

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tipoArquivo);
        dest.writeString(materia);
        dest.writeString(turma);
        dest.writeString(titulo);
        dest.writeString(descricao);
        dest.writeString(url);
        dest.writeLong(timestamp);
    }
}
