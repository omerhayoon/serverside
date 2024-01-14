package com.dev.controllers;

import com.dev.BasicResponse;
import com.dev.User;
import com.dev.utils.DbUtils;
import com.dev.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.dev.Errors.*;


@RestController
public class TestController {

    private List<User> users = new ArrayList<>();

    @Autowired
    public Utils utils;

    @Autowired
    private DbUtils dbUtils;





    @RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.POST})
    public Object test () {
        return "Hello From Server";
    }


    @RequestMapping (value = "check-username")
    public boolean checkUsername (String username) {
        return dbUtils.checkIfUsernameAvailable(username);
    }

    @RequestMapping (value = "/sign-in")
    public boolean signIn (String username, String password) {
        return dbUtils.checkCredentials(username, password);
    }


    @RequestMapping(value = "sign-up", method = {RequestMethod.GET, RequestMethod.POST})
    public Object signUp (String username, String password, String password2) {
        boolean success = false;
        Integer errorCode = null;
        if (username != null && username.length() > 0) {
            if (password != null && password.length() > 0) {
                if (password2 != null && password2.equals(password)) {
                    if (!this.usernameTaken(username)) {
                        User newUser = new User(username, password);
                        this.users.add(newUser);
                        success = true;
                    } else {
                        errorCode = ERROR_SIGN_UP_USERNAME_TAKEN;
                    }
                } else {
                    errorCode = ERROR_SIGN_UP_PASSWORDS_DONT_MATCH;
                }
            } else {
                errorCode = ERROR_SIGN_UP_NO_PASSWORD;
            }
        } else {
            errorCode = ERROR_SIGN_UP_NO_USERNAME;
        }
        return new BasicResponse(success, errorCode);
    }

    private boolean usernameTaken (String username) {
        boolean taken = false;
        for (User user : this.users) {
            if (user.isSameUsername(username)) {
                taken = true;
                break;
            }
        }

        return taken;
    }

    @RequestMapping (value = "login", method = {RequestMethod.GET, RequestMethod.POST})
    public Object login (String username, String password) {
        boolean success = false;
        Integer errorCode = null;
        if (username != null && username.length() > 0) {
            if (password != null && password.length() > 0) {
                User user = this.getUser(username, password);
                if (user != null) {
                    success = true;
                } else {
                    errorCode = ERROR_LOGIN_WRONG_CREDS;
                }
            } else {
                errorCode = ERROR_SIGN_UP_NO_PASSWORD;
            }
        } else {
            errorCode = ERROR_SIGN_UP_NO_USERNAME;
        }
        return new BasicResponse(success, errorCode);
    }

    private User getUser (String username, String password) {
        User user = null;
        for (User currentUser : this.users) {
            if (currentUser.isSameCreds(username, password)) {
                user = currentUser;
                break;
            }
        }
        return user;
    }

    @RequestMapping (value = "add-note", method = RequestMethod.POST)
    public Object addNote (String username, String text) {
        boolean success = false;
        Integer errorCode = null;
        User user = getUser(username);
        if (user != null) {
            user.addNote(text);
            success = true;
        } else {
            errorCode = ERROR_NO_SUCH_USERNAME;
        }
        return new BasicResponse(success, errorCode);
    }

    private User getUser (String username) {
        User user = null;
        for (User currentUser : this.users) {
            if (currentUser.isSameUsername(username)) {
                user = currentUser;
                break;
            }
        }

        return user;
    }




}
