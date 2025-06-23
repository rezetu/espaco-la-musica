package com.example.sistemaescolar.controller;

import com.example.sistemaescolar.model.Curso;
import com.example.sistemaescolar.service.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    private final CursoService cursoService;

    @Autowired
    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    @PostMapping
    public ResponseEntity<Curso> criarCurso(@RequestBody Curso curso) {
        Curso novoCurso = cursoService.salvar(curso);
        return new ResponseEntity<>(novoCurso, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Curso>> listarTodosOsCursos() {
        List<Curso> cursos = cursoService.listarTodos();
        return new ResponseEntity<>(cursos, HttpStatus.OK);
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<Curso>> listarCursosAtivos() {
        List<Curso> cursosAtivos = cursoService.listarAtivos();
        return new ResponseEntity<>(cursosAtivos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Curso> buscarCursoPorId(@PathVariable Long id) {
        Optional<Curso> curso = cursoService.buscarPorId(id);
        return curso.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Curso> atualizarCurso(@PathVariable Long id, @RequestBody Curso curso) {
        curso.setId(id);
        Curso cursoAtualizado = cursoService.salvar(curso);
        return new ResponseEntity<>(cursoAtualizado, HttpStatus.OK);
    }

    @PatchMapping("/{id}/status/{ativo}")
    public ResponseEntity<Curso> alterarStatusCurso(@PathVariable Long id, @PathVariable boolean ativo) {
        try {
            Curso curso = cursoService.alterarStatus(id, ativo);
            return new ResponseEntity<>(curso, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCurso(@PathVariable Long id) {
        try {
            cursoService.excluir(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            // Ex: Curso possui matrículas associadas
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}


