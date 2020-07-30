package com.demo.blogger;

import com.demo.blogger.model.Blog;
import com.demo.blogger.model.BlogRepository;
import com.demo.blogger.model.PostRepository;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Lists;
import com.google.api.services.blogger.Blogger;
import com.google.api.services.blogger.BloggerScopes;
import com.google.api.services.blogger.model.BlogList;
import com.google.api.services.blogger.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.demo.blogger.model.Post;
import com.demo.blogger.model.Blog;

import static com.google.api.client.util.Lists.*;

@Controller
public class WelcomeController {


   Credential credential=null;
   static String token;

    @Autowired
    HttpSession session;

    @Autowired
    BlogRepository blogRepository;

    @Autowired
    PostRepository postRepository;


    @GetMapping("/Welcome")
    String printTest(HttpSession session) throws Exception {
       return "Welcome";

   }
    @GetMapping  ("/Callback")
    String setAuthentication(HttpSession session)
    {
        session.setAttribute("Authenticated","true");
        Iterable<Blog> iterableBlogs = blogRepository.findAll();
        if(!iterableBlogs.iterator().hasNext()) {
            com.demo.blogger.model.Blog blog = new Blog();
            blog.setFirstName("Admin");
            blog.setLastName("Blogger");
            blog.setUserId("Admin");
            blog = blogRepository.save(blog);
            Post post = new Post();
            post.setPostContent("My First Motivational Post");
            post.setBlog(blog);
            postRepository.save(post);
        }
        return "DisplayBlog";
    }

    @GetMapping  ("/blog/display")
    String listpost(HttpSession session)
    {
        Iterable<Blog> iterableBlogs = blogRepository.findAll();
        if(!iterableBlogs.iterator().hasNext()) {
            com.demo.blogger.model.Blog blog = new Blog();
            blog.setFirstName("Admin");
            blog.setLastName("Blogger");
            blog.setUserId("Admin");
            blog = blogRepository.save(blog);
            Post post = new Post();
            post.setPostContent("My First Motivational Post");
            post.setBlog(blog);
            postRepository.save(post);
        }
        return "DisplayBlog";
    }

    private static Credential authorize()throws Exception
  {
      HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
      JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

      GoogleClientSecrets.Details details = new GoogleClientSecrets.Details();
      details.setClientId("840235111122-4fq5j5diquqkt2864mmfqmba51lkib9n.apps.googleusercontent.com");
      details.setClientSecret("KlKbdJbJ5N-M94flyWx69DgW");

      String redirectUrl = "http://localhost:9000/Callback";
      List<String> scopes = Arrays.asList(BloggerScopes.BLOGGER);

      GoogleClientSecrets clientSecrets = new GoogleClientSecrets();
      clientSecrets.setInstalled(details);

      GoogleAuthorizationCodeFlow authorizationFlow = new GoogleAuthorizationCodeFlow
              .Builder(httpTransport, jsonFactory, clientSecrets, scopes)
              .setAccessType("offline").build();

      String authorizeUrl =
              authorizationFlow.newAuthorizationUrl().setRedirectUri(redirectUrl).build();
      System.out.println("Paste this url in your browser: \n" + authorizeUrl + '\n');

      // Wait for the authorization code.
      System.out.println("Type the code you received here: ");
      String authorizationCode = new BufferedReader(new InputStreamReader(System.in)).readLine();

      // Authorize the OAuth2 token.
      GoogleAuthorizationCodeTokenRequest tokenRequest =
              authorizationFlow.newTokenRequest(authorizationCode);
      tokenRequest.setRedirectUri(redirectUrl);
      GoogleTokenResponse tokenResponse = tokenRequest.execute();

      // Create the OAuth2 credential.
      GoogleCredential credential = new GoogleCredential.Builder()
              .setTransport(new NetHttpTransport())
              .setJsonFactory(new JacksonFactory())
              .setClientSecrets(clientSecrets)
              .build();


      // Set authorized credentials.
      //credential.setFromTokenResponse(tokenResponse);

      return new AuthorizationCodeInstalledApp(authorizationFlow, new LocalServerReceiver()).authorize("self");
  }

   protected void doTest2() throws Exception
   {
      // Configure the Installed App OAuth2 flow.
       credential = authorize();
       //token=credential.getRefreshToken();
       session.setAttribute("credential",credential);
     /*HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
      JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

      Blogger blogger = new Blogger.Builder(httpTransport, jsonFactory, credential).setApplicationName("BloggerApp").setHttpRequestInitializer(credential)
              .build();

      // The request action object.
      // Blogger blogger = Blogger.builder(HTTP_TRANSPORT, JSON_FACTORY)
      //    .setApplicationName("Blogger-BlogGet-Snippet/1.0")
      //   .setHttpRequestInitializer(credential).build();

// This is the request action that you can configure before sending the request.
      Blogger.Blogs.Get blogGetAction = blogger.blogs().get("2890972386721562237");

// Restrict the result content to just the data we need.
      blogGetAction.setFields("description,name,posts/totalItems,updated");

// This step sends the request to the server.
      Blog blog = blogGetAction.execute();

// Now we can navigate the response.
      System.out.println("Name: "+blog.getName());
      System.out.println("Description: "+blog.getDescription());
      System.out.println("Post Count: "+blog.getPosts().getTotalItems());
      System.out.println("Last Updated: "+blog.getUpdated());*/



   }


}
