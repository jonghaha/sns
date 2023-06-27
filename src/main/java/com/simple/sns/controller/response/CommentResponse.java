package com.simple.sns.controller.response;

import com.simple.sns.model.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class CommentResponse {
    private Integer id;

    private String comment;

    private String userName;

    private Integer postId;

    private Timestamp registerAt;

    private Timestamp updatedAt;

    private Timestamp deletedAt;

    public static CommentResponse fromComment(Comment comment) {
        return new CommentResponse(comment.getId(), comment.getComment(), comment.getUserName(), comment.getPostId(), comment.getRegisterAt(), comment.getUpdatedAt(), comment.getDeletedAt());
    }
}
