package be.vives.ti.barnespizza.controller;

import be.vives.ti.barnespizza.domain.Pizza;
import be.vives.ti.barnespizza.repository.PizzaRepository;
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

@WebMvcTest(PizzaController.class)
class PizzaControllerTest {

    @Autowired
    private MockMvc mvc;

    private Pizza pizza1;
    private Pizza pizza2;
    private Pizza pizza3;

    @MockBean
    private PizzaRepository pizzaRepository;

    @BeforeEach
    void setUp() {
        pizza1 = new Pizza("Pizza1", 10.0, "This is pizza1");
        pizza2 = new Pizza("Pizza2", 10.0, "This is pizza2");
        pizza3 = new Pizza("Pizza3", 10.0, "This is pizza3");
    }

    @Test
    void getAllPizzas() throws Exception {
        when(pizzaRepository.findAll()).thenReturn(List.of(pizza1, pizza2, pizza3));

        mvc.perform(get("/pizza/all"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Pizza1"))
                .andExpect(jsonPath("$[1].name").value("Pizza2"))
                .andExpect(jsonPath("$[2].name").value("Pizza3"));
    }

    @Test
    void getPizzaByName() throws Exception {
        when(pizzaRepository.findByName("Pizza1")).thenReturn(Optional.of(pizza1));

        mvc.perform(get("/pizza/name/Pizza1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Pizza1"));
    }

    @Test
    void getAllPizzasEmpty() throws Exception {
        when(pizzaRepository.findAll()).thenReturn(List.of());

        mvc.perform(get("/pizza/all"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getPizzaByNameWrong() throws Exception {
        when(pizzaRepository.findByName("Pizza1")).thenReturn(Optional.empty());

        mvc.perform(get("/pizza/name/aap"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getPizzaById() throws Exception {
        when(pizzaRepository.findById(1)).thenReturn(Optional.of(pizza1));

        mvc.perform(get("/pizza/id/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Pizza1"));
    }

    @Test
    void getPizzaByWrongId() throws Exception {
        when(pizzaRepository.findById(1)).thenReturn(Optional.empty());

        mvc.perform(get("/pizza/id/2"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getPizzaByIdNull() throws Exception {
        when(pizzaRepository.findById(1)).thenReturn(Optional.empty());

        mvc.perform(get("/pizza/id/"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }


}