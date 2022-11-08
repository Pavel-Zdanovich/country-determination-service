package com.example.countrydeterminationservice;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "COUNTRIES")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Country {

    @Id
    private Long id;

    @Column
    @NotBlank
    private String name;

    @Column
    @NotBlank
    @Pattern(regexp = "\\d+")
    private String callingCode;

    @Column
    @Positive
    private Integer nsnLength;

}
