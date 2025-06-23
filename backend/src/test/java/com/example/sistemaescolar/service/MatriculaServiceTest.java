package com.example.sistemaescolar.service;

import com.example.sistemaescolar.dto.MatriculaDTO;
import com.example.sistemaescolar.model.Curso;
import com.example.sistemaescolar.model.Matricula;
import com.example.sistemaescolar.model.Pessoa;
import com.example.sistemaescolar.model.StatusPagamento;
import com.example.sistemaescolar.repository.CursoRepository;
import com.example.sistemaescolar.repository.MatriculaRepository;
import com.example.sistemaescolar.repository.PessoaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatriculaServiceTest {

    @Mock
    private PessoaRepository pessoaRepository;

    @Mock
    private CursoRepository cursoRepository;

    @Mock
    private MatriculaRepository matriculaRepository;

    @InjectMocks
    private MatriculaServiceImpl matriculaService;

    private Pessoa aluno;
    private Curso curso;
    private Matricula matricula;

    @BeforeEach
    void setUp() {
        aluno = new Pessoa();
        aluno.setId(1L);
        aluno.setNome("Aluno Teste");
        aluno.setCpf("11122233344");

        curso = new Curso();
        curso.setId(1L);
        curso.setNome("Curso Teste");
        curso.setAtivo(true);
        curso.setValor(new BigDecimal("1000.00"));

        matricula = new Matricula();
        matricula.setId(1L);
        matricula.setAluno(aluno);
        matricula.setCurso(curso);
        matricula.setValorCobrado(new BigDecimal("1000.00"));
        matricula.setDataMatricula(LocalDate.now());
        matricula.setDataVencimento(LocalDate.now().plusMonths(1));
        matricula.setStatusPagamento(StatusPagamento.PENDENTE);
    }

    @Test
    @DisplayName("Deve realizar matrícula com sucesso")
    void deveRealizarMatriculaComSucesso() {
        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(aluno));
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));
        when(matriculaRepository.findByAlunoIdAndCursoId(1L, 1L)).thenReturn(Optional.empty());
        when(matriculaRepository.save(any(Matricula.class))).thenReturn(matricula);

        Matricula result = matriculaService.realizarMatricula(1L, 1L, new BigDecimal("1000.00"), LocalDate.now().plusMonths(1));

        assertNotNull(result);
        assertEquals(aluno.getNome(), result.getAluno().getNome());
        assertEquals(curso.getNome(), result.getCurso().getNome());
        verify(pessoaRepository, times(1)).findById(1L);
        verify(cursoRepository, times(1)).findById(1L);
        verify(matriculaRepository, times(1)).findByAlunoIdAndCursoId(1L, 1L);
        verify(matriculaRepository, times(1)).save(any(Matricula.class));
    }

    @Test
    @DisplayName("Deve lançar exceção se aluno não encontrado ao matricular")
    void deveLancarExcecaoSeAlunoNaoEncontradoAoMatricular() {
        when(pessoaRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            matriculaService.realizarMatricula(1L, 1L, new BigDecimal("1000.00"), LocalDate.now().plusMonths(1));
        });

        assertEquals("Aluno não encontrado com ID: 1", exception.getMessage());
        verify(pessoaRepository, times(1)).findById(1L);
        verify(cursoRepository, never()).findById(anyLong());
        verify(matriculaRepository, never()).save(any(Matricula.class));
    }

    @Test
    @DisplayName("Deve lançar exceção se curso não encontrado ao matricular")
    void deveLancarExcecaoSeCursoNaoEncontradoAoMatricular() {
        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(aluno));
        when(cursoRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            matriculaService.realizarMatricula(1L, 1L, new BigDecimal("1000.00"), LocalDate.now().plusMonths(1));
        });

        assertEquals("Curso não encontrado com ID: 1", exception.getMessage());
        verify(pessoaRepository, times(1)).findById(1L);
        verify(cursoRepository, times(1)).findById(1L);
        verify(matriculaRepository, never()).save(any(Matricula.class));
    }

    @Test
    @DisplayName("Deve lançar exceção se curso inativo ao matricular")
    void deveLancarExcecaoSeCursoInativoAoMatricular() {
        curso.setAtivo(false);
        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(aluno));
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            matriculaService.realizarMatricula(1L, 1L, new BigDecimal("1000.00"), LocalDate.now().plusMonths(1));
        });

        assertEquals("Curso inativo. Não é possível realizar matrícula.", exception.getMessage());
        verify(pessoaRepository, times(1)).findById(1L);
        verify(cursoRepository, times(1)).findById(1L);
        verify(matriculaRepository, never()).save(any(Matricula.class));
    }

    @Test
    @DisplayName("Deve lançar exceção se aluno já matriculado no curso")
    void deveLancarExcecaoSeAlunoJaMatriculadoNoCurso() {
        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(aluno));
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));
        when(matriculaRepository.findByAlunoIdAndCursoId(1L, 1L)).thenReturn(Optional.of(matricula));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            matriculaService.realizarMatricula(1L, 1L, new BigDecimal("1000.00"), LocalDate.now().plusMonths(1));
        });

        assertEquals("Aluno já matriculado neste curso.", exception.getMessage());
        verify(pessoaRepository, times(1)).findById(1L);
        verify(cursoRepository, times(1)).findById(1L);
        verify(matriculaRepository, times(1)).findByAlunoIdAndCursoId(1L, 1L);
        verify(matriculaRepository, never()).save(any(Matricula.class));
    }

    @Test
    @DisplayName("Deve buscar matrícula por ID com sucesso e retornar DTO")
    void deveBuscarMatriculaPorIdComSucessoERetornarDTO() {
        when(matriculaRepository.findById(1L)).thenReturn(Optional.of(matricula));

        Optional<MatriculaDTO> foundMatriculaDTO = matriculaService.buscarPorId(1L);

        assertTrue(foundMatriculaDTO.isPresent());
        assertEquals(matricula.getId(), foundMatriculaDTO.get().getId());
        assertEquals(matricula.getAluno().getNome(), foundMatriculaDTO.get().getAluno().getNome());
        verify(matriculaRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve retornar Optional vazio se matrícula não encontrada por ID")
    void deveRetornarOptionalVazioSeMatriculaNaoEncontradaPorId() {
        when(matriculaRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<MatriculaDTO> foundMatriculaDTO = matriculaService.buscarPorId(1L);

        assertFalse(foundMatriculaDTO.isPresent());
        verify(matriculaRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve listar matrículas por aluno com sucesso e retornar DTOs")
    void deveListarMatriculasPorAlunoComSucessoERetornarDTOs() {
        Matricula matricula2 = new Matricula();
        matricula2.setId(2L);
        matricula2.setAluno(aluno);
        matricula2.setCurso(new Curso());

        when(matriculaRepository.findByAlunoId(1L)).thenReturn(Arrays.asList(matricula, matricula2));

        List<MatriculaDTO> matriculasDTO = matriculaService.listarMatriculasPorAluno(1L);

        assertNotNull(matriculasDTO);
        assertEquals(2, matriculasDTO.size());
        assertEquals(aluno.getNome(), matriculasDTO.get(0).getAluno().getNome());
        verify(matriculaRepository, times(1)).findByAlunoId(1L);
    }

    @Test
    @DisplayName("Deve atualizar status de pagamento com sucesso")
    void deveAtualizarStatusPagamentoComSucesso() {
        when(matriculaRepository.findById(1L)).thenReturn(Optional.of(matricula));
        when(matriculaRepository.save(any(Matricula.class))).thenReturn(matricula);

        Matricula result = matriculaService.atualizarStatusPagamento(1L, StatusPagamento.PAGO);

        assertNotNull(result);
        assertEquals(StatusPagamento.PAGO, result.getStatusPagamento());
        verify(matriculaRepository, times(1)).findById(1L);
        verify(matriculaRepository, times(1)).save(matricula);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar status de pagamento de matrícula não encontrada")
    void deveLancarExcecaoAoAtualizarStatusPagamentoDeMatriculaNaoEncontrada() {
        when(matriculaRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            matriculaService.atualizarStatusPagamento(1L, StatusPagamento.PAGO);
        });

        assertEquals("Matrícula não encontrada com ID: 1", exception.getMessage());
        verify(matriculaRepository, times(1)).findById(1L);
        verify(matriculaRepository, never()).save(any(Matricula.class));
    }

    @Test
    @DisplayName("Deve cancelar matrícula com sucesso")
    void deveCancelarMatriculaComSucesso() {
        when(matriculaRepository.existsById(1L)).thenReturn(true);
        doNothing().when(matriculaRepository).deleteById(1L);

        assertDoesNotThrow(() -> matriculaService.cancelarMatricula(1L));

        verify(matriculaRepository, times(1)).existsById(1L);
        verify(matriculaRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar cancelar matrícula não encontrada")
    void deveLancarExcecaoAoCancelarMatriculaNaoEncontrada() {
        when(matriculaRepository.existsById(1L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            matriculaService.cancelarMatricula(1L);
        });

        assertEquals("Matrícula não encontrada com ID: 1", exception.getMessage());
        verify(matriculaRepository, times(1)).existsById(1L);
        verify(matriculaRepository, never()).deleteById(anyLong());
    }
}


