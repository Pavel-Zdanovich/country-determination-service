package com.example.countrydeterminationservice;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.hamcrest.Description;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@WebMvcTest
public class CountryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CountryService countryService;

    @BeforeEach
    void setup() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    void getById_OKAndEquality() {
        Long id = 1L;
        String name = "United States";
        String callingCode = "1";
        Integer nsnLength = 10;

        Mockito
                .when(countryService.getById(id))
                .thenReturn(new Country(id, name, callingCode, nsnLength));

        RestAssuredMockMvc
                .given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/countries/{id}", id)
                .then()
                .statusCode(200)
                .body("id", new TypeSafeMatcher<Integer>() {
                    @Override
                    public void describeTo(Description description) {
                        description.appendText("<").appendValue(id).appendText(">");
                    }

                    @Override
                    protected boolean matchesSafely(Integer item) {
                        return id == item.longValue();
                    }
                })
                .body("name", Matchers.equalTo(name))
                .body("callingCode", Matchers.equalTo(callingCode))
                .body("nsnLength", Matchers.equalTo(nsnLength));
    }

    @Test
    void getByMobile_OKAndEquality() {
        String mobile = "+1336644859";
        Long id = 1L;
        String name = "United States";
        String callingCode = "1";
        Integer nsnLength = 9;

        Mockito
                .when(countryService.getByMobile(mobile))
                .thenReturn(List.of(new Country(id, name, callingCode, nsnLength)));

        RestAssuredMockMvc
                .given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/countries?mobile={mobile}", mobile)
                .then()
                .statusCode(200)
                .body("id", Matchers.hasItem(new TypeSafeMatcher<Integer>() {
                                                 @Override
                                                 public void describeTo(Description description) {
                                                     description.appendText("<").appendValue(id).appendText(">");
                                                 }

                                                 @Override
                                                 protected boolean matchesSafely(Integer item) {
                                                     return id == item.longValue();
                                                 }
                                             }
                ))
                .body("name", Matchers.hasItem(name))
                .body("callingCode", Matchers.hasItem(callingCode))
                .body("nsnLength", Matchers.hasItem(nsnLength));
    }

}
