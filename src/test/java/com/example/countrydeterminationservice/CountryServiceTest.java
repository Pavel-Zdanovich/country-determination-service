package com.example.countrydeterminationservice;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CountryServiceTest {

    private CountryService countryService;

    @Mock
    private CountryRepository countryRepository;

    @BeforeEach
    void setup() {
        Mockito.when(countryRepository.findMinOfCodeAndNsn()).thenReturn(7);
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

}
