package ua.com.owu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import ua.com.owu.models.Manager;
import ua.com.owu.models.Place;
import ua.com.owu.service.accountService.AccountService;
import ua.com.owu.service.placeService.PlaceService;


@Controller
public class PlaceController {

    private final
    PlaceService placeService;
    private final AccountService accountService;

    @Autowired
    public PlaceController(PlaceService placeService, AccountService accountService) {
        this.placeService = placeService;
        this.accountService = accountService;
    }

    @PostMapping("manager-account/place")
    public String createPlace(Place place){
        Manager manager = (Manager) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        placeService.save(place);
        manager.setPlace(place);
        accountService.save(manager);
        return "redirect:/manager-account";
    }

    @PostMapping("manager-account/place/update")
    public String updatePlace(Place place){
        Manager manager = (Manager) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        placeService.update(manager, place);
        return "redirect:/manager-account";
    }


}
