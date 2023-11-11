package ru.toporkov.validator;

import ru.toporkov.dao.CurrencyDAO;
import ru.toporkov.dto.CreateCurrencyDTO;
import ru.toporkov.validator.exception.CurrencyAlreadyExistException;
import ru.toporkov.validator.exception.InvalidFormFieldException;
import ru.toporkov.validator.exception.InvalidLengthISOCodeException;

import java.util.Optional;

import static ru.toporkov.validator.ErrorMessage.CURRENCY_ALREADY_EXISTS;
import static ru.toporkov.validator.ErrorMessage.INVALID_CURRENCY_ISO_CODE;
import static ru.toporkov.validator.ErrorMessage.INVALID_FORM_FIELD;

public class CreateCurrencyValidator implements Validator<CreateCurrencyDTO> {

    private static final Integer ISO_CODE_LENGTH = 3;
    private static volatile CreateCurrencyValidator instance;
    private final CurrencyDAO currencyDAO = CurrencyDAO.getInstance();

    private CreateCurrencyValidator() {}

    public static CreateCurrencyValidator getInstance() {
        if (instance == null) {
            synchronized (CreateCurrencyValidator.class) {
                if (instance == null) {
                    instance = new CreateCurrencyValidator();
                }
            }
        }
        return instance;
    }

    @Override
    public boolean isValid(CreateCurrencyDTO object) throws CurrencyAlreadyExistException, InvalidFormFieldException, InvalidLengthISOCodeException {

        if (currencyDAO.findByIsoCode(object.code()).isPresent()) {
            throw new CurrencyAlreadyExistException(CURRENCY_ALREADY_EXISTS);
        }

        if (object.code() == null || object.fullName() == null) {
            throw new InvalidFormFieldException(INVALID_FORM_FIELD);
        }

        if (object.code().length() != ISO_CODE_LENGTH) {
            throw new InvalidLengthISOCodeException(INVALID_CURRENCY_ISO_CODE);
        }

        return true;
    }
}
