package com.agussuhardi.restapisimulator.repository;

import com.agussuhardi.restapisimulator.entity.Rest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestRepository extends JpaRepository<Rest, String> {}
