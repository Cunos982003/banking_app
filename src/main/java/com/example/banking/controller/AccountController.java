package com.example.banking.controller;

import com.example.banking.model.Account;
import com.example.banking.repository.AccountRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountRepository repo;

    public AccountController(AccountRepository repo) {
        this.repo = repo;
    }

    record CreateAccountRequest(String ownerName, BigDecimal initialBalance) {}

    @PostMapping
    public ResponseEntity<Account> create(@RequestBody CreateAccountRequest req) {
        String accNum = UUID.randomUUID().toString();
        BigDecimal bal = req.initialBalance() == null ? BigDecimal.ZERO : req.initialBalance();
        Account a = new Account(accNum, req.ownerName(), bal);
        Account saved = repo.save(a);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public List<Account> list() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> get(@PathVariable Long id) {
        Optional<Account> o = repo.findById(id);
        return o.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
