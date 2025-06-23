package com.example.sistemaescolar.service;

import com.example.sistemaescolar.model.Matricula;
import com.example.sistemaescolar.model.StatusPagamento;
import com.example.sistemaescolar.dto.MatriculaDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Interface para o serviço de gestão de matrículas.
 * Define as operações de negócio relacionadas a matrículas.
 */
public interface MatriculaService {

    /**
     * Realiza uma nova matrícula de um aluno em um curso.
     *
     * @param alunoId O ID do aluno (Pessoa) a ser matriculado.
     * @param cursoId O ID do curso no qual o aluno será matriculado.
     * @param valorCobrado O valor específico a ser cobrado nesta matrícula.
     * @param dataVencimento A data de vencimento para o pagamento desta matrícula.
     * @return A entidade Matricula recém-criada e salva.
     * @throws RuntimeException se o aluno ou curso não for encontrado, ou se o curso estiver inativo, ou outra regra de negócio for violada.
     */
    Matricula realizarMatricula(Long alunoId, Long cursoId, BigDecimal valorCobrado, LocalDate dataVencimento);

    /**
     * Busca uma matrícula pelo seu ID.
     *
     * @param id ID da matrícula.
     * @return Um Optional contendo a matrícula, se encontrada.
     */
    Optional<MatriculaDTO> buscarPorId(Long id);

    /**
     * Lista todas as matrículas de um aluno específico.
     *
     * @param alunoId ID do aluno.
     * @return Uma lista de matrículas do aluno.
     */
    List<MatriculaDTO> listarMatriculasPorAluno(Long alunoId);

    /**
     * Atualiza o status de pagamento de uma matrícula.
     *
     * @param id ID da matrícula.
     * @param novoStatus O novo status de pagamento.
     * @return A matrícula atualizada.
     * @throws RuntimeException se a matrícula não for encontrada.
     */
    Matricula atualizarStatusPagamento(Long id, StatusPagamento novoStatus);

    /**
     * Cancela uma matrícula.
     *
     * @param id ID da matrícula a ser cancelada.
     * @throws RuntimeException se a matrícula não for encontrada.
     */
    void cancelarMatricula(Long id);
}


