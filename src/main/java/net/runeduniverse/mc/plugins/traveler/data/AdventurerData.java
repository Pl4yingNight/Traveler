package net.runeduniverse.mc.plugins.traveler.data;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RBucket;

import net.runeduniverse.mc.plugins.snowflake.api.data.IDataAccess;
import net.runeduniverse.mc.plugins.snowflake.api.data.player.IPlayerData;
import net.runeduniverse.mc.plugins.snowflake.api.data.player.PlayerDataWrapper;
import net.runeduniverse.mc.plugins.traveler.TravelerMain;
import net.runeduniverse.mc.plugins.traveler.model.Adventurer;
import net.runeduniverse.mc.plugins.traveler.model.PointOfInterest;

public class AdventurerData extends PlayerDataWrapper {

	private Adventurer adventurer;
	private IDataAccess dataAccess;

	private PointOfInterest lastKnownPOI = null;

	@Override
	public void setDataAccess(IDataAccess dataAccess) {
		this.dataAccess = dataAccess.getDataAccess("traveler");
		super.setDataAccess(dataAccess);
	}

	@Override
	public IPlayerData wrap(IPlayerData data) {
		String query = "MATCH (a:Adventurer) -[" + Adventurer.PLAYER_RELATION + "]-> (:ShadowPlayer{uuid: "
				+ data.getUUID() + "}) RETURN a;";
		this.adventurer = TravelerMain.travelerSession.queryForObject(Adventurer.class, query,
				new HashMap<String, Object>());

		if (this.adventurer == null)
			this.adventurer = new Adventurer();
		return super.wrap(data);
	}
	
	@Override
	public void save() {
		TravelerMain.travelerSession.save(this.adventurer, 6);
		super.save();
	}

	@Override
	public void sync() {
		super.sync();

		RBucket<Long> r_lastpoi = this.dataAccess.getBucket("lastKnownPOI");
		this.setLastKnownPOI(TravelerMain.travelerSession.load(PointOfInterest.class, r_lastpoi.get(), 2));
	}

	@Override
	public void expire(long ttl, TimeUnit unit) {
		super.expire(ttl, unit);
		this.dataAccess.getBucket("lastKnownPOI").expire(ttl, unit);
	}

	@Override
	public void expireAsync(long ttl, TimeUnit unit) {
		super.expireAsync(ttl, unit);
		this.dataAccess.getBucket("lastKnownPOI").expireAsync(ttl, unit);
	}

	// GETTER
	public PointOfInterest getLastKnownPOI() {
		return lastKnownPOI;
	}

	// SETTER
	public void setLastKnownPOI(PointOfInterest lastKnownPOI) {
		this.lastKnownPOI = lastKnownPOI;
		this.dataAccess.getBucket("lastKnownPOI").setAsync(this.lastKnownPOI.getID());
	}
}
