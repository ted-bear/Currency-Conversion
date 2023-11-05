package ru.toporkov.entity;

import java.util.Objects;

public class Currency {
    private int id;
    private String code;
    private String fullName;
    private String sign;

    public Currency() {}

    public Currency(int id, String code, String fullName, String sign) {
        this.id = id;
        this.code = code;
        this.fullName = fullName;
        this.sign = sign;
    }

    public Currency(CurrencyBuilder currencyBuilder) {
        if (currencyBuilder == null) {
            throw new IllegalArgumentException("Please provide currency builder to build entity");
        }
        if (currencyBuilder.id < 0) {
            throw new IllegalArgumentException("Please provide valid currency id");
        }
        if (currencyBuilder.code == null ||currencyBuilder.code.trim().isEmpty()) {
            throw new IllegalArgumentException("Please provide valid currency code");
        }

        this.id = currencyBuilder.id;
        this.code = currencyBuilder.code;
        this.fullName = currencyBuilder.fullName;
        this.sign = currencyBuilder.sign;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public static CurrencyBuilder builder() {
        return new CurrencyBuilder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Currency currency = (Currency) o;
        return id == currency.id && Objects.equals(code, currency.code) && Objects.equals(fullName, currency.fullName) && Objects.equals(sign, currency.sign);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, fullName, sign);
    }

    @Override
    public String toString() {
        return "Currency{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", fullName='" + fullName + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }

    public static class CurrencyBuilder {
        protected int id;
        protected String code;
        protected String fullName;
        protected String sign;

        public CurrencyBuilder() {
            super();
        }

        public CurrencyBuilder id(int id) {
            this.id = id;
            return this;
        }

        public CurrencyBuilder code(String code) {
            this.code = code;
            return this;
        }

        public CurrencyBuilder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public CurrencyBuilder sign(String sign) {
            this.sign = sign;
            return this;
        }

        public Currency build() {
            Currency currency = null;
            if (validateCurrency()) {
                currency = new Currency(this);
            } else {
                System.out.println("Sorry! Currency objects can't be build without required details");
            }

            return currency;
        }

        private boolean validateCurrency() {
            return (id >= 0 && code != null && !code.trim().isEmpty());
        }
    }
}
