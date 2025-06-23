package com.example.sistemaescolar.service;

import com.example.sistemaescolar.model.Curso;
import com.example.sistemaescolar.model.Matricula;
import com.example.sistemaescolar.model.Pessoa;
import com.example.sistemaescolar.model.StatusPagamento;
import com.example.sistemaescolar.repository.CursoRepository;
import com.example.sistemaescolar.repository.MatriculaRepository;
import com.example.sistemaescolar.repository.PessoaRepository;
import com.example.sistemaescolar.dto.MatriculaDTO;
import com.example.sistemaescolar.dto.PessoaDTO;
import com.example.sistemaescolar.dto.CursoDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementação do serviço de gestão de matrículas.
 */
@Service // Marca esta classe como um componente de serviço gerenciado pelo Spring
public class MatriculaServiceImpl implements MatriculaService {

    private final PessoaRepository pessoaRepository;
    private final CursoRepository cursoRepository;
    private final MatriculaRepository matriculaRepository;

    // Injeção de dependência via construtor (prática recomendada)
    public MatriculaServiceImpl(PessoaRepository pessoaRepository,
                                CursoRepository cursoRepository,
                                MatriculaRepository matriculaRepository) {
        this.pessoaRepository = pessoaRepository;
        this.cursoRepository = cursoRepository;
        this.matriculaRepository = matriculaRepository;
    }

    @Override
    @Transactional // Garante que a operação seja atômica (ou tudo funciona ou nada é salvo)
    public Matricula realizarMatricula(Long alunoId, Long cursoId, BigDecimal valorCobrado, LocalDate dataVencimento) {
        // 1. Buscar o aluno pelo ID
        Pessoa aluno = pessoaRepository.findById(alunoId)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado com ID: " + alunoId)); // Lança exceção se não encontrar

        // 2. Buscar o curso pelo ID
        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado com ID: " + cursoId));

        // 3. Verificar se o curso está ativo
        if (!curso.isAtivo()) {
            throw new RuntimeException("Não é possível matricular em um curso inativo: " + curso.getNome());
        }

        // --- Outras validações poderiam ser adicionadas aqui ---
        // 4. Verificar se o aluno já está matriculado neste curso
        if (matriculaRepository.existsByAlunoIdAndCursoId(alunoId, cursoId)) {
            throw new RuntimeException("Aluno já matriculado neste curso.");
        }

        // 5. Criar a nova instância de Matricula
        Matricula novaMatricula = new Matricula();
        novaMatricula.setAluno(aluno);
        novaMatricula.setCurso(curso);
        novaMatricula.setDataMatricula(LocalDate.now()); // Define a data atual como data da matrícula
        novaMatricula.setValorCobrado(valorCobrado);
        novaMatricula.setStatusPagamento(StatusPagamento.PENDENTE); // Status inicial
        novaMatricula.setDataVencimento(dataVencimento);

        // 6. Salvar a matrícula no banco de dados
        return matriculaRepository.save(novaMatricula);
    }

    @Override
    public Optional<MatriculaDTO> buscarPorId(Long id) {
        return matriculaRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Override
    public List<MatriculaDTO> listarMatriculasPorAluno(Long alunoId) {
        return matriculaRepository.findByAlunoId(alunoId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Matricula atualizarStatusPagamento(Long id, StatusPagamento novoStatus) {
        Matricula matricula = matriculaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Matrícula não encontrada com ID: " + id));
        matricula.setStatusPagamento(novoStatus);
        return matriculaRepository.save(matricula);
    }

    @Override
    @Transactional
    public void cancelarMatricula(Long id) {
        if (!matriculaRepository.existsById(id)) {
            throw new RuntimeException("Matrícula não encontrada com ID: " + id);
        }
        matriculaRepository.deleteById(id);
    }

    private MatriculaDTO convertToDTO(Matricula matricula) {
        PessoaDTO alunoDTO = new PessoaDTO(
                matricula.getAluno().getId(),
                matricula.getAluno().getNome(),
                matricula.getAluno().getCpf(),
                matricula.getAluno().getDataNascimento(),
                matricula.getAluno().getEmail(),
                matricula.getAluno().getTelefone()
        );

        CursoDTO cursoDTO = new CursoDTO(
                matricula.getCurso().getId(),
                matricula.getCurso().getNome(),
                matricula.getCurso().getDescricao(),
                matricula.getCurso().getValor(),
                matricula.getCurso().getCargaHoraria(),
                matricula.getCurso().isAtivo()
        );

        return new MatriculaDTO(
                matricula.getId(),
                alunoDTO,
                cursoDTO,
                matricula.getDataMatricula(),
                matricula.getValorCobrado(),
                matricula.getStatusPagamento(),
                matricula.getDataVencimento()
        );
    }
}


