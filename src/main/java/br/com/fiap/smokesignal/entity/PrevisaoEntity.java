package br.com.fiap.smokesignal.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

// Única classe na aplicação que será persistida no banco de dados H2
@Entity
@Getter
@Setter
@Table(name = "PREVISOES")
public class PrevisaoEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PREVISAO")
    private Long id;

    @NotBlank(message = "O estado é obrigatório")
    @Size(max = 100, message = "O estado deve ter no máximo 100 caracteres")
    @Column(name = "ESTADO", nullable = false, length = 100)
    private String estado; 
    
    @NotNull(message = "O mês deve ser informado")
    @Min(value = 1, message = "O mês deve ser entre 1 e 12")
    @Max(value = 12, message = "O mês deve ser entre 1 e 12")
    @Column(name = "MES", nullable = false)
    private Integer mes; 
    
    @NotNull(message = "A probabilidade de incêndio deve ser informada")
    @DecimalMin(value = "0.0", inclusive = true, message = "A probabilidade não pode ser negativa")
    @DecimalMax(value = "100.0", inclusive = true, message = "A probabilidade não pode exceder 100%")
    @Column(name = "PROB_INCENDIO", precision = 5, scale = 2, nullable = false)
    private BigDecimal probabilidadeIncendio; 
    
    @NotNull(message = "A data e hora da previsão devem ser informadas")
    @Column(name = "DATA_HORA", nullable = false)
    private LocalDateTime dataHora;
}