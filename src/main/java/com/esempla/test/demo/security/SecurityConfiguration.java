package com.esempla.test.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final DataSource dataSource;

    public SecurityConfiguration(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .jdbcAuthentication()
                .dataSource(dataSource)
                .authoritiesByUsernameQuery("select \"user\".username, user_role.role\n" +
                        "from \"user\"\n" +
                        "inner join user_role\n" +
                        "on \"user\".id = user_role.user_id where username = ?")
                .usersByUsernameQuery("select username, password, true FROM \"user\" where username = ?")
                .passwordEncoder(new BCryptPasswordEncoder())
        ;
    }
//
//@Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//                .inMemoryAuthentication()
//                .withUser("admin").password(passwordEncoder().encode("admin123")).roles("ADMIN")
//                .and()
//                .withUser("dan").password(passwordEncoder().encode("dan123")).roles("USER")
//                .and()
//                .withUser("manager").password(passwordEncoder().encode("manager")).roles("MANAGER");
//    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //it does matter the order of antMatchers
                .csrf()
                .disable()
                .authorizeRequests()
                /*.antMatchers(HttpMethod.DELETE).hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/users").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/user/{id}").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/comments").authenticated()
                .antMatchers("/comment/{id}").authenticated()
                .antMatchers("/posts").authenticated()
                .antMatchers("/post/{id}").authenticated()*/
                .anyRequest().permitAll()
                .and()
                .httpBasic()
                .and()
                ;

                // Only admin can perform HTTP delete operation
//                .authorizeRequests().antMatchers(HttpMethod.DELETE).hasRole(Role.ADMIN)
//                // any authenticated user can perform all other operations
//                .antMatchers("/products/**").hasAnyRole(Role.ADMIN, Role.USER).and().httpBasic()
//                // Permit all other request without authentication
//                .and().authorizeRequests().anyRequest().permitAll()

    }
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
