package ua.com.owu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.com.owu.dao.AccountDAO;
import ua.com.owu.models.Account;

import java.util.List;

@RestController
public class AdminRestController {

    @Autowired
    AccountDAO accountDAO;

    @GetMapping("/api/ad")
    List<Account> accountList() {
        List<Account> all = accountDAO.findAll();
        return all;
    }

    @GetMapping("/api/ad/{id}")
    Account getById(
            @PathVariable int id
    ) {
        return accountDAO.findById(id);
    }

    @PostMapping("/api/ad")
    public void saveAccount(
            @RequestBody Account account
    ) {
        accountDAO.save(account);
    }

    @DeleteMapping("/api/ad/{id}")
    public void deleteAcc(@PathVariable int id) {
        accountDAO.delete(id);
    }

}
