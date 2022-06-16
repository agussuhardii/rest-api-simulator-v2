//package com.agussuhardi.restapisimulator.controller;
//
//import com.agussuhardi.restapisimulator.dto.LogsDTO;
//import com.agussuhardi.restapisimulator.service.impl.LogsService;
//import com.agussuhardi.restapisimulator.vo.LogsQueryVO;
//import com.agussuhardi.restapisimulator.vo.LogsUpdateVO;
//import com.agussuhardi.restapisimulator.vo.LogsVO;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//
//import javax.validation.Valid;
//import javax.validation.constraints.NotNull;
//
//@Validated
//@RestController
//@RequestMapping("/logs")
//public class LogsController {
//
//    @Autowired
//    private LogsService logsService;
//
//    @PostMapping
//    public String save(@Valid @RequestBody LogsVO vO) {
//        return logsService.save(vO).toString();
//    }
//
//    @DeleteMapping("/{id}")
//    public void delete(@Valid @NotNull @PathVariable("id") String id) {
//        logsService.delete(id);
//    }
//
//    @PutMapping("/{id}")
//    public void update(@Valid @NotNull @PathVariable("id") String id,
//                       @Valid @RequestBody LogsUpdateVO vO) {
//        logsService.update(id, vO);
//    }
//
//    @GetMapping("/{id}")
//    public LogsDTO getById(@Valid @NotNull @PathVariable("id") String id) {
//        return logsService.getById(id);
//    }
//
//    @GetMapping
//    public Page<LogsDTO> query(@Valid LogsQueryVO vO) {
//        return logsService.query(vO);
//    }
//}
