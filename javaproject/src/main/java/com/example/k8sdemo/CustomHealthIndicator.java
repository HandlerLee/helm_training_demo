
package com.example.k8sdemo;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class CustomHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        // Add your custom health check logic here
        boolean isHealthy = checkIfApplicationIsHealthy();
        
        if (isHealthy) {
            return Health.up().withDetail("message", "Application is healthy").build();
        } else {
            return Health.down().withDetail("message", "Application is not healthy").build();
        }
    }

    private boolean checkIfApplicationIsHealthy() {
        // Implement your health check logic here
        // For example, you could check database connectivity, external service availability, etc.
        return true; // Returning true for this example
    }
}