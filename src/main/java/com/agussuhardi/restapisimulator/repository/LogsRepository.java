package com.agussuhardi.restapisimulator.repository;

import com.agussuhardi.restapisimulator.entity.Logs;
import com.agussuhardi.restapisimulator.entity.Rest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.http.HttpMethod;

import java.util.UUID;

public interface LogsRepository extends PagingAndSortingRepository<Logs, UUID> {



    @Query("select l from Logs l where (?1 is null or l.method=?1) and (?2 is null or l.uri=?2)")
    Page<Logs> findAllBy(String method, String pathUrl, Pageable pageable);
}