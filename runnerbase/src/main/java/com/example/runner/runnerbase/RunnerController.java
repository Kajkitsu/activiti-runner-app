package com.example.runner.runnerbase;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RunnerController {

    private final RunnerService runnerService;

    public RunnerController(RunnerService runnerService) {
        this.runnerService = runnerService;
    }

    @PostMapping("/runners")
    public ResponseEntity addRunner(@RequestBody RunnerDTO runnerDTO){
        runnerService.addRunner(runnerDTO);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("/runners")
    public ResponseEntity<List<RunnerDTO>> listAllRunners(){
        return new ResponseEntity<>(runnerService.getAllRunners(), HttpStatus.OK);
    }
}
