package com.wolvencraft.yasp.Database.data.detailed;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.wolvencraft.yasp.Database.CachedData;
import com.wolvencraft.yasp.Database.tables.detailed.ItemsPickedUpTable;
import com.wolvencraft.yasp.Utils.Util;

public class ItemPickedUp implements DetailedDataHolder {
	
	private boolean onHold = false;
	
	public ItemPickedUp(Player player, ItemStack item) {
		this.playerName = player.getPlayerListName();
		this.item = item;
		this.location = player.getLocation();
		this.timestamp = Util.getCurrentTime().getTime();
	}
	
	private String playerName;
	private ItemStack item;
	private Location location;
	private long timestamp;

	@Override
	public String getQuery() {
		if(onHold) return null;
		String query = "INSERT INTO " + ItemsPickedUpTable.TableName + " (" + ItemsPickedUpTable.MaterialId + ", " + ItemsPickedUpTable.PlayerId + ", " + ItemsPickedUpTable.World + ", " + ItemsPickedUpTable.XCoord + ", " + ItemsPickedUpTable.YCoord + ", " + ItemsPickedUpTable.ZCoord + ", " + ItemsPickedUpTable.Timestamp + ") " 
				+ "VALUES (" + item.getTypeId() + ", " + CachedData.getCachedPlayerId(playerName) + ", " + location.getWorld().getName() + ", " + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() + ", " + timestamp + ")";
		return query;
	}

	@Override
	public boolean isOnHold() { return onHold; }

	@Override
	public void setOnHold(boolean onHold) { this.onHold = onHold; }

	@Override
	public boolean refresh() { return onHold; }

}
