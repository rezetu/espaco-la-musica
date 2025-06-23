package com.example.sistemaescolar.controller;

import com.example.sistemaescolar.model.Matricula;
import com.example.sistemaescolar.model.StatusPagamento;
import com.example.sistemaescolar.service.MatriculaService;
import com.example.sistemaescolar.dto.MatriculaDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controller para operações relacionadas a matrículas.
 * Expõe endpoints REST para gerenciar matrículas no sistema.
 */
@RestController
@RequestMapping("/api/matriculas")
public class MatriculaController {

    private final MatriculaService matriculaService;

    // Injeção de dependência via construtor
    public MatriculaController(MatriculaService matriculaService) {
        this.matriculaService = matriculaService;
    }

    /**
     * Endpoint para realizar uma nova matrícula.
     *
     * @param dadosMatricula Mapa contendo os dados necessários para a matrícula
     * @return ResponseEntity com a matrícula criada ou mensagem de erro
     */
    @PostMapping
    public ResponseEntity<?> realizarMatricula(@RequestBody Map<String, Object> dadosMatricula) {
        try {
            // Extrair dados do request body
            Long alunoId = Long.valueOf(dadosMatricula.get("alunoId").toString());
            Long cursoId = Long.valueOf(dadosMatricula.get("cursoId").toString());
            BigDecimal valorCobrado = new BigDecimal(dadosMatricula.get("valorCobrado").toString());
            LocalDate dataVencimento = LocalDate.parse(dadosMatricula.get("dataVencimento").toString());

            // Chamar o serviço para realizar a matrícula
            Matricula novaMatricula = matriculaService.realizarMatricula(alunoId, cursoId, valorCobrado, dataVencimento);

            // Retornar a matrícula criada com status 201 (Created)
            return new ResponseEntity<>(novaMatricula, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // Em caso de erro, retornar uma mensagem amigável com status 400 (Bad Request)
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Endpoint para buscar uma matrícula pelo ID.
     *
     * @param id ID da matrícula a ser buscada
     * @return ResponseEntity com a matrícula encontrada ou status 404 (Not Found)
     */
    @GetMapping("/{id}")
    public ResponseEntity<MatriculaDTO> buscarMatriculaPorId(@PathVariable Long id) {
        Optional<MatriculaDTO> matricula = matriculaService.buscarPorId(id);
        return matricula.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Endpoint para listar todas as matrículas de um aluno específico.
     *
     * @param alunoId ID do aluno
     * @return ResponseEntity com a lista de matrículas do aluno
     */
    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<List<MatriculaDTO>> listarMatriculasPorAluno(@PathVariable Long alunoId) {
        List<MatriculaDTO> matriculas = matriculaService.listarMatriculasPorAluno(alunoId);
        return new ResponseEntity<>(matriculas, HttpStatus.OK);
    }

    /**
     * Endpoint para atualizar o status de pagamento de uma matrícula.
     *
     * @param id ID da matrícula
     * @param dadosAtualizacao Mapa contendo o novo status de pagamento
     * @return ResponseEntity com a matrícula atualizada ou mensagem de erro
     */
    @PatchMapping("/{id}/status-pagamento")
    public ResponseEntity<Matricula> atualizarStatusPagamento(@PathVariable Long id, @RequestBody Map<String, String> dadosAtualizacao) {
        try {
            StatusPagamento novoStatus = StatusPagamento.valueOf(dadosAtualizacao.get("status").toUpperCase());
            Matricula matriculaAtualizada = matriculaService.atualizarStatusPagamento(id, novoStatus);
            return new ResponseEntity<>(matriculaAtualizada, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint para cancelar uma matrícula.
     *
     * @param id ID da matrícula a ser cancelada
     * @return ResponseEntity com mensagem de sucesso ou erro
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelarMatricula(@PathVariable Long id) {
        try {
            matriculaService.cancelarMatricula(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}


