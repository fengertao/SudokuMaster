/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.web.aa.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class JWTWebSecurityConfig {

    private final JwtUnAuthorizedResponseAuthenticationEntryPoint jwtUnAuthorizedResponseAuthenticationEntryPoint;

    private final UserDetailsService jwtJPAUserDetailsService;

    private final JwtTokenAuthorizationOncePerRequestFilter jwtAuthenticationTokenFilter;

    @Value("${jwt.path.authentication}")
    private String authenticationPath;

    @Value("${jwt.path.signup}")
    private String signupPath;

    @Value("${server.error.path}")
    private String errorPath;

    @Value("${spring.h2.console.enabled:false}")
    private boolean h2ConsoleEnabled;

    @Value("${spring.h2.console.path:}")
    private String h2ConsolePath;

    @Autowired
    public JWTWebSecurityConfig(JwtTokenAuthorizationOncePerRequestFilter jwtAuthenticationTokenFilter, UserDetailsService jwtJPAUserDetailsService, JwtUnAuthorizedResponseAuthenticationEntryPoint jwtUnAuthorizedResponseAuthenticationEntryPoint) {
        this.jwtAuthenticationTokenFilter = jwtAuthenticationTokenFilter;
        this.jwtJPAUserDetailsService = jwtJPAUserDetailsService;
        this.jwtUnAuthorizedResponseAuthenticationEntryPoint = jwtUnAuthorizedResponseAuthenticationEntryPoint;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(jwtJPAUserDetailsService)
                .passwordEncoder(passwordEncoderBean());
    }

    private SecurityExpressionHandler<FilterInvocation> roleHierarchyWebExpressionHandler() {
        DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
        defaultWebSecurityExpressionHandler.setRoleHierarchy(roleHierarchy());
        return defaultWebSecurityExpressionHandler;
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ROOT > ROLE_ADMIN > ROLE_VIP > ROLE_USER > ROLE_APPLICANT > ROLE_ANONYMOUS");
        return roleHierarchy;
    }

    public PasswordEncoder passwordEncoderBean() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        //Todo hardcoded url are used by ReactJs 16.10
                        //Todo h2-console/** only enabled in production
                        .requestMatchers(authenticationPath, errorPath, signupPath, "/h2-console/**", "/", "/index.html", "/images/**", "/static/**", "/*.json", "/*.js", "/logo.*", "/theme.less", "/robot.txt", "/favicon.ico", "/app/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

//    @Override
//    protected void configure(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity
//                .csrf(csrf -> csrf.disable())s
//                .exceptionHandling().authenticationEntryPoint(jwtUnAuthorizedResponseAuthenticationEntryPoint).and()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//                .authorizeRequests()
//                .expressionHandler(roleHierarchyWebExpressionHandler())
//                .anyRequest().authenticated();
//
//        httpSecurity
//                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
//
//        httpSecurity
//                .headers()
//                .frameOptions().sameOrigin()  //H2 Console Needs this setting
//                .cacheControl(); //disable caching
//    }
//
//    @Override
//    public void configure(WebSecurity webSecurity) {
//        WebSecurity.IgnoredRequestConfigurer ignoredRequestConfigurer = webSecurity
//                .ignoring()
//                .antMatchers(
//                        HttpMethod.POST,
//                        authenticationPath,
//                        errorPath,
//                        signupPath
//                )
//                .antMatchers(HttpMethod.OPTIONS, "/**")
//                .antMatchers(HttpMethod.GET, "/")
//                .antMatchers("/index.html")
//                //Todo Below url are used by ReactJs 16.10
//                .antMatchers("/images/**", "/static/**", "/*.json", "/*.js", "/logo.*", "/theme.less", "/robot.txt", "/favicon.ico", "/app/**");
//        //Steps to enable h2-console in dev profile:
//        //1. Delete src/main/resources/static folder
//        //2. Invalidate caches in intellij and restart
//        //3. Rebuild project
//        //4. Clear cache in browser
//        //5. Navigate to http://127.0.0.1:8080/h2-console
//        //Not for production!
//        if (h2ConsoleEnabled && StringUtils.hasText(h2ConsolePath)) {
//            ignoredRequestConfigurer.antMatchers("/h2-console/**");
//        }
//    }
}
