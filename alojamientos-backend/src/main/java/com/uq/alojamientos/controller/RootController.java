package com.uq.alojamientos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Redirige la raíz a Swagger UI en desarrollo.
 */
@Controller
public class RootController {

    @GetMapping("/")
    public String root() {
        return "redirect:/swagger-ui/index.html";
    }
}
