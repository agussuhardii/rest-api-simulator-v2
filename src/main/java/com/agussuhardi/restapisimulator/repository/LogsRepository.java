package com.agussuhardi.restapisimulator.repository;

import com.agussuhardi.restapisimulator.entity.Logs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface LogsRepository extends PagingAndSortingRepository<Logs, UUID> {

}