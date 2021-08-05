package model.units;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import model.disasters.Collapse;
import model.disasters.Disaster;
import model.disasters.Fire;
import model.disasters.GasLeak;
import model.disasters.Infection;
import model.disasters.Injury;
import model.events.SOSResponder;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import simulation.Address;
import simulation.Rescuable;
import simulation.Simulatable;

public abstract class Unit implements Simulatable, SOSResponder {
	private String unitID;
	private UnitState state;
	private Address location;
	private Rescuable target;
	private int distanceToTarget;
	private int stepsPerCycle;
	private WorldListener worldListener;

	
	public String toString() {
		StringBuilder b=new StringBuilder();
		b.append("unit id "+unitID +"/  state "+ state +"\n");
		b.append("location : "+location.getX()+" "+location.getY()+"\n");
		if(this instanceof Ambulance)
			b.append("Ambulance");
		else if(this instanceof DiseaseControlUnit)
			b.append("DiseaseControlUnit");
		else if(this instanceof Evacuator) {
			b.append( "Evacuator\n"+"num of passenger :-  "+((Evacuator)this).getPassengers().size()+"\n" );
			for(Citizen c:((Evacuator)this).getPassengers()) {
				b.append(c.toString());
			}
		}
		if(target!=null) {
			if(target instanceof Citizen) {
			b.append("target is citizen and location is  " + target.getLocation().getX()+" "+target.getLocation().getY()+"\n");
		}
		else {
			b.append("target is building and location is " + target.getLocation().getX()+" "+target.getLocation().getY()+"\n");
		}
		}
		
		return b.toString();
	}
	public Unit(String unitID, Address location, int stepsPerCycle,
			WorldListener worldListener) {
		this.unitID = unitID;
		this.location = location;
		this.stepsPerCycle = stepsPerCycle;
		this.state = UnitState.IDLE;
		this.worldListener = worldListener;
	}

	public void setWorldListener(WorldListener listener) {
		this.worldListener = listener;
	}

	public WorldListener getWorldListener() {
		return worldListener;
	}

	public UnitState getState() {
		return state;
	}

	public void setState(UnitState state) {
		this.state = state;
	}

	public Address getLocation() {
		return location;
	}

	public void setLocation(Address location) {
		this.location = location;
	}

	public String getUnitID() {
		return unitID;
	}

	public Rescuable getTarget() {
		return target;
	}

	public int getStepsPerCycle() {
		return stepsPerCycle;
	}

	public void setDistanceToTarget(int distanceToTarget) {
		this.distanceToTarget = distanceToTarget;
	}

	@Override
	public void respond(Rescuable r) throws IncompatibleTargetException, CannotTreatException {
		if(this instanceof FireTruck) {
			if(r instanceof ResidentialBuilding)
				;
			else
				throw new IncompatibleTargetException(this, r, "IncompatibleTargetException");
		}
		else if (this instanceof GasControlUnit){
			if(r instanceof ResidentialBuilding)
				;
			else
				throw new IncompatibleTargetException(this, r, "IncompatibleTargetException");
		}
		else if(this instanceof Evacuator) {
			if(r instanceof ResidentialBuilding)
				;
			else
				throw new IncompatibleTargetException(this, r, "IncompatibleTargetException");
		}
		if(canTreat(r)) {
			if (target != null && state == UnitState.TREATING)
			reactivateDisaster();
		finishRespond(r);
		}
		else 
			throw new CannotTreatException(this, r, "CannotTreatException");
		
	}

	public void reactivateDisaster() {
		Disaster curr = target.getDisaster();
		curr.setActive(true);
	}

	public void finishRespond(Rescuable r) throws IncompatibleTargetException {
		target=r;
		state = UnitState.RESPONDING;
		Address t = r.getLocation();
		distanceToTarget = Math.abs(t.getX() - location.getX())
				+ Math.abs(t.getY() - location.getY());

	}

	public abstract void treat();

	public void cycleStep() {
		if (state == UnitState.IDLE)
			return;
		if (distanceToTarget > 0) {
			distanceToTarget = distanceToTarget - stepsPerCycle;
			if (distanceToTarget <= 0) {
				distanceToTarget = 0;
				Address t = target.getLocation();
				worldListener.assignAddress(this, t.getX(), t.getY());
			}
		} else {
			state = UnitState.TREATING;
			treat();
		}
	}
	public boolean canTreat(Rescuable r) {
		if(r.getDisaster()==null)
			return false;
		if(r instanceof ResidentialBuilding) {
			ResidentialBuilding b=(ResidentialBuilding) r;
			if(this instanceof FireTruck)
				if(b .getFireDamage()!=0 && (b.getDisaster() instanceof Fire))
					return true;
				else
					return false;
			else if(this instanceof GasControlUnit)
				if(b .getGasLevel()!=0 && (b.getDisaster() instanceof GasLeak))
					return true;
				else
					return false;
			else {
				if(b .getStructuralIntegrity()!=0 && (b.getDisaster() instanceof Collapse))
					return true;
				else
					return false;
			}
		}
		else {
			Citizen c=(Citizen) r;
			if(this instanceof Ambulance) {
				if((c .getBloodLoss()!=0 || c.getHp()!=100) && (c.getDisaster() instanceof Injury))
					return true;
				else
					return false;
				
			}
			else {
				if((c .getToxicity()!=0 || c.getHp()!=100)  && (c.getDisaster() instanceof Infection))
					return true;
				else
					return false;
			}
		}
	}
	
	public void jobsDone() {
		target = null;
		state = UnitState.IDLE;

	}
}
