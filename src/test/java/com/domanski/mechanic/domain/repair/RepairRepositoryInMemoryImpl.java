package com.domanski.mechanic.domain.repair;

import com.domanski.mechanic.domain.repair.model.Repair;
import com.domanski.mechanic.domain.repair.model.RepairStatus;
import com.domanski.mechanic.domain.repair.repository.RepairRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class RepairRepositoryInMemoryImpl implements RepairRepository {

    private final Map<Long, Repair> database = new HashMap<>();
    private Long indexCounter = 1L;


    @Override
    public List<Repair> findAllByUserId(Long userId) {
        return database.values().stream()
                .filter(repair -> repair.getUserId().equals(userId))
                .toList();
    }

    @Override
    public List<Repair> findAllByDate(LocalDate date) {
        return database.values().stream()
                .filter(repair -> repair.getDate().isEqual(date))
                .toList();
    }

    @Override
    public List<Repair> findAllByRepairStatus(RepairStatus status) {
        return database.values().stream()
                .filter(repair -> repair.getRepairStatus().equals(status))
                .toList();
    }

    @Override
    public List<Repair> findAll() {
        return null;
    }

    @Override
    public List<Repair> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Repair> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Repair> findAllById(Iterable<Long> longs) {
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
    public void delete(Repair entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Repair> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends Repair> S save(S repair) {
        database.put(indexCounter, repair);
        repair.setId(indexCounter);
        indexCounter++;
        return repair;
    }

    @Override
    public <S extends Repair> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Repair> findById(Long id) {
        Repair repair = database.get(id);
        if (repair == null) {
            return Optional.empty();
        }
        return Optional.of(repair);
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Repair> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Repair> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Repair> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Repair getOne(Long aLong) {
        return null;
    }

    @Override
    public Repair getById(Long aLong) {
        return null;
    }

    @Override
    public Repair getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends Repair> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Repair> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Repair> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Repair> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Repair> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Repair> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Repair, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}
