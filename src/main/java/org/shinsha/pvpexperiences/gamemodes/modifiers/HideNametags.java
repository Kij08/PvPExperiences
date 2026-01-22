package org.shinsha.pvpexperiences.gamemodes.modifiers;

import org.bukkit.scoreboard.Team;
import org.shinsha.pvpexperiences.gamemodes.GameModeBase;
import org.shinsha.pvpexperiences.sessions.Session;

public class HideNametags extends ModifierBase {

    public HideNametags(Session session, GameModeBase mode) { super(session, mode); }

    @Override
    public void SetupModifier() {
        if(owningMode.GetModeScoreboard() != null){
            for(Team t : owningMode.GetModeScoreboard().getTeams()){
                t.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
            }
        }
        else{
            //TODO: Make empty board and teams
        }
    }

    @Override
    public void CleanupModifier() {

    }

}
