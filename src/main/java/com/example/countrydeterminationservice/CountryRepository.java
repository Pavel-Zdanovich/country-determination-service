package com.example.countrydeterminationservice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

    List<Country> findByCallingCodeAndNsnLengthNull(String callingCode);

    List<Country> findByCallingCodeAndNsnLength(String callingCode, int nsnLength);

}
