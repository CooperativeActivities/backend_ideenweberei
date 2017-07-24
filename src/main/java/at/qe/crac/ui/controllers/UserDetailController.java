package at.qe.crac.ui.controllers;

import at.qe.crac.model.Role;
import at.qe.crac.model.User;
import at.qe.crac.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Controller
@Scope("view")
public class UserDetailController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserListController userListController;

    private User user;
    private List<Role> roleList;
    private String password1;
    private String password2;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    public String getPassword1() {
        return password1;
    }

    public void setPassword1(String password1) {
        this.password1 = password1;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public List<Role> completeRole(String query) {
        List<Role> allItems = userListController.getRoleList();
        List<Role> filteredItems = new ArrayList<>();

        for(Role skin : allItems) {
            if(skin.getName().startsWith(query)) {
                filteredItems.add(skin);
            }
        }
        return filteredItems;
    }

    public User create() {
        return userService.createUser();
    }

    public void save(User user) {
        //todo: optimize
        if(user.isNew()) {
            for (User test : userListController.getUserList()) {
                if (test.getName().equals(user.getName())) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Username already exists."));
                    return;
                }
            }
        } else {
            if(password1 != null || password2 != null) {
                if(password1.equals(password2)) {
                    user.setPassword(password1);
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Info", "Passwords do not match."));
                    user.setPassword(null);
                }
            } else {
                user.setPassword(null);
            }
        }

        this.user = userService.saveUser(user);
        userListController.setUserList(null);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "User saved."));
    }

    public void save() {
        save(user);
    }

    public void delete() {
        userService.deleteUser(user);
        userListController.setUserList(null);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "User deleted."));
    }
}
