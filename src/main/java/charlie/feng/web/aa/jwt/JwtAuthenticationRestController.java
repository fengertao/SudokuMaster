/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.web.aa.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;

//Todo remove hard coded origin
@RestController
@CrossOrigin(origins = {"${jwt.url.ui1}", "${jwt.url.ui2}", "${jwt.url.ui3}", "${jwt.url.ui4}"})
public class JwtAuthenticationRestController {

    private static Logger logger = LoggerFactory.getLogger(JwtAuthenticationRestController.class);

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService jwtJPAUserDetailsService;
    @Value("${jwt.http.request.header}")
    private String tokenHeader;

    @Autowired
    public JwtAuthenticationRestController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, UserDetailsService jwtJPAUserDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.jwtJPAUserDetailsService = jwtJPAUserDetailsService;
    }

    @RequestMapping(value = "${jwt.path.authentication}", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtTokenRequest authenticationRequest)
            throws AuthenticationException {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final UserDetails userDetails = jwtJPAUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        String fullname = userDetails.getUsername();
        if (userDetails instanceof JwtUserDetails) {
            fullname = ((JwtUserDetails) userDetails).getFullname();
        }

        final String token = jwtTokenUtil.generateToken(userDetails);

        logger.info(String.format("%s login successful.", userDetails.getUsername()));

        return ResponseEntity.ok(new JwtTokenAndUserInfoResponse(token, userDetails.getUsername(), fullname, userDetails.getAuthorities()));
    }

    @RequestMapping(value = "${jwt.path.refresh}", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        String authToken = request.getHeader(tokenHeader);

        final String token = authToken.substring(7);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUserDetails user = (JwtUserDetails) jwtJPAUserDetailsService.loadUserByUsername(username);

        if (user.isEnabled() && jwtTokenUtil.canTokenBeRefreshed(token)) {
            String refreshedToken = jwtTokenUtil.refreshToken(token);
            return ResponseEntity.ok(new JwtTokenResponse(refreshedToken));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<String> handleAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    private void authenticate(String username, String password) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            logger.info(String.format("%s login failed because of disable.", username));
            throw new AuthenticationException("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            logger.info(String.format("%s login failed with bad password.", username));
            throw new AuthenticationException("INVALID_CREDENTIALS", e);
        }
    }
}
