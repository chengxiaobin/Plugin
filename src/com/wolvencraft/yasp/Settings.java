/*
 * Settings.java
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

package com.wolvencraft.yasp;

import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.tables.Normal.SettingsTable;

/**
 * Data store that contains both local and remote plugin configurations
 * @author bitWolfy
 *
 */
public class Settings {
    
    /**
     * Represents the local configuration, stored in <i>config.yml</i>
     * @author bitWolfy
     *
     */
    public enum LocalConfiguration {
        Debug("debug", true),
        DBHost("database.host", true),
        DBPort("database.port", true),
        DBName("database.name", true),
        DBUser("database.user", true),
        DBPass("database.pass", true),
        DBPrefix("database.prefix", true),
        DBConnect("jdbc:mysql://" + DBHost.asString() + ":" + DBPort.asInteger() + "/" + DBName.asString()),
        LogPrefix("log-prefix", true),
        Cloud(true);
        
        Object entry;
        
        LocalConfiguration(Object entry) {
            this.entry = entry;
        }
        
        LocalConfiguration(Object entry, boolean fromFile) {
            if(fromFile) this.entry = Statistics.getInstance().getConfig().getString((String) entry);
            else this.entry = entry;
        }
        
        @Override
        public String toString() { return asString(); }
        public String asString() { return (String) entry; }
        public Boolean asBoolean() { return (Boolean) entry; }
        public Integer asInteger() { return (Integer) entry; }
    }
    
    /**
     * Checks for the modules activated via the portal admin interface.
     * @author bitWolfy
     *
     */
    public enum Modules {
        Server("module.server"),
        Blocks("module.blocks"),
        Items("module.items"),
        Deaths("module.deaths"),
        Inventory("module.inventory"),
        
        HookVault("hook.vault"),
        HookWorldGuard("hook.worldguard"),
        HookJobs("hook.jobs"),
        HookMcMMO("hook.mcmmo");;
        
        Modules(String row) { this.row = row;}
        
        String row;
        
        public boolean getEnabled() {
            try {
                return Query.table(SettingsTable.TableName)
                    .column("value")
                    .condition("key", row)
                    .select()
                    .asBoolean("value");
            } catch (Throwable t) { return true; }
        }
    }
    
    /**
     * Represents modules that are currently active.
     * @author bitWolfy
     *
     */
    public enum ActiveHooks {
        HookVault,
        HookWorldGuard,
        HookJobs,
        HookMcMMO;
        
        ActiveHooks() { active = false; }
        
        boolean active;
        
        public boolean getActive() { return active; }
        public void setActive(boolean active) { this.active = active; }
    }
    
    /**
     * Represents the configuration pulled from the database.<br />
     * No data is stored locally; all information is pulled from the database during runtime.
     * @author bitWolfy
     *
     */
    public enum RemoteConfiguration {
        DatabaseVersion("version"),
        
        Ping("ping"),
        LogDelay("log_delay"),
        ShowWelcomeMessages("show_welcome_messages"),
        WelcomeMessage("welcome_message"),
        ShowFirstJoinMessages("show_first_join_messages"),
        FirstJoinMessage("first_join_message");
        
        RemoteConfiguration(String row) {
            this.row = row;
        }
        
        String row;
        
        public String toString() { return asString(); }
        public String asString() {
            try {
                return Query.table(SettingsTable.TableName)
                    .column("value")
                    .condition("key", row)
                    .select()
                    .asString("value");
            } catch (Throwable t) { return ""; }
        }
        public Integer asInteger() { 
            try {
                return Query.table(SettingsTable.TableName)
                    .column("value")
                    .condition("key", row)
                    .select()
                    .asInt("value");
            } catch (Throwable t) { return 0; }
        }
        public Boolean asBoolean() {
            try {
                return Query.table(SettingsTable.TableName)
                    .column("value")
                    .condition("key", row)
                    .select()
                    .asBoolean("value");
            } catch (Throwable t) { return false; }
        }
        
        public boolean update(Object value) {
            return Query.table(SettingsTable.TableName)
                .value("value", value)
                .condition("key", row)
                .update();
        }
    }
    
    /**
     * A hard-coded list of items that have a metadata that should be tracked
     * @author bitWolfy
     *
     */
    public enum ItemsWithMetadata {
        Plank(5),
        Sapling(6),
        Log(17),
        Leave(18),
        Sandstone(24),
        TallGrass(31),
        Wool(35),
        DoubleSlab(43),
        Slab(44),
        PlankDoubleSlab(125),
        PlankSlab(126),
        MobHeadBlock(144),
        Quartz(155),
        GoldenApple(322),
        Dye(351),
        Potion(373),
        MobEgg(383, 50),
        MobHead(397);
        
        ItemsWithMetadata(int itemId){
            this.itemId = itemId;
            this.data = 0;
        }
        
        ItemsWithMetadata(int itemId, int data) {
            this.itemId = itemId;
            this.data = data;
        }
        
        int itemId;
        int data;
        
        private int getId() { return itemId; }
        public int getData() { return data; }
        
        /**
         * Checks if the specified ID is in the list
         * @param id Item ID
         * @return <b>true</b> if the item is in the list, <b>false</b> otherwise
         */
        public static boolean checkAgainst(int id) {
            for(ItemsWithMetadata entry : ItemsWithMetadata.values()) {
                if(entry.getId() == id) return true;
            }
            return false;
        }
        
        public static ItemsWithMetadata get(int id) {
            for(ItemsWithMetadata entry : ItemsWithMetadata.values()) {
                if(entry.getId() == id) return entry;
            }
            return null;
        }
    }
    
}
