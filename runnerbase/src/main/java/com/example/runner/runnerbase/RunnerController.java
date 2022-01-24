package com.example.runner.runnerbase;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    public ResponseEntity<RunnerResponseDTO> addRunner(@RequestBody RunnerRequestDTO runnerRequestDTO) {
        return ResponseEntity.ok(runnerService.addRunner(runnerRequestDTO));
    }

    @GetMapping("/runners")
    public ResponseEntity<List<RunnerResponseDTO>> listRunners() {
        return new ResponseEntity<>(runnerService.getRunners(), HttpStatus.OK);
    }

    @GetMapping("/runners/{email}")
    public ResponseEntity<List<RunnerResponseDTO>> listRunnersByEmail(@PathVariable String email) {
        return new ResponseEntity<>(runnerService.getRunnersForEmail(email), HttpStatus.OK);
    }

    @PutMapping("/runners/{id}")
    public ResponseEntity<?> markAsPaid(@PathVariable Integer id){
        try {
            return ResponseEntity.ok(runnerService.markAsPaid(id));
        } catch (AlreadyPaidException e) {
            return ResponseEntity.badRequest().body("Already paid");
        }
    }
}
