package com.domanski.mechanic.domain.repair;

import com.domanski.mechanic.domain.repair.model.RepairPart;
import com.domanski.mechanic.domain.repair.repository.RepairPartRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class RepairPartRepositoryInMemoryImpl implements RepairPartRepository {

    HashMap<Long, RepairPart> database = new HashMap<>();
    Long indexCounter = 1L;

    @Override
    public List<RepairPart> findAll() {
        return null;
    }

    @Override
    public List<RepairPart> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<RepairPart> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<RepairPart> findAllById(Iterable<Long> longs) {
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
    public void delete(RepairPart entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends RepairPart> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends RepairPart> S save(S repairPart) {
        database.put(indexCounter, repairPart);
        repairPart.setId(indexCounter);
        indexCounter++;
        return repairPart;
    }

    @Override
    public <S extends RepairPart> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<RepairPart> findById(Long id) {
        if(database.get(id) == null) {
            return Optional.empty();
        }
        return Optional.of(database.get(id));
    }

    @Override
    public boolean existsById(Long id) {
        return database.get(id) != null;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends RepairPart> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends RepairPart> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<RepairPart> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public RepairPart getOne(Long aLong) {
        return null;
    }

    @Override
    public RepairPart getById(Long aLong) {
        return null;
    }

    @Override
    public RepairPart getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends RepairPart> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends RepairPart> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends RepairPart> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends RepairPart> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends RepairPart> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends RepairPart> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends RepairPart, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}
