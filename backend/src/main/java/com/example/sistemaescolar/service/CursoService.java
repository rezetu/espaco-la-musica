package com.example.sistemaescolar.service;

import com.example.sistemaescolar.model.Curso;

import java.util.List;
import java.util.Optional;

/**
 * Interface para o serviço de gestão de cursos.
 * Define as operações de negócio relacionadas a cursos.
 */
public interface CursoService {

    /**
     * Salva um novo curso no sistema ou atualiza um existente.
     *
     * @param curso A entidade Curso a ser salva ou atualizada.
     * @return A entidade Curso salva, com ID gerado se for um novo curso.
     */
    Curso salvar(Curso curso);

    /**
     * Busca um curso pelo seu ID.
     *
     * @param id O ID do curso a ser buscado.
     * @return Um Optional contendo o curso encontrado, ou vazio se não encontrado.
     */
    Optional<Curso> buscarPorId(Long id);

    /**
     * Lista todos os cursos cadastrados no sistema.
     *
     * @return Uma lista de todos os cursos.
     */
    List<Curso> listarTodos();

    /**
     * Lista apenas os cursos que estão ativos.
     *
     * @return Uma lista de cursos ativos.
     */
    List<Curso> listarAtivos();

    /**
     * Ativa ou inativa um curso.
     *
     * @param id O ID do curso a ser ativado/inativado.
     * @param ativo true para ativar, false para inativar.
     * @return O curso atualizado.
     * @throws RuntimeException se o curso não for encontrado.
     */
    Curso alterarStatus(Long id, boolean ativo);

    /**
     * Exclui um curso pelo seu ID.
     *
     * @param id O ID do curso a ser excluído.
     * @throws RuntimeException se o curso não for encontrado ou se houver restrições (ex: matrículas existentes).
     */
    void excluir(Long id);
}
