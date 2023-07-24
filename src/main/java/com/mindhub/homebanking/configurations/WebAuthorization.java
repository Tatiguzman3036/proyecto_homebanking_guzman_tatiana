package com.mindhub.homebanking.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@EnableWebSecurity
@Configuration
class WebAuthorization extends GlobalAuthenticationConfigurerAdapter implements WebMvcConfigurer {

    @Bean
    protected SecurityFilterChain filterChain (HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/web/index.html", "/web/pages/login.html").permitAll()
                .antMatchers(HttpMethod.POST,"/api/login", "api/clients").permitAll()
                .antMatchers(HttpMethod.POST,"/api/clients/current/accounts","/api/clients/current/cards",
                        "/api/transactions", "/api/loans").hasAuthority("CLIENT")
                .antMatchers("/web/manager.html", "/rest/**","/h2-console/**").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.POST,"/api/loans/createdLoans").hasAuthority("ADMIN")
                .antMatchers("/api/loans","/api/clients/current/accounts","/api/clients/accounts").hasAuthority("CLIENT")
                .antMatchers("/web/pages/account.html","/web/pages/accounts.html","/web/pages/card.html","/web/pages/client-loan-aplication.html"
                        ,"/web/pages/transfer.html","/web/pages/loan-application.html","/web/pages/createdCard.html").hasAuthority("CLIENT");
//                .anyRequest().denyAll();


        http.formLogin()

                .usernameParameter("email")

                .passwordParameter("password")

                .loginPage("/api/login");

//        http.cors();
        http.logout().logoutUrl("/api/logout").deleteCookies("JSESSIONID");

        // Desactivar la verificación de tokens CSRF

        http.csrf().disable();

        // Desactivar las opciones de frameOptions para acceder a h2-console

        http.headers().frameOptions().disable();

        // Si el usuario no está autenticado, enviar una respuesta de fallo de autenticación

        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // Si el inicio de sesión tiene éxito, limpiar las banderas que solicitan autenticación

        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        // Si el inicio de sesión falla, enviar una respuesta de fallo de autenticación

        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // Si el cierre de sesión es exitoso, enviar una respuesta exitosa

        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

        return http.build();
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session != null) {

            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

        }

    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") //todas las solicitudes que se realizen a URLs que comienzen con /api/
                .allowedOrigins("http://127.0.0.1:5500") // permitimos solicitudes del front alojado en este puerto que comienzen con api
                .allowedMethods("GET", "POST", "PUT", "DELETE") //los tipos de solicitudes pueden acceder a laruta api desd el front
                .allowedHeaders("*"); // se usa para verificar autenticacion y evitar ataques crsf y otros, son medidas de seguridad o cabeceras
    }

}