package nelson.aparecido.estudaki;

public class MaterialAula {

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
}
