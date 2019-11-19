package net.alphadragon.plugins.traveler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class Options implements Serializable{
	
	private static Options options = null;
	
	public static Options getOptions() {
		return options;
	}
	
	public static Boolean load() {
		
		File optionsFile = new File("traveler-data"+System.getProperty("file.separator")+"traveler.json");
		
		if(!optionsFile.exists())
			options = new Options();
		else
			try {
				options = new Gson().fromJson(new FileReader(optionsFile), Options.class);
			} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
				e.printStackTrace();
				return false;
			}
		
		return true;
	}
	
	public static Boolean save() {
		
		new File("traveler-data").mkdirs();
		
			try {
				FileWriter writer = new FileWriter("traveler-data"+System.getProperty("file.separator")+"traveler.json");
				new Gson().toJson(options, writer);
				writer.close();
			} catch (JsonIOException | IOException e) {
				e.printStackTrace();
				return false;
			}
		
		return true;
	}
	
	// variables
	private Map<Long, MapEntry> map = new HashMap<Long, MapEntry>();
	
	// set/get
	public MapEntry addMapEntry(MapEntry entry) {
		Long id;
		if(map.isEmpty())
			id = 0l;
		else
			id = Collections.max(map.keySet())+1;
		
		entry.setID(id);
		this.map.put(entry.getID(), entry);
		return entry;
	}
	public MapEntry getMapEntry(Long id) {
		if(!this.map.containsKey(id))
			return null;
		return this.map.get(id);
	}
	public Map<Long, MapEntry> getMapEntries() {
		return map;
	}
	public void removeMapEntry(Long id) {
		this.map.remove(id);
	}
	
	
}
