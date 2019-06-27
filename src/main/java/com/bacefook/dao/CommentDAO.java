package com.bacefook.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class CommentDAO {
	private static final String FIND_COMMENT_LIKE_BY = "SELECT comment_id FROM comments_like WHERE user_id= ? AND comment_id= ?;";
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Integer> findCommentLikeByUserIdAndCommentId(Integer userId, Integer commentId ) {
		return jdbcTemplate.query(FIND_COMMENT_LIKE_BY, ps -> {
			ps.setInt(1, userId);
			ps.setInt(2, commentId);
		}, (resultSet, rowNum) -> resultSet.getInt("id"));
	}
}
