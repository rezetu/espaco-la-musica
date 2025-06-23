package com.example.sistemaescolar.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

@Entity
@Table(name = "cursos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id") // Baseia equals/hashCode apenas no ID para cursos
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(length = 500) // Permite uma descrição um pouco mais longa
    private String descricao;

    @Column(nullable = false, precision = 10, scale = 2) // precision=total de dígitos, scale=dígitos após a vírgula
    private BigDecimal valor;

    @Column(name = "carga_horaria")
    private Integer cargaHoraria; // Em horas

    @Column(nullable = false)
    private boolean ativo = true; // Valor padrão true ao criar um novo curso

    // Lombok gera getters, setters, toString, etc.
    // O campo 'ativo' com valor padrão true é inicializado aqui.
    // Se precisar de um construtor que não inclua o 'id' (útil para criar novos cursos antes de salvar),
    // você pode adicioná-lo manualmente ou usar uma biblioteca como MapStruct para DTOs.
}
