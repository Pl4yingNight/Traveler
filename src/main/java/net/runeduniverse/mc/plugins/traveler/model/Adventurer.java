package net.runeduniverse.mc.plugins.traveler.model;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import net.runeduniverse.mc.plugins.snowflake.api.data.model.AShadowPlayerExtension;

@NodeEntity
public class Adventurer extends AShadowPlayerExtension {
	public static final String HAS_VISITED_RELATION = "HAS_VISITED";

	private PointOfInterest lastKnownPOI = null;

	// GETTER

	public PointOfInterest getLastKnownPOI() {
		return lastKnownPOI;
	}

	// SETTER

	public void setLastKnownPOI(PointOfInterest lastKnownPOI) {
		this.lastKnownPOI = lastKnownPOI;
	}

	@Relationship(type = HAS_VISITED_RELATION, direction = Relationship.OUTGOING)
	public List<Traveler> visitedTravelers = new ArrayList<>();

}
