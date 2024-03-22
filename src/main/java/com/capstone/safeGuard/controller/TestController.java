package com.capstone.safeGuard.controller;

import com.capstone.safeGuard.dto.TestDTO;
import com.capstone.safeGuard.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {
    private final TestService testService;

    @GetMapping("/")
    public String dbConnectionTest(){
        TestDTO dto = new TestDTO("홍길동", 20);
        testService.dbConnectionTest(dto);

        return "testing";
    }

}
