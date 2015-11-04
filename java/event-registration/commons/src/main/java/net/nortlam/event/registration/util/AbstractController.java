/**
 * Copyright 2014 Mauricio "Maltron" Leal <maltron@gmail.com>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.nortlam.event.registration.util;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

/**
 * A easy way to create JSF Beans
 * @author Mauricio "Maltron" Leal */
public class AbstractController implements Serializable {
    
    public static final String DEFAULT_COMPONENT_MESSAGE = "messages";

    private static final Logger LOG = Logger.getLogger(AbstractController.class.getName());

    public AbstractController() {
    }
    
    protected FacesContext getContext() {
        return FacesContext.getCurrentInstance();
    }
    
    protected UIViewRoot getViewRoot() {
        return getContext().getViewRoot();
    }
    
    protected ExternalContext getExternal() {
        return FacesContext.getCurrentInstance().getExternalContext();
    }
    
    protected HttpServletRequest getServletRequest() {
        return (HttpServletRequest)getExternal().getRequest();
    }
    
    protected Flash getFlash() {
        return getExternal().getFlash();
    }
    
    protected void redirect(String contextPath) {
        redirect(null, contextPath);
    }
    
    protected void redirect(String host, String contextPath) {
        try {
            String original = host != null ? host : getServletRequest().getContextPath();
            getExternal().redirect(String.format("%s/%s", original, 
                    contextPath != null ? contextPath : ""));
        } catch(IOException ex) {
            LOG.log(Level.SEVERE, "### redirect() IO EXCEPTION:{0}", ex.getMessage());
        }
    }
    
    protected void navigate(String fromAction, String outcome) {
        NavigationHandler handler = getContext().getApplication().getNavigationHandler();
        handler.handleNavigation(getContext(), fromAction, outcome);
    }
    
    protected HttpServletRequest getRequest() {
        return (HttpServletRequest)getExternal().getRequest();
    }
    
    protected String getOriginalURL() {
        return (String)getExternal().getRequestMap()
                                    .get(RequestDispatcher.FORWARD_REQUEST_URI);
    }
    
//    protected ResourceBundle getBundle() {
//        FacesContext context = FacesContext.getCurrentInstance();
//        return context.getApplication()
//                .getResourceBundle(context, Porcupine.DEFAULT_RESOUCE_BUNDLE);
//    }
    
    protected void info(String message) {
        info(message, null);
    }
    
    protected void info(String message, String detail) {
        FacesMessage infoMessage = new FacesMessage(FacesMessage.SEVERITY_INFO,
            message, detail);
        getContext().addMessage(DEFAULT_COMPONENT_MESSAGE, infoMessage);
    }
    
    protected void fatal(String message) {
        fatal(message, null);
    }
    
    protected void fatal(String message, String detail) {
        FacesMessage fatalMessage = new FacesMessage(FacesMessage.SEVERITY_FATAL,
            message, detail);
        getContext().addMessage(DEFAULT_COMPONENT_MESSAGE, fatalMessage);
    }
    
    protected void error(String message) {
        error(message, null, null);
    }
    
    protected void error(String message, String detail) {
        error(message, detail, DEFAULT_COMPONENT_MESSAGE);
    }
    
    protected void error(String message, String detail, String messageComponents) {
        FacesMessage errorMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,
            message, detail);
        getContext().addMessage(messageComponents, errorMessage);
    }
    
    protected void warning(String message) {
        warning(message, null);
    }
    
    protected void warning(String message, String detail) {
        FacesMessage warningMessage = new FacesMessage(FacesMessage.SEVERITY_WARN,
            message, detail);
        getContext().addMessage(DEFAULT_COMPONENT_MESSAGE, warningMessage);
    }
}
