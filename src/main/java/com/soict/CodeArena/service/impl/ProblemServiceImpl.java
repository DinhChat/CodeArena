package com.soict.CodeArena.service.impl;

import com.soict.CodeArena.model.Problem;
import com.soict.CodeArena.model.User;
import com.soict.CodeArena.model.UserProblemStat;
import com.soict.CodeArena.repository.ProblemRepository;
import com.soict.CodeArena.repository.UserProblemStatRepository;
import com.soict.CodeArena.request.ProblemRequest;
import com.soict.CodeArena.response.DefaultProblemResponse;
import com.soict.CodeArena.response.PagedResponse;
import com.soict.CodeArena.response.ProblemItemResponse;
import com.soict.CodeArena.response.ProblemDetailResponse;
import com.soict.CodeArena.service.ProblemService;
import com.soict.CodeArena.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProblemServiceImpl implements ProblemService {

    private final ProblemRepository problemRepository;
    private final UserService userService;
    private final UserProblemStatRepository userProblemStatRepository;

    public ProblemServiceImpl(ProblemRepository problemRepository, UserService userService,
            UserProblemStatRepository userProblemStatRepository) {
        this.problemRepository = problemRepository;
        this.userService = userService;
        this.userProblemStatRepository = userProblemStatRepository;
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ProblemDetailResponse createProblem(ProblemRequest request, String username) throws Exception {
        User user = userService.findByUsername(username);

        if (problemRepository.findByProblemCode(request.getProblemCode()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Problem Code already exists");
        }

        Problem problem = new Problem();
        problem.setProblemCode(request.getProblemCode());
        problem.setTitle(request.getTitle());
        problem.setDescription(request.getDescription());
        problem.setInputFormat(request.getInputFormat());
        problem.setOutputFormat(request.getOutputFormat());
        problem.setSampleInput(request.getSampleInput());
        problem.setSampleOutput(request.getSampleOutput());
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
    public ProblemDetailResponse updateProblem(Long problemId, ProblemRequest request, String username)
            throws Exception {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Problem Not Found"));

        if (!problem.getCreatedBy().getUserId()
                .equals(userService.findByUsername(username).getUserId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can't activate this problem");
        }

        problem.setTitle(request.getTitle());
        problem.setDescription(request.getDescription());
        problem.setInputFormat(request.getInputFormat());
        problem.setOutputFormat(request.getOutputFormat());
        problem.setSampleInput(request.getSampleInput());
        problem.setSampleOutput(request.getSampleOutput());
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
    public ProblemDetailResponse activeProblem(Long problemId, String username) throws Exception {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Problem Not Found"));

        if (!problem.getCreatedBy().getUserId()
                .equals(userService.findByUsername(username).getUserId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can't activate this problem");
        }

        problem.setActive(true);
        Problem updatedProblem = problemRepository.save(problem);
        return convertToResponse(updatedProblem);
    }

    @Override
    public ProblemDetailResponse getProblemById(Long problemId) {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Problem Not Found"));
        return convertToResponse(problem);
    }

    @Override
    public ProblemDetailResponse getProblemByCode(String problemCode) {
        Problem problem = problemRepository.findByProblemCode(problemCode)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Problem Not Found"));
        return convertToResponse(problem);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public PagedResponse<DefaultProblemResponse> getMyProblems(String username, Integer page, Integer pageSize,
            Integer offset) throws Exception {
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new IllegalStateException("Authenticated user not found");
        }

        int actualPageSize = (pageSize != null && pageSize > 0) ? pageSize : 10;
        int actualPage;

        if (offset != null && offset >= 0) {
            actualPage = offset / actualPageSize;
        } else if (page != null && page >= 0) {
            actualPage = page;
        } else {
            actualPage = 0;
        }

        Pageable pageable = PageRequest.of(actualPage, actualPageSize, Sort.by("createdDate").descending());
        Page<Problem> problemPage = problemRepository.findAllByCreatedBy(user, pageable);

        List<DefaultProblemResponse> responses = problemPage.getContent().stream()
                .map(this::convertToDefaultResponse)
                .collect(Collectors.toList());

        return new PagedResponse<>(
                responses,
                problemPage.getNumber(),
                problemPage.getSize(),
                problemPage.getTotalElements(),
                problemPage.getTotalPages(),
                problemPage.isLast(),
                problemPage.isFirst());
    }

    @Override
    public PagedResponse<ProblemItemResponse> getActiveProblems(String username, Integer page, Integer pageSize,
            Integer offset) throws Exception {
        User user = userService.findByUsername(username);

        int actualPageSize = (pageSize != null && pageSize > 0) ? pageSize : 10;
        int actualPage;

        if (offset != null && offset >= 0) {
            actualPage = offset / actualPageSize;
        } else if (page != null && page >= 0) {
            actualPage = page;
        } else {
            actualPage = 0;
        }

        Pageable pageable = PageRequest.of(actualPage, actualPageSize, Sort.by("createdDate").descending());
        Page<Problem> problemPage = problemRepository.findActiveProblemsPageable(pageable);

        List<ProblemItemResponse> responses = problemPage.getContent().stream()
                .map(problem -> {
                    UserProblemStat stat = userProblemStatRepository
                            .findByUserAndProblem(user, problem)
                            .orElse(null);
                    return convertToItemResponse(problem, stat);
                })
                .collect(Collectors.toList());

        return new PagedResponse<>(
                responses,
                problemPage.getNumber(),
                problemPage.getSize(),
                problemPage.getTotalElements(),
                problemPage.getTotalPages(),
                problemPage.isLast(),
                problemPage.isFirst());
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public void deleteProblem(Long problemId, String username) throws Exception {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Problem Not Found"));

        if (!problem.getCreatedBy().getUserId()
                .equals(userService.findByUsername(username).getUserId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can't delete this problem");
        } else {
            problemRepository.deleteById(problemId);
        }
    }

    private ProblemDetailResponse convertToResponse(Problem problem) {
        ProblemDetailResponse response = new ProblemDetailResponse();
        response.setProblemId(problem.getProblemId());
        response.setProblemCode(problem.getProblemCode());
        response.setTitle(problem.getTitle());
        response.setDescription(problem.getDescription());
        response.setInputFormat(problem.getInputFormat());
        response.setOutputFormat(problem.getOutputFormat());
        response.setSampleInput(response.getSampleInput());
        response.setSampleOutput(response.getSampleOutput());
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

    private ProblemItemResponse convertToItemResponse(Problem problem, UserProblemStat stat) {
        ProblemItemResponse res = new ProblemItemResponse();
        res.setProblemId(problem.getProblemId());
        res.setProblemCode(problem.getProblemCode());
        res.setTitle(problem.getTitle());
        res.setDifficultyLevel(problem.getDifficultyLevel());

        if (stat != null) {
            res.setAttempted(true);
            res.setBestStatus(stat.getBestStatus());
            res.setBestScore(stat.getBestScore());
        } else {
            res.setAttempted(false);
            res.setBestStatus(null);
            res.setBestScore(null);
        }
        return res;
    }

    private DefaultProblemResponse convertToDefaultResponse(Problem problem) {
        DefaultProblemResponse res = new DefaultProblemResponse();
        res.setProblemId(problem.getProblemId());
        res.setTitle(problem.getTitle());
        res.setDifficultyLevel(problem.getDifficultyLevel());
        res.setActive(problem.isActive());
        return res;
    }
}
