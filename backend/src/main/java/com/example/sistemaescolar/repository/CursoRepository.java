package com.example.sistemaescolar.repository;

import com.example.sistemaescolar.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório para a entidade Curso.
 * Estende JpaRepository para fornecer operações CRUD básicas e mais.
 */
@Repository // Indica ao Spring que esta interface é um componente Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {

    /**
     * Busca todos os cursos que estão ativos.
     * O Spring Data JPA implementa este método automaticamente baseado no nome.
     *
     * @return Uma lista de cursos ativos.
     */
    List<Curso> findByAtivoTrue();

    /**
     * Busca todos os cursos que não estão ativos.
     * O Spring Data JPA implementa este método automaticamente baseado no nome.
     *
     * @return Uma lista de cursos inativos.
     */
    List<Curso> findByAtivoFalse();

    // JpaRepository<Curso, Long> já fornece métodos como:
    // - save(Curso curso): Salva ou atualiza um curso.
    // - findById(Long id): Busca um curso pelo ID.
    // - findAll(): Busca todos os cursos.
    // - deleteById(Long id): Deleta um curso pelo ID.
    // ... e muitos outros!
}
