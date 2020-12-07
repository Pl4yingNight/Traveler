package net.runeduniverse.mc.plugins.traveler.data.model;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.runeduniverse.libs.rogm.annotations.Direction;
import net.runeduniverse.libs.rogm.annotations.Relationship;
import net.runeduniverse.mc.plugins.snowflake.api.data.model.AShadowPlayerExtension;
import net.runeduniverse.mc.plugins.snowflake.api.data.model.ShadowPlayer;

@Getter
@NoArgsConstructor
public class Adventurer extends AShadowPlayerExtension {
	public static final String LAST_SEEN_TRAVELER_RELATION = "LAST_SEEN";
	public static final String HAS_VISITED_RELATION = "HAS_VISITED";

	@Relationship(label = LAST_SEEN_TRAVELER_RELATION, direction = Direction.OUTGOING)
	@Setter
	private Traveler last = null;

	@Relationship(label = HAS_VISITED_RELATION, direction = Direction.OUTGOING)
	private Set<Traveler> travelers = new HashSet<>();

	public Adventurer(ShadowPlayer shadowPlayer) {
		super(shadowPlayer);
	}

	// SETTER
	public boolean addTraveler(Traveler traveler) {
		return this.travelers.add(traveler);
	}

	public void removeTraveler(Traveler traveler) {
		this.travelers.remove(traveler);
	}
}
