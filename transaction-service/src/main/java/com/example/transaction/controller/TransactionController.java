package com.example.transaction.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

@RestController
public class TransactionController {

    record TransferRequest(String sourceAccountNumber, String destAccountNumber, BigDecimal amount) {}

    private final RestTemplate rest;

    public TransactionController(RestTemplate rest) {
        this.rest = rest;
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestBody TransferRequest req) {
        // Call account-service via Docker DNS
        String url = "http://account-service:8080/accounts";
        ResponseEntity<AccountDto[]> resp = rest.getForEntity(url, AccountDto[].class);
        AccountDto[] accounts = resp.getBody();
        if (accounts == null) return ResponseEntity.status(500).body("account-service returned no data");

        Optional<AccountDto> source = Arrays.stream(accounts)
                .filter(a -> req.sourceAccountNumber().equals(a.getAccountNumber()))
                .findFirst();

        if (source.isEmpty()) {
            return ResponseEntity.status(404).body("source account not found");
        }

        // For demo: don't perform real DB transfer, just return OK
        return ResponseEntity.ok("transfer accepted: from=" + req.sourceAccountNumber() + " to=" + req.destAccountNumber() + " amount=" + req.amount());
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class AccountDto {
        private Long id;
        private String accountNumber;
        private String ownerName;
        private BigDecimal balance;

        public AccountDto() {}

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getAccountNumber() { return accountNumber; }
        public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
        public String getOwnerName() { return ownerName; }
        public void setOwnerName(String ownerName) { this.ownerName = ownerName; }
        public BigDecimal getBalance() { return balance; }
        public void setBalance(BigDecimal balance) { this.balance = balance; }
    }
}
