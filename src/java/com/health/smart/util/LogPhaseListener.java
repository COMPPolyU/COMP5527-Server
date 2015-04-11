/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.health.smart.util;


import java.util.Arrays;
import java.util.List;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author sadraco
 */
public class LogPhaseListener implements PhaseListener {

    public long startTime;

    private static final List<String> DEV_IPS = Arrays.asList(new String[]{"127.0.0.1"});

    private boolean isDevIP(FacesContext fc) {
        return DEV_IPS.contains(((HttpServletRequest) fc.getExternalContext().getRequest()).getRemoteHost());
    }

    private String getViewId(FacesContext fc) {
        return fc.getViewRoot().getViewId();
    }

    private void redirect(FacesContext fc, String page) {
        NavigationHandler nh = fc.getApplication().getNavigationHandler();
        nh.handleNavigation(fc, null, page);
    }

    @Override
    public void afterPhase(PhaseEvent event) {
        if (event.getPhaseId() == PhaseId.RESTORE_VIEW) {
            FacesContext fc = event.getFacesContext();
        }
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
    }

    @Override
    public void beforePhase(PhaseEvent pe) {
    }
}
