package br.senai.sc.editoralivros.model.service;


import br.senai.sc.editoralivros.model.entities.Pessoa;
import br.senai.sc.editoralivros.repository.PessoaRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
public class PessoaService {
    private PessoaRepository repository;

    public PessoaService(PessoaRepository repository) {
        this.repository = repository;
    }

    public List<Pessoa> findAll() {
        return repository.findAll();
    }

    public List<Pessoa> findAll(Sort sort) {
        return repository.findAll(sort);
    }

    public List<Pessoa> findAllById(Iterable<Long> longs) {
        return repository.findAllById(longs);
    }

    public <S extends Pessoa> List<S> saveAll(Iterable<S> entities) {
        return repository.saveAll(entities);
    }

    public Optional<Pessoa> findById(Long cpf) {
        return repository.findById(cpf);
    }

    public Optional<Pessoa> findByEmail(String email) {
        return repository.findByEmail(email);
    }


    public void deleteById(Long aLong) {
        repository.deleteById(aLong);
    }

    public void delete(Pessoa entity) {
        repository.delete(entity);
    }

    public <S extends Pessoa> S save(S entity) {
        return repository.save(entity);
    }

    public boolean existsById(Long aLong) {
        return repository.existsById(aLong);
    }

    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }
}