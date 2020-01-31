package net.runeduniverse.mc.plugins.traveler.model;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.typeconversion.Convert;
import org.neo4j.ogm.typeconversion.EnumStringConverter;

import net.runeduniverse.mc.plugins.snowflake.api.data.model.ANodeEntity;
import net.runeduniverse.mc.plugins.snowflake.api.data.model.Location;

@NodeEntity(label = "POI")
public class PointOfInterest extends ANodeEntity {

	private Boolean isPublic;
	@Convert(EnumStringConverter.class)
	private POIType type = POIType.NORMAL;
	private Location location;
}
