//package com.agussuhardi.restapisimulator.service.impl;
//
//import com.agussuhardi.restapisimulator.dto.LogsDTO;
//import com.agussuhardi.restapisimulator.entity.Logs;
//import com.agussuhardi.restapisimulator.repository.LogsRepository;
//import com.agussuhardi.restapisimulator.vo.LogsQueryVO;
//import com.agussuhardi.restapisimulator.vo.LogsUpdateVO;
//import com.agussuhardi.restapisimulator.vo.LogsVO;
//import org.springframework.beans.BeanUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.stereotype.Service;
//
//import java.util.NoSuchElementException;
//
//@Service
//public class LogsService {
//
//    @Autowired
//    private LogsRepository logsRepository;
//
//    public void save(LogsVO vO) {
//        Logs bean = new Logs();
//        BeanUtils.copyProperties(vO, bean);
//        bean = logsRepository.save(bean);
//    }
//
//    public void delete(String id) {
//        logsRepository.deleteById(id);
//    }
//
//    public void update(String id, LogsUpdateVO vO) {
//        Logs bean = requireOne(id);
//        BeanUtils.copyProperties(vO, bean);
//        logsRepository.save(bean);
//    }
//
//    public LogsDTO getById(String id) {
//        Logs original = requireOne(id);
//        return toDTO(original);
//    }
//
//    public Page<LogsDTO> query(LogsQueryVO vO) {
//        throw new UnsupportedOperationException();
//    }
//
//    private LogsDTO toDTO(Logs original) {
//        LogsDTO bean = new LogsDTO();
//        BeanUtils.copyProperties(original, bean);
//        return bean;
//    }
//
//    private Logs requireOne(String id) {
//        return logsRepository.findById(id)
//                .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
//    }
//}
