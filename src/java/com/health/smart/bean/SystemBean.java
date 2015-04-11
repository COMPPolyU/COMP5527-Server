package com.health.smart.bean;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import com.health.smart.ejb.ClinicEJB;
import com.health.smart.entity.User;
import com.health.smart.util.FCHelper;
import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.MenuModel;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;

/**
 *
 * @author sadraco
 */
@Named
@SessionScoped
public class SystemBean implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private ClinicEJB ejb;
    private String staffId;
    private String password;
    private int pollInterval = 600;
    private MenuModel model = null;
    private User user;

    public SystemBean() {
    }

    public User getLoggedInUser() {
        return user;
    }

    public void login() {
        try {
            user = ejb.login(staffId, password);
            FCHelper.redirect("home");
        } catch (Exception ex) {
            FCHelper.addError(ex);
        }
    }

    

    public boolean isLoggedIn() {
        return user != null;
    }

   

    public MenuModel getModel() {
        try {
            if (model == null) {
                model = new DefaultMenuModel();
                if (user == null) {
                    return model;
                }
                Map<String, DefaultSubMenu> mapping = new TreeMap<>();
                user.getFunction().stream().forEach((f) -> {
                    DefaultMenuItem item = new DefaultMenuItem();
                    item.setValue(f.getName());
                    item.setUrl(f.getPath());
                    model.addElement(item);
                });
            }
            return model;
        } catch (Exception ex) {
            FCHelper.addError(ex);
        }
        return null;
    }

    public String logout() {
        FCHelper.clearSession();
        return FCHelper.redirect("login", true);
    }

    public void poll() {
        // do nothing, only keep session alive.
    }

    public void idleListener() {
        logout();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public int getPollInterval() {
        return pollInterval;
    }

    public void setPollInterval(int pollInterval) {
        this.pollInterval = pollInterval;
    }

    
}
