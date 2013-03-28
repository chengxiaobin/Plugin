/*
 * Statistics
 * Copyright (C) 2013 bitWolfy <http://www.wolvencraft.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
*/

package com.wolvencraft.yasp.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import com.wolvencraft.yasp.DataCollector;
import com.wolvencraft.yasp.StatsPlugin;

/**
 * Listens to generic server events and reports them to the plugin.
 * @author bitWolfy
 *
 */
public class ServerListener implements Listener {
	
	/**
	 * <b>Default constructor</b><br />
	 * Creates a new instance of the Listener and registers it with the PluginManager
	 * @param plugin StatsPlugin instance
	 */
	public ServerListener(StatsPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onWeatherChange(WeatherChangeEvent event) {
		if(StatsPlugin.getPaused()) return;
		if(!Bukkit.getWorlds().get(0).equals(event.getWorld())) return;
		DataCollector.getServerStats().weatherChange(event.toWeatherState(), event.getWorld().getWeatherDuration());
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPluginEnable(PluginEnableEvent event) {
		if(StatsPlugin.getPaused()) return;
		DataCollector.getServerStats().pluginNumberChange();
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPluginDisable(PluginDisableEvent event) {
		if(StatsPlugin.getPaused()) return;
		DataCollector.getServerStats().pluginNumberChange();
	}
}
