package com.example.runner.runnerbase.service;

import com.example.runner.runnerbase.AlreadyPaidException;
import com.example.runner.runnerbase.enitity.Runner;
import com.example.runner.runnerbase.repository.RunnerRepository;
import com.example.runner.runnerbase.dto.RunnerRequestDTO;
import com.example.runner.runnerbase.dto.RunnerResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RunnerService {

    private final RunnerRepository runnerRepository;
    private final PayPalService payPalService;

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

    public Optional<RunnerResponseDTO> getRunnersForEmail(String email) {
        return runnerRepository.findFirstByEmail(email)
                .map(RunnerResponseDTO::from);
    }


    public RunnerResponseDTO markAsPaid(Integer id) throws AlreadyPaidException {
        Runner runner = runnerRepository.getById(id);
        if (runner.getPaid()) {
            throw new AlreadyPaidException();
        }
        runner.setPaid(true);
        return RunnerResponseDTO.from(runner);
    }

    public Map<String, Object> createPayment(Integer id) throws AlreadyPaidException {
        Runner runner = runnerRepository.getById(id);
        if (runner.getPaid()) {
            throw new AlreadyPaidException();
        }
        return payPalService.createPayment(String.valueOf(runner.getPrice()));
    }
}
