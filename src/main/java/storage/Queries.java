package storage;

public class Queries
{
    public final static String createTable = "CREATE TABLE IF NOT EXISTS eternia " +
            "(`UUID` varchar(32) NOT NULL, " +
            "`NAME` varchar(32) NOT NULL, " +
            "`XP` int(11) NOT NULL);";
}
