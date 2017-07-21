package at.qe.crac.ui.controllers;

import at.qe.crac.model.User;
import at.qe.crac.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User create() {
        return userService.createUser();
    }

    public void save() {
        //todo: optimize
        if(user.isNew()) {
            for (User test : userListController.getUserList()) {
                if (test.getName().equals(user.getName())) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Username already exists."));
                    return;
                }
            }
        }

        user = userService.saveUser(user);
        userListController.setUserList(null);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "User saved."));
    }

    public void delete() {
        userService.deleteUser(user);
        userListController.setUserList(null);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "User deleted."));
    }
}
