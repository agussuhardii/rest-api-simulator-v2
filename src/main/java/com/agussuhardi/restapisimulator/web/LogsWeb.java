package com.agussuhardi.restapisimulator.web;

import com.agussuhardi.restapisimulator.service.impl.LogsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
@RequestMapping("l")
@RequiredArgsConstructor
public class LogsWeb {

  private final LogsService logsService;

  @GetMapping("v")
  public String getDetail(Model model, @RequestParam(required = false) String id) {
    model.addAttribute("content", logsService.getById(id));
    return "logs/view";
  }
}
