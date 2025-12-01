package com.soict.CodeArena.repository;

import com.soict.CodeArena.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    // Find all comments by problem ID (not deleted, top-level only)
    List<Comment> findByProblem_ProblemIdAndIsDeletedFalseAndParentCommentIsNullOrderByCreatedDateDesc(Long problemId);
    
    // Find all replies to a comment (not deleted)
    List<Comment> findByParentComment_CommentIdAndIsDeletedFalseOrderByCreatedDateAsc(Long parentCommentId);
    
    // Find all comments by user
    List<Comment> findByUser_UserIdAndIsDeletedFalseOrderByCreatedDateDesc(Long userId);
    
    // Count comments for a problem (not deleted)
    Long countByProblem_ProblemIdAndIsDeletedFalse(Long problemId);
}
