package com.example.sistemaescolar.repository;

import com.example.sistemaescolar.model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório para a entidade Pessoa.
 * Estende JpaRepository para fornecer operações CRUD básicas e mais.
 */
@Repository // Indica ao Spring que esta interface é um componente Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

    /**
     * Busca uma pessoa pelo seu CPF.
     * O Spring Data JPA implementa este método automaticamente baseado no nome.
     * Retorna um Optional para lidar com o caso de não encontrar a pessoa.
     *
     * @param cpf O CPF a ser buscado.
     * @return Um Optional contendo a Pessoa encontrada, ou vazio se não encontrada.
     */
    Optional<Pessoa> findByCpf(String cpf);

    // O JpaRepository<Pessoa, Long> já fornece métodos como:
    // - save(Pessoa pessoa): Salva ou atualiza uma pessoa.
    // - findById(Long id): Busca uma pessoa pelo ID.
    // - findAll(): Busca todas as pessoas.
    // - deleteById(Long id): Deleta uma pessoa pelo ID.
    // - count(): Conta o número total de pessoas.
    // - existsById(Long id): Verifica se uma pessoa com o ID existe.
    // ... e muitos outros!
}
