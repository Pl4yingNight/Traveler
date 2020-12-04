package net.runeduniverse.mc.plugins.traveler.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.annotation.command.Command;
import org.bukkit.plugin.java.annotation.command.Commands;

import lombok.RequiredArgsConstructor;
import net.runeduniverse.mc.plugins.snowflake.api.data.model.Location;
import net.runeduniverse.mc.plugins.snowflake.api.data.model.ShadowPlayer;
import net.runeduniverse.mc.plugins.snowflake.api.services.IPlayerService;
import net.runeduniverse.mc.plugins.snowflake.api.services.IStorageService;
import net.runeduniverse.mc.plugins.traveler.data.model.Traveler;
import net.runeduniverse.mc.plugins.traveler.services.TravelerService;
import net.runeduniverse.mc.plugins.traveler.services.TravelerService.Info;

@Commands(@Command(name = "tnpc", usage = "/tnpc <Traveler ID> <options>"))
@RequiredArgsConstructor
public class TravelerCommandExecutor implements CommandExecutor {

	private static final double PLAYER_TO_TRAVELER_DISTANCE = 250d;
	private static final double HOME_TO_DESTINATION_DISTANCE = 50d;

	private final TravelerService travelerService;
	private final IStorageService storageService;
	private final IPlayerService playerService;

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
		System.out.println("command captured!");
		System.out.println(sender);
		System.out.println(command);
		System.out.println(label);
		System.out.println(String.join(", ", args));
		if (label != "tnpc" || args.length < 1)
			return false;
		System.out.println("prim check");

		Player player = null;
		ShadowPlayer shadow = null;
		if (sender instanceof Player) {
			player = (Player) sender;
			shadow = this.playerService.getData(player).getShadow();
			System.out.println("is Player");
		}

		Traveler traveler = null;
		if (player != null && player.isOp() && args[0] == "summon") {
			System.out.println("summon");
			traveler = this.travelerService.createTraveler();
			Location loc = this.storageService.convert(player.getLocation());
			traveler.setHome(loc);
			traveler.setLocation(loc);
			this.travelerService.saveTraveler(traveler);
			sender.sendMessage("Traveler summoned! ID: " + traveler.getId());
			return true;
		}

		Long id = null;
		try {
			id = Long.parseLong(args[0]);
		} catch (NumberFormatException e) {
			sender.sendMessage(error("The value <" + args[0] + "> is not a valid number!"));
			return true;
		}

		traveler = this.travelerService.loadTraveler(id);
		if (traveler == null) {
			sender.sendMessage(error("Invalid Traveler ID <" + id
					+ "> received!\nThe Traveler ID can be optained through a journal or by searching the database provided by Snowflake!"));
			return true;
		}

		if (player != null) {
			if (!storageService.contains(traveler.getHome())) {
				sender.sendMessage(error("Traveler is not on your server!"));
				return true;
			}
			if (!traveler.getHome().inRRage(shadow.getLocation(), PLAYER_TO_TRAVELER_DISTANCE)) {
				sender.sendMessage(error("You are out of range! (" + PLAYER_TO_TRAVELER_DISTANCE + " Blocks)"));
				return true;
			}
		}

		Info info = this.travelerService.getInfo(traveler);

		if (args.length < 2) {
			// tnpc <id> => shows info
			sender.sendMessage(info.full());
			return true;
		}

