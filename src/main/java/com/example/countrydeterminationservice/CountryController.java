package com.example.countrydeterminationservice;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/countries")
@RequiredArgsConstructor
@ResponseStatus(HttpStatus.OK)
public class CountryController {

    private final CountryService countryService;

    @GetMapping("/{id}")
    Country getById(@PathVariable Long id) {
        return countryService.getById(id);
    }

    @GetMapping
    List<Country> getByMobile(@RequestParam(required = false) String mobile) {
        if (StringUtils.isBlank(mobile)) {
            return countryService.getAll();
        } else {
            return countryService.getByMobile(mobile);
        }
    }

}
