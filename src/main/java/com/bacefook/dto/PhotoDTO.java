package com.bacefook.dto;

import com.bacefook.model.Post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhotoDTO {
	@NonNull
	private Integer id;
	@NonNull
	private String url;
	@NonNull
	private Integer postId;
}
