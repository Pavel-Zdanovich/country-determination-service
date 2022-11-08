package com.example.countrydeterminationservice;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class CountryRepositoryTest {

    @Autowired
    private CountryRepository countryRepository;

    @Test
    void findById_EqualityIsTrue() {
        Country expected = new Country(1L, "Papua New Guinea", "675", 8);

        Country actual = countryRepository.findById(1L).orElseThrow();

        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void findByWrongId_OptionalIsEmpty() {
        Optional<Country> optional = countryRepository.findById(0L);

        Assertions.assertThat(optional).isEmpty();
    }

    @Test
    void findByCallingCodeAndNsnLengthNull_EqualityIsTrue() {
        List<Country> actual = countryRepository.findByCallingCodeAndNsnLengthNull("7");

        Assertions.assertThat(actual)
                .filteredOn("callingCode", "7")
                .filteredOn(country -> country.getNsnLength() == null)
                .hasSize(1);
    }

    @Test
    void findByCallingCodeAndNsnLength_EqualityIsTrue() {
        List<Country> actual = countryRepository.findByCallingCodeAndNsnLength("1", 10);

        Assertions.assertThat(actual)
                .filteredOn("callingCode", "1")
                .filteredOn("nsnLength", 10)
                .hasSize(2);
    }

}
