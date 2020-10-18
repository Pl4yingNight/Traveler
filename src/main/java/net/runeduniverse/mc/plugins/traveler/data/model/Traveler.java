package net.runeduniverse.mc.plugins.traveler.data.model;

import lombok.Getter;
import lombok.Setter;
import net.runeduniverse.libs.rogm.annotations.Direction;
import net.runeduniverse.libs.rogm.annotations.NodeEntity;
import net.runeduniverse.libs.rogm.annotations.PostLoad;
import net.runeduniverse.libs.rogm.annotations.PreDelete;
import net.runeduniverse.libs.rogm.annotations.Property;
import net.runeduniverse.libs.rogm.annotations.Relationship;
import net.runeduniverse.mc.plugins.snowflake.api.data.model.ANodeEntity;
import net.runeduniverse.mc.plugins.snowflake.api.data.model.Location;
import net.runeduniverse.mc.plugins.traveler.services.TravelerService;

@NodeEntity
@Getter
@Setter
public class Traveler extends ANodeEntity{

	public static final String LOCATION_RELATION = "LOCATION";
	public static final String HOME_RELATION = "HOME";
	
	@Property
	private String name;
	
	@Relationship(label = LOCATION_RELATION, direction = Direction.OUTGOING)
	private Location location;
	
	@Relationship(label = HOME_RELATION, direction = Direction.OUTGOING)
	private Location home;
	
	private boolean invulnerable;
	private boolean canMove;
	
	@PostLoad
	private void postLoad() {
		TravelerService.INSTANCE.registerTraveler(this);
	}
	
	@PreDelete
	private void perDelete() {
		TravelerService.INSTANCE.removeTraveler(this);
	}
}
