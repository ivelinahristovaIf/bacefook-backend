package com.bacefook.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bacefook.dto.CommentContentDTO;
import com.bacefook.dto.CommentDTO;
import com.bacefook.exception.AlreadyContainsException;
import com.bacefook.exception.ElementNotFoundException;
import com.bacefook.exception.UnauthorizedException;
import com.bacefook.entity.Comment;
import com.bacefook.service.CommentService;
import com.bacefook.service.PostService;
import com.bacefook.service.UserService;

//@CrossOrigin(origins = "http://bacefook.herokuapp.com")
@RestController
public class CommentsController {
	@Autowired
	private CommentService commentsService;
	@Autowired
	private UserService userService;
	@Autowired
	private PostService postService;

	private ModelMapper mapper = new ModelMapper();

	@PostMapping("/commentlikes")//DONE
	public String addLikeToComment(@RequestParam("commentId") Integer commentId, HttpServletRequest request)
			throws UnauthorizedException, ElementNotFoundException, AlreadyContainsException {
		commentsService.addLikeToComment(commentId, request);
		return "Comment " + commentId + " was liked";
	}

	@DeleteMapping("/commentlikes/unlike")
	public String unlikeAComment(@RequestParam("commentId")Integer commentId,HttpServletRequest request) throws UnauthorizedException, ElementNotFoundException {
		int userId = SessionManager.getLoggedUser(request);
		int rows = commentsService.unlikeAComment(commentId, userId);
		if(rows>0) {
			return "Comment was unliked!";
		}else {
			return "Could not unlike comment.";
		}
	}

	@PostMapping("/commentreply")//DONE
	public void addReplyToComment(@RequestParam("commentId") Integer commentId,
			@RequestBody CommentContentDTO commentContentDto, HttpServletRequest request)
			throws UnauthorizedException, ElementNotFoundException {
		commentsService.replyTo(request, commentId, commentContentDto);
	}

	@PostMapping("/comments")
	public Integer addCommentToPost(@RequestParam("postId") Integer postId,
			@RequestBody CommentContentDTO commentContentDto, HttpServletRequest request)
			throws UnauthorizedException, ElementNotFoundException {
		return commentsService.save(request, postId, commentContentDto);
	}

	@PutMapping("/comments")
	public void updateComment(@RequestParam("commentId") Integer commentId, @RequestBody CommentContentDTO content,
			HttpServletRequest request) throws UnauthorizedException, ElementNotFoundException {
		if (SessionManager.isLogged(request)) {
			if (content.getContent().isEmpty()) {
				throw new ElementNotFoundException("Cannot update comment with empty content!");
			}
			commentsService.update(commentId,content.getContent());
		} else {
			throw new UnauthorizedException("You are not logged in! Please log in before trying to update your posts.");
		}
	}
	
	@DeleteMapping("/comments/delete")
	public String deleteComment(@RequestParam("commentId")Integer id,HttpServletRequest request) throws UnauthorizedException, ElementNotFoundException {
		commentsService.deleteComment(id,request);
		return "Comment was deleted";
	}

	@GetMapping("/comments")
	public List<CommentDTO> getAllCommentsByPost(@RequestParam("postId") Integer postId)
			throws ElementNotFoundException {
		/**
		 *  checks if post exists
		 **/
		postService.findById(postId);
		List<Comment> comments = commentsService.findAllByPostId(postId);
		List<CommentDTO> commentsOnPost = new ArrayList<>();

		for (Comment comment : comments) {
			String posterFullName = userService.findById(comment.getPosterId()).getFullName();
			CommentDTO commentDto = new CommentDTO();
			this.mapper.map(comment, commentDto);
			commentDto.setPosterFullName(posterFullName);
			commentDto.setComentedOnId(comment.getCommentedOn().getId());
			commentsOnPost.add(commentDto);
		}
		return commentsOnPost;

	}

	@GetMapping("/commentreplies")
	public List<CommentDTO> getAllCommentReplies(@RequestParam("commentId") Integer commentId)
			throws ElementNotFoundException {
		/**
		 * checks if comment exists
		 */
		commentsService.findById(commentId);
		return commentsService.findAllRepliesTo(commentId);
	}

}
