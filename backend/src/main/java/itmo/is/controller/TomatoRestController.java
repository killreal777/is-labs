package itmo.is.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tomatoes")
public class TomatoRestController {

    @GetMapping
    public String getAllTomatoes() {
        return "GET";
    }

    @PostMapping
    public String addTomato() {
        return "POST";
    }

    @DeleteMapping
    public String deleteAllTomatoes() {
        return "DELETE";
    }
}