package com.example.springJWT.repo;

import com.example.springJWT.entities.Request;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepo extends JpaRepository<Request, Long> {
    Request findRequestByRequestId(Long id);
}
