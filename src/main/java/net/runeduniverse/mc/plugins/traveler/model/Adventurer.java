package net.runeduniverse.mc.plugins.traveler.model;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import net.runeduniverse.mc.plugins.snowflake.api.data.model.AShadowPlayerExtension;

@NodeEntity
public class Adventurer extends AShadowPlayerExtension {
	public static final String LAST_KNOWN_POINT_OF_INTEREST_RELATION = "LAST_KNOWN_POI";
	public static final String HAS_VISITED_RELATION = "HAS_VISITED";

	@Relationship(type = LAST_KNOWN_POINT_OF_INTEREST_RELATION, direction = Relationship.OUTGOING)
	private PointOfInterest lastKnownPOI = null;

	@Relationship(type = HAS_VISITED_RELATION, direction = Relationship.OUTGOING)
	public List<Traveler> travelers = new ArrayList<>();
	
	// GETTER

	public PointOfInterest getLastKnownPOI() {
		return lastKnownPOI;
	}
	public List<Traveler> getTravelers(){
		return this.travelers;
	}

	// SETTER

	public void setLastKnownPOI(PointOfInterest lastKnownPOI) {
		this.lastKnownPOI = lastKnownPOI;
	}
	public void addTraveler(Traveler traveler) {
		this.travelers.add(traveler);
	}

	

}
