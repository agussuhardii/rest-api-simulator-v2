package com.agussuhardi.restapisimulator.repository;

import com.agussuhardi.restapisimulator.entity.Rest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpMethod;

import java.util.Optional;

public interface RestRepository extends JpaRepository<Rest, String> {

  @Query("select r from Rest r where r.method=?1 and r.pathUrl=?2")
  Optional<Rest> getUrl(HttpMethod method, String pathUrl);
}
