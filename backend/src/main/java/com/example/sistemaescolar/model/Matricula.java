package com.example.sistemaescolar.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "matriculas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id") // Igualdade baseada apenas no ID da matrícula
public class Matricula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // Muitos para Um: Muitas matrículas podem pertencer a uma Pessoa (aluno).
    @JoinColumn(name = "pessoa_id", nullable = false) // Define a coluna de chave estrangeira e que não pode ser nula.
    private Pessoa aluno;

    @ManyToOne(fetch = FetchType.LAZY) // Muitos para Um: Muitas matrículas podem pertencer a um Curso.
    @JoinColumn(name = "curso_id", nullable = false) // Define a coluna de chave estrangeira e que não pode ser nula.
    private Curso curso;

    @Column(name = "data_matricula", nullable = false)
    private LocalDate dataMatricula;

    @Column(name = "valor_cobrado", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorCobrado;

    @Enumerated(EnumType.STRING) // Salva o Enum como String ("PENDENTE", "PAGO", "ATRASADO") no banco.
    @Column(name = "status_pagamento", nullable = false)
    private StatusPagamento statusPagamento;

    @Column(name = "data_vencimento") // Pode ser nulo se o pagamento for único e já pago?
    private LocalDate dataVencimento;

    // Lombok gera getters, setters, toString, etc.
    // FetchType.LAZY é uma otimização: os dados do Aluno e Curso só serão carregados do banco
    // quando você explicitamente acessá-los (ex: matricula.getAluno().getNome()),
    // evitando carregar dados desnecessários em consultas de matrículas.
}
