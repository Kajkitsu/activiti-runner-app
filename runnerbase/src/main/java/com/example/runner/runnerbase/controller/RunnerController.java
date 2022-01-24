package com.example.runner.runnerbase.controller;

import com.example.runner.runnerbase.AlreadyPaidException;
import com.example.runner.runnerbase.dto.RunnerRequestDTO;
import com.example.runner.runnerbase.dto.RunnerResponseDTO;
import com.example.runner.runnerbase.service.RunnerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/runners")
public class RunnerController {

    private final RunnerService runnerService;

    public RunnerController(RunnerService runnerService) {
        this.runnerService = runnerService;
    }

    @PostMapping()
    public ResponseEntity<RunnerResponseDTO> addRunner(@RequestBody RunnerRequestDTO runnerRequestDTO) {
        return ResponseEntity.ok(runnerService.addRunner(runnerRequestDTO));
    }

    @GetMapping()
    public ResponseEntity<List<RunnerResponseDTO>> listRunners() {
        return new ResponseEntity<>(runnerService.getRunners(), HttpStatus.OK);
    }

    @GetMapping("{email}")
    public ResponseEntity<RunnerResponseDTO> getRunnersByEmail(@PathVariable String email) {
        Optional<RunnerResponseDTO> responseDTO = runnerService.getRunnersForEmail(email);
        if(responseDTO.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(responseDTO.get(), HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> markAsPaid(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(runnerService.markAsPaid(id));
        } catch (AlreadyPaidException e) {
            return ResponseEntity.badRequest().body("Already paid");
        }
    }
}
