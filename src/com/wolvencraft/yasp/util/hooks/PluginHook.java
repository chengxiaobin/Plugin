/*
 * PluginHook.java
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

package com.wolvencraft.yasp.util.hooks;

import lombok.AccessLevel;
import lombok.Getter;

import com.wolvencraft.yasp.HookManager.ApplicableHook;
import com.wolvencraft.yasp.db.Database;
import com.wolvencraft.yasp.util.ExceptionHandler;
import com.wolvencraft.yasp.util.PatchFetcher;

@Getter(AccessLevel.PUBLIC)
public abstract class PluginHook {
    
    protected ApplicableHook type;
    
    public PluginHook(ApplicableHook type) {
        this.type = type;
    }
    
    /**
     * Code that is to be executed when the hook is being enabled.<br />
     * This should include a database patch, if necessary
     */
    public void onEnable() {
        try {
            PatchFetcher.fetch(type.getPatch());
            Database.patchModule(false, type.getModule());
        } catch (Throwable t) {
            ExceptionHandler.handle(t);
        }
    }
    
    /**
     * Code that is to be executed when the hook is being disabled.<br />
     * This should include a cleanup routine.
     */
    public void onDisable() {
        // Do nothing
    }
    
}