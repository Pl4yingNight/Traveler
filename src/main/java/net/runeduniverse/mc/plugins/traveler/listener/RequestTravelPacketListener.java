package net.runeduniverse.mc.plugins.traveler.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.MinecraftKey;

import net.runeduniverse.mc.plugins.traveler.TravelerMain;
import net.runeduniverse.mc.plugins.traveler.services.TravelerService;

import static net.runeduniverse.libs.utils.CollectionUtils.firstNotNull;

import org.bukkit.NamespacedKey;

public class RequestTravelPacketListener extends PacketAdapter {

	TravelerMain main;
	TravelerService service;

	public RequestTravelPacketListener(TravelerMain main) {
		super(main, ListenerPriority.HIGH, PacketType.Play.Client.AUTO_RECIPE);
		this.main = main;
		this.service = this.main.getTravelerService();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onPacketReceiving(PacketEvent event) {
		if (!event.getPacketType().equals(PacketType.Play.Client.AUTO_RECIPE))
			super.onPacketReceiving(event);

		MinecraftKey key = firstNotNull(event.getPacket().getMinecraftKeys().getValues());
		if (key == null || !key.getFullKey().startsWith("traveler:loc-"))
			return;

		event.setCancelled(true);
		this.service.teleport(event.getPlayer(), new NamespacedKey(key.getPrefix(), key.getKey()));
	}

}
