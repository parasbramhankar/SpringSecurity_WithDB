package com.example.SpringSecurity_WithDB.Service;


import com.example.SpringSecurity_WithDB.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.example.SpringSecurity_WithDB.entity.User;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepo userRepo;

    @Override
    /**
     * Ye method Spring Security ka part hai.
     * Jab bhi koi user login karta hai,
     * Spring Security internally is method ko call karta hai.
     *
     * Iska kaam:
     * 1. Username ke basis par user ko database se nikalna
     * 2. User ko Spring Security ke UserDetails object mein convert karna
     */
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        /**
         * userRepo.findByUserName(username):
         * ---------------------------------
         * Database se username ke basis par user search karta hai.
         *
         * Agar user mil gaya → User object return karega
         * Agar user nahi mila → UsernameNotFoundException throw karega
         *
         * orElseThrow():
         * Agar Optional empty hai, to exception throw hota hai
         */
        User user = userRepo.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));

        /**
         * Yahan hum Spring Security ka User object create kar rahe hain.
         *
         * Ye User object Spring Security ko batata hai:
         * - username kya hai
         * - password kya hai
         * - account active hai ya nahi
         * - user ke roles / authorities kya hain
         */
        return new org.springframework.security.core.userdetails.User(

                /**
                 * Username:
                 * Database se aaya hua username
                 */
                user.getUsername(),

                /**
                 * Password:
                 * Database se aaya hua encrypted password
                 */
                user.getPassword(),

                /**
                 * Ye 4 boolean flags account ki state batate hain:
                 *
                 * enabled → account enabled hai ya nahi
                 * accountNonExpired → account expire to nahi hua
                 * credentialsNonExpired → password expire to nahi hua
                 * accountNonLocked → account locked to nahi hai
                 *
                 * Yahan sab true hai, matlab:
                 * Account fully active aur valid hai
                 */
                true,
                true,
                true,
                true,

                /**
                 * Authorities / Roles:
                 * --------------------
                 * User ke roles ko Spring Security ke format
                 * (SimpleGrantedAuthority) mein convert kar rahe hain
                 *
                 * Example:
                 * ROLE_ADMIN → new SimpleGrantedAuthority("ROLE_ADMIN")
                 * ROLE_USER  → new SimpleGrantedAuthority("ROLE_USER")
                 *
                 * stream():
                 * User ke roles list par loop chala raha hai
                 *
                 * map():
                 * Har role ko SimpleGrantedAuthority mein convert kar raha hai
                 *
                 * collect(Collectors.toSet()):
                 * Sab authorities ko Set ke form mein collect kar raha hai
                 */
                user.getRoles()
                        .stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                        .collect(Collectors.toSet())
        );
    }
}
