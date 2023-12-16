package be.vives.ti.barnespizza.controller;

import be.vives.ti.barnespizza.domain.Beverage;
import be.vives.ti.barnespizza.repository.BeverageRepository;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BeverageController.class)
class BeverageControllerTest {

    @Autowired
    private MockMvc mvc;

    private Beverage beverage1;
    private Beverage beverage2;
    private Beverage beverage3;

    @MockBean
    private BeverageRepository beverageRepository;

    @BeforeEach
    void setUp() {
        beverage1 = new Beverage("Coca Cola", 2.0);
        beverage2 = new Beverage("Fanta", 2.0);
        beverage3 = new Beverage("Sprite", 2.0);
    }

    @Test
    void getAllBeverages() throws Exception {
        when(beverageRepository.findAll()).thenReturn(List.of(beverage1, beverage2, beverage3));

        mvc.perform(get("/beverage/all"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Coca Cola"))
                .andExpect(jsonPath("$[1].name").value("Fanta"))
                .andExpect(jsonPath("$[2].name").value("Sprite"));
    }

    @Test
    void getBeverageByName() throws Exception {
        when(beverageRepository.findByName("Coca Cola")).thenReturn(Optional.of(beverage1));

        mvc.perform(get("/beverage/name/Coca Cola"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Coca Cola"));
    }

    @Test
    void getAllBeveragesEmpty() throws Exception {
        when(beverageRepository.findAll()).thenReturn(List.of());

        mvc.perform(get("/beverage/all"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getBeverageByWrongName() throws Exception {
        when(beverageRepository.findByName("Coca Cola")).thenReturn(Optional.empty());

        mvc.perform(get("/beverage/name/Fanta"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getBeverageById() throws Exception {
        when(beverageRepository.findById(1)).thenReturn(Optional.of(beverage1));

        mvc.perform(get("/beverage/id/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Coca Cola"));
    }

    @Test
    void getBeverageByWrongId() throws Exception {
        when(beverageRepository.findById(1)).thenReturn(Optional.empty());

        mvc.perform(get("/beverage/id/2"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getBeverageByIdNull() throws Exception {
        when(beverageRepository.findById(1)).thenReturn(Optional.empty());

        mvc.perform(get("/beverage/id/"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

}