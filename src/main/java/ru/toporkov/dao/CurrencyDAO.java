package ru.toporkov.dao;

import ru.toporkov.entity.Currency;
import ru.toporkov.exception.DAOException;
import ru.toporkov.util.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyDAO implements DAO<Integer, Currency> {

    private static volatile CurrencyDAO INSTANCE;
    private static final String FIND_ALL_SQL = """
            SELECT id, code, full_name, sign
            FROM currency
            """;
    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE id = ?
            """;
    private static final String UPDATE_SQL = """
            UPDATE currency AS curr
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
        if (INSTANCE == null) {
            synchronized (CurrencyDAO.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CurrencyDAO();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public List<Currency> findAll() {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {

            var resultSet = preparedStatement.executeQuery();
            List<Currency> result = new ArrayList<>();

            while (resultSet.next()) {
                result.add(buildCurrency(resultSet));
            }

            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
    public int save(Currency entity) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(SAVE_SQL)) {

            preparedStatement.setObject(1, entity.getCode());
            preparedStatement.setObject(2, entity.getFullName());
            preparedStatement.setObject(3, entity.getSign());

            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    private Currency buildCurrency(ResultSet resultSet) throws SQLException {
        return Currency.builder()
                .id(resultSet.getObject(1, Integer.class))
                .code(resultSet.getObject(2, String.class))
                .fullName(resultSet.getObject(3, String.class))
                .fullName(resultSet.getObject(4, String.class))
                .build();
    }
}
