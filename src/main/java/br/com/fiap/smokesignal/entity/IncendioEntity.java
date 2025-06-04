package br.com.fiap.smokesignal.entity;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IncendioEntity {
    private Long id; 
    private Date dataIncendio; 
    private String causa; 
    private Double areaQueimada; 
    private LocalidadeEntity local; 
}