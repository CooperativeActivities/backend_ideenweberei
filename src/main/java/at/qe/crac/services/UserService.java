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
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Service for accessing and manipulating user data.
 *
 */
@Component
@Scope("application")
public class UserService extends AbstractService<User> {

    //beware of circular creation
    @Autowired
    private RoleService roleService;

    @Autowired
    private HelperService helperService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // current User
    public User getUser() {
        JsonNode response = helperService.request("/user", "get");
        ObjectMapper mapper = new ObjectMapper();
        User user = null;

        try {
            user = mapper.treeToValue(response.path("object"), User.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return user;
    }

    public User getUser(int id) {
        JsonNode response = helperService.request("/user/"+id, "get");
        ObjectMapper mapper = new ObjectMapper();
        User user = null;

        try {
            user = mapper.treeToValue(response.path("object"), User.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return user;
    }

    public List<User> getUserList() {
        JsonNode response = helperService.request("/user/all", "get");
        ObjectMapper mapper = new ObjectMapper();

        System.out.println(response);

        List<User> user = new ArrayList<>();

        if(response == null) {
            return user;
        }

        if (response.path("object").isArray()) {
            for (final JsonNode node : response.path("object")) {
                try {
                    user.add(mapper.treeToValue(node, User.class));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }

        return user;
    }

    public List<Role> getRoleList() {
        return roleService.getRoleList();
    }

    public User createUser() {
        User user = new User();
        user.setName("");
        user.setFirstName("");
        user.setLastName("");
        String password = passwordEncoder.encode(new Date().toString());
        user.setPassword(password);
        user.setEmail("");
        user.setPhone("");
        user.setRoles(new ArrayList<Role>());
        return user;
    }

    public void deleteUser(User user) {
        if(!user.getName().equals("frontend")) {
            helperService.request("/admin/user/"+user.id, "delete");
        }
    }

    //PUT /admin/user/{id}
    //POST /admin/user
    public User saveUser(User user) {
        List<Role> roleList = user.getRoles();

        user.setRoles(null);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.convertValue(user, JsonNode.class);

        JsonNode response;
        if(user.isNew()) {
            response = helperService.request("/admin/user", "post", node);
            if(response == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", "Could not create User."));
                return user;
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Password:", user.getPassword()));
            }
        } else {
            response = helperService.request("/admin/user/"+user.getId(), "put", node);
        }

        if(response == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", "Could not save User."));
            return user;
        }

        try {
            user = mapper.treeToValue(response.path("object"), User.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", "Could not save User."));
            return user;
        }

        user.setRoles(roleList);
        if(!user.equals(getUser())) {
            roleService.saveRoleList(user, roleList);
        }

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "User saved."));
        return user;
    }
}

