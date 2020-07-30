package com.demo.blogger.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface BlogRepository  extends CrudRepository<Blog,Long> {
    @Override
    Optional<Blog> findById(Long blogId);



    @Query("SELECT  b from Blog b where b.userId=:userId")
    List<Blog> findByUserId(@Param("userId") String userId);

    @Query("SELECT  b.posts from Blog b where b.id=:blogid")
    List<Post> listOfPost(@Param("blogid") Long blogId);


    @Override
    <S extends Blog> S save(S s);

    Post save(Post post);
}
