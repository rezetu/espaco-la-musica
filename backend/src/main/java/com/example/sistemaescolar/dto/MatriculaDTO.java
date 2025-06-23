package com.example.sistemaescolar.dto;

import com.example.sistemaescolar.model.StatusPagamento;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatriculaDTO {
    private Long id;
    private PessoaDTO aluno;
    private CursoDTO curso;
    private LocalDate dataMatricula;
    private BigDecimal valorCobrado;
    private StatusPagamento statusPagamento;
    private LocalDate dataVencimento;
}


