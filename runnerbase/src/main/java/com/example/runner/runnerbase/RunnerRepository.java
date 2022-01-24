package com.example.runner.runnerbase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RunnerRepository extends JpaRepository<Runner, Integer> {
    Boolean existsByEmail(String email);
}
