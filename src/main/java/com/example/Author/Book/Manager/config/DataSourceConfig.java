package com.example.Author.Book.Manager.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

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
        
        if (databaseUrl != null && !databaseUrl.isEmpty()) {
            // Handle both postgresql:// and postgres:// formats
            if (databaseUrl.startsWith("postgresql://") || databaseUrl.startsWith("postgres://")) {
                try {
                    log.info("Detected DATABASE_URL environment variable");
                    
                    // Parse Railway DATABASE_URL format: postgresql://user:password@host:port/database
                    URI dbUri = new URI(databaseUrl);
                    
                    // Extract components
                    String host = dbUri.getHost();
                    int port = dbUri.getPort();
                    String path = dbUri.getPath();
                    if (path != null && path.startsWith("/")) {
                        path = path.substring(1); // Remove leading slash
                    }
                    
                    // Build JDBC URL
                    String dbUrl = "jdbc:postgresql://" + host + ':' + port + '/' + path;
                    
                    // Handle SSL mode for Railway
                    String query = dbUri.getQuery();
                    if (query != null && query.contains("sslmode")) {
                        dbUrl += "?" + query;
                    } else {
                        // Railway databases typically require SSL
                        dbUrl += "?sslmode=require";
                    }
                    
                    // Parse user info (format: username:password)
                    // Handle URL-encoded passwords
                    String userInfo = dbUri.getUserInfo();
                    if (userInfo == null || userInfo.isEmpty()) {
                        throw new RuntimeException("DATABASE_URL missing user credentials");
                    }
                    
                    String[] userInfoParts = userInfo.split(":", 2);
                    String dbUsername = userInfoParts[0];
                    String dbPassword = userInfoParts.length > 1 ? userInfoParts[1] : "";
                    
                    // Decode URL-encoded password if needed
                    try {
                        dbPassword = URLDecoder.decode(dbPassword, StandardCharsets.UTF_8.name());
                    } catch (Exception e) {
                        // If decoding fails, use original password
                        log.warn("Could not decode password, using as-is");
                    }
                    
                    log.info("Connecting to Railway database at: {}:{}", host, port);
                    log.info("Database name: {}", path);
                    log.info("Username: {}", dbUsername);
                    
                    // Use HikariCP for better connection management
                    HikariConfig config = new HikariConfig();
                    config.setJdbcUrl(dbUrl);
                    config.setUsername(dbUsername);
                    config.setPassword(dbPassword);
                    config.setDriverClassName("org.postgresql.Driver");
                    config.setMaximumPoolSize(10);
                    config.setMinimumIdle(5);
                    config.setConnectionTimeout(30000);
                    config.setIdleTimeout(600000);
                    config.setMaxLifetime(1800000);
                    config.setLeakDetectionThreshold(60000);
                    
                    return new HikariDataSource(config);
                } catch (URISyntaxException e) {
                    log.error("Invalid DATABASE_URL format: {}", databaseUrl, e);
                    throw new RuntimeException("Invalid DATABASE_URL format: " + databaseUrl, e);
                } catch (Exception e) {
                    log.error("Error parsing DATABASE_URL: {}", databaseUrl, e);
                    throw new RuntimeException("Error parsing DATABASE_URL: " + e.getMessage(), e);
                }
            } else {
                log.warn("DATABASE_URL found but doesn't start with postgresql:// or postgres://. Using standard configuration.");
            }
        }
        
        // Fall back to standard Spring Boot configuration (for local development)
        log.info("Using standard Spring Boot datasource configuration");
        
        if (datasourceUrl == null || datasourceUrl.isEmpty()) {
            log.error("Database URL is not configured!");
            throw new IllegalStateException("Database URL is not configured. Please set SPRING_DATASOURCE_URL or DATABASE_URL environment variable.");
        }
        
        log.info("Database URL: {}", datasourceUrl.replaceAll("password=[^&]*", "password=***"));
        log.info("Username: {}", username.isEmpty() ? "Not set" : username);
        
        // Use HikariCP for better connection management
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(datasourceUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName("org.postgresql.Driver");
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(5);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);
        config.setLeakDetectionThreshold(60000);
        
        return new HikariDataSource(config);
    }
}
