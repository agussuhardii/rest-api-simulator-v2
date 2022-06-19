package com.agussuhardi.restapisimulator.repository;

import com.agussuhardi.restapisimulator.entity.Logs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.http.HttpMethod;

import java.util.UUID;

public interface LogsRepository extends PagingAndSortingRepository<Logs, UUID> {

    Page<Logs> findAllByMethodAndUri(HttpMethod method, String uri, Pageable pageable);
}