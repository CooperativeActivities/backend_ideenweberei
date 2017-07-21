package at.qe.crac.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity()
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;

    @Autowired
    private CustomLogoutSuccessHandler customLogoutSuccessHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/login.xhtml")
                .permitAll()
                .antMatchers("/welcome.xhtml")
                .permitAll()
                .antMatchers("/public/**")
                .permitAll()
                .antMatchers("/secure/**")
                .hasAnyAuthority("USER")
                .antMatchers("/admin/**")
                .hasAnyAuthority("ADMIN")
                .and()
            .formLogin()
                .loginPage("/login.xhtml")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/welcome.xhtml")
                .failureUrl("/login.xhtml?error=true")
                .permitAll()
                .and()
            .logout()
                .logoutSuccessHandler(customLogoutSuccessHandler)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .invalidateHttpSession(false)
                .logoutSuccessUrl("/login.xhtml?logout=true")
                .permitAll()
                .and()
            .exceptionHandling()
                .accessDeniedPage("/login.xhtml?denied=true")
                .and()
            .sessionManagement()
                .invalidSessionUrl("/login.xhtml?session=true");

        http.csrf().disable();
        http.headers().frameOptions().disable(); // needed for H2 console
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customAuthenticationProvider);
    }
}
