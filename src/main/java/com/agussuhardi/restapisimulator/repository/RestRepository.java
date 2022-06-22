package com.agussuhardi.restapisimulator.repository;

import com.agussuhardi.restapisimulator.entity.Rest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

// @Repository
public interface RestRepository extends PagingAndSortingRepository<Rest, UUID> {

  @Query("select r from Rest r where r.method=?1 and r.uri=?2")
  Optional<Rest> getUrl(HttpMethod method, String pathUrl);

  @Query("select r from Rest r where (?1 is null or r.method=?1) and r.uri=?2")
  Page<Rest> findAllBy(String method, String pathUrl, Pageable pageable);
}
