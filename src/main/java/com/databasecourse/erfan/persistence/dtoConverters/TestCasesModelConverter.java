package com.databasecourse.erfan.persistence.dtoConverters;

import com.databasecourse.erfan.persistence.model.Testcase;
import com.databasecourse.erfan.web.dto.TestCaseDto;
import org.springframework.stereotype.Service;

@Service
public class TestCasesModelConverter {


    public TestCaseDto convert(Testcase testCase){
        TestCaseDto testCaseDto = new TestCaseDto();
        testCaseDto.setOutput(testCase.getOutput());
        testCaseDto.setQuery(testCase.getQuery());

        return testCaseDto;
    }


    public Testcase convert(TestCaseDto testCaseDto){
        Testcase testCase = new Testcase();
        testCase.setQuery(testCaseDto.getQuery());
        testCase.setOutput(testCaseDto.getOutput());

        return testCase;
    }


}
