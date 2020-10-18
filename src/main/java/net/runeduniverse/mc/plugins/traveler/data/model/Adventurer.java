package net.runeduniverse.mc.plugins.traveler.data.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import net.runeduniverse.libs.rogm.annotations.Direction;
import net.runeduniverse.libs.rogm.annotations.NodeEntity;
import net.runeduniverse.libs.rogm.annotations.Relationship;
import net.runeduniverse.mc.plugins.snowflake.api.data.model.AShadowPlayerExtension;

@NodeEntity
@Getter
public class Adventurer extends AShadowPlayerExtension {
	public static final String LAST_SEEN_TRAVELER_RELATION = "LAST_SEEN";
	public static final String HAS_VISITED_RELATION = "HAS_VISITED";

	@Relationship(label = LAST_SEEN_TRAVELER_RELATION, direction = Direction.OUTGOING)
	@Setter
	private Traveler last = null;

	@Relationship(label = HAS_VISITED_RELATION, direction = Direction.OUTGOING)
	private List<Traveler> travelers = new ArrayList<>();

	// SETTER
	public void addTraveler(Traveler traveler) {
		this.travelers.add(traveler);
	}

	

}
