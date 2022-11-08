package com.example.countrydeterminationservice;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Validated
public class CountryService {

    private static final int MIN_NUMBERS_LENGTH = 7;

    private static final int MAX_CODE_LENGTH = 7;

    private static final int MIN_NSN_LENGTH = 4;

    private final CountryRepository countryRepository;

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

        if (numbers.length() < MIN_NUMBERS_LENGTH) {
            String error = "Too short: %s [%d] < %d".formatted(numbers, numbers.length(), MIN_NUMBERS_LENGTH);
            log.error(error);
            throw new RuntimeException(error);
        }

        Set<Country> countries = new HashSet<>();
        for (int codeLength = 1; codeLength <= MAX_CODE_LENGTH; codeLength++) {
            String callingCode = numbers.substring(0, codeLength);
            int nsnLength = numbers.length() - codeLength;
            if (nsnLength < MIN_NSN_LENGTH) {
                break;
            }
            List<Country> list = countryRepository.findByCallingCodeAndNsnLength(callingCode, nsnLength);
            if (list.isEmpty()) {
                list = countryRepository.findByCallingCodeAndNsnLengthNull(callingCode);
            }
            countries.addAll(list);
        }

        return new ArrayList<>(countries);
    }

}
