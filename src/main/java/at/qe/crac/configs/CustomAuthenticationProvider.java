package at.qe.crac.configs;

import at.qe.crac.model.Role;
import at.qe.crac.model.User;
import at.qe.crac.services.HelperService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private HelperService helperService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        if(authentication == null) {
            return null;
        }

        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        JsonNode response = helperService.login(username, password);

        if(response == null) {
            return null;
        }

        JsonNode meta = response.path("meta");

        ObjectMapper mapper = new ObjectMapper();
        User user = null;

        try {
            user = mapper.treeToValue(meta.path("user"), User.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        if(user == null) {
            return null;
        }

        List<Role> roles = new ArrayList<>();
        if (meta.path("roles").isArray()) {
            for (final JsonNode node : meta.path("roles")) {
                try {
                    roles.add(mapper.treeToValue(node, Role.class));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }

        user.setRoles(roles);
        System.out.println(roles);

        if(!passwordEncoder.matches(password, user.getPassword())) {
            return null;
        }
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for(Role role : user.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        return new UsernamePasswordAuthenticationToken(username, password, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}