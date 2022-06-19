package com.agussuhardi.restapisimulator.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping
@RequiredArgsConstructor
public class IndexWeb {

  @GetMapping
  public String index(Model model) {
    model.addAttribute("methods", HttpMethod.values());
    return "/index";
  }
}
