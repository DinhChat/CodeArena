package com.soict.CodeArena.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {
    private Long commentId;
    private Long problemId;
    private String problemTitle;
    private Long userId;
    private String username;
    private String content;
    private Long parentCommentId;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private List<CommentResponse> replies;
    private Integer replyCount;
}
