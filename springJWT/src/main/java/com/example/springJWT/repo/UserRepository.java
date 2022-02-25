package com.example.springJWT.repo;


import com.example.springJWT.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Period;

@Repository
public interface UserRepository extends JpaRepository<Person,Long> {
    Person findPersonByUsername(String username);
    Person findPersonByUserID(Long userID);
}
