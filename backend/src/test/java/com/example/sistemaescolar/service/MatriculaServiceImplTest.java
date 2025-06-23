package com.example.sistemaescolar.service;

import com.example.sistemaescolar.model.Curso;
import com.example.sistemaescolar.model.Matricula;
import com.example.sistemaescolar.model.Pessoa;
import com.example.sistemaescolar.model.StatusPagamento;
import com.example.sistemaescolar.repository.CursoRepository;
import com.example.sistemaescolar.repository.MatriculaRepository;
import com.example.sistemaescolar.repository.PessoaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para a classe MatriculaServiceImpl.
 */
@ExtendWith(MockitoExtension.class) // Configura o Mockito para trabalhar com JUnit 5
public class MatriculaServiceImplTest {

    // Mocks das dependências
    @Mock
    private PessoaRepository pessoaRepository;

    @Mock
    private CursoRepository cursoRepository;

    @Mock
    private MatriculaRepository matriculaRepository;

    // A classe que estamos testando (com os mocks injetados)
    @InjectMocks
    private MatriculaServiceImpl matriculaService;

    // Dados de teste
    private Long alunoId = 1L;
    private Long cursoId = 2L;
    private BigDecimal valorCobrado = new BigDecimal("150.00");
    private LocalDate dataVencimento = LocalDate.now().plusMonths(1);
    private Pessoa aluno;
    private Curso curso;
    private Matricula matricula;

    @BeforeEach
    void setUp() {
        // Configuração inicial que será executada antes de cada teste
        
        // Cria um aluno de teste
        aluno = new Pessoa();
        aluno.setId(alunoId);
        aluno.setNome("João da Silva");
        aluno.setCpf("123.456.789-00");
        
        // Cria um curso de teste
        curso = new Curso();
        curso.setId(cursoId);
        curso.setNome("Java Avançado");
        curso.setValor(new BigDecimal("200.00"));
        curso.setAtivo(true);
        
        // Cria uma matrícula de teste
        matricula = new Matricula();
        matricula.setId(1L);
        matricula.setAluno(aluno);
        matricula.setCurso(curso);
        matricula.setDataMatricula(LocalDate.now());
        matricula.setValorCobrado(valorCobrado);
        matricula.setStatusPagamento(StatusPagamento.PENDENTE);
        matricula.setDataVencimento(dataVencimento);
    }

    @Test
    void realizarMatricula_DeveRetornarMatriculaQuandoSucesso() {
        // Arrange (Configuração)
        // Configura o mock do pessoaRepository para retornar o aluno quando findById for chamado
        when(pessoaRepository.findById(alunoId)).thenReturn(Optional.of(aluno));
        
        // Configura o mock do cursoRepository para retornar o curso quando findById for chamado
        when(cursoRepository.findById(cursoId)).thenReturn(Optional.of(curso));
        
        // Configura o mock do matriculaRepository para retornar false quando existsByAlunoIdAndCursoId for chamado
        // (indicando que o aluno ainda não está matriculado neste curso)
        when(matriculaRepository.existsByAlunoIdAndCursoId(alunoId, cursoId)).thenReturn(false);
        
        // Configura o mock do matriculaRepository para retornar a matrícula quando save for chamado
        when(matriculaRepository.save(any(Matricula.class))).thenReturn(matricula);

        // Act (Ação)
        // Chama o método que estamos testando
        Matricula resultado = matriculaService.realizarMatricula(alunoId, cursoId, valorCobrado, dataVencimento);

        // Assert (Verificação)
        // Verifica se o resultado não é nulo
        assertNotNull(resultado);
        
        // Verifica se o resultado tem os valores esperados
        assertEquals(aluno, resultado.getAluno());
        assertEquals(curso, resultado.getCurso());
        assertEquals(valorCobrado, resultado.getValorCobrado());
        assertEquals(dataVencimento, resultado.getDataVencimento());
        assertEquals(StatusPagamento.PENDENTE, resultado.getStatusPagamento());
        
        // Verifica se os métodos dos mocks foram chamados o número correto de vezes
        verify(pessoaRepository, times(1)).findById(alunoId);
        verify(cursoRepository, times(1)).findById(cursoId);
        verify(matriculaRepository, times(1)).existsByAlunoIdAndCursoId(alunoId, cursoId);
        verify(matriculaRepository, times(1)).save(any(Matricula.class));
    }

    @Test
    void realizarMatricula_DeveLancarExcecaoQuandoAlunoJaMatriculado() {
        // Arrange
        when(pessoaRepository.findById(alunoId)).thenReturn(Optional.of(aluno));
        when(cursoRepository.findById(cursoId)).thenReturn(Optional.of(curso));
        
        // Configura o mock para retornar true, indicando que o aluno já está matriculado
        when(matriculaRepository.existsByAlunoIdAndCursoId(alunoId, cursoId)).thenReturn(true);

        // Act & Assert
        // Verifica se uma RuntimeException é lançada com a mensagem esperada
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            matriculaService.realizarMatricula(alunoId, cursoId, valorCobrado, dataVencimento);
        });
        
        assertEquals("Aluno já matriculado neste curso.", exception.getMessage());
        
        // Verifica que save nunca foi chamado, já que uma exceção foi lançada antes
        verify(matriculaRepository, never()).save(any(Matricula.class));
    }

    @Test
    void realizarMatricula_DeveLancarExcecaoQuandoCursoInativo() {
        // Arrange
        when(pessoaRepository.findById(alunoId)).thenReturn(Optional.of(aluno));
        
        // Configura o curso como inativo
        curso.setAtivo(false);
        when(cursoRepository.findById(cursoId)).thenReturn(Optional.of(curso));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            matriculaService.realizarMatricula(alunoId, cursoId, valorCobrado, dataVencimento);
        });
        
        assertTrue(exception.getMessage().contains("Não é possível matricular em um curso inativo"));
        
        // Verifica que existsByAlunoIdAndCursoId e save nunca foram chamados
        verify(matriculaRepository, never()).existsByAlunoIdAndCursoId(anyLong(), anyLong());
        verify(matriculaRepository, never()).save(any(Matricula.class));
    }

    @Test
    void realizarMatricula_DeveLancarExcecaoQuandoAlunoNaoEncontrado() {
        // Arrange
        // Configura o mock para retornar Optional vazio, indicando que o aluno não foi encontrado
        when(pessoaRepository.findById(alunoId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            matriculaService.realizarMatricula(alunoId, cursoId, valorCobrado, dataVencimento);
        });
        
        assertTrue(exception.getMessage().contains("Aluno não encontrado"));
        
        // Verifica que findById do cursoRepository nunca foi chamado, já que uma exceção foi lançada antes
        verify(cursoRepository, never()).findById(anyLong());
    }

    @Test
    void realizarMatricula_DeveLancarExcecaoQuandoCursoNaoEncontrado() {
        // Arrange
        when(pessoaRepository.findById(alunoId)).thenReturn(Optional.of(aluno));
        // Configura o mock para retornar Optional vazio, indicando que o curso não foi encontrado
        when(cursoRepository.findById(cursoId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            matriculaService.realizarMatricula(alunoId, cursoId, valorCobrado, dataVencimento);
        });
        
        assertTrue(exception.getMessage().contains("Curso não encontrado"));
        
        // Verifica que existsByAlunoIdAndCursoId nunca foi chamado
        verify(matriculaRepository, never()).existsByAlunoIdAndCursoId(anyLong(), anyLong());
    }
}
