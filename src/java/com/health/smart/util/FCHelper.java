/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.health.smart.util;

import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.primefaces.context.RequestContext;

/**
 *
 * @author sadraco
 */
public class FCHelper {

    public static String getFullPath() {
        ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
        return ectx.getRequestServerName() + ":" + ectx.getRequestServerPort() + ectx.getRequestContextPath();
    }

    public static String getParameter(String para) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(para);
    }

    public static String getRemoteHost() {
        return ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteHost();
    }

    public static void setSessionObject(String key, Object obj) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(key, obj);
    }

    public static void removeSessionObject(String key) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(key);
    }

    public static Object getSessionObject(String key) {
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(key);
    }

    public static void clearSession() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
    }

    public static String redirect(String page, boolean facesRedirect) {
        return page + "?faces-redirect=" + facesRedirect;
    }

    public static void redirect(String page) {
        FacesContext fc = FacesContext.getCurrentInstance();
        fc.getApplication().getNavigationHandler().handleNavigation(fc, null, page);
    }

    public static void blockMessage(Severity level, String message) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(level, null, message));
        RequestContext.getCurrentInstance().execute("msgDialog.show();");
    }

    public static void info(String message) {
        blockMessage(FacesMessage.SEVERITY_INFO, message);
    }

    public static void warning(String message) {
        blockMessage(FacesMessage.SEVERITY_WARN, message);
    }

    public static void error(BLException be) {
        blockMessage(FacesMessage.SEVERITY_ERROR, be.getMessage());
    }

    public static void error(Exception e) {
        if (e instanceof EJBException) {
            e = new Exception(e.getCause());
        }
        String displayMessage = e.getMessage();
        if (e == null || e.getMessage() == null) {
            displayMessage = "Null Pointer Exception";
        } else if (e.getMessage().contains("Transaction aborted")) {
            if (e.getCause().getMessage().contains("Transaction marked for rollback.")) {
                displayMessage = e.getCause().getCause().getMessage();
            }
        }
        blockMessage(FacesMessage.SEVERITY_ERROR, displayMessage);
    }

    public static void addMessage(String comp, Severity level, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(
                comp, new FacesMessage(level, summary, detail));
    }

    public static void addInfo(String detail) {
        addMessage(null, FacesMessage.SEVERITY_INFO, null, detail);
    }

    public static void addWarning(String detail) {
        addMessage(null, FacesMessage.SEVERITY_WARN, null, detail);
    }

    public static void addError(Exception be) {
        addMessage(null, FacesMessage.SEVERITY_ERROR, null, be.getMessage());
    }

    public static void addError(String msg) {
        addMessage(null, FacesMessage.SEVERITY_ERROR, null, msg);
    }

    public static void output(byte[] data) throws Exception {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        response.setContentLength(data.length);
        response.setContentType("text/csv");
        response.setHeader("Content-disposition", "attachment; filename=data.csv");
        response.getOutputStream().write(data);
        response.getOutputStream().flush();
        response.getOutputStream().close();
        facesContext.getApplication().getStateManager().saveView(facesContext);
        facesContext.responseComplete();
    }
}
