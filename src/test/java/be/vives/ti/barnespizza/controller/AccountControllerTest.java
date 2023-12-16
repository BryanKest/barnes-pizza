package be.vives.ti.barnespizza.controller;

import be.vives.ti.barnespizza.domain.Account;
import be.vives.ti.barnespizza.repository.AccountRepository;
import be.vives.ti.barnespizza.requests.AccountCreateRequest;
import be.vives.ti.barnespizza.requests.AccountUpdateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Account user1;
    private Account user2;
    private Account user3;
    private Account user4;

    @MockBean
    private AccountRepository accountRepository;

    @BeforeEach
    void setUp() {
        user1 = new Account("user1", "password1", "Bryan", "Kesteloot", "Straat", "047123456");
        user2 = new Account("user2", "password2", "Jos", "Dammen", "Straat", "047123456");
        user3 = new Account("user3", "password3", "Brittney", "Barnes", "Straat", "047123456");
        user4 = new Account("user4", "password3", "Bryan", "De vos", "Straat", "047123456");
    }

    @Test
    void getAllAccounts() throws Exception {
        when(accountRepository.findAll()).thenReturn(List.of(user1, user2, user3));

        mvc.perform(get("/account/all"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].email").value("user1"))
                .andExpect(jsonPath("$.[1].email").value("user2"))
                .andExpect(jsonPath("$.[2].email").value("user3"));
    }

    @Test
    void getAccountById() throws Exception {
        user1.setId(1);
        when(accountRepository.findById(1)).thenReturn(Optional.of(user1));

        mvc.perform(get("/account/id/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value("user1"))
                .andExpect(jsonPath("$.password").value("password1"))
                .andExpect(jsonPath("$.fname").value("Bryan"))
                .andExpect(jsonPath("$.name").value("Kesteloot"))
                .andExpect(jsonPath("$.address").value("Straat"))
                .andExpect(jsonPath("$.phone").value("047123456"));
    }

    @Test
    void getAccountByFname() throws Exception{
        when(accountRepository.findByFname("Bryan")).thenReturn(List.of(user1, user4));

        mvc.perform(get("/account/fname/Bryan"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].email").value("user1"))
                .andExpect(jsonPath("$.[0].password").value("password1"))
                .andExpect(jsonPath("$.[0].fname").value("Bryan"))
                .andExpect(jsonPath("$.[0].name").value("Kesteloot"))
                .andExpect(jsonPath("$.[0].address").value("Straat"))
                .andExpect(jsonPath("$.[0].phone").value("047123456"))
                .andExpect(jsonPath("$.[1].email").value("user4"))
                .andExpect(jsonPath("$.[1].password").value("password3"))
                .andExpect(jsonPath("$.[1].fname").value("Bryan"))
                .andExpect(jsonPath("$.[1].name").value("De vos"))
                .andExpect(jsonPath("$.[1].address").value("Straat"))
                .andExpect(jsonPath("$.[1].phone").value("047123456"));
    }

    @Test
    void getAccountByName() throws Exception{
        when(accountRepository.findByName("Kesteloot")).thenReturn(List.of(user1, user4));

        mvc.perform(get("/account/name/Kesteloot"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].email").value("user1"))
                .andExpect(jsonPath("$.[0].password").value("password1"))
                .andExpect(jsonPath("$.[0].fname").value("Bryan"))
                .andExpect(jsonPath("$.[0].name").value("Kesteloot"))
                .andExpect(jsonPath("$.[0].address").value("Straat"))
                .andExpect(jsonPath("$.[0].phone").value("047123456"))
                .andExpect(jsonPath("$.[1].email").value("user4"))
                .andExpect(jsonPath("$.[1].password").value("password3"))
                .andExpect(jsonPath("$.[1].fname").value("Bryan"))
                .andExpect(jsonPath("$.[1].name").value("De vos"))
                .andExpect(jsonPath("$.[1].address").value("Straat"))
                .andExpect(jsonPath("$.[1].phone").value("047123456"));
    }

    @Test
    void getAllAccountsEmpty() throws Exception {
        when(accountRepository.findAll()).thenReturn(Collections.emptyList());

        mvc.perform(get("/account/all"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getAccountByWrongId() throws Exception {
        when(accountRepository.findById(1)).thenReturn(Optional.ofNullable(user1));

        mvc.perform(get("/account/2"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getAccountByNameEmpty() throws Exception {
        when(accountRepository.findByName("NietHier")).thenReturn(Collections.emptyList());

        mvc.perform(get("/account/name/NietHier"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getAccountByFnameEmpty() throws Exception {
        when(accountRepository.findByFname("NietHier")).thenReturn(Collections.emptyList());

        mvc.perform(get("/account/fname/NietHier"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getAccountByNameNull() throws Exception {
        when(accountRepository.findByName(null)).thenReturn(Collections.emptyList());

        mvc.perform(get("/account/name/"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void createAccount() throws Exception {
        when(accountRepository.save(any(Account.class))).thenReturn(user1);

        AccountCreateRequest request = new AccountCreateRequest();
        request.setEmail("test@example.com");
        request.setPassword("password123");
        request.setFname("John");
        request.setName("Doe");
        request.setAddress("123 Street");
        request.setPhone("123456789");

        mvc.perform(post("/account/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void createAccountEmpty() throws Exception {
        when(accountRepository.save(any(Account.class))).thenReturn(user1);

        AccountCreateRequest request = new AccountCreateRequest();
        request.setEmail("");
        request.setPassword("");
        request.setFname("");
        request.setName("");
        request.setAddress("");
        request.setPhone("");

        mvc.perform(post("/account/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createAccountNull() throws Exception {
        when(accountRepository.save(any(Account.class))).thenReturn(user1);

        AccountCreateRequest request = new AccountCreateRequest();
        request.setEmail(null);
        request.setPassword(null);
        request.setFname(null);
        request.setName(null);
        request.setAddress(null);
        request.setPhone(null);

        mvc.perform(post("/account/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateAccount() throws Exception {
        AccountUpdateRequest request = new AccountUpdateRequest();
        request.setFname("UpdatedFirstName");
        request.setAddress("UpdatedAddress");

        Integer accountId = 123;
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(user1));
        Account updatedAccount = new Account("user1", "password1", "UpdatedFirstName", "kesteloot", "UpdatedAddress", "047123456");
        when(accountRepository.save(any())).thenReturn(updatedAccount);

        mvc.perform(put("/account/put/{id}", accountId)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fname").value("UpdatedFirstName"))
                .andExpect(jsonPath("$.address").value("UpdatedAddress"));
    }

    @Test
    void updateAccount_AccountNotFound() throws Exception {
        AccountUpdateRequest request = new AccountUpdateRequest();
        request.setFname("UpdatedFirstName");
        request.setAddress("UpdatedAddress");

        Integer accountId = 123;
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        mvc.perform(put("/account/put/{id}", accountId)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateAccountAccountNull() throws Exception {
        AccountUpdateRequest request = new AccountUpdateRequest();
        request.setFname(null);
        request.setAddress(null);

        Integer accountId = 123;
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        mvc.perform(put("/account/put/{id}", accountId)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    void patchAccount() throws Exception {
        AccountUpdateRequest request = new AccountUpdateRequest();
        request.setFname("UpdatedFirstName");
        request.setAddress("UpdatedAddress");

        Integer accountId = 123;
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(user1));
        Account updatedAccount = new Account("user1", "password1", "UpdatedFirstName", "kesteloot", "UpdatedAddress", "047123456");
        when(accountRepository.save(any())).thenReturn(updatedAccount);

        mvc.perform(patch("/account/patch/{id}", accountId)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fname").value("UpdatedFirstName"))
                .andExpect(jsonPath("$.address").value("UpdatedAddress"));
    }

    @Test
    void patchAccount_AccountNotFound() throws Exception {
        AccountUpdateRequest request = new AccountUpdateRequest();
        request.setFname("UpdatedFirstName");
        request.setAddress("UpdatedAddress");

        Integer accountId = 123;
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());
        verifyNoMoreInteractions(accountRepository);

        mvc.perform(patch("/account/patch/{id}", accountId)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    void patchAccount_AccountEmpty() throws Exception {
        AccountUpdateRequest request = new AccountUpdateRequest();
        request.setFname("");
        request.setAddress("");

        Integer accountId = 123;
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        mvc.perform(patch("/account/patch/{id}", accountId)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteAccount() throws Exception {
        Integer accountId = 123;
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(user1));

        mvc.perform(delete("/account/delete/{id}", accountId))
                .andExpect(status().isNoContent());
    }
}