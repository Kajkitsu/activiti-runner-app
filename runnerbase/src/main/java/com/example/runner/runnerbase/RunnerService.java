package com.example.runner.runnerbase;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class RunnerService {

    private final RunnerRepository runnerRepository;

    public RunnerService(RunnerRepository runnerRepository) {
        this.runnerRepository = runnerRepository;
    }

    public void addRunner(RunnerDTO runnerDTO){
            Runner runner = new Runner();
            runner.setName(runnerDTO.getName());
            runner.setSurname(runnerDTO.getSurname());
            runner.setEmail(runnerDTO.getEmail());
            runner.setCity(runnerDTO.getCity());
            runner.setAge(runnerDTO.getAge());
            runnerRepository.save(runner);
        }

    public List<RunnerDTO> getAllRunners(){
        return StreamSupport.stream(runnerRepository.findAll().stream().spliterator(), false)
                .map(runner -> new RunnerDTO(runner.getName(), runner.getSurname(), runner.getEmail(),runner.getAge(), runner.getCity()))
                .collect(Collectors.toList());
    }
}
