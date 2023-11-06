package ru.toporkov.dao;

import ru.toporkov.entity.ExchangeRate;
import ru.toporkov.exception.DAOException;
import ru.toporkov.util.ConnectionManager;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRateDAO implements DAO<Integer, ExchangeRate> {

    private static volatile ExchangeRateDAO instance;

    private static final String FIND_ALL_SQL = """
            SELECT id, base_currency_id, target_currency_id, rate
            FROM exchange_rate
            """;
    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE id = ?
            """;
    private static final String DELETE_SQL = """
            DELETE FROM exchange_rate
            WHERE id = ?
            """;
    private static final String UPDATE_SQL = """
            UPDATE exchange_rate
            SET base_currency_id = ?,
                target_currency_id = ?,
                rate = ?
            WHERE id = ?
            """;
    private static final String SAVE_SQL = """
            INSERT INTO exchange_rate (base_currency_id, target_currency_id, rate)
            VALUES (?, ?, ?)
            """;

    private ExchangeRateDAO() {}

    public static ExchangeRateDAO getInstance() {
        if (instance == null) {
            synchronized (ExchangeRateDAO.class) {
                if (instance == null) {
                    instance = new ExchangeRateDAO();
                }
            }
        }
        return instance;
    }

    @Override
    public List<ExchangeRate> findAll() {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();

            List<ExchangeRate> result = new ArrayList<>();

            while(resultSet.next()) {
                result.add(buildExchangeRate(resultSet));
            }

            return result;
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public Optional<ExchangeRate> findById(Integer id) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setObject(1, id);

            var resultSet = preparedStatement.executeQuery();
            ExchangeRate exchangeRate = null;

            if (resultSet.next()) {
                exchangeRate = buildExchangeRate(resultSet);
            }

            return Optional.ofNullable(exchangeRate);
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public boolean delete(Integer id) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setObject(1, id);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public int update(ExchangeRate entity) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setObject(1, entity.getBaseCurrencyId());
            preparedStatement.setObject(2, entity.getTargetCurrencyId());
            preparedStatement.setObject(3, entity.getRate());
            preparedStatement.setObject(4, entity.getId());

            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public int save(ExchangeRate entity) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(SAVE_SQL)) {
            preparedStatement.setObject(1, entity.getBaseCurrencyId());
            preparedStatement.setObject(2, entity.getTargetCurrencyId());
            preparedStatement.setObject(3, entity.getRate());

            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    private ExchangeRate buildExchangeRate(ResultSet resultSet) throws SQLException {
        return ExchangeRate.builder()
                .id(resultSet.getObject(1, Integer.class))
                .baseCurrencyId(resultSet.getObject(2, Integer.class))
                .targetCurrencyId(resultSet.getObject(3, Integer.class))
                .rate(resultSet.getObject(4, BigDecimal.class))
                .build();
    }
}