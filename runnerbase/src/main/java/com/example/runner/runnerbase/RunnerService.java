package com.example.runner.runnerbase;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RunnerService {

    private final RunnerRepository runnerRepository;

    public RunnerService(RunnerRepository runnerRepository) {
        this.runnerRepository = runnerRepository;
    }

    public RunnerResponseDTO addRunner(RunnerRequestDTO runnerRequestDTO) {
        return RunnerResponseDTO.from(
                runnerRepository.save(
                        Runner.builder()
                                .age(runnerRequestDTO.getAge())
                                .city(runnerRequestDTO.getCity())
                                .email(runnerRequestDTO.getEmail())
                                .paid(false)
                                .surname(runnerRequestDTO.getSurname())
                                .name(runnerRequestDTO.getName())
                                .price(100.0)
                                .build()));
    }

    public List<RunnerResponseDTO> getRunners() {
        return runnerRepository.findAll().stream()
                .map(RunnerResponseDTO::from)
                .collect(Collectors.toList());
    }

    public List<RunnerResponseDTO> getRunnersForEmail(String email) {
        return runnerRepository.findAllByEmail(email)
                .stream()
                .map(RunnerResponseDTO::from)
                .collect(Collectors.toList());
    }


    public RunnerResponseDTO markAsPaid(Integer id) throws AlreadyPaidException {
        Runner runner = runnerRepository.getById(id);
        if(runner.getPaid()){
            throw new AlreadyPaidException();
        }
        runner.setPaid(true);
        return RunnerResponseDTO.from(runner);
    }
}
