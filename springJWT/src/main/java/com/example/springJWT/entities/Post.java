package com.example.springJWT.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long postID;

    private Long userID;

    private boolean privatePost;

    private String postDescription;

    private boolean isCommentEnabled;

    @OneToMany
    private List<Comment> comment;
}
