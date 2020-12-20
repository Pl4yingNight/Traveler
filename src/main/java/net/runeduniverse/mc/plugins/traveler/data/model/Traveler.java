package net.runeduniverse.mc.plugins.traveler.data.model;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import lombok.Getter;
import lombok.Setter;
import net.runeduniverse.libs.rogm.annotations.Direction;
import net.runeduniverse.libs.rogm.annotations.NodeEntity;
import net.runeduniverse.libs.rogm.annotations.PostLoad;
import net.runeduniverse.libs.rogm.annotations.PostSave;
import net.runeduniverse.libs.rogm.annotations.PreDelete;
import net.runeduniverse.libs.rogm.annotations.Property;
import net.runeduniverse.libs.rogm.annotations.Relationship;
import net.runeduniverse.libs.rogm.annotations.Transient;
import net.runeduniverse.mc.plugins.snowflake.api.data.model.ANodeEntity;
import net.runeduniverse.mc.plugins.snowflake.api.data.model.Location;
import net.runeduniverse.mc.plugins.traveler.data.NamespacedKeys;
import net.runeduniverse.mc.plugins.traveler.services.TravelerService;

@NodeEntity
public class Traveler extends ANodeEntity {

	public static final String LOCATION_RELATION = "LOCATION";
	public static final String HOME_RELATION = "HOME";

	@Getter
	@Property
	private String name;
	@Getter
	@Property
	private String locationName;
	@Getter
	@Property
	private boolean invulnerable;
	@Property
	private boolean canMove;

	@Getter
	@Setter
	@Relationship(label = LOCATION_RELATION, direction = Direction.OUTGOING)
	private Location location;
	@Getter
	@Setter
	@Relationship(label = HOME_RELATION, direction = Direction.OUTGOING)
	private Location home;

	@Getter
	@Transient
	private LivingEntity entity = null;
	@Getter
	@Transient
	private NamespacedKey key = null;
	@Getter
	@Transient
	private ShapelessRecipe fakeRecipe = null;

	@SuppressWarnings("deprecation")
	private void init() {
		this.key = new NamespacedKey("traveler", "loc-" + this.id);
		this.updateFakeRecipe();
	}

	@SuppressWarnings("deprecation")
	private void updateFakeRecipe() {
		ItemStack stack = new ItemStack(Material.FILLED_MAP);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(this.locationName);
		stack.setItemMeta(meta);
		ShapelessRecipe recipe = new ShapelessRecipe(key, stack);
		recipe.addIngredient(new RecipeChoice.ExactChoice(TravelerService.TOKEN));
		this.fakeRecipe = recipe;
		TravelerService.INSTANCE.updateFakeRecipe(this.fakeRecipe);
	}

	@PostLoad
	private void postLoad() {
		TravelerService.INSTANCE.registerTraveler(this);
		this.init();
	}

	@PostSave
	private void postSave() {
		this.init();
	}

	@PreDelete
	private void perDelete() {
		TravelerService.INSTANCE.removeTraveler(this);
	}

	public boolean canMove() {
		return this.canMove;
	}

	public boolean isEntityLoaded() {
		return this.entity != null;
	}

	public void summonEntity() throws NullPointerException {
		org.bukkit.Location loc = this.home.toBukkit();
		if (loc.getWorld() == null)
			throw new NullPointerException("requested World not loaded!");
		WanderingTrader entity = (WanderingTrader) loc.getWorld().spawnEntity(loc, EntityType.WANDERING_TRADER);
		entity.getPersistentDataContainer().set(NamespacedKeys.TRAVELER_ID_KEY, PersistentDataType.LONG, this.id);
		entity.setCanPickupItems(false);
		entity.setRemoveWhenFarAway(false);
		entity.setRecipes(new ArrayList<>());
		this.entity = entity;
	}

	public void setEntity(LivingEntity entity) {
		this.entity = entity;
		if (entity == null)
			return;
		entity.setCustomNameVisible(true);
		entity.setCustomName(ChatColor.DARK_GREEN + this.name);
		entity.setInvulnerable(this.invulnerable);
		entity.setAI(this.canMove);
	}

	public void setName(String name) {
		this.name = name.replace("§", "");
		if (this.entity == null)
			return;
		this.entity.setCustomNameVisible(true);
		this.entity.setCustomName(ChatColor.DARK_GREEN + this.name);
	}

	public void setLocationName(String name) {
		this.locationName = name.replace("§", "");
		this.updateFakeRecipe();
	}

	public void setInvulnerable(boolean invulnerable) {
		this.invulnerable = invulnerable;
		if (this.entity == null)
			return;
		this.entity.setInvulnerable(this.invulnerable);
	}

	public void setCanMove(boolean canMove) {
		this.canMove = canMove;
		if (this.entity == null)
			return;
		this.entity.setAI(this.canMove);
	}
}
