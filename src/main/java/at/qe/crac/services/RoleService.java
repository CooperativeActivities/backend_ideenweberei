package at.qe.crac.services;

import at.qe.crac.model.Role;
import at.qe.crac.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Service for accessing and manipulating user data.
 *
 */
@Component
@Scope("application")
public class RoleService extends AbstractService<Role> {

    @Autowired
    private HelperService helperService;

    // current User
    public Role getRole(int id) {
        List<Role> roleList = getRoleList();
        for(Role role : roleList) {
            if(role.getId() == id) {
                return role;
            }
        }
        return null;
    }

    private List<Role> getRoleList() {
        JsonNode response = helperService.request("/role", "get");
        ObjectMapper mapper = new ObjectMapper();

        List<Role> roles = new ArrayList<>();

        if(response == null) {
            return roles;
        }

        if (response.path("object").isArray()) {
            for (final JsonNode node : response.path("object")) {
                try {
                    roles.add(mapper.treeToValue(node, Role.class));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println(roles);
        return roles;
    }
}
