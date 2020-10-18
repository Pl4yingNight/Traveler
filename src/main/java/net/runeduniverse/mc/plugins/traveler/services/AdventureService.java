package net.runeduniverse.mc.plugins.traveler.services;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import lombok.Getter;
import net.runeduniverse.mc.plugins.snowflake.api.Snowflake;
import net.runeduniverse.mc.plugins.snowflake.api.services.IService;
import net.runeduniverse.mc.plugins.snowflake.api.services.modules.INeo4jModule;
import net.runeduniverse.mc.plugins.traveler.TravelerMain;
import net.runeduniverse.mc.plugins.traveler.data.AdventurerData;

public class AdventureService implements IService {
	public static AdventureService INSTANCE;

	@Getter
	private Snowflake snowflake;
	private TravelerMain main;

	private INeo4jModule neo4jModule;
	public final Map<UUID, AdventurerData> loadedAdventurerData = new HashMap<>();

	public AdventureService(Snowflake snowflake, TravelerMain main) {
		this.snowflake = snowflake;
		this.main = main;
	}

	@Override
	public void prepare() {
	}

	public AdventurerData getAdventurerData(UUID uuid) {
		return this.loadedAdventurerData.get(uuid);
	}
}
