package com.techstar.topic.repository.jpa;

import com.techstar.modules.springframework.data.jpa.repository.MyJpaRepository;
import com.techstar.topic.entity.Topic;

public interface TopicDao extends MyJpaRepository<Topic, String>{

}
