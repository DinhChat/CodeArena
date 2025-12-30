package com.soict.CodeArena.repository;

import com.soict.CodeArena.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByProblem_ProblemIdAndIsDeletedFalseAndParentCommentIsNull(Long problemId, Pageable pageable);

    Page<Comment> findByParentComment_CommentIdAndIsDeletedFalse(Long parentCommentId, Pageable pageable);

    Page<Comment> findByUser_UserIdAndIsDeletedFalse(Long userId, Pageable pageable);

    Long countByProblem_ProblemIdAndIsDeletedFalse(Long problemId);
}
