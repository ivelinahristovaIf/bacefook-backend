package com.bacefook.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bacefook.dto.CommentContentDTO;
import com.bacefook.dto.CommentDTO;
import com.bacefook.exception.ElementNotFoundException;
import com.bacefook.exception.UnauthorizedException;
import com.bacefook.model.Comment;
import com.bacefook.service.CommentService;
import com.bacefook.service.PostService;
import com.bacefook.service.UserService;
import com.bacefook.utility.TimeConverter;

@RestController
public class CommentsController {
	@Autowired
	CommentService commentsService;
	@Autowired
	private UserService userService;

	@Autowired
	private PostService postService;
	
	@PostMapping("/commentlikes")
	public void addLikeToPost(@RequestParam("commentId") Integer commentId, HttpServletRequest request)
			throws UnauthorizedException {
		int userId = SessionManager.getLoggedUser(request).getId();
		commentsService.likeCommentById(userId, commentId);
	}

	@PostMapping("/comments")
	public ResponseEntity<Object> addCommentToPost(@RequestParam("postId") Integer postId,
			@RequestBody CommentContentDTO commentContentDto, HttpServletRequest request) throws UnauthorizedException {
		// throw exception if no user in session
		// TODO check if comment is a reply on another comment
		// TODO validate if properties are not empty

		int posterId = SessionManager.getLoggedUser(request).getId();
		Comment comment = new Comment(posterId, postId, commentContentDto.getContent(), LocalDateTime.now());

		// TODO validate with status code
		commentsService.saveComment(comment);
		return new ResponseEntity<>(comment.getId(), HttpStatus.OK);
	}

	// get comment by id
	@PutMapping("/comments")
	public void updateComment(@RequestParam("commentId") Integer commentId, @RequestBody CommentContentDTO content,
			HttpServletRequest request) throws UnauthorizedException, ElementNotFoundException {
		if (SessionManager.isLogged(request)) {
			System.out.println(content);
			Comment comment = commentsService.findCommentById(commentId);

			if (content.getContent().isEmpty()) {
				throw new ElementNotFoundException("Cannot update comment with empty content!");
			}
			comment.setContent(content.getContent());
			commentsService.saveComment(comment);
		} else {
			throw new UnauthorizedException("You are not logged in! Please log in before trying to update your posts.");
		}
	}

	@GetMapping("/comments")
	public ResponseEntity<List<CommentDTO>> getAllCommentsByPost(@RequestParam("postId") Integer postId,
			HttpServletRequest request) throws ElementNotFoundException {

		// TODO check if user is friend with poster
//		SessionManager.getLoggedUser(request);
		postService.findPostById(postId);

		List<Comment> comments = commentsService.getAllCommentsByPostId(postId);
		List<CommentDTO> commentsOnPost = new ArrayList<>();

		for (Comment comment : comments) {
			String posterFullName = userService.findUserById(comment.getPosterId()).getFullName();

			String timeOfPosting = TimeConverter.convertTimeToString(comment.getPostingTime());

			CommentDTO commentDto = new CommentDTO(posterFullName, comment.getCommentedOnId(), comment.getContent(),
					timeOfPosting);

			commentsOnPost.add(commentDto);
		}
		return new ResponseEntity<>(commentsOnPost, HttpStatus.OK);

	}

}
