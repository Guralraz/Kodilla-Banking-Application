package com.greg.banking_app.repository;

import com.greg.banking_app.domain.CurrencyRates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface CurrencyRatesRepository extends JpaRepository<CurrencyRates, Long> {

    CurrencyRates findByCurrencyIsoCode(final String isoCode);
    boolean existsByCurrencyIsoCode(final String isoCode);
}
