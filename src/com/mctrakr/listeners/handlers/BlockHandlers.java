/*
 * BlockHandler.java
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

package com.mctrakr.listeners.handlers;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import com.mctrakr.cache.SessionCache;
import com.mctrakr.modules.stats.blocks.BlocksDataStore;
import com.mctrakr.session.OnlineSession;
import com.mctrakr.settings.ConfigLock.PrimaryType;

public class BlockHandlers {
    
    /**
     * Executed when a player breaks a block
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    public static class BlockBreak implements Runnable {
        
        private Player player;
        private BlockState block;
        
        @Override
        public void run() {
            OnlineSession session = SessionCache.fetch(player);
            ((BlocksDataStore) session.getDataStore(PrimaryType.Blocks)).blockPlace(block);
            session.getPlayerTotals().blockPlace();
        }
        
    }
    
    /**
     * Executed when a player places a block
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    public static class BlockPlace implements Runnable {

        private Player player;
        private BlockState block;
        
        @Override
        public void run() {
            OnlineSession session = SessionCache.fetch(player);
            ((BlocksDataStore) session.getDataStore(PrimaryType.Blocks)).blockBreak(block);
            session.getPlayerTotals().blockBreak();
        }
        
    }
    
}