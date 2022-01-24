package com.example.runner.runnerbase.repository;

import com.example.runner.runnerbase.enitity.Runner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RunnerRepository extends JpaRepository<Runner, Integer> {
    Optional<Runner> findFirstByEmail(String email);
}