		switch (args[1]) {
		// tnpc <id> name => show name
		// tnpc <id> name set => open $Anvil-Rename-Inv
		// tnpc <id> name set <name> => rename with given name
		case "name":
			if (args.length < 3) {
				sender.sendMessage(info.name());
				return true;
			}
			switch (args[2]) {
			case "set":
				if (args.length < 4) {
					// TODO open $Anvil-Rename-Inv
					return true;
				}

				// TODO set name args[3]
				return true;
			}
			break;
		// tnpc <id> destname => show name
		// tnpc <id> destname set => open $Anvil-Rename-Inv
		// tnpc <id> destname set <name> => rename with given name
		case "destname":
			if (args.length < 3) {
				sender.sendMessage(info.destname());
				return true;
			}
			switch (args[2]) {
			case "set":
				if (args.length < 4) {
					// TODO open $Anvil-Rename-Inv
					return true;
				}

				traveler.setName(args[3]);
				this.travelerService.saveTraveler(traveler);
				return true;
			}
			break;
		// tnpc <id> visibility => show visibility
		// tnpc <id> visibility toggle => toggles visibility
		// tnpc <id> visibility set <public/private>
		case "visibility":
			if (args.length < 3) {
				sender.sendMessage(info.visibility());
				return true;
			}
			switch (args[2]) {
			case "toggle":
				// TODO toggle visibility
				return true;
			case "set":
				if (args.length < 4)
					return false;

				switch (args[3]) {
				case "public":
					// TODO set visibility public
					return true;
				case "private":
					// TODO set visibility private
					return true;
				}
			}
			break;
		// tnpc <id> movement => show movement
		// tnpc <id> movement toggle => toggles movement
		// tnpc <id> movement set <yes/no>
		case "movement":
			if (args.length < 3) {
				sender.sendMessage(info.visibility());
				return true;
			}
			switch (args[2]) {
			case "toggle":
				traveler.setCanMove(!traveler.canMove());
				this.travelerService.saveTraveler(traveler);
				return true;
			case "set":
				if (args.length < 4)
					return false;

				switch (args[3]) {
				case "yes":
					traveler.setCanMove(true);
					this.travelerService.saveTraveler(traveler);
					return true;
				case "no":
					traveler.setCanMove(false);
					this.travelerService.saveTraveler(traveler);
					return true;
				}
			}
			break;
		// tnpc <id> invulnerable => show invulnerable
		// tnpc <id> invulnerable toggle => toggles invulnerable
		// tnpc <id> invulnerable set <yes/no>
		case "invulnerable":
			if (args.length < 3) {
				sender.sendMessage(info.invulnerable());
				return true;
			}
			switch (args[2]) {
			case "toggle":
				traveler.setInvulnerable(!traveler.isInvulnerable());
				this.travelerService.saveTraveler(traveler);
				return true;
			case "set":
				if (args.length < 4)
					return false;

				switch (args[3]) {
				case "yes":
					traveler.setInvulnerable(true);
					this.travelerService.saveTraveler(traveler);
					return true;
				case "no":
					traveler.setInvulnerable(false);
					this.travelerService.saveTraveler(traveler);
					return true;
				}
			}
			break;
		// tnpc <id> home => show home
		// tnpc <id> home set => open $Chest-Selection-Inv
		// tnpc <id> home set x y z => sets home to the given position (must be op)
		case "home":
			if (args.length < 3) {
				sender.sendMessage(info.home());
				return true;
			}
			switch (args[2]) {
			case "set":
				if (args.length < 6) {
					// TODO open $Chest-Selection-Inv
					return true;
				}

				Location loc = new Location();
				traveler.getHome().copyTo(loc);

				try {
					loc.setX(parseCoordinate(player, args[3]));
					loc.setY(parseCoordinate(player, args[4]));
					loc.setZ(parseCoordinate(player, args[5]));
				} catch (NumberFormatException e) {
					sender.sendMessage(error("The value <" + args[0] + "> is not a valid number!"));
					return true;
				} catch (NullPointerException e) {
					sender.sendMessage(error("The <sender> must be a player to be able to use <~> as alias!"));
					return true;
				}

				if (!loc.getWorld().getWorld().getWorldBorder().isInside(
						new org.bukkit.Location(loc.getWorld().getWorld(), loc.getX(), loc.getY(), loc.getZ()))) {
					sender.sendMessage(error("Location outside of the WorldBorder!"));
					return true;
				}

				if (!loc.inRRage(traveler.getLocation(), HOME_TO_DESTINATION_DISTANCE)) {
					sender.sendMessage(
							error("Destination out of range! (" + HOME_TO_DESTINATION_DISTANCE + " Blocks)"));
					return true;
				}

				traveler.setHome(loc);
				this.travelerService.saveTraveler(traveler);
				return true;
			}
			break;
		// tnpc <id> dest => show destination
		// tnpc <id> dest set => sets destination to the player's current position
		// tnpc <id> dest set x y z => sets destination to the given position (must be
		// op)
		case "dest":
			if (args.length < 3) {
				sender.sendMessage(info.destination());
				return true;
			}
			switch (args[2]) {
			case "set":
				if (args.length < 6) {
					// TODO open $Chest-Selection-Inv
					return true;
				}

				Location loc = new Location();
				traveler.getLocation().copyTo(loc);

				try {
					loc.setX(parseCoordinate(player, args[3]));
					loc.setY(parseCoordinate(player, args[4]));
					loc.setZ(parseCoordinate(player, args[5]));
				} catch (NumberFormatException e) {
					sender.sendMessage(error("The value <" + args[0] + "> is not a valid number!"));
					return true;
				} catch (NullPointerException e) {
					sender.sendMessage(error("The <sender> must be a player to be able to use <~> as alias!"));
					return true;
				}

				if (!loc.getWorld().getWorld().getWorldBorder().isInside(
						new org.bukkit.Location(loc.getWorld().getWorld(), loc.getX(), loc.getY(), loc.getZ()))) {
					sender.sendMessage(error("Location outside of the WorldBorder!"));
					return true;
				}

				if (!loc.inRRage(traveler.getHome(), HOME_TO_DESTINATION_DISTANCE)) {
					sender.sendMessage("Home out of range -> synced to new Destination!");
					traveler.setHome(loc);
				}
				traveler.setLocation(loc);
				this.travelerService.saveTraveler(traveler);
				return true;
			}
			break;
		// tnpc <id> owner => show owner
		// tnpc <id> owner set <new owner> => set owner with given new owner
		// tnpc <id> owner add <new owner> => add given new owner
		// tnpc <id> owner del <new owner> => remove given new owner
		case "owner":
			if (args.length < 3) {
				sender.sendMessage(info.owner());
				return true;
			}
			switch (args[2]) {
			case "set":
				if (args.length < 4)
					return false;

				// TODO set owner args[3]
				return true;
			case "add":
				if (args.length < 4)
					return false;

				// TODO add owner args[3]
				return true;
			case "del":
				if (args.length < 4)
					return false;

				// TODO delete owner args[3]
				return true;
			}
			break;
		// tnpc <id> sysowner => set owner to system (must be op)
		case "sysowner":
			if (sender.isOp()) {
				// TODO set owner to ENV
				return true;
			}
		}
		return false;

	}

	private String error(String msg) {
		return ChatColor.RED + "[ERROR] " + ChatColor.RESET + msg;
	}

	private Double parseCoordinate(Player player, String c) throws NumberFormatException, NullPointerException {
		if (c == "~")
			return player.getLocation().getX();
		return Double.parseDouble(c);
	}

}
