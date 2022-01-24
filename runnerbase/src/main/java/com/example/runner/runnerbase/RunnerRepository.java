package com.example.runner.runnerbase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RunnerRepository extends JpaRepository<Runner, Integer> {
    List<Runner> findAllByEmail(String email);
}
