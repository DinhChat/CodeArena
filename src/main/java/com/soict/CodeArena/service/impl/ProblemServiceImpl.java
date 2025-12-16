package com.soict.CodeArena.service.impl;

import com.soict.CodeArena.model.DIFFICULTY_LEVEL;
import com.soict.CodeArena.model.Problem;
import com.soict.CodeArena.model.User;
import com.soict.CodeArena.repository.ProblemRepository;
import com.soict.CodeArena.request.ProblemRequest;
import com.soict.CodeArena.response.ProblemResponse;
import com.soict.CodeArena.service.ProblemService;
import com.soict.CodeArena.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProblemServiceImpl implements ProblemService {

    private final ProblemRepository problemRepository;
    private final UserService userService;

    public ProblemServiceImpl(ProblemRepository problemRepository,  UserService userService) {
        this.problemRepository = problemRepository;
        this.userService = userService;
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ProblemResponse createProblem(ProblemRequest request, String username) throws Exception {
        User user = userService.findByUsername(username);

        if (problemRepository.findByProblemCode(request.getProblemCode()).isPresent()) {
            throw new IllegalArgumentException("Problem code already exists");
        }

        Problem problem = new Problem();
        problem.setProblemCode(request.getProblemCode());
        problem.setTitle(request.getTitle());
        problem.setDescription(request.getDescription());
        problem.setInputFormat(request.getInputFormat());
        problem.setOutputFormat(request.getOutputFormat());
        problem.setConstraints(request.getConstraints());
        problem.setDifficultyLevel(request.getDifficultyLevel());
        problem.setTimeLimit(request.getTimeLimit());
        problem.setMemoryLimit(request.getMemoryLimit());
        problem.setCreatedBy(user);
        problem.setCreatedDate(LocalDateTime.now());
        problem.setUpdatedDate(LocalDateTime.now());

        Problem savedProblem = problemRepository.save(problem);
        return convertToResponse(savedProblem);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ProblemResponse updateProblem(Long problemId, ProblemRequest request, String username) throws Exception {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new Exception("Problem not found"));

        if (problem.getCreatedBy() != userService.findByUsername(username)) {
            throw new IllegalArgumentException("You can't update this problem");
        }

        problem.setTitle(request.getTitle());
        problem.setDescription(request.getDescription());
        problem.setInputFormat(request.getInputFormat());
        problem.setOutputFormat(request.getOutputFormat());
        problem.setConstraints(request.getConstraints());
        problem.setDifficultyLevel(request.getDifficultyLevel());
        problem.setTimeLimit(request.getTimeLimit());
        problem.setMemoryLimit(request.getMemoryLimit());
        problem.setUpdatedDate(LocalDateTime.now());

        Problem updatedProblem = problemRepository.save(problem);
        return convertToResponse(updatedProblem);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ProblemResponse activeProblem(Long problemId, String username) throws Exception {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new Exception("Problem not found"));

        if (problem.getCreatedBy() != userService.findByUsername(username)) {
            throw new IllegalArgumentException("You can't active this problem");
        }

        problem.setActive(true);
        Problem updatedProblem = problemRepository.save(problem);
        return convertToResponse(updatedProblem);
    }


    @Override
    public ProblemResponse getProblemById(Long problemId) throws Exception {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new Exception("Problem not found"));
        return convertToResponse(problem);
    }

    @Override
    public ProblemResponse getProblemByCode(String problemCode) throws Exception {
        Problem problem = problemRepository.findByProblemCode(problemCode)
                .orElseThrow(() -> new Exception("Problem not found"));
        return convertToResponse(problem);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public List<ProblemResponse> getMyProblems(String  username) throws Exception {
        User user  = userService.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("Not found user");
        }
        return problemRepository.findAllByCreatedBy(user).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

//    @Override
//    public List<ProblemResponse> getProblemsByDifficulty(DIFFICULTY_LEVEL difficulty) {
//        return problemRepository.findByDifficultyLevel(difficulty).stream()
//                .map(this::convertToResponse)
//                .collect(Collectors.toList());
//    }

    @Override
    public List<ProblemResponse> getActiveProblems() {
        return problemRepository.findByIsActive(true).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public void deleteProblem(Long problemId, String username) throws Exception {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new Exception("Problem not found"));
        if (problem.getCreatedBy() != userService.findByUsername(username)) {
            throw new IllegalArgumentException("You can't delete this problem");
        } else {
            problemRepository.deleteById(problemId);
        }
    }

    private ProblemResponse convertToResponse(Problem problem) {
        ProblemResponse response = new ProblemResponse();
        response.setProblemId(problem.getProblemId());
        response.setProblemCode(problem.getProblemCode());
        response.setTitle(problem.getTitle());
        response.setDescription(problem.getDescription());
        response.setInputFormat(problem.getInputFormat());
        response.setOutputFormat(problem.getOutputFormat());
        response.setConstraints(problem.getConstraints());
        response.setDifficultyLevel(problem.getDifficultyLevel());
        response.setTimeLimit(problem.getTimeLimit());
        response.setMemoryLimit(problem.getMemoryLimit());
        response.setCreatedBy(problem.getCreatedBy().getUsername());
        response.setCreatedDate(problem.getCreatedDate());
        response.setUpdatedDate(problem.getUpdatedDate());
        response.setActive(problem.isActive());
        return response;
    }
}
