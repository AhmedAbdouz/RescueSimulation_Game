package model.disasters;

import exceptions.BuildingAlreadyCollapsedException;
import exceptions.CitizenAlreadyDeadException;
import model.infrastructure.ResidentialBuilding;
import simulation.Rescuable;
import simulation.Simulatable;

public abstract class Disaster implements Simulatable{
	private int startCycle;
	private Rescuable target;
	private boolean active;
	public Disaster(int startCycle, Rescuable target) {
		this.startCycle = startCycle;
		this.target = target;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public int getStartCycle() {
		return startCycle;
	}
	public Rescuable getTarget() {
		return target;
	}
	public void strike() throws CitizenAlreadyDeadException, BuildingAlreadyCollapsedException 
	{
		
		target.struckBy(this);
		active=true;
	}
	@Override
	public String toString() {
		String b="";
		if(this instanceof Fire) {
			b+="Fire  ";
		}
			
		else if(this instanceof GasLeak) {
			b+="Gas Leak  ";
		}
			
		else if(this instanceof Collapse) {
			b+="Collapse   ";
		}
		else if(this instanceof Injury ) {
			b+="Injury   ";
		}
		else if(this instanceof Infection) {
			b+="Infection   ";
		}
		String a="";
		if(target instanceof  ResidentialBuilding)
			a+="ResidentialBuilding";
		else
			a+="Citezen";
		
		b+=" Target is "+a;
		return b;
	}
}
