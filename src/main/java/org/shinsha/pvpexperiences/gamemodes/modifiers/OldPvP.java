package org.shinsha.pvpexperiences.gamemodes.modifiers;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.shinsha.pvpexperiences.gamemodes.GameModeBase;
import org.shinsha.pvpexperiences.sessions.Session;

public class OldPvP extends ModifierBase {

    private double attackSpeed;

    public OldPvP(Session session, GameModeBase mode) { super(session, mode); }

    @Override
    public void SetupModifier() {
        for(Player p : owningSession.GetActivePlayers()){
            p.getAttribute(Attribute.ATTACK_SPEED).setBaseValue(10);
        }
    }

    @Override
    public void CleanupModifier() {
        for(Player p : owningSession.GetActivePlayers()){
            p.getAttribute(Attribute.ATTACK_SPEED).setBaseValue(4);
        }
    }
}
