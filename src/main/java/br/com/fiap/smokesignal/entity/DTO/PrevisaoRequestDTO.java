package br.com.fiap.smokesignal.entity.DTO;

public class PrevisaoRequestDTO {
    private String estado;
    private int mes;
    
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public int getMes() {
        return mes;
    }
    public void setMes(int mes) {
        this.mes = mes;
    }

    
}
