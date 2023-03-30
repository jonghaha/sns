package com.simple.sns.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simple.sns.controller.request.PostCreateRequest;
import com.simple.sns.controller.request.PostModifyRequest;
import com.simple.sns.exception.ErrorCode;
import com.simple.sns.exception.SnsApplicationException;
import com.simple.sns.model.Post;
import com.simple.sns.service.PostService;

import fixture.PostEntityFixture;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private PostService postService;

	@Test
	@WithMockUser
	void 포스트작성() throws Exception {

		String title = "title";
		String body = "body";

		mockMvc.perform(post("/api/v1/posts")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(new PostCreateRequest(title, body)))
			).andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	@WithAnonymousUser
	void 포스트작성시_로그인하지않은경우() throws Exception {

		String title = "title";
		String body = "body";

		mockMvc.perform(post("/api/v1/posts")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(new PostCreateRequest(title, body)))
			).andDo(print())
			.andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser
	void 포스트수정() throws Exception {

		String title = "title";
		String body = "body";

		when(postService.modify(eq(title), eq(body), any(), any())).
			thenReturn(Post.fromEntity(PostEntityFixture.get("userName", 1, 1)));

		mockMvc.perform(put("/api/v1/posts/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, body)))
			).andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	@WithAnonymousUser
	void 포스트수정시_로그인하지않은경우() throws Exception {

		String title = "title";
		String body = "body";

		mockMvc.perform(put("/api/v1/posts/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, body)))
			).andDo(print())
			.andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser
	void 포스트수정시_본인이_작성한_글이_아니라면_에러발생() throws Exception {

		String title = "title";
		String body = "body";

		doThrow(new SnsApplicationException(ErrorCode.INVALID_PERMISSION)).when(postService).modify(eq(title), eq(body), any(), eq(1));

		mockMvc.perform(put("/api/v1/posts/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, body)))
			).andDo(print())
			.andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser
	void 포스트수정시_수정하려는_글이_없는경우_에러발생() throws Exception {

		String title = "title";
		String body = "body";

		doThrow(new SnsApplicationException(ErrorCode.POST_NOT_FOUND)).when(postService).modify(eq(title), eq(body), any(), eq(1));

		mockMvc.perform(put("/api/v1/posts/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, body)))
			).andDo(print())
			.andExpect(status().isNotFound());
	}
}
