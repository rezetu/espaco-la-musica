package com.example.sistemaescolar.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CursoDTO {
    private Long id;
    private String nome;
    private String descricao;
    private BigDecimal valor;
    private Integer cargaHoraria;
    private boolean ativo;
}


