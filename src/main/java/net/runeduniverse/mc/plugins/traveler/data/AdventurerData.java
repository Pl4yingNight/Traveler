package net.runeduniverse.mc.plugins.traveler.data;

import org.redisson.api.RBucket;

import net.runeduniverse.libs.rogm.Session;
import net.runeduniverse.mc.plugins.snowflake.api.data.IDataAccess;
import net.runeduniverse.mc.plugins.snowflake.api.data.player.IPlayerData;
import net.runeduniverse.mc.plugins.snowflake.api.services.IPlayerService;
import net.runeduniverse.mc.plugins.snowflake.api.data.player.APlayerDataWrapper;
import net.runeduniverse.mc.plugins.traveler.model.Adventurer;
import net.runeduniverse.mc.plugins.traveler.model.Traveler;

public class AdventurerData extends APlayerDataWrapper {

	private static final String LAST_SEEN_TRAVELER_KEY = "lastSeenTraveler";

	private Adventurer adventurer = null;
	private IDataAccess dataAccess = null;
	private IPlayerService service = null;
	private Session session = null;

	@Override
	public void setDataAccess(IDataAccess dataAccess) {
		this.dataAccess = dataAccess.getDataAccess("traveler");
		super.setDataAccess(dataAccess);
	}

	@Override
	public IPlayerData wrap(IPlayerData data) {
		super.wrap(data);
		this.service = data.getPlayerService();
		this.session = this.service.getNeo4jModule().getSession();
		this.adventurer = loadExtension(Adventurer.class, 4);
		if (this.adventurer == null)
			this.adventurer = new Adventurer();
		return this;
	}

	@Override
	public void save() {
		this.session.save(this.adventurer, 6);
		super.save();
	}

	@Override
	public void sync() {
		super.sync();

		RBucket<Long> r_lastseen = this.dataAccess.getBucket(LAST_SEEN_TRAVELER_KEY);
		this.setLastSeen(this.session.load(Traveler.class, r_lastseen.get(), 3));
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
