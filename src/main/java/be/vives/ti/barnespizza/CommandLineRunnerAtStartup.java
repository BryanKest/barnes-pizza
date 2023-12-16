package be.vives.ti.barnespizza;

import be.vives.ti.barnespizza.domain.*;
import be.vives.ti.barnespizza.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class CommandLineRunnerAtStartup implements CommandLineRunner {

    private final PizzaRepository pizzaRepository;
    private final BeverageRepository beverageRepository;
    private final OrderRepository orderRepository;
    private final AccountRepository accountRepository;
    private final BeverageOrderItemRepository beverageOrderItemRepository;
    private final PizzaOrderItemRepository pizzaOrderItemRepository;

    public CommandLineRunnerAtStartup(PizzaRepository pizzaRepository, BeverageRepository beverageRepository, OrderRepository orderRepository, AccountRepository accountRepository, BeverageOrderItemRepository beverageOrderItemRepository, PizzaOrderItemRepository pizzaOrderItemRepository) {
        this.pizzaRepository = pizzaRepository;
        this.beverageRepository = beverageRepository;
        this.orderRepository = orderRepository;
        this.accountRepository = accountRepository;
        this.beverageOrderItemRepository = beverageOrderItemRepository;
        this.pizzaOrderItemRepository = pizzaOrderItemRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Pizza
        Pizza margherita = new Pizza("Margherita", 8.5, "Tomato sauce, mozzarella, oregano");
        Pizza marinara = new Pizza("Marinara", 8.5, "Tomato sauce, garlic");
        Pizza quattroStagioni = new Pizza("Quattro Stagioni", 9.5, "Tomato sauce, mozzarella, ham, mushrooms, artichokes, olives, oregano");
        Pizza carbonara = new Pizza("Carbonara", 9.5, "Tomato sauce, mozzarella, bacon, eggs, parmesan, black pepper");
        Pizza hawai = new Pizza("Hawai", 9.5, "Tomato sauce, mozzarella, ham, pineapple");

        pizzaRepository.saveAll(List.of(margherita, marinara, quattroStagioni, carbonara, hawai));

        // Beverage
        Beverage cocaCola = new Beverage("Coca Cola", 2.5);
        Beverage fanta = new Beverage("Fanta", 2.5);
        Beverage sprite = new Beverage("Sprite", 2.5);
        Beverage iceTea = new Beverage("Ice Tea", 2.5);
        Beverage water = new Beverage("Water", 1.0);

        beverageRepository.saveAll(List.of(cocaCola, fanta, sprite, iceTea, water));

        // Account
        Account jhonDoe = new Account("jhondoe@gmail.com", "Jhon123!@", "Jhon", "Doe","Kerkstraat 1, 9000 Gent", "0470123456");
        Account marryAnn = new Account("marryann@gamil.com", "Marry123@!", "Marry", "Ann","Kerkstraat 2, 9000 Gent", "0470123457");
        Account stefanDeVos = new Account("stefan@msn.com", "Stefan123@!", "Stefan", "De Vos","Kerkstraat 3, 9000 Gent", "0470123458");
        Account stefanDeVos2 = new Account("stefan@gmail.com", "Stefan123!@", "Stefan", "De Vos","Kerkstraat 3, 9000 Gent", "0470123458");
        Account bryanDeVos = new Account("bryan@gmail.com", "Bryan123!@", "Bryan", "De Vos","Kerkstraat 3, 9000 Gent", "0470123458");

        accountRepository.saveAll(List.of(jhonDoe, marryAnn, stefanDeVos, stefanDeVos2, bryanDeVos));

        // Order
        Date date = new Date(2023, 12, 15, 8, 30);

        Order order1 = new Order(jhonDoe,  date, "Kerkstraat 1, 9000 Gent", 8.5);
        Order order2 = new Order(marryAnn,  date, "Kerkstraat 2, 9000 Gent", 11.0);
        Order order3 = new Order(stefanDeVos,  date, "Kerkstraat 3, 9000 Gent", 9.5);
        Order order4 = new Order(stefanDeVos2,  date, "Kerkstraat 3, 9000 Gent", 17.0);

        orderRepository.saveAll(List.of(order1, order2, order3, order4));


        // PizzaOrderItem
        PizzaOrderItem pizzaOrderItem1 = new PizzaOrderItem(order1, 1, margherita);
        PizzaOrderItem pizzaOrderItem2 = new PizzaOrderItem(order2, 1, marinara);
        PizzaOrderItem pizzaOrderItem3 = new PizzaOrderItem(order3, 1, quattroStagioni);
        PizzaOrderItem pizzaOrderItem4 = new PizzaOrderItem(order4, 2, margherita);

        pizzaOrderItemRepository.saveAll(List.of(pizzaOrderItem1, pizzaOrderItem2, pizzaOrderItem3, pizzaOrderItem4));

        // BeverageOrderItem
        BeverageOrderItem beverageOrderItem = new BeverageOrderItem(order2, 1, cocaCola);
        beverageOrderItemRepository.save(beverageOrderItem);
    }
}
