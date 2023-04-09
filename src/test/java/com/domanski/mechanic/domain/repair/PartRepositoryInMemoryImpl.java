package com.domanski.mechanic.domain.repair;

import com.domanski.mechanic.domain.repair.repository.PartRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class PartRepositoryInMemoryImpl implements PartRepository {

    HashMap<Long, Part> database = new HashMap<>();
    Long indexCounter = 1L;

    @Override
    public List<Part> findAll() {
        return null;
    }

    @Override
    public List<Part> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Part> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Part> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(Part entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Part> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends Part> S save(S part) {
        database.put(indexCounter, part);
        part.setId(indexCounter);
        indexCounter++;
        return part;
    }

    @Override
    public <S extends Part> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Part> findById(Long id) {
        if(database.get(id) == null) {
            return Optional.empty();
        }
        return Optional.of(database.get(id));
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Part> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Part> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Part> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Part getOne(Long aLong) {
        return null;
    }

    @Override
    public Part getById(Long aLong) {
        return null;
    }

    @Override
    public Part getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends Part> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Part> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Part> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Part> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Part> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Part> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Part, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}
