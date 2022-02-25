package com.example.springJWT.controller;

import com.example.springJWT.entities.Comment;
import com.example.springJWT.entities.Post;
import com.example.springJWT.service.PostService;
import com.example.springJWT.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PostController {

    @Autowired
    PostService postService;

    @Autowired
    UserService userService;

    @GetMapping("/allPosts")
    public ResponseEntity<List<Post>> getAllPosts(){
        return ResponseEntity.ok().body(postService.getAllPost());
    }

    @PostMapping("/addPost")
    public ResponseEntity<String> createPost(@AuthenticationPrincipal String username, @RequestBody Post post){
        if (username == null){
            throw new RuntimeException("Please, sign in!");
        }

        Map<String, String> requestAnswer = userService.addPost(post,username);


        if(requestAnswer.containsKey("Error")){
            return ResponseEntity
                    .badRequest()
                    .body(requestAnswer.get("Error"));
        }

        return ResponseEntity.ok().body(requestAnswer.get("Message"));
    }

    @PostMapping("/{postID}/addComment")
    public ResponseEntity<String> addComment(@PathVariable(value = "postID") Long postId,
                                          @AuthenticationPrincipal String username, @RequestBody Comment comment){
        if (username == null){
            throw new RuntimeException("Please, sign in!");
        }

        Map<String, String> requestAnswer = postService.addComment(comment, username,postId);


        if(requestAnswer.containsKey("Error")){
            return ResponseEntity
                    .badRequest()
                    .body(requestAnswer.get("Error"));
        }

        return ResponseEntity.ok().body(requestAnswer.get("Message"));
    }
}
