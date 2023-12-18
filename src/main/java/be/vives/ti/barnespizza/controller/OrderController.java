package be.vives.ti.barnespizza.controller;

import be.vives.ti.barnespizza.domain.*;
import be.vives.ti.barnespizza.repository.*;
import be.vives.ti.barnespizza.requests.BeverageOrderItemRequest;
import be.vives.ti.barnespizza.requests.OrderCreateRequest;
import be.vives.ti.barnespizza.requests.OrderUpdateRequest;
import be.vives.ti.barnespizza.requests.PizzaOrderItemRequest;
import be.vives.ti.barnespizza.responses.BeverageOrderResponse;
import be.vives.ti.barnespizza.responses.OrderResponse;
import be.vives.ti.barnespizza.responses.PizzaOrderResponse;
import be.vives.ti.barnespizza.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
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
    public ResponseEntity<List<OrderResponse>> getOrdersByAccountId(@PathVariable int id) {
        List<Order> orders = orderRepository.findByAccountId(id);

        if (!orders.isEmpty()) {
            return new ResponseEntity<>(convertToOrderResponses(orders), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable int id) {
        Optional<Order> order = orderRepository.findById(id);

        if (order.isPresent()) {
            return new ResponseEntity<>(convertToOrderResponse(order.get()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/create")
    @Transactional
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderCreateRequest orderCreateRequest) {
        if (orderCreateRequest.getPizzas() == null && orderCreateRequest.getBeverages() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Account account = accountRepository.findById(orderCreateRequest.getAccountId())
                .orElse(null);

        if (orderCreateRequest.getAccountId() == null || orderCreateRequest.getAddress() == null || account == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Order order = new Order(account, new Date(), orderCreateRequest.getAddress(), 0.0);
        orderRepository.save(order);

        if (orderCreateRequest.getPizzas() != null && !orderCreateRequest.getPizzas().isEmpty()) {
            for (PizzaOrderItemRequest pizzaRequest : orderCreateRequest.getPizzas()) {
                Pizza pizza = pizzaRepository.findById(pizzaRequest.getPizzaId()).orElse(null);
                if (pizzaRequest.getPizzaSize() == null || pizza == null) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
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
                order.getPizzaOrderItems().add(pizzaOrderItem);
            }
        }

        if (orderCreateRequest.getBeverages() != null && !orderCreateRequest.getBeverages().isEmpty()) {
            for (BeverageOrderItemRequest beverageRequest : orderCreateRequest.getBeverages()) {
                Beverage beverage = beverageRepository.findById(beverageRequest.getBeverageId())
                        .orElse(null);
                if (beverage == null) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                BeverageOrderItem beverageOrderItem = new BeverageOrderItem(order, beverageRequest.getAmount(), beverage);
                beverageOrderItemRepository.save(beverageOrderItem);
                order.getBeverageOrderItems().add(beverageOrderItem);
            }
        }

        order.setTotalPrice(calculateTotalPrice(order));
        orderRepository.save(order);

        return new ResponseEntity<>(convertToOrderResponse(order), HttpStatus.CREATED);
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
    @Transactional
    public ResponseEntity<OrderResponse> updateOrder(@PathVariable("orderId") Integer orderId, @Valid @RequestBody OrderUpdateRequest orderUpdateRequest) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();

            order.getPizzaOrderItems().clear();
            order.getBeverageOrderItems().clear();

            if (orderUpdateRequest.getPizzas() != null && !orderUpdateRequest.getPizzas().isEmpty()) {
                for (PizzaOrderItemRequest pizzaRequest : orderUpdateRequest.getPizzas()) {
                    Pizza pizza = pizzaRepository.findById(pizzaRequest.getPizzaId()).orElse(null);
                    if (pizzaRequest.getPizzaSize() == null || pizza == null) {
                        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                    }
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
                    order.getPizzaOrderItems().add(pizzaOrderItem);
                }
            }

            if (orderUpdateRequest.getBeverages() != null && !orderUpdateRequest.getBeverages().isEmpty()) {
                for (BeverageOrderItemRequest beverageRequest : orderUpdateRequest.getBeverages()) {
                    Beverage beverage = beverageRepository.findById(beverageRequest.getBeverageId())
                            .orElse(null);
                    if (beverage == null) {
                        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                    }
                    BeverageOrderItem beverageOrderItem = new BeverageOrderItem(order, beverageRequest.getAmount(), beverage);
                    beverageOrderItemRepository.save(beverageOrderItem);
                    order.getBeverageOrderItems().add(beverageOrderItem);
                }
            }

            order.setTotalPrice(calculateTotalPrice(order));
            orderRepository.save(order);

            return new ResponseEntity<>(convertToOrderResponse(order), HttpStatus.OK);
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

    private OrderResponse convertToOrderResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getAccount(),
                convertPizzaOrderItems(order.getPizzaOrderItems()),
                convertBeverageOrderItems(order.getBeverageOrderItems()),
                order.getOrderTime(),
                order.getAddress(),
                order.getTotalPrice()
        );
    }
    private List<OrderResponse> convertToOrderResponses(List<Order> orders) {
        return Optional.ofNullable(orders)
                .orElse(Collections.emptyList())
                .stream()
                .map(order -> new OrderResponse(
                        order.getId(),
                        order.getAccount(),
                        convertPizzaOrderItems(order.getPizzaOrderItems()),
                        convertBeverageOrderItems(order.getBeverageOrderItems()),
                        order.getOrderTime(),
                        order.getAddress(),
                        order.getTotalPrice()
                ))
                .toList();
    }

    private List<PizzaOrderResponse> convertPizzaOrderItems(List<PizzaOrderItem> pizzaOrderItems) {
        return Optional.ofNullable(pizzaOrderItems)
                .orElse(Collections.emptyList())
                .stream()
                .map(pizzaOrderItem -> new PizzaOrderResponse(pizzaOrderItem.getPizza(), pizzaOrderItem.getAmount()))
                .toList();
    }

    private List<BeverageOrderResponse> convertBeverageOrderItems(List<BeverageOrderItem> beverageOrderItems) {
        return Optional.ofNullable(beverageOrderItems)
                .orElse(Collections.emptyList())
                .stream()
                .map(beverageOrderItem -> new BeverageOrderResponse(beverageOrderItem.getBeverage(), beverageOrderItem.getAmount()))
                .toList();
    }
}
