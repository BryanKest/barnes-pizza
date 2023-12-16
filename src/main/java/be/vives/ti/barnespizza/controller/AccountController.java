package be.vives.ti.barnespizza.controller;

import be.vives.ti.barnespizza.domain.Account;
import be.vives.ti.barnespizza.exception.NoValuesFoundException;
import be.vives.ti.barnespizza.repository.AccountRepository;
import be.vives.ti.barnespizza.requests.AccountCreateRequest;
import be.vives.ti.barnespizza.requests.AccountUpdateRequest;
import be.vives.ti.barnespizza.responses.AccountResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/account")
@CrossOrigin("*")
public class AccountController {
    private final AccountRepository accountRepository;

    public AccountController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @GetMapping("/all")
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        List<Account> accounts = this.accountRepository.findAll();

        List<AccountResponse> accountResponses = accounts.stream()
                .map(AccountResponse::new)
                .collect(Collectors.toList());


        if (accounts.isEmpty()) {
            return new ResponseEntity<>(accountResponses, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(accountResponses, HttpStatus.OK);
    }

    @GetMapping("/fname/{fname}")
    public ResponseEntity<List<AccountResponse>> getAccountByFname(@PathVariable("fname") String fname) {
        List<Account> accounts = accountRepository.findByFname(fname);

        List<AccountResponse> accountResponses = accounts.stream()
                .map(AccountResponse::new)
                .collect(Collectors.toList());

        if (accounts.isEmpty()) {
            return new ResponseEntity<>(accountResponses, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(accountResponses, HttpStatus.OK);
    }
    @GetMapping("/name/{name}")
    public ResponseEntity<List<AccountResponse>> getAccountByName(@PathVariable("name") String name){
        List<Account> accounts = accountRepository.findByName(name);

        List<AccountResponse> accountResponses = accounts.stream()
                .map(AccountResponse::new)
                .collect(Collectors.toList());

        if (accounts.isEmpty()) {
            return new ResponseEntity<>(accountResponses, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(accountResponses, HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public AccountResponse getAccountById(@PathVariable("id") Integer id){
        Account account = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("No account found with id: " +id));
        return new AccountResponse(account);
    }

    @PostMapping("/create")
    public ResponseEntity<AccountResponse> createAccount(@RequestBody @Valid AccountCreateRequest accountCreateRequest) {
        Account newAccount = new Account(
                accountCreateRequest.getEmail(),
                accountCreateRequest.getPassword(),
                accountCreateRequest.getFname(),
                accountCreateRequest.getName(),
                accountCreateRequest.getAddress(),
                accountCreateRequest.getPhone()
        );

        Account savedAccount = accountRepository.save(newAccount);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedAccount.getId())
                .toUri();

        return new ResponseEntity<>(new AccountResponse(savedAccount), HttpStatus.CREATED);
    }

    @PutMapping("/put/{accountId}")
    public ResponseEntity<AccountResponse> updateAccount(
            @PathVariable("accountId") Integer accountId,
            @RequestBody @Valid AccountUpdateRequest accountUpdateRequest) {
        Optional<Account> optionalAccount = accountRepository.findById(accountId);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            account.setEmail(accountUpdateRequest.getEmail());
            account.setPassword(accountUpdateRequest.getPassword());
            account.setFname(accountUpdateRequest.getFname());
            account.setName(accountUpdateRequest.getName());
            account.setAddress(accountUpdateRequest.getAddress());
            account.setPhone(accountUpdateRequest.getPhone());

            return new ResponseEntity<>(new AccountResponse(accountRepository.save(account)), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/patch/{accountId}")
    public ResponseEntity<AccountResponse> patchAccount(
            @PathVariable("accountId") Integer accountId,
            @RequestBody AccountUpdateRequest accountUpdateRequest) {
        Optional<Account> optionalAccount = accountRepository.findById(accountId);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            if (accountUpdateRequest.getEmail() != null) {
                account.setEmail(accountUpdateRequest.getEmail());
            }
            if (accountUpdateRequest.getPassword() != null) {
                account.setPassword(accountUpdateRequest.getPassword());
            }
            if (accountUpdateRequest.getFname() != null) {
                account.setFname(accountUpdateRequest.getFname());
            }
            if (accountUpdateRequest.getName() != null) {
                account.setName(accountUpdateRequest.getName());
            }
            if (accountUpdateRequest.getAddress() != null) {
                account.setAddress(accountUpdateRequest.getAddress());
            }
            if (accountUpdateRequest.getPhone() != null) {
                account.setPhone(accountUpdateRequest.getPhone());
            }

            return new ResponseEntity<>(new AccountResponse(accountRepository.save(account)), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable("accountId") Integer accountId) {
        accountRepository.deleteById(accountId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
