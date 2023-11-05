package ru.toporkov.entity;

import java.util.Objects;

public class Currency {
    private Integer id;
    private String code;
    private String fullName;
    private String sign;

    public Currency() {}

    public Currency(Integer id, String code, String fullName, String sign) {
        this.id = id;
        this.code = code;
        this.fullName = fullName;
        this.sign = sign;
    }

    public Currency(CurrencyBuilder currencyBuilder) {
        this.id = currencyBuilder.id;
        this.code = currencyBuilder.code;
        this.fullName = currencyBuilder.fullName;
        this.sign = currencyBuilder.sign;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
        return Objects.equals(id, currency.id) && Objects.equals(code, currency.code) && Objects.equals(fullName, currency.fullName) && Objects.equals(sign, currency.sign);
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
        protected Integer id;
        protected String code;
        protected String fullName;
        protected String sign;

        public CurrencyBuilder() {
            super();
        }

        public CurrencyBuilder id(Integer id) {
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
            return new Currency(this);
        }
    }
}
