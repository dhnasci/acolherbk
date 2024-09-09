package br.manaus.mysoft.acolherbk.config;

import br.manaus.mysoft.acolherbk.security.JWTAuthenticationFilter;
import br.manaus.mysoft.acolherbk.security.JWTAuthorizationFilter;
import br.manaus.mysoft.acolherbk.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    UserDetailsService userDetailsService;
    JWTUtil jwtUtil;

    @Autowired
    public SecurityConfig(UserDetailsService user, JWTUtil util) {
        this.jwtUtil = util;
        this.userDetailsService = user;
    }

    private final static String[] PUBLIC_MATCHERS = {
            "/home",
    };

    private static final String[] PUBLIC_MATCHERS_GET = {
            "/profissao/**",
            "/genero/**",
            "/especialidade/**",
            "/escolaridade/**",
            "/horario/**",
            "/horario",
            "/paciente/**",
            "/paciente/triagem/**",
            "/horariopaciente",
            "/horariopaciente/**",
            "/horariopsicologo/**",
            "/horariopsicologo",
            "/sessao/**"

    };

    private static final String[] PUBLIC_MATCHERS_POST = {
            "/triagem/**",
            "/triagem",
            "/psicologo/**",
            "/psicologo",
            "/agendamento/**",
            "/agendamento",
            "/paciente/**",
            "/paciente",
            "/horariopsicologo/**",
            "/horariopsicologo",
            "/horariopaciente/**",
            "/horariopaciente",
            "/sessao/**"
    };

    private static final String[] PUBLIC_MATCHERS_PUT = {
            "/triagem",
            "/psicologo",
            "/psicologo/**",
            "/agendamento/**",
            "/paciente",
            "/paciente/**",
    };

    public static final String[] PUBLIC_MATCHERS_DELETE = {
            "/paciente/**",
            "/horariopsicologo/**",
            "/psicologo/**",
            "/horariopaciente/**"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Configuring Security
        http.cors().and().csrf().disable();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests()
                .antMatchers(PUBLIC_MATCHERS).permitAll()
                .antMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET).authenticated()
                .antMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).authenticated()
                .antMatchers(HttpMethod.PUT, PUBLIC_MATCHERS_PUT).authenticated()
                .antMatchers(HttpMethod.DELETE, PUBLIC_MATCHERS_DELETE).authenticated()
                .anyRequest().authenticated();

        http.addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtUtil));
        http.addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtUtil, userDetailsService));

    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
        configuration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "OPTIONS"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public BCryptPasswordEncoder bcryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
        auth.userDetailsService(userDetailsService).passwordEncoder(bcryptPasswordEncoder());
    }
}
