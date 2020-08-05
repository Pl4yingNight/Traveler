package net.runeduniverse.mc.plugins.traveler.model;

import lombok.Getter;
import lombok.Setter;
import net.runeduniverse.libs.rogm.annotations.Direction;
import net.runeduniverse.libs.rogm.annotations.NodeEntity;
import net.runeduniverse.libs.rogm.annotations.Relationship;
import net.runeduniverse.mc.plugins.snowflake.api.data.model.ANodeEntity;
import net.runeduniverse.mc.plugins.snowflake.api.data.model.storage.Location;

@NodeEntity
@Getter
@Setter
public class Traveler extends ANodeEntity{

	public static final String LOACTION_RELATION = "POI";
	public static final String HOME_RELATION = "HOME";
	
	@Relationship(label = LOACTION_RELATION, direction = Direction.OUTGOING)
	private Location location;
	
	@Relationship(label = HOME_RELATION, direction = Direction.OUTGOING)
	private Location home;
	
	private boolean invulnerable;
	private boolean canMove;

	private Boolean publiclyAccessible;
	private POIType type = POIType.NORMAL;
}
