package com.example.springJWT.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long requestId;

    private Boolean requestStatus;

    private Long userId;

    public Request(Long userId){
        this.userId = userId;
        this.requestStatus = false;
    }

    public Request() {

    }
}
