package org.shinsha.pvpexperiences.gamemodes.modifiers;

import org.bukkit.Bukkit;
import org.shinsha.pvpexperiences.gamemodes.GameModeBase;
import org.shinsha.pvpexperiences.sessions.Session;

public abstract class ModifierBase {

    protected Session owningSession;
    protected GameModeBase owningMode;

    public ModifierBase(Session session, GameModeBase mode){
        Bukkit.broadcastMessage("Modifier starting");
        owningSession = session;
        owningMode = mode;
    }

    public abstract void SetupModifier();
    public abstract void CleanupModifier();

}
