package be.vives.ti.barnespizza.controller;

import be.vives.ti.barnespizza.domain.Beverage;
import be.vives.ti.barnespizza.repository.BeverageRepository;
import be.vives.ti.barnespizza.responses.BeverageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/beverage")
@CrossOrigin("*")
public class BeverageController {

    private final BeverageRepository beverageRepository;

    public BeverageController(BeverageRepository beverageRepository) {
        this.beverageRepository = beverageRepository;
    }

    @GetMapping("/all")
    public ResponseEntity<List<BeverageResponse>> getAllBeverages() {
        List<BeverageResponse> beverageResponses = beverageRepository.findAll().stream().map(BeverageResponse::new).toList();

        if (beverageResponses.isEmpty()) {
            return new ResponseEntity<>(beverageResponses, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(beverageResponses, HttpStatus.OK);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<BeverageResponse> getBeverageByName(@PathVariable("name") String name) {
        Optional<BeverageResponse> beverage = beverageRepository.findByName(name).map(BeverageResponse::new);

        if (beverage.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(beverage.get(), HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<BeverageResponse> getBeverageById(@PathVariable("id") int id) {
        Optional<BeverageResponse> beverage = beverageRepository.findById(id).map(BeverageResponse::new);

        if (beverage.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(beverage.get(), HttpStatus.OK);
    }

}
