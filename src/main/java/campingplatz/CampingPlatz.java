package campingplatz;

import org.salespointframework.EnableSalespoint;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.web.SecurityFilterChain;

/**
 * The main application class.
 */
@EnableSalespoint
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
        SecurityFilterChain videoShopSecurity(HttpSecurity http) throws Exception {

            return http
                    .headers(headers -> headers.frameOptions(FrameOptionsConfig::sameOrigin))
                    .csrf(csrf -> csrf.disable())
                    .formLogin(login -> login.loginProcessingUrl("/login"))
                    .logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/"))
                    .build();
        }
    }
}
