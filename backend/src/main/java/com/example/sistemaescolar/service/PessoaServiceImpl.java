package com.example.sistemaescolar.service;

import com.example.sistemaescolar.model.Pessoa;
import com.example.sistemaescolar.repository.PessoaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementação do serviço de gestão de pessoas.
 */
@Service
public class PessoaServiceImpl implements PessoaService {

    private final PessoaRepository pessoaRepository;

    // Injeção de dependência via construtor
    public PessoaServiceImpl(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    @Override
    @Transactional
    public Pessoa salvar(Pessoa pessoa) {
        // Verificar se já existe uma pessoa com o mesmo CPF (se o CPF não for nulo)
        if (pessoa.getCpf() != null && !pessoa.getCpf().isEmpty()) {
            Optional<Pessoa> pessoaExistente = pessoaRepository.findByCpf(pessoa.getCpf());
            
            // Se encontrou alguém com o mesmo CPF e não é a mesma pessoa (IDs diferentes)
            if (pessoaExistente.isPresent() && 
                (pessoa.getId() == null || !pessoa.getId().equals(pessoaExistente.get().getId()))) {
                throw new RuntimeException("Já existe uma pessoa cadastrada com o CPF: " + pessoa.getCpf());
            }
        }
        
        // Aqui poderiam ser feitas outras validações, como:
        // - Verificar se o nome está preenchido
        // - Validar formato do CPF
        // - Validar formato do email
        // - etc.
        
        return pessoaRepository.save(pessoa);
    }

    @Override
    public Optional<Pessoa> buscarPorId(Long id) {
        return pessoaRepository.findById(id);
    }

    @Override
    public Optional<Pessoa> buscarPorCpf(String cpf) {
        return pessoaRepository.findByCpf(cpf);
    }

    @Override
    public List<Pessoa> listarTodos() {
        return pessoaRepository.findAll();
    }

    @Override
    @Transactional
    public void excluir(Long id) {
        // Verificar se a pessoa existe
        Pessoa pessoa = pessoaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada com ID: " + id));
        
        // Aqui poderia ser verificado se a pessoa pode ser excluída
        // Por exemplo, verificar se ela não tem matrículas ativas
        // Isso exigiria injetar o MatriculaRepository e fazer a verificação
        
        // Se tudo estiver ok, exclui a pessoa
        pessoaRepository.deleteById(id);
    }
}
