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

    public Role getRole(int id) {
        List<Role> roleList = getRoleList();
        for(Role role : roleList) {
            if(role.getId() == id) {
                return role;
            }
        }
        return null;
    }

    public List<Role> getRoleList() {
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
        return roles;
    }

    public List<Role> getRoleList(User user) {
        List<Role> roleList = new ArrayList<>();

        if(user.isNew()) {
            return roleList;
        }

        JsonNode response = helperService.request("/user/"+user.id, "get");

        ObjectMapper mapper = new ObjectMapper();

        if (response.path("object").path("roles").isArray()) {
            for (final JsonNode node : response.path("object").path("roles")) {
                try {
                    roleList.add(mapper.treeToValue(node, Role.class));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }

        return roleList;
    }

    public List<Role> saveRoleList(User user, List<Role> roleList) {
        List<Role> oldRoles = getRoleList(user);
        List<Role> newRoles = user.getRoles();

        if(newRoles != null) {
            for (Role role : newRoles) {
                if (oldRoles == null || !oldRoles.contains(role)) {
                    //PUT admin/user/{user_id}/role/{role_id}/add
                    System.out.println("admin/user/" + user.id + "/role/" + role.id + "/add");
                    JsonNode response = helperService.request("admin/user/" + user.id + "/role/" + role.id + "/add", "put");
                    if (response == null) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", "Could not add Role."));
                    }
                }
            }
        }

        if(oldRoles != null) {
            for (Role role : oldRoles) {
                if (newRoles == null || !newRoles.contains(role)) {
                    //DELETE admin/user/{user_id}/role/{role_id}/add
                    JsonNode response = helperService.request("admin/user/" + user.id + "/role/" + role.id + "/add", "delete");
                    if (response == null) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", "Could not remove Role."));
                    }
                }
            }
        }

        return newRoles;
    }
}
