package com.example.countrydeterminationservice;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@Validated
public class CountryService {

    private final CountryRepository countryRepository;

    private final int minNsnLength;

    private final Node<Country> tree;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
        this.minNsnLength = countryRepository.findMinOfCodeAndNsn();
        List<Country> countries = countryRepository.findAll(Sort.by("callingCode"));
        this.tree = Node.createTree(
                countries,
                Country::getCallingCode
        );
    }

    Country getById(@NotNull Long id) {
        return countryRepository.findById(id).orElseThrow();
    }

    List<Country> getAll() {
        return countryRepository.findAll();
    }

    List<Country> getByMobile(@NotBlank String mobile) {
        String numbers = mobile.replaceAll("[+ ()-.]", StringUtils.EMPTY);

        String[] invalidCharacters = numbers.replaceAll("\\d", StringUtils.EMPTY).split(StringUtils.EMPTY);
        if (invalidCharacters.length != 0 && !invalidCharacters[0].isEmpty()) {
            String error = "Contains invalid characters: %s".formatted(Arrays.toString(invalidCharacters));
            log.error(error);
            throw new RuntimeException(error);
        }

        if (numbers.length() < minNsnLength) {
            String error = "Too short: %s [%d] < %d".formatted(numbers, numbers.length(), minNsnLength);
            log.error(error);
            throw new RuntimeException(error);
        }

        return this.tree.findClosestTo(numbers);
    }

    List<Country> getByMobileNew(String numbers) {
        return this.tree.findClosestTo(numbers);
    }

    List<Country> getByMobileOld(String numbers) {
        int codeLength = 1;
        List<Country> countries = countryRepository.findByCallingCodeStartsWith(numbers.substring(0, codeLength++));
        for (int i = 0; i < countries.size(); i++) {
            String callingCode = numbers.substring(0, codeLength++);
            List<Country> list = countries.stream()
                    .filter(country -> country.getCallingCode().startsWith(callingCode))
                    .toList();
            if (list.isEmpty()) {
                break;
            } else {
                countries = list;
            }
        }

        return countries;
    }

}
