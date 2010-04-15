package com.rsc.moneta.action;


import com.opensymphony.xwork2.ActionSupport;
import javax.servlet.ServletContext;
import org.apache.struts2.interceptor.SessionAware;

import javax.persistence.EntityManager;
import java.util.Map;
import org.apache.struts2.util.ServletContextAware;

/**
 * Created by IntelliJ IDEA.
 * User: sulic
 * Date: 06.06.2007
 * Time: 21:36:03
 * To change this template use File | Settings | File Templates.
 */
public class BaseAction  extends ActionSupport implements SessionAware, ServletContextAware {
    protected Map session = null;
    protected EntityManager em;
    protected  ServletContext servletContext;

    public BaseAction() {

    }

    public void setSession(Map map) {
        session = map;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public EntityManager getEm() {
        return em;
    }

    public void setServletContext(ServletContext sc) {
        this.servletContext = sc;
    }

}
