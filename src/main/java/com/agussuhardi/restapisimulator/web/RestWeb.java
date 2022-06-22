package com.agussuhardi.restapisimulator.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("r")
@RequiredArgsConstructor
public class RestWeb {

  @GetMapping("a")
  public String form(Model model) {
    model.addAttribute("methods", HttpMethod.values());
    model.addAttribute("statuses", HttpStatus.values());
    return "rest/form";
  }
}
