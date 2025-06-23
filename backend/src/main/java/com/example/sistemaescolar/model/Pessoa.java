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

import java.time.LocalDate;

@Entity // JPA: Marca como entidade
@Table(name = "pessoas") // JPA: Nome da tabela
@Data // Lombok: Gera getters, setters, toString(), equals() e hashCode() automaticamente.
@NoArgsConstructor // Lombok: Gera um construtor sem argumentos (necessário para JPA).
@AllArgsConstructor // Lombok: Gera um construtor com todos os argumentos.
@EqualsAndHashCode(of = {"id", "cpf"}) // Lombok: Define que equals() e hashCode() usarão apenas id e cpf (sobrescreve o padrão do @Data se necessário refinar).
public class Pessoa {

    @Id // JPA: Chave primária
    @GeneratedValue(strategy = GenerationType.IDENTITY) // JPA: Geração automática do ID
    private Long id;

    @Column(nullable = false, length = 100) // JPA: Coluna nome, não nula, tamanho 100
    private String nome;

    @Column(unique = true, length = 14) // JPA: Coluna cpf, única, tamanho 14
    private String cpf;

    @Column(name = "data_nascimento") // JPA: Coluna data_nascimento
    private LocalDate dataNascimento;

    @Column(length = 100) // JPA: Coluna email, tamanho 100
    private String email;

    @Column(length = 20) // JPA: Coluna telefone, tamanho 20
    private String telefone;

    // Observe como os getters, setters, construtores, equals, hashCode e toString
    // foram removidos. O Lombok gerará tudo isso em tempo de compilação!
}
