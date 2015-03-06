package com.techstar.topic.service;

import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.techstar.modules.utils.Identities;
import com.techstar.post.entity.Post;
import com.techstar.post.repository.jpa.PostDao;
import com.techstar.topic.entity.Topic;
import com.techstar.topic.repository.jpa.TopicDao;

@ContextConfiguration(locations = { "classpath:spring/applicationContext.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@Transactional
public class JTAServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private TopicDao topicDao;// oralce数据源
	@Autowired
	private PostDao postDao;// mysql数据源

	/**
	 * 单独俣存Topic到oracle数据库
	 */
	@Test
	public void saveTopic() {

		Topic topic = new Topic();
		topic.setForumId(Identities.uuid2());
		topic.setTopicReplies(5);
		topic.setTopicTime(new Date());
		topic.setTopicTitle(Identities.uuid2());
		topic.setTopicViews(2);
		topic.setUserId(Identities.uuid2());
		topicDao.save(topic);

		Assert.assertNotNull(topic.getId());
	}

	/**
	 * 单独俣存Post到mydql数据库
	 */
	@Test
	public void savePost() {

		Post post = new Post();
		post.setForumId(Identities.uuid2());
		post.setUserId(Identities.uuid2());
		post.setPostAttach(new String(Identities.uuid2()).getBytes());
		post.setPostTime(new Date());
		post.setTopic(Identities.uuid2());
		postDao.save(post);

		Assert.assertNotNull(post.getId());
	}

	/**
	 * 同时保存Topic到oracle数据库、Post到mydql数据库
	 */
	@Test
	public void saveTopicAndPostCommit() {

		Topic topic = new Topic();
		topic.setForumId(Identities.uuid2());
		topic.setTopicReplies(5);
		topic.setTopicTime(new Date());
		topic.setTopicTitle(Identities.uuid2());
		topic.setTopicViews(2);
		topic.setUserId(Identities.uuid2());
		topicDao.save(topic);

		Post post = new Post();
		post.setForumId(Identities.uuid2());
		post.setUserId(Identities.uuid2());
		post.setPostAttach(new String(Identities.uuid2()).getBytes());
		post.setPostTime(new Date());
		post.setTopic(topic.getId());
		postDao.save(post);

		Assert.assertNotNull(topic.getId());
		Assert.assertNotNull(post.getId());
	}

	/**
	 * Post回滚、Topic一起回滚
	 */
	@Test
	public void saveTopicAndPostRollback() {

		Topic topic = new Topic();
		topic.setForumId(Identities.uuid2());
		topic.setTopicReplies(5);
		topic.setTopicTime(new Date());
		topic.setTopicTitle(Identities.uuid2());
		topic.setTopicViews(2);
		topic.setUserId(Identities.uuid2());

		topicDao.save(topic);

		Post post = new Post();
		post.setForumId(null);// ForumId在数据设置不能为空，在这里设置为null，测试数据一起回滚
		post.setUserId(Identities.uuid2());
		post.setPostAttach(new String(Identities.uuid2()).getBytes());
		post.setPostTime(new Date());
		post.setTopic(topic.getId());

		postDao.save(post);

	}

	/**
	 * Topic回滚、Post一起回滚
	 */
	@Test(expected = ArithmeticException.class)
	public void saveTopicAndPostRollback2() {

		Post post = new Post();
		post.setForumId(Identities.uuid2());
		post.setUserId(Identities.uuid2());
		post.setPostAttach(new String(Identities.uuid2()).getBytes());
		post.setPostTime(new Date());
		post.setTopic(Identities.uuid2());

		postDao.save(post);

		Topic topic = new Topic();
		topic.setForumId(Identities.uuid2());
		topic.setTopicReplies(5);
		topic.setTopicTime(new Date());
		topic.setTopicTitle(Identities.uuid2());
		topic.setTopicViews(2);
		topic.setUserId(null);// UserId在数据设置不能为空，在这里设置为null，测试数据一起回滚

		topicDao.save(topic);

	}
}
