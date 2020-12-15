package me.toonamisenpai.startool;


import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class Main extends JavaPlugin implements Listener {

	public List<String> list = new ArrayList<String>();

	@Override

	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(this,this);
	}

	@Override
	public void onDisable() {

	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("startool")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.DARK_RED + "You cannot run this command!");
				return true;
			}
			Player player = (Player) sender;
			if (player.getInventory().firstEmpty() == -1) {
				Location loc = player.getLocation();
				World world = player.getWorld();

				world.dropItemNaturally(loc, getItem());
				player.sendMessage(ChatColor.GOLD + "The Gods of Minecraft dropped a gift near you.");
				return true;
			}
			player.getInventory().addItem(getItem());
			player.sendMessage(ChatColor.GOLD + "The Gods of Minecraft gave you a gift.");
			return true;
		}
		return false;
	}

	public ItemStack getItem() {

		ItemStack item = new ItemStack(Material.TRIDENT);
		ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Ancient Trident");
		List<String> lore = new ArrayList<String>();
		lore.add("");
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7(Right Click) &a&oSpawn minions"));
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7(Left Click) &a&oShoot explosives"));
		meta.setLore(lore);

		meta.addEnchant(Enchantment.LOYALTY, 10, true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

		item.setItemMeta(meta);

		return item;
	}

	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		if (event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.TRIDENT))
			if (event.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasLore()) {
				Player player = (Player) event.getPlayer();
				//Right click
				if (event.getAction() == Action.RIGHT_CLICK_AIR) {
					if (!list.contains(player.getName()))
						list.add(player.getName());
					return;

				}

				//left click
				if (event.getAction() == Action.LEFT_CLICK_AIR) {
					player.launchProjectile(Fireball.class);
					player.launchProjectile(Arrow.class);
				}
			}
		if (list.contains(event.getPlayer().getName())) {
			list.remove(event.getPlayer().getName());
		}
	}
	@EventHandler
	public void onLand(ProjectileHitEvent event) {
		if (event.getEntityType() == EntityType.TRIDENT) {
			if (event.getEntity().getShooter() instanceof Player) {
				Player player = (Player) event.getEntity();
				if (list.contains(player.getName())) {
					//spawn mobs
					Location loc = event.getEntity().getLocation();
					loc.setY((loc.getY()) + 1);

					loc.getWorld().spawnEntity(loc, EntityType.CREEPER);
				}
			}
		}
	}

}