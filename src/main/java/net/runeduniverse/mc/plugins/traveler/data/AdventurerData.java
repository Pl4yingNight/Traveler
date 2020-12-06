package net.runeduniverse.mc.plugins.traveler.data;

import org.redisson.api.RBucket;

import lombok.Getter;
import net.runeduniverse.libs.rogm.Session;
import net.runeduniverse.mc.plugins.snowflake.api.data.IDataAccess;
import net.runeduniverse.mc.plugins.snowflake.api.data.player.IPlayerData;
import net.runeduniverse.mc.plugins.snowflake.api.services.IPlayerService;
import net.runeduniverse.mc.plugins.traveler.data.model.Adventurer;
import net.runeduniverse.mc.plugins.traveler.data.model.Traveler;
import net.runeduniverse.mc.plugins.traveler.services.AdventureService;
import net.runeduniverse.mc.plugins.snowflake.api.data.player.APlayerDataWrapper;

public class AdventurerData extends APlayerDataWrapper {

	private static final String LAST_SEEN_TRAVELER_KEY = "lastSeenTraveler";

	@Getter
	private Adventurer adventurer = null;
	private IDataAccess dataAccess = null;
	private IPlayerService service = null;
	private Session session = null;

	@Override
	public void setDataAccess(IDataAccess dataAccess) {
		super.setDataAccess(dataAccess);
		this.dataAccess = dataAccess.getDataAccess("traveler");
	}

	@Override
	public IPlayerData wrap(IPlayerData data) {
		super.wrap(data);
		this.service = data.getPlayerService();
		this.session = this.service.getNeo4jModule().getSession();
		return this;
	}

	@Override
	public void save() {
		super.save();
		this.session.save(this.adventurer);
	}

	@Override
	public void sync() {
		super.sync();

		RBucket<Long> r_lastseen = this.dataAccess.getBucket(LAST_SEEN_TRAVELER_KEY);
		if (r_lastseen.isExists())
			this.setLastSeen(this.session.load(Traveler.class, r_lastseen.get(), 3));
	}

	@SuppressWarnings("deprecation")
	@Override
	public void deepSync() {
		super.deepSync();
		this.adventurer = loadExtension(Adventurer.class, 4);
		if (this.adventurer == null)
			this.adventurer = new Adventurer();
		AdventureService.INSTANCE.loadedAdventurerData.put(this.getUUID(), this);
	}

	@Override
	public void expire() {
		super.expire();
		this.service.expireBucket(this.dataAccess.getBucket(LAST_SEEN_TRAVELER_KEY));
	}

	@Override
	public void expireAsync() {
		super.expireAsync();
		this.service.expireBucketAsync(this.dataAccess.getBucket(LAST_SEEN_TRAVELER_KEY));
	}

	// SETTER
	public void setLastSeen(Traveler traveler) {
		this.adventurer.setLast(traveler);
		this.dataAccess.getBucket(LAST_SEEN_TRAVELER_KEY).setAsync(traveler.getId());
	}
}
