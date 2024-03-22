package com.capstone.safeGuard.service;

import com.capstone.safeGuard.domain.Test;
import com.capstone.safeGuard.dto.TestDTO;
import com.capstone.safeGuard.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestService {
    private final TestRepository testRepository;

    public void dbConnectionTest(TestDTO testDTO) {
        Test domain = Test.toDomain(testDTO);

        testRepository.save(domain);
    }

    public String find(int i) {
        Test testDomain = testRepository.findById(i).get();
        return testDomain.getName();
    }
}
