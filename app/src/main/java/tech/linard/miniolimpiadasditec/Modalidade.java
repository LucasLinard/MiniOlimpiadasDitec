package tech.linard.miniolimpiadasditec;

/**
 * Created by lucas on 08/04/17.
 */

public class Modalidade {
    private int modalidadeId;
    private String name;

    public Modalidade(int modalidadeId, String name) {
        this.modalidadeId = modalidadeId;
        this.name = name;
    }

    public Modalidade() {

    }

    public int getModalidadeId() {
        return modalidadeId;
    }

    public void setModalidadeId(int modalidadeId) {
        this.modalidadeId = modalidadeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
