package ru.toporkov.entity;

import java.math.BigDecimal;
import java.util.Objects;

public class ExchangeRate {
    private Integer id;
    private Integer baseCurrencyId;
    private Integer targetCurrencyId;
    private BigDecimal rate;

    public ExchangeRate() {
    }

    public ExchangeRate(Integer id, Integer baseCurrencyId, Integer targetCurrencyId, BigDecimal rate) {
        this.id = id;
        this.baseCurrencyId = baseCurrencyId;
        this.targetCurrencyId = targetCurrencyId;
        this.rate = rate;
    }

    public ExchangeRate(ExchangeRateBuilder exchangeRateBuilder) {
        this.id = exchangeRateBuilder.id;
        this.baseCurrencyId = exchangeRateBuilder.baseCurrencyId;
        this.targetCurrencyId = exchangeRateBuilder.targetCurrencyId;
        this.rate = exchangeRateBuilder.rate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBaseCurrencyId() {
        return baseCurrencyId;
    }

    public void setBaseCurrencyId(Integer baseCurrencyId) {
        this.baseCurrencyId = baseCurrencyId;
    }

    public Integer getTargetCurrencyId() {
        return targetCurrencyId;
    }

    public void setTargetCurrencyId(Integer targetCurrencyId) {
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
        return Objects.equals(id, that.id) && Objects.equals(baseCurrencyId, that.baseCurrencyId) && Objects.equals(targetCurrencyId, that.targetCurrencyId) && Objects.equals(rate, that.rate);
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
        protected Integer id;
        protected Integer baseCurrencyId;
        protected Integer targetCurrencyId;
        protected BigDecimal rate;

        public ExchangeRateBuilder id(Integer id) {
            this.id = id;
            return this;
        }

        public ExchangeRateBuilder baseCurrencyId(Integer baseCurrencyId) {
            this.baseCurrencyId = baseCurrencyId;
            return this;
        }

        public ExchangeRateBuilder targetCurrencyId(Integer targetCurrencyId) {
            this.targetCurrencyId = targetCurrencyId;
            return this;
        }

        public ExchangeRateBuilder rate(BigDecimal rate) {
            this.rate = rate;
            return this;
        }

        public ExchangeRate build() {
            return new ExchangeRate(this);
        }
    }
}
