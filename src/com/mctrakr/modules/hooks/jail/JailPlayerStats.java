/*
 * JailPlayerStats.java
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

package com.mctrakr.modules.hooks.jail;

import com.mctrakr.database.Query;
import com.mctrakr.managers.HookManager;
import com.mctrakr.modules.DataStore.NormalData;
import com.mctrakr.modules.hooks.jail.Tables.JailTable;
import com.mctrakr.session.OnlineSession;
import com.mctrakr.settings.ConfigLock.HookType;

public class JailPlayerStats extends NormalData {
    
    public JailPlayerStats(OnlineSession session) {
        super(session);
        fetchData();
    }
    
    @Override
    public void fetchData() {
        if(Query.table(JailTable.TableName)
                .condition(JailTable.PlayerId, session.getId())
                .exists()) return;
        
        JailHook hook = (JailHook) HookManager.getHook(HookType.Jail);
        if(hook == null) return;
        
        String username = session.getName();
        
        Query.table(JailTable.TableName)
            .value(JailTable.PlayerId, session.getId())
            .value(JailTable.IsJailed, hook.isJailed(username))
            .value(JailTable.Jailer, hook.getJailer(username))
            .value(JailTable.RemainingTime, hook.getRemainingTime(username))
            .insert();
    }

    @Override
    public boolean pushData() {
        JailHook hook = (JailHook) HookManager.getHook(HookType.Jail);
        if(hook == null) return false;
        
        String username = session.getName();
        
        return Query.table(JailTable.TableName)
            .value(JailTable.IsJailed, hook.isJailed(username))
            .value(JailTable.Jailer, hook.getJailer(username))
            .value(JailTable.RemainingTime, hook.getRemainingTime(username))
            .condition(JailTable.PlayerId, session.getId())
            .update();
    }
    
}