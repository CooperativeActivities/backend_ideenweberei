package at.qe.crac.ui.controllers;

import at.qe.crac.model.User;
import at.qe.crac.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 *
 */
@Controller
@Scope("view")
public class UserListController {

    @Autowired
    private UserService userService;

    private List<User> userList;

    public List<User> getUserList() {
        if(userList == null) {
            userList = userService.getUserList();
        }
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }
}
