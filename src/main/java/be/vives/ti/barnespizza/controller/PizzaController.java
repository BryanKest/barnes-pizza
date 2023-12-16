package be.vives.ti.barnespizza.controller;

import be.vives.ti.barnespizza.domain.Pizza;
import be.vives.ti.barnespizza.repository.PizzaRepository;
import be.vives.ti.barnespizza.responses.AccountResponse;
import be.vives.ti.barnespizza.responses.PizzaResponse;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pizza")
@CrossOrigin("*")
public class PizzaController {

    private final PizzaRepository pizzaRepository;

    public PizzaController(PizzaRepository pizzaRepository) {
        this.pizzaRepository = pizzaRepository;
    }

    @GetMapping("/all")
    public ResponseEntity<List<PizzaResponse>> getAllPizzas() {
        List<Pizza> pizzas = this.pizzaRepository.findAll();

        List<PizzaResponse> pizzaResponses = pizzas.stream()
                .map(PizzaResponse::new).toList();

        if (pizzas.isEmpty()) {
            return new ResponseEntity<>(pizzaResponses, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(pizzaResponses, HttpStatus.OK);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<PizzaResponse> getPizzaByName(@PathVariable("name") String name) {
        Optional<Pizza> pizza = pizzaRepository.findByName(name);

        if (pizza.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new PizzaResponse(pizza.get()), HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<PizzaResponse> getPizzaById(@PathVariable("id") int id) {
        Optional<Pizza> pizza = pizzaRepository.findById(id);

        if (pizza.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new PizzaResponse(pizza.get()), HttpStatus.OK);
    }
}
