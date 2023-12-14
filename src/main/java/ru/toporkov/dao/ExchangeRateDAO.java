package ru.toporkov.dao;

import ru.toporkov.entity.ExchangeRate;
import ru.toporkov.util.ConnectionManager;
import ru.toporkov.validator.exception.DAOException;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
    private static final String FIND_BY_ISO_CODES_SQL = FIND_ALL_SQL + """
            WHERE base_currency_id = ? AND target_currency_id = ?
            """;
    private static final String UPDATE_EXCHANGE_RATE_SQL = """
            UPDATE exchange_rate
            SET rate = ?
            WHERE base_currency_id = ? AND target_currency_id = ?
            """;
    private static final String SAVE_SQL = """
            INSERT INTO exchange_rate (base_currency_id, target_currency_id, rate)
            VALUES (?, ?, ?)
            """;
    private final CurrencyDAO currencyDAO = CurrencyDAO.getInstance();

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
    public List<ExchangeRate> findAll() throws SQLException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();

            List<ExchangeRate> result = new ArrayList<>();

            while(resultSet.next()) {
                result.add(buildExchangeRate(resultSet));
            }

            return result;
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
    public Optional<ExchangeRate> update(ExchangeRate entity) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(UPDATE_EXCHANGE_RATE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setObject(1, entity.getRate());
            preparedStatement.setObject(2, entity.getBaseCurrencyId());
            preparedStatement.setObject(3, entity.getTargetCurrencyId());

            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();
            ExchangeRate exchangeRate = null;

            if (generatedKeys.next()) {
                exchangeRate = buildExchangeRate(generatedKeys);
            }

            return Optional.ofNullable(exchangeRate);
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public ExchangeRate save(ExchangeRate entity) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setObject(1, entity.getBaseCurrencyId());
            preparedStatement.setObject(2, entity.getTargetCurrencyId());
            preparedStatement.setBigDecimal(3, entity.getRate());

            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();
            generatedKeys.next();
            entity.setId(generatedKeys.getObject("id", Integer.class));

            return entity;
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    public Optional<ExchangeRate> findByIds(Integer baseCurrencyId, Integer targetCurrencyId) throws SQLException  {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_ISO_CODES_SQL)) {

            preparedStatement.setObject(1, baseCurrencyId);
            preparedStatement.setObject(2, targetCurrencyId);

            var resultSet = preparedStatement.executeQuery();
            ExchangeRate exchangeRate = null;

            if (resultSet.next()) {
                exchangeRate = buildExchangeRate(resultSet);
            }

            return Optional.ofNullable(exchangeRate);
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
