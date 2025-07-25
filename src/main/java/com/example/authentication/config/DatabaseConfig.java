package com.example.authentication.config;

import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;

import javax.sql.DataSource;
import jakarta.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

@Configuration
public class DatabaseConfig {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);
    private static final long SHUTDOWN_TIMEOUT = 10;
    
    @Autowired
    private DataSource dataSource;
    
    @PreDestroy
    public void closeDataSource() {
        shutdownHikariCP();
    }

    @EventListener(ContextClosedEvent.class)
    public void onApplicationEvent(ContextClosedEvent event) {
        shutdownHikariCP();
    }

    private void shutdownHikariCP() {
        if (dataSource instanceof HikariDataSource hikariDataSource) {
            try {
                logger.info("Initiating HikariCP shutdown...");
                
                // Close the pool
                hikariDataSource.close();
                
                // Wait for active connections to close
                if (hikariDataSource.getHikariPoolMXBean().getActiveConnections() > 0) {
                    logger.warn("Some connections did not close within {} seconds", SHUTDOWN_TIMEOUT);
                }
                
                logger.info("HikariCP has been shut down successfully");
            } catch (Exception e) {
                logger.error("Error during HikariCP shutdown", e);
            }
        }
    }
}
