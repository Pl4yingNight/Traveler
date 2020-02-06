package net.runeduniverse.mc.plugins.traveler.model;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import net.runeduniverse.mc.plugins.snowflake.api.data.model.ANodeEntity;
import net.runeduniverse.mc.plugins.snowflake.api.data.model.Location;

@NodeEntity
public class Traveler extends ANodeEntity{

	public static final String POI_RELATION = "POI";
	public static final String LOCATION_RELATION = "LOCATION";
	
	@Relationship(type = POI_RELATION, direction = Relationship.OUTGOING)
	private PointOfInterest poi;
	
	@Relationship(type = LOCATION_RELATION, direction = Relationship.OUTGOING)
	private Location location;
	
	private boolean invulnerable;
	private boolean canMove;
	
	// GETTER
	
	public boolean isInvulnerable() {
		return invulnerable;
	}
	public boolean canMove() {
		return canMove;
	}
	public PointOfInterest getPointOfInterest() {
		return this.poi;
	}
	public Location getLocation() {
		return this.location;
	}
	
	// SETTER
	
	public void setInvulnerable(boolean invulnerable) {
		this.invulnerable = invulnerable;
	}
	public void setCanMove(boolean canMove) {
		this.canMove = canMove;
	}
	public void setPointOfInterest(PointOfInterest poi) {
		this.poi = poi;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	
}
