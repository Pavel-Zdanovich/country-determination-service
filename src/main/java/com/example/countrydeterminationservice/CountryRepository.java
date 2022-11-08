package com.example.countrydeterminationservice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

    @Query(value = "SELECT MIN(LENGTH(CALLING_CODE) + NSN_LENGTH) FROM COUNTRIES", nativeQuery = true)
    Integer findMinOfCodeAndNsn();

    List<Country> findByCallingCodeStartsWith(String callingCode);

}
