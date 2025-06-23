package com.example.sistemaescolar.service;

import com.example.sistemaescolar.model.Pessoa;

import java.util.List;
import java.util.Optional;

/**
 * Interface para o serviço de gestão de pessoas.
 * Define as operações de negócio relacionadas a pessoas (alunos).
 */
public interface PessoaService {

    /**
     * Salva uma nova pessoa no sistema ou atualiza uma existente.
     *
     * @param pessoa A entidade Pessoa a ser salva ou atualizada.
     * @return A entidade Pessoa salva, com ID gerado se for uma nova pessoa.
     * @throws RuntimeException se houver erro de validação (ex: CPF duplicado).
     */
    Pessoa salvar(Pessoa pessoa);

    /**
     * Busca uma pessoa pelo seu ID.
     *
     * @param id O ID da pessoa a ser buscada.
     * @return Um Optional contendo a pessoa encontrada, ou vazio se não encontrada.
     */
    Optional<Pessoa> buscarPorId(Long id);

    /**
     * Busca uma pessoa pelo seu CPF.
     *
     * @param cpf O CPF a ser buscado.
     * @return Um Optional contendo a pessoa encontrada, ou vazio se não encontrada.
     */
    Optional<Pessoa> buscarPorCpf(String cpf);

    /**
     * Lista todas as pessoas cadastradas no sistema.
     *
     * @return Uma lista de todas as pessoas.
     */
    List<Pessoa> listarTodos();

    /**
     * Exclui uma pessoa pelo seu ID.
     *
     * @param id O ID da pessoa a ser excluída.
     * @throws RuntimeException se a pessoa não for encontrada ou se houver restrições (ex: matrículas ativas).
     */
    void excluir(Long id);
}
