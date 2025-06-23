package com.example.sistemaescolar.service;

import com.example.sistemaescolar.model.Pessoa;
import com.example.sistemaescolar.repository.PessoaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PessoaServiceTest {

    @Mock
    private PessoaRepository pessoaRepository;

    @InjectMocks
    private PessoaServiceImpl pessoaService;

    private Pessoa pessoa;

    @BeforeEach
    void setUp() {
        pessoa = new Pessoa();
        pessoa.setId(1L);
        pessoa.setNome("Teste Pessoa");
        pessoa.setCpf("12345678900");
        pessoa.setDataNascimento(LocalDate.of(2000, 1, 1));
        pessoa.setEmail("teste@example.com");
        pessoa.setTelefone("11987654321");
    }

    @Test
    @DisplayName("Deve cadastrar uma nova pessoa com sucesso")
    void deveCadastrarPessoaComSucesso() {
        when(pessoaRepository.findByCpf(pessoa.getCpf())).thenReturn(Optional.empty());
        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoa);

        Pessoa pessoaCadastrada = pessoaService.cadastrarPessoa(pessoa);

        assertNotNull(pessoaCadastrada);
        assertEquals(pessoa.getNome(), pessoaCadastrada.getNome());
        verify(pessoaRepository, times(1)).findByCpf(pessoa.getCpf());
        verify(pessoaRepository, times(1)).save(pessoa);
    }

    @Test
    @DisplayName("Não deve cadastrar pessoa com CPF duplicado")
    void naoDeveCadastrarPessoaComCpfDuplicado() {
        when(pessoaRepository.findByCpf(pessoa.getCpf())).thenReturn(Optional.of(pessoa));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pessoaService.cadastrarPessoa(pessoa);
        });

        assertEquals("CPF já cadastrado.", exception.getMessage());
        verify(pessoaRepository, times(1)).findByCpf(pessoa.getCpf());
        verify(pessoaRepository, never()).save(any(Pessoa.class));
    }

    @Test
    @DisplayName("Deve buscar pessoa por ID com sucesso")
    void deveBuscarPessoaPorIdComSucesso() {
        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(pessoa));

        Optional<Pessoa> foundPessoa = pessoaService.buscarPorId(1L);

        assertTrue(foundPessoa.isPresent());
        assertEquals(pessoa.getNome(), foundPessoa.get().getNome());
        verify(pessoaRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve retornar Optional vazio se pessoa não encontrada por ID")
    void deveRetornarOptionalVazioSePessoaNaoEncontradaPorId() {
        when(pessoaRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Pessoa> foundPessoa = pessoaService.buscarPorId(1L);

        assertFalse(foundPessoa.isPresent());
        verify(pessoaRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve buscar pessoa por CPF com sucesso")
    void deveBuscarPessoaPorCpfComSucesso() {
        when(pessoaRepository.findByCpf(pessoa.getCpf())).thenReturn(Optional.of(pessoa));

        Optional<Pessoa> foundPessoa = pessoaService.buscarPorCpf(pessoa.getCpf());

        assertTrue(foundPessoa.isPresent());
        assertEquals(pessoa.getNome(), foundPessoa.get().getNome());
        verify(pessoaRepository, times(1)).findByCpf(pessoa.getCpf());
    }

    @Test
    @DisplayName("Deve retornar Optional vazio se pessoa não encontrada por CPF")
    void deveRetornarOptionalVazioSePessoaNaoEncontradaPorCpf() {
        when(pessoaRepository.findByCpf(pessoa.getCpf())).thenReturn(Optional.empty());

        Optional<Pessoa> foundPessoa = pessoaService.buscarPorCpf(pessoa.getCpf());

        assertFalse(foundPessoa.isPresent());
        verify(pessoaRepository, times(1)).findByCpf(pessoa.getCpf());
    }

    @Test
    @DisplayName("Deve listar todas as pessoas com sucesso")
    void deveListarTodasAsPessoasComSucesso() {
        when(pessoaRepository.findAll()).thenReturn(java.util.Arrays.asList(pessoa, new Pessoa()));

        List<Pessoa> pessoas = pessoaService.listarTodasPessoas();

        assertNotNull(pessoas);
        assertEquals(2, pessoas.size());
        verify(pessoaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve atualizar pessoa com sucesso")
    void deveAtualizarPessoaComSucesso() {
        Pessoa updatedPessoa = new Pessoa();
        updatedPessoa.setId(1L);
        updatedPessoa.setNome("Pessoa Atualizada");
        updatedPessoa.setCpf("12345678900");
        updatedPessoa.setDataNascimento(LocalDate.of(2001, 2, 2));
        updatedPessoa.setEmail("atualizado@example.com");
        updatedPessoa.setTelefone("11999999999");

        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(pessoa));
        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(updatedPessoa);

        Pessoa result = pessoaService.atualizarPessoa(1L, updatedPessoa);

        assertNotNull(result);
        assertEquals(updatedPessoa.getNome(), result.getNome());
        verify(pessoaRepository, times(1)).findById(1L);
        verify(pessoaRepository, times(1)).save(any(Pessoa.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar pessoa não encontrada")
    void deveLancarExcecaoAoAtualizarPessoaNaoEncontrada() {
        when(pessoaRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pessoaService.atualizarPessoa(1L, new Pessoa());
        });

        assertEquals("Pessoa não encontrada com ID: 1", exception.getMessage());
        verify(pessoaRepository, times(1)).findById(1L);
        verify(pessoaRepository, never()).save(any(Pessoa.class));
    }

    @Test
    @DisplayName("Deve deletar pessoa com sucesso")
    void deveDeletarPessoaComSucesso() {
        when(pessoaRepository.existsById(1L)).thenReturn(true);
        doNothing().when(pessoaRepository).deleteById(1L);

        assertDoesNotThrow(() -> pessoaService.deletarPessoa(1L));

        verify(pessoaRepository, times(1)).existsById(1L);
        verify(pessoaRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar deletar pessoa não encontrada")
    void deveLancarExcecaoAoDeletarPessoaNaoEncontrada() {
        when(pessoaRepository.existsById(1L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pessoaService.deletarPessoa(1L);
        });

        assertEquals("Pessoa não encontrada com ID: 1", exception.getMessage());
        verify(pessoaRepository, times(1)).existsById(1L);
        verify(pessoaRepository, never()).deleteById(anyLong());
    }
}


