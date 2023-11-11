package ru.toporkov.dao;

import ru.toporkov.entity.Currency;
import ru.toporkov.validator.exception.DAOException;
import ru.toporkov.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyDAO implements DAO<Integer, Currency> {

    private static volatile CurrencyDAO instance;
    private static final String FIND_ALL_SQL = """
            SELECT id, code, full_name, sign
            FROM currency
            """;
    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE id = ?
            """;
    private static final String FIND_BY_ISO_CODE_SQL = FIND_ALL_SQL + """
            WHERE code = ?
            """;
    private static final String UPDATE_SQL = """
            UPDATE currency
            SET code = ?,
                full_name = ?,
                sign = ?
            WHERE id = ?
            """;
    private static final String SAVE_SQL = """
            INSERT INTO currency (code, full_name, sign)
            VALUES (?, ?, ?)
            """;
    private static final String DELETE_SQL = """
            DELETE FROM currency
            WHERE id = ?
            """;

    private CurrencyDAO() {}

    public static CurrencyDAO getInstance() {
        if (instance == null) {
            synchronized (CurrencyDAO.class) {
                if (instance == null) {
                    instance = new CurrencyDAO();
                }
            }
        }
        return instance;
    }

    @Override
    public List<Currency> findAll() throws SQLException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {

            var resultSet = preparedStatement.executeQuery();
            List<Currency> result = new ArrayList<>();

            while (resultSet.next()) {
                result.add(buildCurrency(resultSet));
            }

            return result;
        }
    }

    @Override
    public Optional<Currency> findById(Integer id) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {

            preparedStatement.setObject(1, id);
            var resultSet = preparedStatement.executeQuery();
            Currency currency = null;

            if (resultSet.next()) {
                currency = buildCurrency(resultSet);
            }

            return Optional.ofNullable(currency);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(Integer id) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(DELETE_SQL)) {

            preparedStatement.setObject(1, id);
            var affectedRows = preparedStatement.executeUpdate();

            return affectedRows > 0;
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public int update(Currency entity) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(UPDATE_SQL)) {

            preparedStatement.setObject(1, entity.getCode());
            preparedStatement.setObject(2, entity.getFullName());
            preparedStatement.setObject(3, entity.getSign());
            preparedStatement.setObject(4, entity.getId());

            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Currency save(Currency entity) throws SQLException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setObject(1, entity.getCode());
            preparedStatement.setObject(2, entity.getFullName());
            preparedStatement.setObject(3, entity.getSign());

            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();
            generatedKeys.next();
            entity.setId(generatedKeys.getObject("id", Integer.class));
            return entity;
        }
    }

    public Optional<Currency> findByIsoCode(String code) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_ISO_CODE_SQL)) {

            preparedStatement.setObject(1, code);

            var resultSet = preparedStatement.executeQuery();
            Currency currency = null;

            if (resultSet.next()) {
                currency = buildCurrency(resultSet);
            }

            return Optional.ofNullable(currency);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Currency buildCurrency(ResultSet resultSet) throws SQLException {
        return Currency.builder()
                .id(resultSet.getObject(1, Integer.class))
                .code(resultSet.getObject(2, String.class))
                .fullName(resultSet.getObject(3, String.class))
                .sign(resultSet.getObject(4, String.class))
                .build();
    }
}
