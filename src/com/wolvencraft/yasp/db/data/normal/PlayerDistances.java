package com.wolvencraft.yasp.db.data.normal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wolvencraft.yasp.db.QueryResult;
import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.normal.DistancePlayers;

/**
 * Represents the distances a player travelled.
 * Only one entry per player is allowed.
 * @author bitWolfy
 *
 */
public class PlayerDistances implements _NormalData {
	
	/**
	 * Default constructor. Takes in the Player object and pulls corresponding values from the remote database.<br />
	 * If no data is found in the database, the default values are inserted.
	 * @param player <b>Player</b> tracked player
	 */
	public PlayerDistances(int playerId) {
		this.playerId = playerId;
		this.foot = 0;
		this.boat = 0;
		this.minecart = 0;
		this.pig = 0;
		
		fetchData();
	}
	
	private int playerId;
	private double foot;
	private double boat;
	private double minecart;
	private double pig;
	
	@Override
	public void fetchData() {
		List<QueryResult> results = QueryUtils.select(
			DistancePlayers.TableName.toString(),
			"*",
			DistancePlayers.PlayerId.toString() + " = " + playerId
		);
		if(results.isEmpty()) QueryUtils.insert(DistancePlayers.TableName.toString(), getValues());
		else {
			foot = results.get(0).getValueAsInteger(DistancePlayers.Foot.toString());
			boat = results.get(0).getValueAsInteger(DistancePlayers.Boat.toString());
			minecart = results.get(0).getValueAsInteger(DistancePlayers.Minecart.toString());
			pig = results.get(0).getValueAsInteger(DistancePlayers.Pig.toString());
		}
	}

	@Override
	public boolean pushData() {
		return QueryUtils.update(DistancePlayers.TableName.toString(),
			getValues(),
			DistancePlayers.PlayerId + " = " + playerId
		);
	}
	
	@Override
	public Map<String, Object> getValues() {
		Map<String, Object> valueMap = new HashMap<String, Object>();
		valueMap.put(DistancePlayers.Foot.toString(), foot);
		valueMap.put(DistancePlayers.Boat.toString(), boat);
		valueMap.put(DistancePlayers.Minecart.toString(), minecart);
		valueMap.put(DistancePlayers.Pig.toString(), pig);
		return valueMap;
	}
	
	/**
	 * Increments the distance travelled by foot by the specified amount.
	 * @param distance Distance to add to the statistics
	 */
	public void addFootDistance(double distance) { foot += distance; }
	
	/**
	 * Increments the distance travelled by boat by the specified amount.
	 * @param distance Distance to add to the statistics
	 */
	public void addBoatDistance(double distance) { boat += distance; }
	
	/**
	 * Increments the distance travelled by minecart by the specified amount.
	 * @param distance Distance to add to the statistics
	 */
	public void addMinecartDistance(double distance) { minecart += distance; }
	
	/**
	 * Increments the distance travelled by pig by the specified amount.
	 * @param distance Distance to add to the statistics
	 */
	public void addPigDistance (double distance) { pig += distance; }
	
}
