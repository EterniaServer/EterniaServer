package br.com.eterniaserver.eterniaserver.API;

import br.com.eterniaserver.eternialib.sql.Queries;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Vars;

public class Exp {

    private final EterniaServer plugin;
    private final Vars vars;

    public Exp(EterniaServer plugin, Vars vars) {
        this.plugin = plugin;
        this.vars = vars;
    }

    /**
     * Gets experience of a player on a database.
     * @param playerName to check
     * @return Amount currently held in player's database
     */
    public Integer getExp(String playerName) {
        if (vars.xp.containsKey(playerName)) {
            return vars.xp.get(playerName);
        }

        final String querie = "SELECT xp FROM " + plugin.serverConfig.getString("sql.table-xp") + " WHERE player_name='" + playerName + "';";
        return Queries.queryInteger(querie, "xp");
    }

    /**
     * Defines the amount experience in player's database.
     * @param playerName to check
     * @param amount to set
     */
    public void setExp(String playerName, int amount) {
        vars.xp.put(playerName, amount);
        Queries.executeQuery("UPDATE " + plugin.serverConfig.getString("sql.table-xp") + " SET xp='" + amount + "' WHERE player_name='" + playerName + "';");
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
