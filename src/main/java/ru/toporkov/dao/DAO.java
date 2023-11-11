package ru.toporkov.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface DAO<K, T> {

    List<T> findAll() throws SQLException;

    Optional<T> findById(K id);

    boolean delete(K id);

    int update(T entity);

    T save(T entity) throws SQLException;
}
