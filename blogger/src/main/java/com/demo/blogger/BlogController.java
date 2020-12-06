package com.demo.blogger;

import com.demo.blogger.model.*;
import com.google.api.client.auth.oauth2.Credential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

@RestController
public class BlogController {


    static Credential credential;
    static String token;

    @Autowired
    HttpSession session;

    @Autowired
    BlogRepository blogRepository;

   // @Autowired
   // PostRepository postRepository;


    @GetMapping("/blogs/listBlog")
    List<Blog> getBlogs() {
        List<Blog> Blogs = new ArrayList<Blog>();
        Iterable<Blog> iterableBlogs = blogRepository.findAll();
        iterableBlogs.forEach(Blogs::add);
        return Blogs;
    }

    @GetMapping("/blogs/createFirstBlog")
    Blog createBlog() {
        Blog blog = new Blog();
        blog.setFirstName("Admin");
        blog.setLastName("Blogger");
        blog.setUserId("Admin");
        blog = blogRepository.save(blog);
        return blog;

    }

    @PostMapping("/blogs/createBlog")
    Blog createBlog(@RequestBody Blog blog) {
        blogRepository.save(blog);
        return blog;
    }

    @PostMapping("/blogs/addupdateBlog")
    Blog updateBlog(@RequestBody Blog blog) {
        blogRepository.save(blog);
        return blog;
    }

    @PostMapping("/blogs/findById")
    Blog getBlogById(@RequestParam Long id) throws BlogNotFoundException {
        Optional<Blog> blogOptional = blogRepository.findById(id);
        if(!blogOptional.isPresent())
            throw new BlogNotFoundException();
        Blog blog =  blogOptional.get();
        return blog;
    }

    @PostMapping("/blogs/deleteBlog")
    String deleteBlog(@RequestBody Blog blog) {
        blogRepository.deleteById(blog.getId());
        return "Success";
    }

    @PostMapping("/blog/TestBlog")
     String testBlog(@RequestBody Blog b)
    {
        return "TestCase"+b.getLastName();
    }

}
