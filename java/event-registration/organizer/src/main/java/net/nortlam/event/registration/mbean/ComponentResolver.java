package net.nortlam.event.registration.mbean;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
 
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
 
@ManagedBean(name="componentResolver")
@ViewScoped
public class ComponentResolver {
    
    private static final Logger LOG = Logger.getLogger(ComponentResolver.class.getName());
    
    /**
     * Returns the absolute path of a given {@link UIComponent}.
     * E.g. :namingContainer1:namingContainer2:componentId.
     *
     * @param currentComponent
     * @return The absolute path of the given component
     */
    public static String getAbsoluteComponentPath(UIComponent currentComponent) {
        final char separatorChar = UINamingContainer.getSeparatorChar(FacesContext.getCurrentInstance());
 
        String path = "";
        if(!(currentComponent instanceof NamingContainer)) path = currentComponent.getId();
 
        do {
            if(currentComponent instanceof NamingContainer) {
                path = currentComponent.getId() + (!path.isEmpty() ? separatorChar : "") + path;
            }
            currentComponent = currentComponent.getParent();
        }
        while(currentComponent != null);
        path = separatorChar + path;
 
        return path;
    }
 
    /**
     * Returns a whitespace-separated list of the absolute paths of all given components.
     *
     * @param components The list of components
     * @return A whitespace-separated list of all absolute paths.
     * @see ComponentResolver#getAbsoluteComponentPath(UIComponent)
     */
    public static String getAbsoluteComponentPaths(Collection<UIComponent> components) {
        String paths = "";
        for(UIComponent c : components) {
            if(!paths.isEmpty()) paths += " ";
            paths += ComponentResolver.getAbsoluteComponentPath(c);
        }
        return paths;
    }
 
    /**
     * Finds all components with the given id, and returns a whitespace-separated list
     * of their absolute paths (e.g. <code>:j_id1:j_id2:myId :j_id1:j_id3:myId</code>).
     *
     * @param id The id to search the component tree for
     * @return Whitespace-separated list of absolute component paths
     * @see #resolveList(String)
     */
    public static String resolve(String id) {
        List<UIComponent> components = resolveList(id);
        String result =  getAbsoluteComponentPaths(components);
        LOG.log(Level.INFO, ">>> resolve():{0}", result);
        return result;
    }
 
    /**
     * Finds all components with the given id by traversing the component tree.
     * @param id Id to search for.
     * @return List of components that have got this id.
     */
    public static List<UIComponent> resolveList(String id)
    {
        return resolveList(id, FacesContext.getCurrentInstance().getViewRoot());
    }
 
    /**
     * Recursive function that traverses the component tree and accumulates all components
     * that match the given id.
     * @param id The id to look for
     * @param currentComponent Component of the tree to start traversal at
     * @return Accumulation of all components that match the given id
     */
    private static List<UIComponent> resolveList(String id, UIComponent currentComponent) {
        List<UIComponent> accumulator = new LinkedList<UIComponent>();
         
        if(null != currentComponent.getId() && currentComponent.getId().equals(id)) {
            accumulator.add(currentComponent);
        }
 
        Iterator<UIComponent> childIt = currentComponent.getFacetsAndChildren();
        while(childIt.hasNext()) {
            UIComponent child = childIt.next();
            accumulator.addAll(resolveList(id, child));
        }
 
        return accumulator;
    }
}