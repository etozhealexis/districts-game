package ru.etozhealexis.districtsservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/map")
    public String getMap() {
        return "map";
    }
}
