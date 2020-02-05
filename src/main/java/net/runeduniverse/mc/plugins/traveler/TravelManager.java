package net.runeduniverse.mc.plugins.traveler;

import java.util.ArrayList;
import java.util.List;
import net.runeduniverse.mc.plugins.traveler.model.Traveler;

public class TravelManager {

	private List<Traveler> travelers = new ArrayList<Traveler>();
	
	public TravelManager() {
		this.travelers = (List<Traveler>) TravelerMain.travelerSession.loadAll(Traveler.class, 2);
	}
	
	public void addNPC(Traveler traveler) {
		this.travelers.add(traveler);
		TravelerMain.travelerSession.save(traveler);
	}
	
}
