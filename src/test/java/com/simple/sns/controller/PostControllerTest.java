package com.simple.sns.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simple.sns.controller.request.PostCreateRequest;
import com.simple.sns.controller.request.PostModifyRequest;
import com.simple.sns.exception.ErrorCode;
import com.simple.sns.exception.SnsApplicationException;
import com.simple.sns.model.Post;
import com.simple.sns.service.PostService;
import fixture.PostEntityFixture;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

	@Test
	@WithMockUser
	void 포스트삭제() throws Exception {
		mockMvc.perform(delete("/api/v1/posts/1")
				.contentType(MediaType.APPLICATION_JSON)
			).andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	@WithAnonymousUser
	void 포스트삭제시_로그인하지_않은경우() throws Exception {
		mockMvc.perform(delete("/api/v1/posts/1")
				.contentType(MediaType.APPLICATION_JSON)
			).andDo(print())
			.andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser
	void 포스트삭제시_작성자와_삭제요청자가_다를경우() throws Exception {
		// mocking
		doThrow(new SnsApplicationException(ErrorCode.INVALID_PERMISSION)).when(postService).delete(any(), any());

		mockMvc.perform(delete("/api/v1/posts/1")
				.contentType(MediaType.APPLICATION_JSON)
			).andDo(print())
			.andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser
	void 포스트삭제시_삭제하려는_포스트가_존재하지_않을_경우() throws Exception {
		// mocking
		doThrow(new SnsApplicationException(ErrorCode.POST_NOT_FOUND)).when(postService).delete(any(), any());

		mockMvc.perform(delete("/api/v1/posts/1")
				.contentType(MediaType.APPLICATION_JSON)
			).andDo(print())
			.andExpect(status().isNotFound());
	}

	@Test
	@WithMockUser
	void 피드목록() throws Exception {
		when(postService.list(any())).thenReturn(Page.empty());

		mockMvc.perform(get("/api/v1/posts")
				.contentType(MediaType.APPLICATION_JSON)
			).andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	@WithAnonymousUser
	void 피드목록요청시_로그인하지_않은경우() throws Exception {
		when(postService.list(any())).thenReturn(Page.empty());

		mockMvc.perform(get("/api/v1/posts")
				.contentType(MediaType.APPLICATION_JSON)
			).andDo(print())
			.andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser
	void 내피드목록() throws Exception {
		when(postService.my(any(), any())).thenReturn(Page.empty());

		mockMvc.perform(get("/api/v1/posts/my")
				.contentType(MediaType.APPLICATION_JSON)
			).andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	@WithAnonymousUser
	void 내피드목록요청시_로그인하지_않은경우() throws Exception {
		when(postService.my(any(), any())).thenReturn(Page.empty());

		mockMvc.perform(get("/api/v1/posts/my")
				.contentType(MediaType.APPLICATION_JSON)
			).andDo(print())
			.andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser
	void 좋아요기능() throws Exception {
		mockMvc.perform(post("/api/v1/posts/1/likes")
				.contentType(MediaType.APPLICATION_JSON)
			).andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	@WithAnonymousUser
	void 좋아요버튼클릭시_로그인하지_않은경우() throws Exception {
		mockMvc.perform(get("/api/v1/posts/1/likes")
				.contentType(MediaType.APPLICATION_JSON)
			).andDo(print())
			.andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser
	void 좋아요버튼클릭시_게시물이_없는경우() throws Exception {
		doThrow(new SnsApplicationException(ErrorCode.POST_NOT_FOUND)).when(postService).like(any(),any());

		mockMvc.perform(get("/api/v1/posts/1/likes")
				.contentType(MediaType.APPLICATION_JSON)
			).andDo(print())
			.andExpect(status().isNotFound() );
	}

}
