package net.runeduniverse.mc.plugins.traveler.data;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.neo4j.ogm.cypher.ComparisonOperator;
import org.neo4j.ogm.cypher.Filter;
import org.redisson.api.RBucket;

import net.runeduniverse.mc.plugins.snowflake.api.data.IDataAccess;
import net.runeduniverse.mc.plugins.snowflake.api.data.model.ShadowPlayer;
import net.runeduniverse.mc.plugins.snowflake.api.data.player.IPlayerData;
import net.runeduniverse.mc.plugins.snowflake.api.data.player.PlayerDataWrapper;
import net.runeduniverse.mc.plugins.traveler.TravelerMain;
import net.runeduniverse.mc.plugins.traveler.model.Adventurer;
import net.runeduniverse.mc.plugins.traveler.model.PointOfInterest;

public class AdventurerData extends PlayerDataWrapper{

	private Adventurer adventurer;
	private IDataAccess dataAccess;
	
	private PointOfInterest lastKnownPOI = null;
	
	@Override
	public void setDataAccess(IDataAccess dataAccess) {
		this.dataAccess=dataAccess.getDataAccess("traveler");
		super.setDataAccess(dataAccess);
	}
	
	@Override
	public IPlayerData wrap(IPlayerData data) {
		
		// TODO: https://neo4j.com/docs/java-reference/current/java-embedded/query-parameters/
		
		
		Collection<Adventurer> search = TravelerMain.Neo4jSession.qu
		
		if(search.isEmpty()) 
			this.adventurer = new Adventurer();
		else
			this.adventurer = (Adventurer) search.toArray()[0];
		if(this.adventurer == null)
			this.adventurer = null;
		
		return super.wrap(data);
	}
	
	@Override
	public void create() {
		super.create();
	}
	
	@Override
	public void sync() {
		super.sync();
		
		RBucket<UUID> r_lastpoi = this.dataAccess.getBucket("lastKnownPOI");
		this.setLastKnownPOI(TravelerMain.Neo4jSession.load(PointOfInterest.class, r_lastpoi.get(), 2));
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
