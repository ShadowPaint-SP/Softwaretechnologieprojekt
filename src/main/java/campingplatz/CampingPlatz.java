package campingplatz;

import org.salespointframework.EnableSalespoint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * The main application class.
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

    @Configuration
    static class WebSecurityConfiguration {

        @Bean
        SecurityFilterChain CampingPlatzSecurity(HttpSecurity http) throws Exception {

            http.csrf(csrf -> csrf.disable());
            http.formLogin(login -> login.loginProcessingUrl("/conf/login"));
            http.logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/"));
            return http.build();
        }
    }
}