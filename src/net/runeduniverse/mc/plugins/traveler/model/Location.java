package net.runeduniverse.mc.plugins.traveler.model;

import java.util.UUID;

public class Location {

	public Long travelerID;
	public Long locationTokenID;
	
	public UUID owner;
	public String type;
	public String name;
	public String world;
	public double x;
	public double y;
	public double z;
}
