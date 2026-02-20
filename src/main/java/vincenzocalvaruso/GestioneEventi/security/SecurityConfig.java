package vincenzocalvaruso.GestioneEventi.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // classe configurazione apposita per Spring Security
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
        //Con questo bean configuro le impostazioni di sicurezza di SpirngSecurity, potrò
        //disabilitare comportamenti non richiesti
        httpSecurity.formLogin(formLogin -> formLogin.disable());
        //Levo il formLogin offerto perchè avrò un FrontEnd che ci penserà
        httpSecurity.csrf(csrf -> csrf.disable());
        //Non voglio protezione ad attacchi csrf (non serve per autenticazione con JWT)

        //modificare comportamenti già esistenti
        httpSecurity.sessionManagement(sessions -> sessions.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // L'autenticazione JWT è l'esatto opposto del lavorare tramite sessioni, quindi lavoro in modalità STATELESS
        //aggiungere comportamenti custom
        httpSecurity.authorizeHttpRequests(authorization -> authorization.requestMatchers("/**").permitAll());
        // Disabilito il meccanismo di default di Spring Security che ritorna 401 ad ogni richiesta.
        //Lo disattivo perchè ho un meccanismo di autenticazione custom,
        // sarà questo a proteggere gli endpoint e non direttamente SpringSecurity

        return httpSecurity.build();

    }

    @Bean
    public PasswordEncoder getBCrypt() {
        // Metodo per hash+salt della password, cosi da non mostrarla in chiaro ma proteggerla
        return new BCryptPasswordEncoder(12);
    }
}
