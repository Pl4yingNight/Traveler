package net.runeduniverse.mc.plugins.traveler.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

import net.runeduniverse.mc.plugins.traveler.TravelerMain;

public class RequestTravelPacketListener extends PacketAdapter {

	TravelerMain main;

	public RequestTravelPacketListener(TravelerMain main) {
		super(main, ListenerPriority.HIGH, PacketType.Play.Client.AUTO_RECIPE);
		this.main = main;
	}

	@Override
	public void onPacketReceiving(PacketEvent event) {
		if (!event.getPacketType().equals(PacketType.Play.Client.AUTO_RECIPE))
			super.onPacketReceiving(event);
		event.setCancelled(true);

	}

}
