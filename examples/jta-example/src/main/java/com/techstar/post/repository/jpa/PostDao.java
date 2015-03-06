package com.techstar.post.repository.jpa;

import com.techstar.modules.springframework.data.jpa.repository.MyJpaRepository;
import com.techstar.post.entity.Post;

public interface PostDao extends MyJpaRepository<Post, String>{

}
