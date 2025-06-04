package br.com.fiap.smokesignal.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocalidadeEntity {
    private Long id; 
    private String estado;
    private double latitude;
    private double longitude; 
}
