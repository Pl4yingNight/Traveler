package net.alphadragon.plugins.traveler;

import java.io.Serializable;

public class MapEntry implements Serializable{
	
	private Long id;
	private String name;
	private String world;
	private Double[] location = new Double[3];
	
	public Long getID(){
		return id;
	}
	public void setID(Long id) {
		this.id = id;
	}

	public String getName(){
		return name;
	}
	public MapEntry setName(String name) {
		this.name = name;
		return this;
	}
	
	public String getWorld(){
		return world;
	}
	public MapEntry setWorld(String world) {
		this.world = world;
		return this;
	}
	
	public Double getX(){
		return location[0];
	}
	public MapEntry setX(Double x) {
		this.location[0] = x;
		return this;
	}
	
	public Double getY(){
		return location[1];
	}
	public MapEntry setY(Double y) {
		this.location[1] = y;
		return this;
	}
	
	public Double getZ(){
		return location[2];
	}
	public MapEntry setZ(Double z) {
		this.location[2] = z;
		return this;
	}
}
