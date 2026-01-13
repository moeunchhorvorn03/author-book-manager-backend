package com.example.Author.Book.Manager.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@Configuration
public class DataSourceConfig {

    @Value("${spring.datasource.url:}")
    private String datasourceUrl;

    @Value("${spring.datasource.username:}")
    private String username;

    @Value("${spring.datasource.password:}")
    private String password;

    @Bean
    @Primary
    public DataSource dataSource() {
        // Check if DATABASE_URL environment variable is set (Railway format)
        String databaseUrl = System.getenv("DATABASE_URL");
        
        if (databaseUrl != null && !databaseUrl.isEmpty() && databaseUrl.startsWith("postgresql://")) {
            try {
                log.info("Using DATABASE_URL from environment variable (Railway format)");
                
                // Parse Railway DATABASE_URL format: postgresql://user:password@host:port/database
                URI dbUri = new URI(databaseUrl);
                
                String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
                
                // Handle SSL mode for Railway
                if (dbUri.getQuery() != null && dbUri.getQuery().contains("sslmode")) {
                    dbUrl += "?" + dbUri.getQuery();
                } else {
                    // Railway databases typically require SSL
                    dbUrl += "?sslmode=require";
                }
                
                // Parse user info (format: username:password)
                String[] userInfo = dbUri.getUserInfo().split(":");
                String dbUsername = userInfo[0];
                String dbPassword = userInfo.length > 1 ? userInfo[1] : "";
                
                log.info("Connecting to database at: {}:{}", dbUri.getHost(), dbUri.getPort());
                
                return DataSourceBuilder.create()
                        .driverClassName("org.postgresql.Driver")
                        .url(dbUrl)
                        .username(dbUsername)
                        .password(dbPassword)
                        .build();
            } catch (URISyntaxException e) {
                log.error("Invalid DATABASE_URL format: {}", databaseUrl, e);
                throw new RuntimeException("Invalid DATABASE_URL format: " + databaseUrl, e);
            } catch (Exception e) {
                log.error("Error parsing DATABASE_URL: {}", databaseUrl, e);
                throw new RuntimeException("Error parsing DATABASE_URL", e);
            }
        }
        
        // Fall back to standard Spring Boot configuration (for local development)
        log.info("Using standard Spring Boot datasource configuration");
        log.info("Database URL: {}", datasourceUrl.isEmpty() ? "Not set" : datasourceUrl.replaceAll("password=[^&]*", "password=***"));
        log.info("Username: {}", username.isEmpty() ? "Not set" : username);
        
        if (datasourceUrl == null || datasourceUrl.isEmpty()) {
            throw new IllegalStateException("Database URL is not configured. Please set SPRING_DATASOURCE_URL or DATABASE_URL environment variable.");
        }
        
        return DataSourceBuilder.create()
                .driverClassName("org.postgresql.Driver")
                .url(datasourceUrl)
                .username(username)
                .password(password)
                .build();
    }
}
