package net.runeduniverse.mc.plugins.traveler.model;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.Convert;
import org.neo4j.ogm.typeconversion.EnumStringConverter;

import net.runeduniverse.mc.plugins.snowflake.api.data.model.ANodeEntity;
import net.runeduniverse.mc.plugins.snowflake.api.data.model.Location;

@NodeEntity(label = "POI")
public class PointOfInterest extends ANodeEntity {
	
	public static final String LOCATION_RELATION = "LOCATION";

	@Relationship(type = LOCATION_RELATION, direction = Relationship.OUTGOING)
	private Location location;

	private Boolean isPublic;
	@Convert(EnumStringConverter.class)
	private POIType type = POIType.NORMAL;
	
	// GETTER
	
	public Boolean isPublic() {
		return isPublic;
	}
	public Location getLocation() {
		return location;
	}
	public POIType getType() {
		return this.type;
	}
	
	// SETTER
	
	public void setPublic(Boolean isPublic) {
		this.isPublic = isPublic;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public void setType(POIType type) {
		this.type = type;
	}
}
