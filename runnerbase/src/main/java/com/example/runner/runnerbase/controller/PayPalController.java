package com.example.runner.runnerbase.controller;

import com.example.runner.runnerbase.AlreadyPaidException;
import com.example.runner.runnerbase.service.PayPalService;
import com.example.runner.runnerbase.service.RunnerService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value = "/paypal")
@AllArgsConstructor
public class PayPalController {
    private final PayPalService payPalClient;
    private final RunnerService runnerService;


    @PostMapping(value = "/make_payment/{runnerId}")
    public ResponseEntity<?> makePayment(@PathVariable Integer runnerId) {
        try {
            return ResponseEntity.ok(runnerService.createPayment(runnerId));
        } catch (AlreadyPaidException e) {
            return ResponseEntity.badRequest().body("Already paid");
        }
    }

    @PostMapping(value = "/complete_payment/{paymentId}/{payerId}")
    public Map<String, Object> completePayment(@PathVariable String paymentId, @PathVariable String payerId) {
        return payPalClient.completePayment(paymentId, payerId);
    }
}