package ru.toporkov.entity;

import java.math.BigDecimal;
import java.util.Objects;

public class ExchangeRate {
    private int id;
    private int baseCurrencyId;
    private int targetCurrencyId;
    private BigDecimal rate;

    public ExchangeRate() {
    }

    public ExchangeRate(int id, int baseCurrencyId, int targetCurrencyId, BigDecimal rate) {
        this.id = id;
        this.baseCurrencyId = baseCurrencyId;
        this.targetCurrencyId = targetCurrencyId;
        this.rate = rate;
    }

    public ExchangeRate(ExchangeRateBuilder exchangeRateBuilder) {
        if (exchangeRateBuilder == null) {
            throw new IllegalArgumentException("Please provide exchange builder");
        }
        if (exchangeRateBuilder.id < 0 ) {
            throw new IllegalArgumentException("Please provide valid exchange builder id");
        }
        if (exchangeRateBuilder.baseCurrencyId < 0 || exchangeRateBuilder.targetCurrencyId < 0) {
            throw new IllegalArgumentException("Please provide valid currencies ids");
        }

        this.id = exchangeRateBuilder.id;
        this.baseCurrencyId = exchangeRateBuilder.baseCurrencyId;
        this.targetCurrencyId = exchangeRateBuilder.targetCurrencyId;
        this.rate = exchangeRateBuilder.rate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBaseCurrencyId() {
        return baseCurrencyId;
    }

    public void setBaseCurrencyId(int baseCurrencyId) {
        this.baseCurrencyId = baseCurrencyId;
    }

    public int getTargetCurrencyId() {
        return targetCurrencyId;
    }

    public void setTargetCurrencyId(int targetCurrencyId) {
        this.targetCurrencyId = targetCurrencyId;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public ExchangeRateBuilder builder() {
        return new ExchangeRateBuilder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExchangeRate that = (ExchangeRate) o;
        return id == that.id && baseCurrencyId == that.baseCurrencyId && targetCurrencyId == that.targetCurrencyId && Objects.equals(rate, that.rate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, baseCurrencyId, targetCurrencyId, rate);
    }

    @Override
    public String toString() {
        return "ExchangeRate{" +
                "id=" + id +
                ", baseCurrencyId=" + baseCurrencyId +
                ", targetCurrencyId=" + targetCurrencyId +
                ", rate=" + rate +
                '}';
    }

    public static class ExchangeRateBuilder {
        protected int id;
        protected int baseCurrencyId;
        protected int targetCurrencyId;
        protected BigDecimal rate;

        public ExchangeRateBuilder id(int id) {
            this.id = id;
            return this;
        }

        public ExchangeRateBuilder baseCurrencyId(int baseCurrencyId) {
            this.baseCurrencyId = baseCurrencyId;
            return this;
        }

        public ExchangeRateBuilder targetCurrencyId(int targetCurrencyId) {
            this.targetCurrencyId = targetCurrencyId;
            return this;
        }

        public ExchangeRateBuilder rate(BigDecimal rate) {
            this.rate = rate;
            return this;
        }

        public ExchangeRate build() {
            ExchangeRate rate = null;
            if (validateExchangeRate()) {
                rate = new ExchangeRate(this);
            } else {
                System.out.println("Sorry! ExchangeRate objects can't be build without required details");
            }
            return rate;
        }

        private boolean validateExchangeRate() {
            return this.id > -1 && this.targetCurrencyId > -1 && this.baseCurrencyId > -1;
        }
    }
}
