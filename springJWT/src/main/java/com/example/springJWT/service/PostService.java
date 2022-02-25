package com.example.springJWT.service;

import com.example.springJWT.entities.Comment;
import com.example.springJWT.entities.Person;
import com.example.springJWT.entities.Post;
import com.example.springJWT.repo.CommentRepo;
import com.example.springJWT.repo.PostRepo;
import com.example.springJWT.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    UserRepository repository;

    @Autowired
    PostRepo postRepo;

    @Autowired
    CommentRepo commentRepo;

    public Post newPost(Post post, Long userId){
        post.setUserID(userId);

        postRepo.save(post);
        return post;
    }

    public List<Post> getAllPost(){
        return postRepo.findAll().stream().filter(post -> !post.isPrivatePost()).collect(Collectors.toList());
    }


    public Map<String, String> addComment(Comment comment, String username, Long postId){
        Map<String, String> requestAnswer = new HashMap<>();

        Person author = repository.findPersonByUsername(username);
        Post post = postRepo.findPostByPostID(postId);

        if(!post.isCommentEnabled()){
            requestAnswer.put("Error", "Comments are not allowed!");
            return requestAnswer;
        }

        if(comment.getComment()==null){
            requestAnswer.put("Error","Comment data can not be null!");
            return requestAnswer;
        }

        comment.setAuthorId(author.getUserID());
        comment.setPostId(postId);

        if(post==null){
            requestAnswer.put("Error", "Post with that id does not exist!");
            return requestAnswer;
        }

        commentRepo.save(comment);
        post.getComment().add(comment);
        postRepo.save(post);

        requestAnswer.put("Message", "Comment was added!");
        return requestAnswer;
    }
}
