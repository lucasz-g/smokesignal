package br.com.fiap.smokesignal.entity.DTO;

// PrevisaoResponseDTO.java
public class PrevisaoResponseDTO {
    private String estado;
    private int mes;
    private double probabilidade;

    public PrevisaoResponseDTO(String estado, int mes, double probabilidade) {
        this.estado = estado;
        this.mes = mes;
        this.probabilidade = probabilidade;
    }

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

    public double getProbabilidade() {
        return probabilidade;
    }

    public void setProbabilidade(double probabilidade) {
        this.probabilidade = probabilidade;
    }

    
}
