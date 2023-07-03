package com.mindhub.homebanking.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@EnableWebSecurity
@Configuration
class WebAuthorization {

    @Bean
    protected SecurityFilterChain filterchain (HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/web/index.html", "/web/pages/login.html").permitAll()
                .antMatchers(HttpMethod.POST,"/api/login", "api/clients").permitAll()
                .antMatchers(HttpMethod.POST,"/api/clients/current/accounts","/api/clients/current/cards").hasAuthority("CLIENT")
                .antMatchers("/web/manager.html", "/rest/**","/h2-console/**").hasAuthority("ADMIN")
                .antMatchers("/web/pages/account.html","/web/pages/accounts.html","/web/pages/card.html","/api/clients/current/accounts").hasAuthority("CLIENT");

        //autenticacion en el formulario
        http.formLogin()

                .usernameParameter("email")

                .passwordParameter("password")

                .loginPage("/api/login");


        //cierre de sesion
        http.logout().logoutUrl("/api/logout").deleteCookies("JSESSIONID");

        // Desactivar la verificación de tokens CSRF(evita ataques de falsificaciones de solicitud entre sitios)

        http.csrf().disable();

        // Desactivar las opciones de frameOptions para acceder a h2-console

        http.headers().frameOptions().disable();

        // Si el usuario no está autenticado, enviar una respuesta de fallo de autenticación

        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // Si el inicio de sesión tiene éxito,se eliminan los atributos de autenticacion

        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        // Si el inicio de sesión falla, enviar una respuesta de fallo de autenticación

        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // Si el cierre de sesión es exitoso, enviar una respuesta exitosa

        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
        //contruye y devuelve el filtro configurado
        return http.build();
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session != null) {

            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

        }

    }

}