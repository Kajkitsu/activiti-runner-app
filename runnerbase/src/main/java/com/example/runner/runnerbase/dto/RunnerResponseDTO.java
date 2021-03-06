package com.example.runner.runnerbase.dto;

import com.example.runner.runnerbase.enitity.Runner;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RunnerResponseDTO {
    private Integer id;
    private String name;
    private String surname;
    private String email;
    private int age;
    private String city;
    private Boolean paid;
    private Double price;

    public static RunnerResponseDTO from(Runner runner) {
        return new RunnerResponseDTO(
                runner.getId(),
                runner.getName(),
                runner.getSurname(),
                runner.getEmail(),
                runner.getAge(),
                runner.getCity(),
                runner.getPaid(),
                runner.getPrice());
    }
}
