package com.example.countrydeterminationservice;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CountryServiceTest {

    private CountryService countryService;

    @Mock
    private CountryRepository countryRepository;

    @BeforeEach
    void setup() {
        countryService = new CountryService(countryRepository);
    }

    @Test
    void getByIncorrectMobile_ThrowsException() {
        Assertions.assertThatExceptionOfType(RuntimeException.class).isThrownBy(() ->
                countryService.getByMobile("+375 (33) 66-44.859 a")
        );
        Assertions.assertThatExceptionOfType(RuntimeException.class).isThrownBy(() ->
                countryService.getByMobile("66-44.85")
        );
    }

    @Test
    void getByCorrectMobile_ReturnCountries() {
        String mobile = "+93336644859";

        Mockito.when(countryRepository.findByCallingCodeAndNsnLength("9", 10))
                .thenReturn(List.of());
        Mockito.when(countryRepository.findByCallingCodeAndNsnLength("93", 9))
                .thenReturn(List.of(new Country(1L, "Name1", "93", 9)));
        Mockito.when(countryRepository.findByCallingCodeAndNsnLength("933", 8))
                .thenReturn(List.of());
        Mockito.when(countryRepository.findByCallingCodeAndNsnLength("9333", 7))
                .thenReturn(List.of());

        List<Country> countries = countryService.getByMobile(mobile);

        Assertions.assertThat(countries)
                .filteredOn("id", 1L)
                .filteredOn("name", "Name1")
                .filteredOn("callingCode", "93")
                .filteredOn("nsnLength", 9)
                .hasSize(1);
    }

}
