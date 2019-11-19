package net.runeduniverse.mc.plugins.traveler.sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import net.runeduniverse.mc.plugins.traveler.model.Location;

public class DataManager {

	private static Connection con;
	private static boolean hasData = false;
	
	public static void initialize() throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");
		con=DriverManager.getConnection("jdbc:sqlite:Traveler.db");
		
		Statement statement = con.createStatement();
		ResultSet rs = statement.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='traveler'");
		ResultSet rs = statement.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='loc_token'");
		ResultSet rs = statement.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='traveler'");
	}
	
	public static Set<Location> getTravelersFromUser(UUID uuid) throws SQLException{
		Statement statement = con.createStatement();
		ResultSet rs = statement.executeQuery("SELECT t.id, l.id, type, name, world, x, y, z FROM Traveler t join LocationToken l ON t.loc_token_id = l.id");
		Set<Location> vlocs = new HashSet<Location>();
		
		while (rs.next()) {
			Location loc = new Location();
            loc.travelerID = rs.getLong(0);
            loc.locationTokenID = rs.getLong(1);
            loc.type = rs.getString(2);
            loc.name = rs.getString(3);
            loc.world = rs.getString(4);
            loc.x = rs.getDouble(5);
            loc.y = rs.getDouble(6);
            loc.z = rs.getDouble(7);
            vlocs.add(loc);
        }
		return vlocs;
	}
	
	
}
