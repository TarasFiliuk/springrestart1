package ua.com.owu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ua.com.owu.dao.AccountDAO;
import ua.com.owu.models.Account;

import javax.xml.ws.Response;
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



    @RequestMapping(value = "/api/adminsearch/data", method = RequestMethod.GET)
    @ResponseBody
    public List<Account> getBarBySimplePathWithRequestParam(
            @RequestParam(value = "username", defaultValue = "test") String username,
            @RequestParam(value = "email", defaultValue = "test") String email,
            @RequestParam(value = "role", defaultValue = "test") String role

    ) {


        List<Account> all = accountDAO.findAll();
        System.out.println(all);

        return all;

//        return "text=" + username +" "+email+" "+role;
    }



    @DeleteMapping("/api/ad/{id}")
    public void deleteAcc(@PathVariable int id) {
        accountDAO.delete(id);
    }

}
