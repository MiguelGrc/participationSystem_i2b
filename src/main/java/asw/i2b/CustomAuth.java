package asw.i2b;


import asw.i2b.model.UserModel;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * @author nokutu
 * @since 22/03/2017.
 */
@Component
public class CustomAuth implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String login = authentication.getName().trim();
        String password = authentication.getCredentials().toString().trim();
        UserModel u = new UserModel(login);

        if("admin@admin.com".equals(login) && "admin".equals(password)) {
            u.setAdmin(true);
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            return new UsernamePasswordAuthenticationToken(u, password, authorities);
        } else {
            try {
                StringBuilder result = new StringBuilder();
                URL url = new URL("http://localhost:8080/user");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("Content-Type", "application/json");

                conn.setDoOutput(true);

                String body = "{\"login\": \"" + login + "\", \"password\": \"" + password + "\"}";
                conn.setRequestProperty("Content-Length", String.valueOf(body.length()));
                conn.getOutputStream().write(body.getBytes("UTF8"));

                int code = conn.getResponseCode();
                if (code == 404) {
                    return null;
                } else {
                    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line;
                    while ((line = rd.readLine()) != null) {
                        result.append(line);
                    }
                    rd.close();

                    List<GrantedAuthority> authorities = new ArrayList<>();
                    authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                    return new UsernamePasswordAuthenticationToken(u, password, authorities);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(
                UsernamePasswordAuthenticationToken.class);
    }

}