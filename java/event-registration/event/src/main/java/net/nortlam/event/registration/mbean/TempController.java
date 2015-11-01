package net.nortlam.event.registration.mbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import net.nortlam.event.registration.util.EventRegistrationCommonController;

/**
 *
 * @author Mauricio "Maltron" Leal <maltron@gmail.com>
 */
@ManagedBean(name = "temp")
@ViewScoped
public class TempController extends EventRegistrationCommonController
        implements Serializable {

    private String selectedCity;
    private List<String> cities;

    private static final Logger LOG = Logger.getLogger(TempController.class.getName());

    public TempController() {
    }

    public String getSelectedCity() {
        return selectedCity;
    }

    public void setSelectedCity(String selectedCity) {
        this.selectedCity = selectedCity;
    }

    public List<String> getCities() {
        return cities;
    }

    @PostConstruct
    private void init() {
        cities = new ArrayList<String>();
        cities.add("Miami");
        cities.add("London");
        cities.add("Paris");
        cities.add("Istanbul");
        cities.add("Berlin");
        cities.add("Barcelona");
        cities.add("Rome");
        cities.add("Brasilia");
        cities.add("Amsterdam");
    }
    
    public void save(ActionEvent e) {
        info("Selected City:", getSelectedCity());
    }

}
