package ua.com.owu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.owu.models.*;
import ua.com.owu.service.accountService.AccountService;
import ua.com.owu.utils.AccountEditor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class AdminController {

	private final
	AccountService accountService;

	private final
	AccountEditor accountEditor;


    @Autowired
    public AdminController(AccountService accountService, AccountEditor accountEditor) {
        this.accountService = accountService;
        this.accountEditor = accountEditor;
    }


    @GetMapping("/admin/active/manager/id/{id}")
    String confirm(
            @PathVariable int id
    ) {
        Account managerAccount = accountService.findById(id);
        managerAccount.setAccountNonLocked(true);
        accountService.save(managerAccount);
        return "redirect:/admin/page";
    }

    @GetMapping("/admin/page")
    String adminPage(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        model.addAttribute("adminName", name);

        List<Account> manager = accountService.findByAccountType("manager");
        Stream<Account> stream = manager.stream();
        List<Account> collect = stream.filter(account -> !account.isAccountNonLocked()).collect(Collectors.toList());
        model.addAttribute("manager", collect);
        return "admin";
    }


    @PostMapping("/admin/saveUser")
    String adminSaveUser(User user){
        accountEditor.setValue(user);
        accountService.save(user);
        return "redirect:/admin/page";

    }

    @PostMapping("/admin/saveManager")
    String adminSaveUser(Manager manager){
        accountEditor.setValue(manager);
        accountService.save(manager);
        return "redirect:/admin/page";

    }

    @PostMapping("/admin/saveAdmin")
    String adminSaveUser(Admin admin){
        accountEditor.setValue(admin);
        accountService.save(admin);
        return "redirect:/admin/page";

    }

	@PostMapping("/admin/search/account")
	public String adminSearch(Model model,
							  @RequestParam String username,
							  @RequestParam String email,
							  @RequestParam String role
	) {
		Stream<Account> accountStream = accountService.findAll().stream();
		List<Account> accountList = new ArrayList<>();
		if (username == "" & email == "" & role == "") {
			accountList = accountStream.collect(Collectors.toList());
		} else {
			if (username != "") {
				accountStream = accountStream.filter(account -> account.getUsername().matches(username));
			}
			if (email != "") {
				accountStream = accountStream.filter(account -> account.getEmail().equals(email));
			}
			if (role != "") {
				Role myrole = null;
				switch (role) {
					case "ROLE_ADMIN":
						myrole = Role.ROLE_ADMIN;
						break;
					case "ROLE_MANAGER":
						myrole = Role.ROLE_MANAGER;
						break;
					case "ROLE_USER":
						myrole = Role.ROLE_USER;
						break;
				}
				Role finalMyrole = myrole;
				accountStream = accountStream.filter(account -> account.getRole().equals(finalMyrole));
			}
			accountList = accountStream.collect(Collectors.toList());
		}
		model.addAttribute("accounts", accountList);
		return "admin";
	}
}
