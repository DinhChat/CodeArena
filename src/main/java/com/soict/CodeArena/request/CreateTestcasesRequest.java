package com.soict.CodeArena.request;

import lombok.Data;

import java.util.List;

@Data
public class CreateTestcasesRequest {
    private List<TestcaseRequest> testcases;
}
