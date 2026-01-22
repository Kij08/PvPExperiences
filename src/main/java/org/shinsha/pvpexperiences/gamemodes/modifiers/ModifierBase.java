package org.shinsha.pvpexperiences.gamemodes.modifiers;

import org.shinsha.pvpexperiences.gamemodes.GameModeBase;
import org.shinsha.pvpexperiences.sessions.Session;

public abstract class ModifierBase {

    protected Session owningSession;
    protected GameModeBase owningMode;

    public ModifierBase(Session session, GameModeBase mode){
        owningSession = session;
        owningMode = mode;
    }

    public abstract void SetupModifier();

}
