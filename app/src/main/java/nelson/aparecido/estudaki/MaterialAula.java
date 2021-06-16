package nelson.aparecido.estudaki;

import android.net.Uri;

import com.google.android.gms.tasks.Task;

public class MaterialAula {

    private String  tipoArquivo;
    private String materia;
    private String turma;
    private String titulo;
    private String descricao;
    private String url;

    public MaterialAula() {
    }

    public MaterialAula(String tipoArquivo, String materia, String turma, String titulo, String descricao, String url) {
        this.tipoArquivo = tipoArquivo;
        this.materia = materia;
        this.turma = turma;
        this.titulo = titulo;
        this.descricao = descricao;
        this.url = url;
    }

    public String getTipoArquivo() {
        return tipoArquivo;
    }

    public String getMateria() {
        return materia;
    }

    public String getTurma() {
        return turma;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getUrl() {
        return url;
    }
}
