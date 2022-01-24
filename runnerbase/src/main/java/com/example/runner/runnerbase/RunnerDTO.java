package com.example.runner.runnerbase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RunnerDTO {
    private String name;
    private String surname;
    private String email;
    private int age;
    private String city;
}
