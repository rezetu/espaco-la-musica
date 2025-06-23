package com.example.sistemaescolar.service;

import com.example.sistemaescolar.model.Curso;
import com.example.sistemaescolar.repository.CursoRepository;
import com.example.sistemaescolar.repository.MatriculaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementação do serviço de gestão de cursos.
 */
@Service
public class CursoServiceImpl implements CursoService {

    private final CursoRepository cursoRepository;
    private final MatriculaRepository matriculaRepository;

    // Injeção de dependência via construtor
    public CursoServiceImpl(CursoRepository cursoRepository, MatriculaRepository matriculaRepository) {
        this.cursoRepository = cursoRepository;
        this.matriculaRepository = matriculaRepository;
    }

    @Override
    @Transactional
    public Curso salvar(Curso curso) {
        // Aqui poderiam ser feitas validações, como:
        // - Verificar se o nome está preenchido
        // - Verificar se o valor é positivo
        // - etc.
        
        return cursoRepository.save(curso);
    }

    @Override
    public Optional<Curso> buscarPorId(Long id) {
        return cursoRepository.findById(id);
    }

    @Override
    public List<Curso> listarTodos() {
        return cursoRepository.findAll();
    }

    @Override
    public List<Curso> listarAtivos() {
        return cursoRepository.findByAtivoTrue();
    }

    @Override
    @Transactional
    public Curso alterarStatus(Long id, boolean ativo) {
        // Buscar o curso pelo ID
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado com ID: " + id));
        
        // Alterar o status
        curso.setAtivo(ativo);
        
        // Salvar e retornar o curso atualizado
        return cursoRepository.save(curso);
    }

    @Override
    @Transactional
    public void excluir(Long id) {
        // Verificar se o curso existe
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado com ID: " + id));
        
        // Verificar se existem matrículas para este curso
        boolean temMatriculas = matriculaRepository.existsByCursoId(id);
        if (temMatriculas) {
            throw new RuntimeException("Não é possível excluir o curso pois existem matrículas associadas a ele.");
        }
        
        // Se não houver matrículas, exclui o curso
        cursoRepository.deleteById(id);
    }
}
