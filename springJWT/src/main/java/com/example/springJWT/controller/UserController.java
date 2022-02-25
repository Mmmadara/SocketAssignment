package com.example.springJWT.controller;

import com.example.springJWT.entities.Post;
import com.example.springJWT.entities.Request;
import com.example.springJWT.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;
import java.util.Map;


@RequestMapping("/user")
@Controller
public class UserController {

    @Autowired
    UserService userService;



    @GetMapping("/getFriendsList")
    public ResponseEntity<List<String>> getFriendsList(@AuthenticationPrincipal String username) throws IOException {

        if (username == null){
            throw new RuntimeException("Please, sign in!");
        }
         return ResponseEntity.ok().body(userService.getFriends(username));
    }

    @GetMapping("/getPosts")
    public ResponseEntity<List<Post>> getPosts(@AuthenticationPrincipal String username){
        if (username == null){
            throw new RuntimeException("Please, sign in!");
        }
        return ResponseEntity.ok().body(userService.getFriendsPosts(username));
    }

    @GetMapping("/getRequests")
    public ResponseEntity<List<Request>> getRequests(@AuthenticationPrincipal String username){
        if (username == null){
            throw new RuntimeException("Please, sign in!");
        }
        return ResponseEntity.ok().body(userService.getRequests(username));
    }

    @PostMapping("/sendFriendRequest/{userID}")
    public ResponseEntity<String> sendFriendRequest(@AuthenticationPrincipal String username, @PathVariable(value="userID") Long userID){
        Map<String, String> requestAnswer = userService.sendFriendRequest(username, userID);

        if (username == null){
            throw new RuntimeException("Please, sign in!");
        }

        if(requestAnswer.containsKey("Error")){
            return ResponseEntity
                    .badRequest()
                    .body(requestAnswer.get("Error"));
        }

        return ResponseEntity.ok().body(requestAnswer.get("Message"));
    }

    @PostMapping("/acceptFriendRequest/{requestId}")
    public ResponseEntity<String> acceptFriendRequest(@AuthenticationPrincipal String username, @PathVariable String requestId){
        Map<String, String> requestAnswer = userService.acceptFriendRequest(requestId, username);

        if (username == null){
            throw new RuntimeException("Please, sign in!");
        }

        if(requestAnswer.containsKey("Error")){
            return ResponseEntity
                    .badRequest()
                    .body(requestAnswer.get("Error"));
        }

        return ResponseEntity.ok().body(requestAnswer.get("Message"));
    }

}
