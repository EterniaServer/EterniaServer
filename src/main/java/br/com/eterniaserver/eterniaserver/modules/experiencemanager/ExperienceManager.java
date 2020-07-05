package br.com.eterniaserver.eterniaserver.modules.experiencemanager;

import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eterniaserver.EterniaServer;

public class ExperienceManager {

    private final EterniaServer plugin;

    public ExperienceManager(EterniaServer plugin) {
        this.plugin = plugin;
    }

    /**
     * Gets experience of a player on a database.
     * @param playerName to check
     * @return Amount currently held in player's database
     */
    public Integer getExp(String playerName) {
        if (plugin.getXp().containsKey(playerName)) {
            return plugin.getXp().get(playerName);
        }

        final String querie = "SELECT xp FROM " + plugin.serverConfig.getString("sql.table-xp") + " WHERE player_name='" + playerName + "';";
        return EQueries.queryInteger(querie, "xp");
    }

    /**
     * Defines the amount experience in player's database.
     * @param playerName to check
     * @param amount to set
     */
    public void setExp(String playerName, int amount) {
        plugin.getXp().put(playerName, amount);
        EQueries.executeQuery("UPDATE " + plugin.serverConfig.getString("sql.table-xp") + " SET xp='" + amount + "' WHERE player_name='" + playerName + "';");
    }

    /**
     * Adds experience of player's database.
     * @param playerName to check
     * @param amount to add
     */
    public void addExp(String playerName, int amount) {
        setExp(playerName, getExp(playerName) + amount);
    }

    /**
     * Removes experience of player's database.
     * @param playerName to check
     * @param amount to remove
     */
    public void removeExp(String playerName, int amount) {
        setExp(playerName, getExp(playerName) - amount);
    }

}
