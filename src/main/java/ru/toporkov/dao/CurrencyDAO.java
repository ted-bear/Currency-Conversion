package ru.toporkov.dao;

import ru.toporkov.entity.Currency;

import java.util.List;
import java.util.Optional;

public class CurrencyDAO implements DAO<Integer, Currency> {


    @Override
    public List<Currency> findAll() {
        return null;
    }

    @Override
    public Optional<Currency> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }

    @Override
    public void update(Currency entity) {

    }

    @Override
    public Currency save(Currency entity) {
        return null;
    }
}
