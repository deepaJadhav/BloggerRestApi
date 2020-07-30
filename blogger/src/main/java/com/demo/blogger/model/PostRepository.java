package com.demo.blogger.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

import java.util.Optional;


public interface PostRepository extends CrudRepository<Post,Long> {
    @Override
    Optional<Post> findById(Long aLong);

    @Override
    <S extends Post> S save(S s);


    @Override
    void deleteById(Long aLong);
    //Post save(Post post);

}

