package ru.toporkov.dao;

import java.util.List;
import java.util.Optional;

public interface DAO<K, T> {

    List<T> findAll();

    Optional<T> findById(K id);

    boolean delete(K id);

    int update(T entity);

    int save(T entity);
}
