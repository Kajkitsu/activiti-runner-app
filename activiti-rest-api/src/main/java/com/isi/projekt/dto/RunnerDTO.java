package com.isi.projekt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class RunnerDTO {
    private static final ObjectMapper objMapper = new ObjectMapper();

    private Integer id;
    private String name;
    private String surname;
    private String email;
    private String paymentLink;
    private int age;
    private String city;
    private Boolean paid;
    private Double price;
    private WeatherDTO weatherDTO;

    public static RunnerDTO from(RunnerResponseDTO it) {
        return RunnerDTO.builder()
                .id(it.getId())
                .age(it.getAge())
                .email(it.getEmail())
                .city(it.getCity())
                .price(it.getPrice())
                .name(it.getName())
                .surname(it.getSurname())
                .paid(it.getPaid())
                .build();
    }

    public static RunnerDTO from(String value) {
        try {
            return objMapper.readValue(value, RunnerDTO.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String toJson() {
        try {
            return objMapper.writeValueAsString(this);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}
