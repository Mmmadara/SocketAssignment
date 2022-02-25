package com.example.springJWT.service;

import com.example.springJWT.entities.Person;
import com.example.springJWT.entities.Post;
import com.example.springJWT.entities.Request;
import com.example.springJWT.repo.RequestRepo;
import com.example.springJWT.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository repository;

    @Autowired
    PostService postService;

    @Autowired
    RequestRepo requestRepo;

    @Autowired
    PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person user = repository.findPersonByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found in the database");
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();


        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(),
                true,
                true,
                true,
                true,
                authorities);
    }

    public Person getUserByUsername(String username) {
        return repository.findPersonByUsername(username);
    }

    public Map<String, String> registerUser(Person user) {
        Person checkUserByUsername = repository.findPersonByUsername(user.getUsername());

        Map<String, String> response = new HashMap<>();

        if (checkUserByUsername != null) {
            response.put("Error", "User with the given username exists!");
            return response;
        }

        if (user.getPassword() == null || user.getPassword().equals("")) {
            response.put("Error", "Password cannot be empty!");
            return response;
        }

        user.setPassword(encoder.encode(user.getPassword()));
        repository.save(user);
        response.put("Message", "You have successfully registered!");
        return response;
    }

    public List<String> getFriends(String username) {
        Person user = repository.findPersonByUsername(username);
        return user.getFriends().stream().map(Person::getUsername).collect(Collectors.toList());
    }

    public List<Post> getFriendsPosts(String username) {
        Person user = repository.findPersonByUsername(username);
        List<Post> returnPost = new ArrayList<>();

        user.getFriends().forEach(person -> person.getPosts().forEach(post -> {
            if(post.isPrivatePost()) {
                returnPost.add(post);
            }
        }));

        return returnPost;
    }


    public Map<String, String> addPost(Post post, String username){
        Person user = repository.findPersonByUsername(username);
        Map<String, String> response = new HashMap<>();
        if(post == null){
            response.put("Error", "Post can not be empty!");
            return response;
        }

        Post newPost = postService.newPost(post, user.getUserID());
        user.getPosts().add(newPost);
        repository.save(user);

        response.put("Message", "You have created new post!");
        return response;
    }

    public List<Request> getRequests(String username) {
        Person user = repository.findPersonByUsername(username);

        return user.getRequests();
    }

    public Map<String, String> sendFriendRequest(String username, Long receiverId){
        Person receiver = repository.findPersonByUserID(receiverId);
        Person sender = repository.findPersonByUsername(username);
        Map<String, String> response = new HashMap<>();

        Request req = new Request(sender.getUserID());

        if(receiver == null){
            response.put("Error", "User does not exist!");
            return response;
        }

        requestRepo.save(req);

        receiver.getRequests().add(req);
        repository.save(receiver);
        response.put("Message", "Request was successfully sent");
        return response;
    }

    public Map<String, String> acceptFriendRequest(String requestId, String username){
        Request req = requestRepo.findRequestByRequestId(Long.parseLong(requestId));
        Person user = repository.findPersonByUsername(username);

        Map<String, String> response = new HashMap<>();


        if(req == null){
            response.put("Error", "Request is invalid!");
            return response;
        }

        if(!user.getRequests().contains(req)){
            response.put("Error", "Request is invalid!");
            return response;
        }



        Person newFriend = repository.findPersonByUserID(req.getUserId());
        user.getRequests().remove(req);
        user.getFriends().add(newFriend);

        newFriend.getFriends().add(user);
        repository.save(user);

        repository.save(user);
        requestRepo.delete(req);
        response.put("Message", "New friend was accepted!");
        return response;
    }
}
