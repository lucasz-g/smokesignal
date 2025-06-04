package br.com.fiap.smokesignal.entity;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter
public class UsuarioEntity {
    private Long id;    
    private String nome;
    private String email;

    @Enumerated(EnumType.STRING)
    private TipoUsuarioEnum tipo;
    
}