package com.demo.blogger;

import com.demo.blogger.model.*;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.blogger.Blogger;
import com.google.api.services.blogger.BloggerScopes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.google.api.client.util.Lists.newArrayList;

@RestController
public class PostController {


   static Credential credential;
   static String token;

    @Autowired
    HttpSession session;

    @Autowired
    BlogRepository blogRepository;

    @Autowired
    PostRepository postRepository;


    @GetMapping("/posts/listPosts")
    List <com.demo.blogger.model.Post> getPosts(){
        List<Post> posts=new ArrayList<Post>();
        Iterable<Post> iterableposts=postRepository.findAll();
        iterableposts.forEach(posts::add);
        return posts;
    }

    @PostMapping("/posts/addPost")
    Post addPost(@RequestBody PostContent postcontent) throws BlogNotFoundException {
        Optional<Blog> blogOptional=blogRepository.findById(postcontent.getBlogid());
        Post post=new Post();
        post.setPostContent(postcontent.getContent());
        if(!blogOptional.isPresent())
            throw new BlogNotFoundException();
        Blog blog=blogOptional.get();
        post.setBlog(blog);
        postRepository.save(post);
        return post;
    }
    @PostMapping("/posts/updatePost")
    Post updatePost(@RequestBody PostContent postcontent) throws PostNotFoundException {
        Optional <Post> postOptional=postRepository.findById(postcontent.getId());
        Post post=null;
        if(!postOptional.isPresent()) {
            throw new PostNotFoundException();
        }
        
        postRepository.save(postOptional.get());
        return post;
    }
    

    @PostMapping("/posts/deletePost")
    String deletePost(@RequestBody PostContent postcontent) throws PostNotFoundException {
        Optional <Post> post=postRepository.findById(postcontent.getId());
        if(!post.isPresent()) {
            throw new PostNotFoundException();
        }
         postRepository.deleteById(post.get().getPostId());
        return "Success";
    }


}
