package tech.linard.miniolimpiadasditec;

import java.util.Map;

/**
 * Created by lucas on 10/04/17.
 */

public class Equipe {
    private String UID;
    private int modalidadeId;
    private int submodalidadeId;
    private Map<String,Boolean> participantes;


    public Equipe() {
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public int getModalidadeId() {
        return modalidadeId;
    }

    public void setModalidadeId(int modalidadeId) {
        this.modalidadeId = modalidadeId;
    }

    public int getSubmodalidadeId() {
        return submodalidadeId;
    }

    public void setSubmodalidadeId(int submodalidadeId) {
        this.submodalidadeId = submodalidadeId;
    }

    public Map<String, Boolean> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(Map<String, Boolean> participantes) {
        this.participantes = participantes;
    }
}
