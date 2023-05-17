package uz.attendance_system.attendance_system.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import uz.attendance_system.attendance_system.security.JwtConfig;
import uz.attendance_system.attendance_system.security.JwtProvider;


@Configuration
@EnableWebSecurity
public class SecurityConf extends WebSecurityConfigurerAdapter {

    // private final UserDetailsService userDetailsService;

    // private

    // public SecurityConf(@Lazy UserDetailsService userDetailsService) {
    //     this.userDetailsService = userDetailsService;
    // }


    // // @Override
    // // protected void configure(AuthenticationManagerBuilder auth) throws Exception{
    // //     auth 
    // //         // .inMemoryAuthentication()
    // //         // .withUser("admin").password(passwordEncoder().encode("1234")).roles("Admin")
    // //         // .and()
    // //         // .withUser("user").password(passwordEncoder().encode("12122134")).roles("User");
    // //         .userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    // // }

    private final JwtProvider jwtProvider;


    public SecurityConf(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf()
            .disable()
            .headers()
            .frameOptions()
            .disable()
            .and()
            .authorizeRequests() //anyrole 2 ta va undan ortiq qo'shish 
            .antMatchers("/auth/posts").hasRole("ADMIN")
            .antMatchers("/auth/teacher/**").hasRole("USER")
            .antMatchers("/auth/register").permitAll()
            .antMatchers("/auth/authecated").permitAll()
            .anyRequest().authenticated() //permitterole barchasiga dostup berish
            .and()
            .httpBasic()
            .and()
            .apply(securityJwt());
    }

    private JwtConfig securityJwt(){
        return new JwtConfig(jwtProvider);
    }

  
}
