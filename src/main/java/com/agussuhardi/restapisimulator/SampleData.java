package com.agussuhardi.restapisimulator;

import com.agussuhardi.restapisimulator.entity.Rest;
import com.agussuhardi.restapisimulator.repository.RestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * init 13/06/212
 *
 * @author agus.suhardii@gmail.com
 */
@Component
@Slf4j
public class SampleData implements ApplicationListener<ContextRefreshedEvent> {

  @Autowired private RestRepository restRepository;

  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {

    //    var rest = new Rest();
    //    rest.setName("aa");
    //    rest.setPathUrl("aa");
    //    rest.setMethod(HttpMethod.GET);
    //    rest.setRequestHeaders(new HashMap<>());

    if (restRepository.findAll().iterator().hasNext()) return;

    Map<String, Object> map = new HashMap<>();
    Map<String, String[]> headers = new HashMap<>();
    map.put("hello", "");

    var rest =
        Rest.builder()
            .name("agenpe")
            .uri("/agenpe/api/v1/products")
            .method(HttpMethod.GET)
            .requestHeaders(new HashMap<>())
            .requestBody(new HashMap<>())
            .requestParams(new HashMap<>())
            .isRandomSuccessResponseBody(false)
            .successResponseHeaders(new HashMap<>())
            .successResponseBody(new HashMap<>())
            .failResponseHeaders(new HashMap<>())
            .failResponseBody(new HashMap<>())
            .responseInNanoSecond(0L)
            .failResponseCode(400)
            .successResponseCode(200)
            //            .createdAt(System.currentTimeMillis())
            //            .updatedAt(System.currentTimeMillis())
            .build();
    restRepository.save(rest);
  }
}
