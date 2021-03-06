package com.example.runner.runnerbase.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RunnerRequestDTO {
    private String name;
    private String surname;
    private String email;
    private int age;
    private String city;
}
