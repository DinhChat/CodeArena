package com.soict.CodeArena.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequest {
    private Long problemId;
    private String content;
    private Long parentCommentId;
}
