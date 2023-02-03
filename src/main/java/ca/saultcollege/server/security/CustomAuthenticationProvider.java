package ca.saultcollege.server.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import
        org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;


@Service
@Configurable
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws
            AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        if (!username.equals("jane@doe.com") && !password.equals("Test123!")) {
            throw new BadCredentialsException("1000");
        }
        List<GrantedAuthority> grantedAuths = new ArrayList<>();
        grantedAuths.add(new SimpleGrantedAuthority("ADMIN"));
        return new UsernamePasswordAuthenticationToken(username, password,
                grantedAuths);
    }
    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
