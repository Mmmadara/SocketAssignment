package com.example.springJWT.repo;

import com.example.springJWT.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepo extends JpaRepository<Post, Long> {
    List<Post> findAll();
    Post findPostByPostID(Long postID);
}
