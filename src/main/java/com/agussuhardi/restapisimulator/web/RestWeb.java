package com.agussuhardi.restapisimulator.web;

import com.agussuhardi.restapisimulator.entity.Rest;
import com.agussuhardi.restapisimulator.service.impl.RestService;
import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
@RequestMapping("r")
@RequiredArgsConstructor
public class RestWeb {

  private final RestService restService;

  @GetMapping("a")
  public String form(Model model, @RequestParam(required = false) String id) {
    log.info(id);
    model.addAttribute("methods", HttpMethod.values());
    model.addAttribute("statuses", HttpStatus.values());

    Rest content = new Rest();
    content.setMethod(HttpMethod.GET);
    content.setSuccessResponseCode(HttpStatus.OK);
    content.setFailResponseCode(HttpStatus.BAD_REQUEST);

    if (!Strings.isNullOrEmpty(id)) content = restService.getById(id);
    model.addAttribute("content", content);
    return "rest/form";
  }
}
