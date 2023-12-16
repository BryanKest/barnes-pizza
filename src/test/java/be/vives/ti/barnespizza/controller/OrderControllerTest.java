package be.vives.ti.barnespizza.controller;

import be.vives.ti.barnespizza.domain.*;
import be.vives.ti.barnespizza.repository.*;
import be.vives.ti.barnespizza.requests.BeverageOrderItemRequest;
import be.vives.ti.barnespizza.requests.OrderCreateRequest;
import be.vives.ti.barnespizza.requests.OrderUpdateRequest;
import be.vives.ti.barnespizza.requests.PizzaOrderItemRequest;
import be.vives.ti.barnespizza.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Account user1;
    private Account user2;
    private Beverage beverage1;
    private Beverage beverage2;
    private Pizza pizza1;
    private Pizza pizza2;
    private Pizza pizza3;

    private Order order1;
    private Order order2;
    private PizzaOrderItem orderItem1;
    private BeverageOrderItem orderItem2;
    private PizzaOrderItem orderItem3;
    private Date date1;

    @MockBean
    private OrderRepository orderRepository;
    @MockBean
    private OrderService orderService;
    @MockBean
    private AccountRepository accountRepository;
    @MockBean
    private PizzaRepository pizzaRepository;
    @MockBean
    private BeverageRepository beverageRepository;
    @MockBean
    private PizzaOrderItemRepository pizzaOrderItemRepository;
    @MockBean
    private BeverageOrderItemRepository beverageOrderItemRepository;


    @BeforeEach
    void setUp() {
        user1 = new Account("user1", "password1", "Bryan", "Kesteloot", "Straat", "047123456");
        user1.setId(1);
        user2 = new Account("user2", "password2", "Jos", "Dammen", "Straat", "047123456");
        user2.setId(2);
        beverage1 = new Beverage("Cola", 2.5);
        beverage1.setId(1);
        beverage2 = new Beverage("Fanta", 2.5);
        beverage2.setId(2);
        pizza1 = new Pizza("Hawaii", 10.0, "tomato sauce, mozzarella, pineaplle, ham");
        pizza1.setId(1);
        pizza1.setSize(Pizza.PizzaSize.LARGE);
        pizza2 = new Pizza("Margherita", 10.0, "tomato sauce, mozzarella, oregano");
        pizza2.setId(2);
        pizza2.setSize(Pizza.PizzaSize.SMALL);
        pizza3 = new Pizza("Quattro Stagioni", 10.0, "tomato sauce, mozzarella, ham, mushrooms, artichokes, olives, oregano");
        pizza3.setId(3);
        date1 = new Date(2021, 5, 1, 12, 0);
        order1 = new Order(user1, date1, "Straat", 20.0);
        order1.setId(1);
        order2 = new Order(user2, date1, "Straat", 20.0);
        order2.setId(2);
        orderItem1 = new PizzaOrderItem(order1, 2, pizza1);
        orderItem1.setId(1);
        orderItem2 = new BeverageOrderItem(order1, 2, beverage1);
        orderItem2.setId(2);
        orderItem3 = new PizzaOrderItem(order2, 1, pizza1);
        orderItem3.setId(3);
        order1.setBeverageOrderItems(List.of(orderItem2));
        order1.setPizzaOrderItems(List.of(orderItem1));
        order2.setPizzaOrderItems(List.of(orderItem3));
    }

    @Test
    void getOrderByAccountId() throws Exception {
        when(orderRepository.findByAccountId(1)).thenReturn(Optional.ofNullable(order1));

        mvc.perform(get("/order/account/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.account.email").value("user1"))
                .andExpect(jsonPath("$.pizzaOrderItems[0].amount").value(2))
                .andExpect(jsonPath("$.pizzaOrderItems[0].pizza.name").value("Hawaii"))
                .andExpect(jsonPath("$.pizzaOrderItems[1]").doesNotExist()) // Check that there is no second element
                .andExpect(jsonPath("$.address").value("Straat"))
                .andExpect(jsonPath("$.totalPrice").value(20.0))
                .andDo(print());
    }

    @Test
    void getOrderByAccountIdNotFound() throws Exception {
        when(orderRepository.findByAccountId(1)).thenReturn(Optional.empty());

        mvc.perform(get("/order/account/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void getOrderById() throws Exception {
        when(orderRepository.findById(1)).thenReturn(Optional.ofNullable(order1));

        mvc.perform(get("/order/id/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.account.email").value("user1"))
                .andExpect(jsonPath("$.pizzaOrderItems[0].amount").value(2))
                .andExpect(jsonPath("$.pizzaOrderItems[0].pizza.name").value("Hawaii"))
                .andExpect(jsonPath("$.pizzaOrderItems[1]").doesNotExist()) // Check that there is no second element
                .andExpect(jsonPath("$.address").value("Straat"))
                .andExpect(jsonPath("$.totalPrice").value(20.0))
                .andDo(print());
    }

    @Test
    void getOrderByIdNotFound() throws Exception {
        when(orderRepository.findById(1)).thenReturn(Optional.empty());

        mvc.perform(get("/order/id/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void createOrder() throws Exception {
        when(orderRepository.findById(1)).thenReturn(Optional.of(order1));
        when(pizzaRepository.findById(1)).thenReturn(Optional.of(pizza1));
        when(beverageRepository.findById(1)).thenReturn(Optional.of(beverage1));
        when(accountRepository.findById(1)).thenReturn(Optional.of(user1));
        when(orderRepository.save(order1)).thenReturn(order1);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));


        BeverageOrderItemRequest beverageOrderItemRequest = new BeverageOrderItemRequest();
        beverageOrderItemRequest.setAmount(2);
        beverageOrderItemRequest.setBeverageId(1);

        PizzaOrderItemRequest pizzaOrderItemRequest = new PizzaOrderItemRequest();
        pizzaOrderItemRequest.setAmount(2);
        pizzaOrderItemRequest.setPizzaId(1);
        pizzaOrderItemRequest.setPizzaSize("large");

        OrderCreateRequest orderCreateRequest = new OrderCreateRequest();
        orderCreateRequest.setAccountId(1);
        orderCreateRequest.setAddress("Straat");
        orderCreateRequest.setBeverages(List.of(beverageOrderItemRequest));
        orderCreateRequest.setPizzas(List.of(pizzaOrderItemRequest));

        mvc.perform(post("/order/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderCreateRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.account.email").value("user1"))
                .andExpect(jsonPath("$.pizzaOrderItems[0].amount").value(2))
                .andExpect(jsonPath("$.pizzaOrderItems[0].amount").value(2))
                .andExpect(jsonPath("$.pizzaOrderItems[0].pizza.name").value("Hawaii"))
                .andExpect(jsonPath("$.pizzaOrderItems[1]").doesNotExist()) // Check that there is no second element
                .andExpect(jsonPath("$.address").value("Straat"))
                .andExpect(jsonPath("$.totalPrice").value(25.0))
                .andDo(print());
    }

    @Test
    void createOrderNoPizza() throws Exception {
        when(orderRepository.findById(1)).thenReturn(Optional.empty());
        when(beverageRepository.findById(1)).thenReturn(Optional.of(beverage1));
        when(accountRepository.findById(1)).thenReturn(Optional.of(user1));
        when(orderRepository.save(order1)).thenReturn(order1);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BeverageOrderItemRequest beverageOrderItemRequest = new BeverageOrderItemRequest();
        beverageOrderItemRequest.setAmount(2);
        beverageOrderItemRequest.setBeverageId(1);

        OrderCreateRequest orderCreateRequest = new OrderCreateRequest();
        orderCreateRequest.setAccountId(1);
        orderCreateRequest.setAddress("Straat");
        orderCreateRequest.setBeverages(List.of(beverageOrderItemRequest));


        mvc.perform(post("/order/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderCreateRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.account.email").value("user1"))
                .andExpect(jsonPath("$.pizzaOrderItems[0]").doesNotExist()) // Check that there is no second element
                .andExpect(jsonPath("$.beverageOrderItems[0].amount").value(2))
                .andExpect(jsonPath("$.beverageOrderItems[0].beverage.name").value("Cola"))
                .andExpect(jsonPath("$.address").value("Straat"))
                .andDo(print());
    }

    @Test
    void createOrderNoBeverage() throws Exception {
        when(orderRepository.findById(1)).thenReturn(Optional.empty());
        when(pizzaRepository.findById(1)).thenReturn(Optional.of(pizza1));
        when(accountRepository.findById(1)).thenReturn(Optional.of(user1));
        when(orderRepository.save(order1)).thenReturn(order1);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PizzaOrderItemRequest pizzaOrderItemRequest = new PizzaOrderItemRequest();
        pizzaOrderItemRequest.setAmount(2);
        pizzaOrderItemRequest.setPizzaId(1);
        pizzaOrderItemRequest.setPizzaSize("large");

        OrderCreateRequest orderCreateRequest = new OrderCreateRequest();
        orderCreateRequest.setAccountId(1);
        orderCreateRequest.setAddress("Straat");
        orderCreateRequest.setPizzas(List.of(pizzaOrderItemRequest));

        mvc.perform(post("/order/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderCreateRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.account.email").value("user1"))
                .andExpect(jsonPath("$.pizzaOrderItems[0].amount").value(2))
                .andExpect(jsonPath("$.pizzaOrderItems[0].pizza.name").value("Hawaii"))
                .andExpect(jsonPath("$.beverageOrderItems[0]").doesNotExist()) // Check that there is no second element
                .andExpect(jsonPath("$.address").value("Straat"))
                .andDo(print());
    }

    @Test
    void createOrderNoPizzaNoBeverage() throws Exception {
        when(orderRepository.findById(1)).thenReturn(Optional.empty());
        when(accountRepository.findById(1)).thenReturn(Optional.of(user1));
        when(orderRepository.save(order1)).thenReturn(order1);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OrderCreateRequest orderCreateRequest = new OrderCreateRequest();
        orderCreateRequest.setAccountId(1);
        orderCreateRequest.setAddress("Straat");

        mvc.perform(post("/order/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderCreateRequest)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void createOrderNoAccount() throws Exception {
        when(orderRepository.findById(1)).thenReturn(Optional.empty());
        when(pizzaRepository.findById(1)).thenReturn(Optional.of(pizza1));
        when(accountRepository.findById(1)).thenReturn(Optional.empty());
        when(orderRepository.save(order1)).thenReturn(order1);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PizzaOrderItemRequest pizzaOrderItemRequest = new PizzaOrderItemRequest();
        pizzaOrderItemRequest.setAmount(2);
        pizzaOrderItemRequest.setPizzaId(1);

        OrderCreateRequest orderCreateRequest = new OrderCreateRequest();
        orderCreateRequest.setAccountId(1);
        orderCreateRequest.setAddress("Straat");
        orderCreateRequest.setPizzas(List.of(pizzaOrderItemRequest));

        mvc.perform(post("/order/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderCreateRequest)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void createOrderNoAddress() throws Exception {
        when(orderRepository.findById(1)).thenReturn(Optional.empty());
        when(pizzaRepository.findById(1)).thenReturn(Optional.of(pizza1));
        when(accountRepository.findById(1)).thenReturn(Optional.of(user1));
        when(orderRepository.save(order1)).thenReturn(order1);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PizzaOrderItemRequest pizzaOrderItemRequest = new PizzaOrderItemRequest();
        pizzaOrderItemRequest.setAmount(2);
        pizzaOrderItemRequest.setPizzaId(1);

        OrderCreateRequest orderCreateRequest = new OrderCreateRequest();
        orderCreateRequest.setAccountId(1);
        orderCreateRequest.setPizzas(List.of(pizzaOrderItemRequest));

        mvc.perform(post("/order/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderCreateRequest)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void createOrderNoPizzaSize() throws Exception {
        when(orderRepository.findById(1)).thenReturn(Optional.empty());
        when(pizzaRepository.findById(1)).thenReturn(Optional.of(pizza1));
        when(accountRepository.findById(1)).thenReturn(Optional.of(user1));
        when(orderRepository.save(order1)).thenReturn(order1);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PizzaOrderItemRequest pizzaOrderItemRequest = new PizzaOrderItemRequest();
        pizzaOrderItemRequest.setAmount(2);
        pizzaOrderItemRequest.setPizzaId(1);

        OrderCreateRequest orderCreateRequest = new OrderCreateRequest();
        orderCreateRequest.setAccountId(1);


        mvc.perform(post("/order/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderCreateRequest)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void createMultipleOrders() throws Exception {
        when(orderRepository.findById(1)).thenReturn(Optional.of(order1));
        when(orderRepository.findById(2)).thenReturn(Optional.of(order2));
        when(pizzaRepository.findById(1)).thenReturn(Optional.of(pizza1));
        when(pizzaRepository.findById(2)).thenReturn(Optional.of(pizza2));
        when(beverageRepository.findById(1)).thenReturn(Optional.of(beverage1));
        when(beverageRepository.findById(2)).thenReturn(Optional.of(beverage2));
        when(accountRepository.findById(1)).thenReturn(Optional.of(user1));
        when(accountRepository.findById(2)).thenReturn(Optional.of(user2));
        when(orderRepository.save(order1)).thenReturn(order1);
        when(orderRepository.save(order2)).thenReturn(order2);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));


        BeverageOrderItemRequest beverageOrderItemRequest = new BeverageOrderItemRequest();
        beverageOrderItemRequest.setAmount(2);
        beverageOrderItemRequest.setBeverageId(1);

        PizzaOrderItemRequest pizzaOrderItemRequest = new PizzaOrderItemRequest();
        pizzaOrderItemRequest.setAmount(2);
        pizzaOrderItemRequest.setPizzaId(1);
        pizzaOrderItemRequest.setPizzaSize("large");

        OrderCreateRequest orderCreateRequest = new OrderCreateRequest();
        orderCreateRequest.setAccountId(1);
        orderCreateRequest.setAddress("Straat");
        orderCreateRequest.setBeverages(List.of(beverageOrderItemRequest));
        orderCreateRequest.setPizzas(List.of(pizzaOrderItemRequest));

        PizzaOrderItemRequest pizzaOrderItemRequest2 = new PizzaOrderItemRequest();
        pizzaOrderItemRequest2.setAmount(1);
        pizzaOrderItemRequest2.setPizzaId(1);
        pizzaOrderItemRequest2.setPizzaSize("small");

        OrderCreateRequest orderCreateRequest2 = new OrderCreateRequest();
        orderCreateRequest2.setAccountId(2);
        orderCreateRequest2.setAddress("Straat");
        orderCreateRequest2.setPizzas(List.of(pizzaOrderItemRequest2));

        mvc.perform(post("/order/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderCreateRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.account.email").value("user1"))
                .andExpect(jsonPath("$.pizzaOrderItems[0].amount").value(2))
                .andExpect(jsonPath("$.pizzaOrderItems[0].amount").value(2))
                .andExpect(jsonPath("$.pizzaOrderItems[0].pizza.name").value("Hawaii"))
                .andExpect(jsonPath("$.pizzaOrderItems[1]").doesNotExist()) // Check that there is no second element
                .andExpect(jsonPath("$.address").value("Straat"))
                .andExpect(jsonPath("$.totalPrice").value(25.0))
                .andDo(print());

        mvc.perform(post("/order/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderCreateRequest2)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.account.email").value("user2"))
                .andExpect(jsonPath("$.pizzaOrderItems[0].amount").value(1))
                .andExpect(jsonPath("$.pizzaOrderItems[0].amount").value(1))
                .andExpect(jsonPath("$.pizzaOrderItems[0].pizza.name").value("Hawaii"))
                .andExpect(jsonPath("$.pizzaOrderItems[1]").doesNotExist()) // Check that there is no second element
                .andExpect(jsonPath("$.address").value("Straat"))
                .andExpect(jsonPath("$.totalPrice").value(10.0))
                .andDo(print());
    }

    @Test
    void updateOrder() throws Exception {
        when(orderRepository.findById(1)).thenReturn(Optional.of(order1));
        when(pizzaRepository.findById(1)).thenReturn(Optional.of(pizza1));
        when(beverageRepository.findById(1)).thenReturn(Optional.of(beverage1));
        when(accountRepository.findById(1)).thenReturn(Optional.of(user1));
        when(orderRepository.save(order1)).thenReturn(order1);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BeverageOrderItemRequest beverageOrderItemRequest = new BeverageOrderItemRequest();
        beverageOrderItemRequest.setAmount(2);
        beverageOrderItemRequest.setBeverageId(1);

        PizzaOrderItemRequest pizzaOrderItemRequest = new PizzaOrderItemRequest();
        pizzaOrderItemRequest.setAmount(2);
        pizzaOrderItemRequest.setPizzaId(1);

        OrderUpdateRequest orderUpdateRequest = new OrderUpdateRequest();
        orderUpdateRequest.setAccountId(1);
        orderUpdateRequest.setAddress("Straat");
        orderUpdateRequest.setBeverages(List.of(beverageOrderItemRequest));
        orderUpdateRequest.setPizzas(List.of(pizzaOrderItemRequest));

        mvc.perform(put("/order/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderUpdateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.account.email").value("user1"))
                .andExpect(jsonPath("$.pizzaOrderItems[0].amount").value(2))
                .andExpect(jsonPath("$.pizzaOrderItems[0].pizza.name").value("Hawaii"))
                .andExpect(jsonPath("$.beverageOrderItems[0].amount").value(2))
                .andExpect(jsonPath("$.beverageOrderItems[0].beverage.name").value("Cola"))
                .andExpect(jsonPath("$.address").value("Straat"))
                .andDo(print());
    }

    @Test
    void updateOrderNoPizza() throws Exception {
        Order testOrder = new Order(user1, date1, "Straat", 20.0);
        testOrder.setId(1);
        testOrder.setBeverageOrderItems(List.of(orderItem2));

        when(orderRepository.findById(1)).thenReturn(Optional.of(testOrder));
        when(beverageRepository.findById(1)).thenReturn(Optional.of(beverage1));
        when(accountRepository.findById(1)).thenReturn(Optional.of(user1));
        when(orderRepository.save(testOrder)).thenReturn(testOrder);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BeverageOrderItemRequest beverageOrderItemRequest = new BeverageOrderItemRequest();
        beverageOrderItemRequest.setAmount(2);
        beverageOrderItemRequest.setBeverageId(1);

        OrderUpdateRequest orderUpdateRequest = new OrderUpdateRequest();
        orderUpdateRequest.setAccountId(1);
        orderUpdateRequest.setAddress("Straat");
        orderUpdateRequest.setBeverages(List.of(beverageOrderItemRequest));

        mvc.perform(put("/order/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderUpdateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.account.email").value("user1"))
                .andExpect(jsonPath("$.pizzaOrderItems[0]").doesNotExist())
                .andExpect(jsonPath("$.beverageOrderItems[0].amount").value(2))
                .andExpect(jsonPath("$.beverageOrderItems[0].beverage.name").value("Cola"))
                .andExpect(jsonPath("$.address").value("Straat"))
                .andDo(print());

    }

    @Test
    void updateOrderNoBeverage() throws Exception {
        Order testOrder = new Order(user1, date1, "Straat", 20.0);
        testOrder.setId(1);
        testOrder.setPizzaOrderItems(List.of(orderItem1));

        when(orderRepository.findById(1)).thenReturn(Optional.of(testOrder));
        when(pizzaRepository.findById(1)).thenReturn(Optional.of(pizza1));
        when(accountRepository.findById(1)).thenReturn(Optional.of(user1));
        when(orderRepository.save(testOrder)).thenReturn(testOrder);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PizzaOrderItemRequest pizzaOrderItemRequest = new PizzaOrderItemRequest();
        pizzaOrderItemRequest.setAmount(2);
        pizzaOrderItemRequest.setPizzaId(1);

        OrderUpdateRequest orderUpdateRequest = new OrderUpdateRequest();
        orderUpdateRequest.setAccountId(1);
        orderUpdateRequest.setAddress("Straat");
        orderUpdateRequest.setPizzas(List.of(pizzaOrderItemRequest));

        mvc.perform(put("/order/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderUpdateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.account.email").value("user1"))
                .andExpect(jsonPath("$.pizzaOrderItems[0].amount").value(2))
                .andExpect(jsonPath("$.pizzaOrderItems[0].pizza.name").value("Hawaii"))
                .andExpect(jsonPath("$.beverageOrderItems[0]").doesNotExist())
                .andExpect(jsonPath("$.address").value("Straat"))
                .andDo(print());

    }

    @Test
    void updateOrderNoPizzaNoBeverage() throws Exception {
        when(orderRepository.findById(1)).thenReturn(Optional.of(order1));
        when(accountRepository.findById(1)).thenReturn(Optional.of(user1));
        when(orderRepository.save(order1)).thenReturn(order1);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OrderUpdateRequest orderUpdateRequest = new OrderUpdateRequest();
        orderUpdateRequest.setAccountId(1);
        orderUpdateRequest.setAddress("Straat");

        mvc.perform(put("/order/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderUpdateRequest)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void updateOrderNoAccount() throws Exception {
        when(orderRepository.findById(1)).thenReturn(Optional.of(order1));
        when(pizzaRepository.findById(1)).thenReturn(Optional.of(pizza1));
        when(beverageRepository.findById(1)).thenReturn(Optional.of(beverage1));
        when(accountRepository.findById(1)).thenReturn(Optional.empty());
        when(orderRepository.save(order1)).thenReturn(order1);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BeverageOrderItemRequest beverageOrderItemRequest = new BeverageOrderItemRequest();
        beverageOrderItemRequest.setAmount(2);
        beverageOrderItemRequest.setBeverageId(1);

        PizzaOrderItemRequest pizzaOrderItemRequest = new PizzaOrderItemRequest();
        pizzaOrderItemRequest.setAmount(2);
        pizzaOrderItemRequest.setPizzaId(1);

        OrderUpdateRequest orderUpdateRequest = new OrderUpdateRequest();
        orderUpdateRequest.setAddress("Straat");
        orderUpdateRequest.setBeverages(List.of(beverageOrderItemRequest));
        orderUpdateRequest.setPizzas(List.of(pizzaOrderItemRequest));

        mvc.perform(put("/order/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderUpdateRequest)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void deleteOrder() throws Exception {
        when(orderRepository.findById(1)).thenReturn(Optional.of(order1));

        mvc.perform(delete("/order/delete/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}