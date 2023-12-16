package be.vives.ti.barnespizza.controller;

import be.vives.ti.barnespizza.domain.*;
import be.vives.ti.barnespizza.repository.*;
import be.vives.ti.barnespizza.requests.BeverageOrderItemRequest;
import be.vives.ti.barnespizza.requests.OrderCreateRequest;
import be.vives.ti.barnespizza.requests.OrderUpdateRequest;
import be.vives.ti.barnespizza.requests.PizzaOrderItemRequest;
import be.vives.ti.barnespizza.responses.OrderResponse;
import be.vives.ti.barnespizza.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/order")
@CrossOrigin("*")
public class OrderController {
    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final PizzaRepository pizzaRepository;
    private final BeverageRepository beverageRepository;
    private final PizzaOrderItemRepository pizzaOrderItemRepository;
    private final BeverageOrderItemRepository beverageOrderItemRepository;
    private final AccountRepository accountRepository;


    public OrderController(OrderRepository orderRepository, OrderService orderService, PizzaRepository pizzaRepository, BeverageRepository beverageRepository, PizzaOrderItemRepository pizzaOrderItemRepository, BeverageOrderItemRepository beverageOrderItemRepository, AccountRepository accountRepository) {
        this.orderRepository = orderRepository;
        this.orderService = orderService;
        this.pizzaRepository = pizzaRepository;
        this.beverageRepository = beverageRepository;
        this.pizzaOrderItemRepository = pizzaOrderItemRepository;
        this.beverageOrderItemRepository = beverageOrderItemRepository;
        this.accountRepository = accountRepository;
    }

