package tech.linard.miniolimpiadasditec;

/**
 * Created by lucas on 08/04/17.
 */

public class SubModalidade {
    private int idSubModalidade;
    private int idModalidade;
    private String descricao;
    private boolean feminino;
    private boolean masculino;
    private boolean individual;
    private boolean dupla;
    private boolean equipe;
    private int[] teams;

    public SubModalidade() {
    }

    public int getIdSubModalidade() {
        return idSubModalidade;
    }

    public void setIdSubModalidade(int idSubModalidade) {
        this.idSubModalidade = idSubModalidade;
    }

    public int getIdModalidade() {
        return idModalidade;
    }

    public void setIdModalidade(int idModalidade) {
        this.idModalidade = idModalidade;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isFeminino() {
        return feminino;
    }

    public void setFeminino(boolean feminino) {
        this.feminino = feminino;
    }

    public boolean isMasculino() {
        return masculino;
    }

    public void setMasculino(boolean masculino) {
        this.masculino = masculino;
    }

    public boolean isIndividual() {
        return individual;
    }

    public void setIndividual(boolean individual) {
        this.individual = individual;
    }

    public boolean isDupla() {
        return dupla;
    }

    public void setDupla(boolean dupla) {
        this.dupla = dupla;
    }

    public boolean isEquipe() {
        return equipe;
    }

    public void setEquipe(boolean equipe) {
        this.equipe = equipe;
    }

    public int[] getTeams() {
        return teams;
    }

    public void setTeams(int[] teams) {
        this.teams = teams;
    }
}
