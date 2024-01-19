package campingplatz;

import org.salespointframework.EnableSalespoint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Main application class for the campingplatz management system.
 * This class bootstraps the Spring Boot application and configures the security
 * settings.
 */
@EnableSalespoint
@SpringBootApplication(scanBasePackages = "campingplatz")
public class CampingPlatz {

    /**
     * The main application method
     * 
     * @param args application arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(CampingPlatz.class, args);
    }

    /**
     * Configures the security settings for the application.
     */
    @Configuration
    static class WebSecurityConfiguration {

        /**
         * This method sets up the security settings for the application.
         *
         * @param http the HttpSecurity object to configure
         * @return the configured SecurityFilterChain bean
         * @throws Exception if an error occurs during configuration
         */
        @Bean
        SecurityFilterChain CampingPlatzSecurity(HttpSecurity http) throws Exception {

            http.csrf(csrf -> csrf.disable());
            http.formLogin(login -> login.loginProcessingUrl("/login").loginPage("/default/login")
                    .failureUrl("/default/login?error=true"));
            http.logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/"));

            return http.build();
        }
    }
}