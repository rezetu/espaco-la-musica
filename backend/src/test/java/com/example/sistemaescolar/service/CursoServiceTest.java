package com.example.sistemaescolar.service;

import com.example.sistemaescolar.model.Curso;
import com.example.sistemaescolar.repository.CursoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CursoServiceTest {

    @Mock
    private CursoRepository cursoRepository;

    @InjectMocks
    private CursoServiceImpl cursoService;

    private Curso curso;

    @BeforeEach
    void setUp() {
        curso = new Curso();
        curso.setId(1L);
        curso.setNome("Curso Teste");
        curso.setDescricao("Descricao do curso teste");
        curso.setValor(new BigDecimal("1000.00"));
        curso.setCargaHoraria(40);
        curso.setAtivo(true);
    }

    @Test
    @DisplayName("Deve cadastrar um novo curso com sucesso")
    void deveCadastrarCursoComSucesso() {
        when(cursoRepository.save(any(Curso.class))).thenReturn(curso);

        Curso cursoCadastrado = cursoService.cadastrarCurso(curso);

        assertNotNull(cursoCadastrado);
        assertEquals(curso.getNome(), cursoCadastrado.getNome());
        verify(cursoRepository, times(1)).save(curso);
    }

    @Test
    @DisplayName("Deve buscar curso por ID com sucesso")
    void deveBuscarCursoPorIdComSucesso() {
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));

        Optional<Curso> foundCurso = cursoService.buscarPorId(1L);

        assertTrue(foundCurso.isPresent());
        assertEquals(curso.getNome(), foundCurso.get().getNome());
        verify(cursoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve retornar Optional vazio se curso não encontrado por ID")
    void deveRetornarOptionalVazioSeCursoNaoEncontradoPorId() {
        when(cursoRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Curso> foundCurso = cursoService.buscarPorId(1L);

        assertFalse(foundCurso.isPresent());
        verify(cursoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve listar todos os cursos com sucesso")
    void deveListarTodosOsCursosComSucesso() {
        when(cursoRepository.findAll()).thenReturn(Arrays.asList(curso, new Curso()));

        List<Curso> cursos = cursoService.listarTodosCursos();

        assertNotNull(cursos);
        assertEquals(2, cursos.size());
        verify(cursoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve listar cursos ativos com sucesso")
    void deveListarCursosAtivosComSucesso() {
        Curso cursoAtivo = new Curso();
        cursoAtivo.setAtivo(true);
        Curso cursoInativo = new Curso();
        cursoInativo.setAtivo(false);

        when(cursoRepository.findByAtivo(true)).thenReturn(Arrays.asList(curso, cursoAtivo));

        List<Curso> cursosAtivos = cursoService.listarCursosAtivos();

        assertNotNull(cursosAtivos);
        assertEquals(2, cursosAtivos.size());
        assertTrue(cursosAtivos.stream().allMatch(Curso::isAtivo));
        verify(cursoRepository, times(1)).findByAtivo(true);
    }

    @Test
    @DisplayName("Deve atualizar curso com sucesso")
    void deveAtualizarCursoComSucesso() {
        Curso updatedCurso = new Curso();
        updatedCurso.setId(1L);
        updatedCurso.setNome("Curso Atualizado");
        updatedCurso.setDescricao("Descricao atualizada");
        updatedCurso.setValor(new BigDecimal("1200.00"));
        updatedCurso.setCargaHoraria(50);
        updatedCurso.setAtivo(false);

        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));
        when(cursoRepository.save(any(Curso.class))).thenReturn(updatedCurso);

        Curso result = cursoService.atualizarCurso(1L, updatedCurso);

        assertNotNull(result);
        assertEquals(updatedCurso.getNome(), result.getNome());
        verify(cursoRepository, times(1)).findById(1L);
        verify(cursoRepository, times(1)).save(any(Curso.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar curso não encontrado")
    void deveLancarExcecaoAoAtualizarCursoNaoEncontrado() {
        when(cursoRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cursoService.atualizarCurso(1L, new Curso());
        });

        assertEquals("Curso não encontrado com ID: 1", exception.getMessage());
        verify(cursoRepository, times(1)).findById(1L);
        verify(cursoRepository, never()).save(any(Curso.class));
    }

    @Test
    @DisplayName("Deve alterar status do curso com sucesso")
    void deveAlterarStatusCursoComSucesso() {
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));
        when(cursoRepository.save(any(Curso.class))).thenReturn(curso);

        Curso result = cursoService.alterarStatusCurso(1L, false);

        assertNotNull(result);
        assertFalse(result.isAtivo());
        verify(cursoRepository, times(1)).findById(1L);
        verify(cursoRepository, times(1)).save(any(Curso.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar alterar status de curso não encontrado")
    void deveLancarExcecaoAoAlterarStatusDeCursoNaoEncontrado() {
        when(cursoRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cursoService.alterarStatusCurso(1L, true);
        });

        assertEquals("Curso não encontrado com ID: 1", exception.getMessage());
        verify(cursoRepository, times(1)).findById(1L);
        verify(cursoRepository, never()).save(any(Curso.class));
    }

    @Test
    @DisplayName("Deve deletar curso com sucesso")
    void deveDeletarCursoComSucesso() {
        when(cursoRepository.existsById(1L)).thenReturn(true);
        when(cursoRepository.existsMatriculaByCursoId(1L)).thenReturn(false);
        doNothing().when(cursoRepository).deleteById(1L);

        assertDoesNotThrow(() -> cursoService.deletarCurso(1L));

        verify(cursoRepository, times(1)).existsById(1L);
        verify(cursoRepository, times(1)).existsMatriculaByCursoId(1L);
        verify(cursoRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar deletar curso não encontrado")
    void deveLancarExcecaoAoDeletarCursoNaoEncontrado() {
        when(cursoRepository.existsById(1L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cursoService.deletarCurso(1L);
        });

        assertEquals("Curso não encontrado com ID: 1", exception.getMessage());
        verify(cursoRepository, times(1)).existsById(1L);
        verify(cursoRepository, never()).existsMatriculaByCursoId(anyLong());
        verify(cursoRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar deletar curso com matrículas associadas")
    void deveLancarExcecaoAoDeletarCursoComMatriculasAssociadas() {
        when(cursoRepository.existsById(1L)).thenReturn(true);
        when(cursoRepository.existsMatriculaByCursoId(1L)).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cursoService.deletarCurso(1L);
        });

        assertEquals("Não é possível deletar o curso pois existem matrículas associadas a ele.", exception.getMessage());
        verify(cursoRepository, times(1)).existsById(1L);
        verify(cursoRepository, times(1)).existsMatriculaByCursoId(1L);
        verify(cursoRepository, never()).deleteById(anyLong());
    }
}