    @GetMapping("/account/{id}")
    public ResponseEntity<OrderResponse> getOrderByAccountId(@PathVariable int id) {
        Optional<Order> order = orderRepository.findByAccountId(id);

        if (order.isPresent()) {
            return new ResponseEntity<>(new OrderResponse(order.get()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable int id) {
        Optional<Order> order = orderRepository.findById(id);

        if (order.isPresent()) {
            return new ResponseEntity<>(new OrderResponse(order.get()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/create")
    @Transactional
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderCreateRequest orderCreateRequest) {
        /*          When using the service
        Order order = orderService.createOrder(orderCreateRequest);
        return new ResponseEntity<>(new OrderResponse(order), HttpStatus.CREATED);
        */

        if(orderCreateRequest.getPizzas() == null && orderCreateRequest.getBeverages() == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Account account = accountRepository.findById(orderCreateRequest.getAccountId())
                .orElse(null);


        if(orderCreateRequest.getAccountId() == null || orderCreateRequest.getAddress() == null || account == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Order order = new Order(account, new Date(), orderCreateRequest.getAddress(), 0.0);
        orderRepository.save(order);

        if (orderCreateRequest.getPizzas() != null) {
            for (PizzaOrderItemRequest pizzaRequest : orderCreateRequest.getPizzas()) {
                if(pizzaRequest.getPizzaSize() == null){
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                Pizza pizza = pizzaRepository.findById(pizzaRequest.getPizzaId())
                        .orElseThrow(() -> new RuntimeException("Pizza not found with id: " + pizzaRequest.getPizzaId()));
                if(pizzaRequest.getPizzaSize().equals("small")){
                    pizza.setSize(Pizza.PizzaSize.SMALL);
                }
                else if(pizzaRequest.getPizzaSize().equals("medium")){
                    pizza.setSize(Pizza.PizzaSize.MEDIUM);
                }
                else if(pizzaRequest.getPizzaSize().equals("large")){
                    pizza.setSize(Pizza.PizzaSize.LARGE);
                }
                else {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                PizzaOrderItem pizzaOrderItem = new PizzaOrderItem(order, pizzaRequest.getAmount(), pizza);
                pizzaOrderItemRepository.save(pizzaOrderItem);
                if(order.getPizzaOrderItems() != null){
                    order.getPizzaOrderItems().add(pizzaOrderItem);
                }
                else {
                    order.setPizzaOrderItems(List.of(pizzaOrderItem));
                }
            }
        }

        if (orderCreateRequest.getBeverages() != null) {
            for (BeverageOrderItemRequest beverageRequest : orderCreateRequest.getBeverages()) {
                Beverage beverage = beverageRepository.findById(beverageRequest.getBeverageId())
                        .orElseThrow(() -> new RuntimeException("Beverage not found with id: " + beverageRequest.getBeverageId()));
                BeverageOrderItem beverageOrderItem = new BeverageOrderItem(order, beverageRequest.getAmount(), beverage);
                beverageOrderItemRepository.save(beverageOrderItem);
                if(order.getBeverageOrderItems() != null){
                    order.getBeverageOrderItems().add(beverageOrderItem);
                }
                else {
                    order.setBeverageOrderItems(List.of(beverageOrderItem));
                }
            }
        }

        order.setTotalPrice(calculateTotalPrice(order));
        orderRepository.save(order);

        return new ResponseEntity<>(new OrderResponse(order), HttpStatus.CREATED);

    }

    private Double calculateTotalPrice(Order order) {
        Double totalPrice = 0.0;

        if(order.getPizzaOrderItems() != null){
            for (PizzaOrderItem pizzaOrderItem : order.getPizzaOrderItems()) {
                totalPrice += pizzaOrderItem.getAmount() * pizzaOrderItem.getPizza().getPrice();
            }
        }

        if(order.getBeverageOrderItems() != null){
            for (BeverageOrderItem beverageOrderItem : order.getBeverageOrderItems()) {
                totalPrice += beverageOrderItem.getAmount() * beverageOrderItem.getBeverage().getPrice();
            }
        }
        return totalPrice;
    }

    @PutMapping("/update/{orderId}")
    public ResponseEntity<OrderResponse> updateOrder(@PathVariable("orderId") int orderId, @RequestBody @Valid OrderUpdateRequest orderUpdateRequest) {

        if(orderUpdateRequest.getAccountId() == null || orderUpdateRequest.getAddress() == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<Order> order = orderRepository.findById(orderId);

        if(orderUpdateRequest.getPizzas() == null && orderUpdateRequest.getBeverages() == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (order.isPresent()) {
            if(orderUpdateRequest.getPizzas() !=null){
                for(PizzaOrderItemRequest pizzaRequest : orderUpdateRequest.getPizzas()) {
                    Pizza pizza = pizzaRepository.findById(pizzaRequest.getPizzaId())
                            .orElseThrow(() -> new RuntimeException("Pizza not found with id: " + pizzaRequest.getPizzaId()));
                    PizzaOrderItem pizzaOrderItem = new PizzaOrderItem(order.get(), pizzaRequest.getAmount(), pizza);
                    pizzaOrderItemRepository.save(pizzaOrderItem);
                    order.get().setPizzaOrderItems(List.of(pizzaOrderItem));
                }
            }

            if(orderUpdateRequest.getBeverages() !=null){
                for(BeverageOrderItemRequest beverageRequest : orderUpdateRequest.getBeverages()) {
                    Beverage beverage = beverageRepository.findById(beverageRequest.getBeverageId())
                            .orElseThrow(() -> new RuntimeException("Beverage not found with id: " + beverageRequest.getBeverageId()));
                    BeverageOrderItem beverageOrderItem = new BeverageOrderItem(order.get(), beverageRequest.getAmount(), beverage);
                    beverageOrderItemRepository.save(beverageOrderItem);
                    order.get().setBeverageOrderItems(List.of(beverageOrderItem));
                }
            }

            order.get().setAddress(orderUpdateRequest.getAddress());
            order.get().setOrderTime(new Date());
            order.get().setTotalPrice(calculateTotalPrice(order.get()));
            orderRepository.save(order.get());
            return new ResponseEntity<>(new OrderResponse(order.get()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable("orderId") Integer orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            orderRepository.deleteById(orderId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
