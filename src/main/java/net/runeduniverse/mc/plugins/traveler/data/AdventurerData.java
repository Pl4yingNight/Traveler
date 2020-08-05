package net.runeduniverse.mc.plugins.traveler.data;

import java.util.concurrent.TimeUnit;

import org.redisson.api.RBucket;

import net.runeduniverse.mc.plugins.snowflake.api.data.IDataAccess;
import net.runeduniverse.mc.plugins.snowflake.api.data.player.IPlayerData;
import net.runeduniverse.mc.plugins.snowflake.api.data.player.PlayerDataWrapper;
import net.runeduniverse.mc.plugins.traveler.TravelerMain;
import net.runeduniverse.mc.plugins.traveler.model.Adventurer;
import net.runeduniverse.mc.plugins.traveler.model.Traveler;

public class AdventurerData extends PlayerDataWrapper {

	private static final String LAST_SEEN_TRAVELER_KEY = "lastSeenTraveler";
	
	private Adventurer adventurer = null;
	private IDataAccess dataAccess = null;

	@Override
	public void setDataAccess(IDataAccess dataAccess) {
		this.dataAccess = dataAccess.getDataAccess("traveler");
		super.setDataAccess(dataAccess);
	}

	@Override
	public IPlayerData wrap(IPlayerData data) {
		super.wrap(data);
		this.adventurer = Adventurer.load(getDataManager().getNeo4jSession(), Adventurer.class, getUUID(), 4);
		if (this.adventurer == null)
			this.adventurer = new Adventurer();
		return this;
	}

	@Override
	public void save() {
		getDataManager().getNeo4jSession().save(this.adventurer, 6);
		super.save();
	}

	@Override
	public void sync() {
		super.sync();

		RBucket<Long> r_lastseen = this.dataAccess.getBucket(LAST_SEEN_TRAVELER_KEY);
		this.setLastSeen(TravelerMain.travelerSession.load(Traveler.class, r_lastseen.get(), 3));
	}

	@Override
	public void expire(long ttl, TimeUnit unit) {
		super.expire(ttl, unit);
		this.dataAccess.getBucket(LAST_SEEN_TRAVELER_KEY).expire(ttl, unit);
	}

	@Override
	public void expireAsync(long ttl, TimeUnit unit) {
		super.expireAsync(ttl, unit);
		this.dataAccess.getBucket(LAST_SEEN_TRAVELER_KEY).expireAsync(ttl, unit);
	}

	// SETTER
	public void setLastSeen(Traveler traveler) {
		this.adventurer.setLast(traveler);
		this.dataAccess.getBucket(LAST_SEEN_TRAVELER_KEY).setAsync(traveler.getId());
	}
}
