package com.example.sistemaescolar.repository;

import com.example.sistemaescolar.model.Matricula;
import com.example.sistemaescolar.model.StatusPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repositório para a entidade Matricula.
 * Estende JpaRepository para fornecer operações CRUD básicas e mais.
 */
@Repository // Indica ao Spring que esta interface é um componente Repository
public interface MatriculaRepository extends JpaRepository<Matricula, Long> {

    /**
     * Busca todas as matrículas de um aluno específico, usando o ID do aluno.
     *
     * @param alunoId O ID da Pessoa (aluno).
     * @return Uma lista de matrículas pertencentes ao aluno.
     */
    List<Matricula> findByAlunoId(Long alunoId);

    /**
     * Busca todas as matrículas de um curso específico, usando o ID do curso.
     *
     * @param cursoId O ID do Curso.
     * @return Uma lista de matrículas pertencentes ao curso.
     */
    List<Matricula> findByCursoId(Long cursoId);

    /**
     * Busca todas as matrículas com um determinado status de pagamento.
     *
     * @param status O StatusPagamento a ser buscado (PENDENTE, PAGO, ATRASADO).
     * @return Uma lista de matrículas com o status especificado.
     */
    List<Matricula> findByStatusPagamento(StatusPagamento status);

    /**
     * Busca todas as matrículas com data de vencimento antes de uma data específica
     * e com status de pagamento PENDENTE (útil para lembretes de pagamento).
     *
     * @param dataLimite A data limite para o vencimento.
     * @return Uma lista de matrículas pendentes e vencidas até a data limite.
     */
    List<Matricula> findByDataVencimentoBeforeAndStatusPagamento(LocalDate dataLimite, StatusPagamento status);

    /**
     * Verifica se já existe uma matrícula para um aluno específico em um curso específico.
     *
     * @param alunoId O ID do aluno (Pessoa).
     * @param cursoId O ID do curso.
     * @return true se a matrícula já existe, false caso contrário.
     */
    boolean existsByAlunoIdAndCursoId(Long alunoId, Long cursoId);
    
    /**
     * Verifica se existem matrículas para um curso específico.
     * Usado para validar se um curso pode ser excluído.
     *
     * @param cursoId O ID do curso.
     * @return true se existem matrículas para o curso, false caso contrário.
     */
    boolean existsByCursoId(Long cursoId);

    // JpaRepository<Matricula, Long> já fornece métodos como:
    // - save(Matricula matricula): Salva ou atualiza uma matrícula.
    // - findById(Long id): Busca uma matrícula pelo ID.
    // - findAll(): Busca todas as matrículas.
    // - deleteById(Long id): Deleta uma matrícula pelo ID.
    // ... e muitos outros!
}
