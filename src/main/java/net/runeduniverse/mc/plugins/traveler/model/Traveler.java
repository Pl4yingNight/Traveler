package net.runeduniverse.mc.plugins.traveler.model;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import net.runeduniverse.mc.plugins.snowflake.api.data.model.ANodeEntity;

@NodeEntity
public class Traveler extends ANodeEntity{

	public static final String POI_RELATION = "POI";
	
	@Relationship(type = POI_RELATION, direction = Relationship.OUTGOING)
	private PointOfInterest poi;
	
}
