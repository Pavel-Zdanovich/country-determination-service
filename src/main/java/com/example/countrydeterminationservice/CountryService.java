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
import java.util.TreeMap;

@Service
@Slf4j
@Validated
public class CountryService {

    private final CountryRepository countryRepository;

    private final int minNsnLength;

    private final Node<Country> tree;

    private final TreeMap<String, Country> treeMap;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
        this.minNsnLength = countryRepository.findMinOfCodeAndNsn();
        List<Country> countries = countryRepository.findAll(Sort.by("callingCode"));
        this.tree = Node.createTree(
                countries,
                Country::getCallingCode
        );
        this.treeMap = new TreeMap<>();
        for (Country country : countries) {
            this.treeMap.put(country.getCallingCode(), country);
        }
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

    List<Country> getByMobileMap(String numbers) {
        return List.of(this.treeMap.lowerEntry(numbers).getValue());
    }

    List<Country> getByMobileNew(String numbers) {
        return this.tree.findClosestTo(numbers);
    }

}
