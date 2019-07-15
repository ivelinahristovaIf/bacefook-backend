package com.bacefook.service;

import java.util.NoSuchElementException;

import com.bacefook.controller.SessionManager;
import com.bacefook.entity.Comment;
import com.bacefook.entity.User;
import com.bacefook.exception.ElementNotFoundException;
import com.bacefook.exception.UnauthorizedException;
import com.bacefook.repository.CommentsLikeRepository;
import com.bacefook.repository.CommentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bacefook.dao.CommentDAO;
import com.bacefook.exception.AlreadyContainsException;

import javax.servlet.http.HttpServletRequest;

@Service
public class CommentLikeService {
	@Autowired
	private CommentsLikeRepository commentsLikeRepository;
	@Autowired
	private CommentsRepository commentsRepository;
	@Autowired
	private UserService userService;

	public void addLikeToComment(HttpServletRequest request, Integer commentId) throws AlreadyContainsException, UnauthorizedException, ElementNotFoundException {
		int userId = SessionManager.getLoggedUser(request);
		//TODO
		List<CommentLike> likes =commentsLikeRepository.findAllByUserIdAndCommentId(userId,commentId);
		if(!likes.isEmpty()) {
			throw new AlreadyContainsException("You have already liked this comment!");
		}
		Comment comment;
		User user;
		try {
			comment = commentsRepository.findById(commentId).get();
		}catch (NoSuchElementException ex) {
			throw new NoSuchElementException(ex.getMessage());
		}
		user = userService.findById(userId);
			commentsLikeRepository.save(CommentLike.builder()
				.comment(comment)
				.user(user).build());

	}

	public void unlikeAComment(Integer commentId, Integer userId) throws ElementNotFoundException {
		//TODO test
		Comment comment;
		User user;
		try {
			comment = commentsRepository.findById(commentId).get();
		}catch (NoSuchElementException ex){
			throw new NoSuchElementException(ex.getMessage());
		}
		user = userService.findById(userId);
		commentsLikeRepository.delete(CommentLike.builder().comment(comment).user(user).build());
	}
}
