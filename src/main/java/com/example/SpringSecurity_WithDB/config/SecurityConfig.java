package com.example.SpringSecurity_WithDB.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {


    @Bean
    /**
     * Ye method application ka SecurityFilterChain define karta hai.
     *
     * SecurityFilterChain ka matlab hai security filters ka ek chain,
     * jisse har incoming HTTP request controller tak pahunchne se
     * pehle pass hoti hai.
     *
     * Spring Security application startup ke time is method ko call karta hai
     * aur yahan likhe rules apply karta hai.
     */
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {

        /**
         * authorizeHttpRequests():
         * -----------------------
         * Is method ka use authorization rules define karne ke liye hota hai,
         * yaani kaunsa user kaunsa URL access kar sakta hai.
         *
         * Har request in rules ke according check hoti hai.
         */
        httpSecurity.authorizeHttpRequests(req ->

                        /**
                         * requestMatchers("/admin"):
                         * --------------------------
                         * Ye rule "/admin" URL ke liye apply hota hai.
                         *
                         * hasRole("ADMIN"):
                         * Sirf wahi users is URL ko access kar sakte hain
                         * jinke paas ADMIN role hai.
                         *
                         * Important:
                         * Spring Security automatically "ROLE_" prefix add karta hai,
                         * isliye hasRole("ADMIN") ka matlab ROLE_ADMIN hota hai.
                         */
                        req.requestMatchers("/admin")
                                .hasRole("ADMIN")

                                /**
                                 * requestMatchers("/user"):
                                 * -------------------------
                                 * Ye rule "/user" URL ke liye apply hota hai.
                                 *
                                 * hasAnyRole("ADMIN", "USER"):
                                 * Agar user ke paas ADMIN ya USER role hai,
                                 * to access mil jayega.
                                 */
                                .requestMatchers("/user")
                                .hasAnyRole("ADMIN", "USER")

                                /**
                                 * requestMatchers("/"):
                                 * ---------------------
                                 * Ye rule home page ("/") ke liye hai.
                                 *
                                 * permitAll():
                                 * Koi bhi user access kar sakta hai,
                                 * chahe login ho ya na ho.
                                 */
                                .requestMatchers("/")
                                .permitAll()

                                /**
                                 * anyRequest():
                                 * -------------
                                 * Baaki sab URLs jo upar define nahi hain,
                                 * unke liye ye rule apply hota hai.
                                 *
                                 * authenticated():
                                 * User ka logged-in hona compulsory hai.
                                 */
                                .anyRequest()
                                .authenticated()
                )

                /**
                 * formLogin(Customizer.withDefaults()):
                 * -----------------------------------
                 * Form-based login enable karta hai.
                 *
                 * Spring Security apna default login page
                 * automatically provide karta hai.
                 */
                .formLogin(Customizer.withDefaults());

        /**
         * build():
         * --------
         * Saari security configuration ko final karta hai
         * aur SecurityFilterChain return karta hai.
         */
        return httpSecurity.build();
    }




    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }

}
