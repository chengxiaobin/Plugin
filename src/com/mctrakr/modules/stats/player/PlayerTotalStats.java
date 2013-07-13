/*
 * PlayerTotalStats.java
 * 
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

package com.mctrakr.modules.stats.player;

import lombok.AccessLevel;
import lombok.Getter;

import com.mctrakr.database.Query;
import com.mctrakr.database.Query.QueryResult;
import com.mctrakr.modules.DataStore.NormalData;
import com.mctrakr.modules.stats.player.Tables.PlayersTable;
import com.mctrakr.session.OnlineSession;
import com.mctrakr.util.Util;

/**
 * Represents the Player data that is being tracked.<br />
 * Each entry must have a unique player name.
 * @author bitWolfy
 *
 */
public class PlayerTotalStats {
    
    public static class BasicPlayerStats extends NormalData {
        
        private long lastSync;
        
        @Getter(AccessLevel.PUBLIC) private long longestSession;
        @Getter(AccessLevel.PUBLIC) private long currentSession;
        
        @Getter(AccessLevel.PUBLIC) private long totalPlaytime;
        
        public BasicPlayerStats (OnlineSession session) {
            super(session);
            lastSync = Util.getTimestamp();
            
            currentSession = 0;
            longestSession = 0;
            
            long firstLogin = lastSync;
            int logins = 0;
            
            QueryResult result = Query.table(PlayersTable.TableName)
                .column(PlayersTable.Logins)
                .column(PlayersTable.FirstLogin)
                .column(PlayersTable.Playtime)
                .column(PlayersTable.LongestSession)
                .condition(PlayersTable.PlayerId, session.getId())
                .select();
            
            if(result == null) {
                Query.table(PlayersTable.TableName)
                     .value(PlayersTable.Name, session.getName())
                     .insert();
            } else {
                firstLogin = result.asLong(PlayersTable.FirstLogin);
                if(firstLogin == -1) { firstLogin = lastSync; }
                logins = result.asInt(PlayersTable.Logins);
                this.totalPlaytime = result.asLong(PlayersTable.Playtime);
                longestSession = result.asLong(PlayersTable.LongestSession);
            }
            
            Query.table(PlayersTable.TableName)
                .value(PlayersTable.LoginTime, lastSync)
                .value(PlayersTable.FirstLogin, firstLogin)
                .value(PlayersTable.Logins, ++logins)
                .condition(PlayersTable.PlayerId, session.getId())
                .update();
        }
        
        @Override
        @Deprecated
        public void fetchData() { }
        
        @Override
        public boolean pushData() {
            currentSession += Util.getTimestamp() - lastSync;
            if(longestSession > currentSession) longestSession = currentSession;
            totalPlaytime += Util.getTimestamp() - lastSync;
            lastSync = Util.getTimestamp();
            
            return Query.table(PlayersTable.TableName)
                .value(PlayersTable.Playtime, totalPlaytime)
                .value(PlayersTable.LongestSession, longestSession)
                .condition(PlayersTable.PlayerId, session.getId())
                .update();
        }
        
    }
    
}