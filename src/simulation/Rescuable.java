package simulation;

import exceptions.CitizenAlreadyDeadException;
import model.disasters.Disaster;

public interface Rescuable {
public void struckBy(Disaster d) throws CitizenAlreadyDeadException;
public Address getLocation();
public Disaster getDisaster();
}
