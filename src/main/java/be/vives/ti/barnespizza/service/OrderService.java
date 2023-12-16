package be.vives.ti.barnespizza.service;

import be.vives.ti.barnespizza.domain.*;
import be.vives.ti.barnespizza.repository.*;
import be.vives.ti.barnespizza.requests.BeverageOrderItemRequest;
import be.vives.ti.barnespizza.requests.OrderCreateRequest;
import be.vives.ti.barnespizza.requests.PizzaOrderItemRequest;
import be.vives.ti.barnespizza.responses.OrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final PizzaOrderItemRepository pizzaOrderItemRepository;
    private final BeverageOrderItemRepository beverageOrderItemRepository;
    private final AccountRepository accountRepository;
    private final PizzaRepository pizzaRepository;
    private final BeverageRepository beverageRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository,
                        PizzaOrderItemRepository pizzaOrderItemRepository,
                        BeverageOrderItemRepository beverageOrderItemRepository,
                        AccountRepository accountRepository,
                        PizzaRepository pizzaRepository,
                        BeverageRepository beverageRepository) {
        this.orderRepository = orderRepository;
        this.pizzaOrderItemRepository = pizzaOrderItemRepository;
        this.beverageOrderItemRepository = beverageOrderItemRepository;
        this.accountRepository = accountRepository;
        this.pizzaRepository = pizzaRepository;
        this.beverageRepository = beverageRepository;
    }

    @Transactional
    public Order createOrder(OrderCreateRequest orderCreateRequest) {

        Account account = accountRepository.findById(orderCreateRequest.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + orderCreateRequest.getAccountId()));

        Order order = new Order(account, new Date(), orderCreateRequest.getAddress(), 0.0);
        orderRepository.save(order);

        if (orderCreateRequest.getPizzas() != null) {
            for (PizzaOrderItemRequest pizzaRequest : orderCreateRequest.getPizzas()) {
                Pizza pizza = pizzaRepository.findById(pizzaRequest.getPizzaId())
                        .orElseThrow(() -> new RuntimeException("Pizza not found with id: " + pizzaRequest.getPizzaId()));
                PizzaOrderItem pizzaOrderItem = new PizzaOrderItem(order, pizzaRequest.getAmount(), pizza);
                pizzaOrderItemRepository.save(pizzaOrderItem);
                order.getPizzaOrderItems().add(pizzaOrderItem);
            }
        }

        if (orderCreateRequest.getBeverages() != null) {
            for (BeverageOrderItemRequest beverageRequest : orderCreateRequest.getBeverages()) {
                Beverage beverage = beverageRepository.findById(beverageRequest.getBeverageId())
                        .orElseThrow(() -> new RuntimeException("Beverage not found with id: " + beverageRequest.getBeverageId()));
                BeverageOrderItem beverageOrderItem = new BeverageOrderItem(order, beverageRequest.getAmount(), beverage);
                beverageOrderItemRepository.save(beverageOrderItem);
                order.getBeverageOrderItems().add(beverageOrderItem);
            }
        }

        order.setTotalPrice(calculateTotalPrice(order));
        orderRepository.save(order);

        return order;
    }

    private Double calculateTotalPrice(Order order) {
        Double totalPrice = 0.0;

        for (PizzaOrderItem pizzaOrderItem : order.getPizzaOrderItems()) {
            totalPrice += pizzaOrderItem.getAmount() * pizzaOrderItem.getPizza().getPrice();
        }

        for (BeverageOrderItem beverageOrderItem : order.getBeverageOrderItems()) {
            totalPrice += beverageOrderItem.getAmount() * beverageOrderItem.getBeverage().getPrice();
        }

        return totalPrice;
    }
}
