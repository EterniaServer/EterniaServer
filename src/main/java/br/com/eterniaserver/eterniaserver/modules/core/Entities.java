package br.com.eterniaserver.eterniaserver.modules.core;

import br.com.eterniaserver.eternialib.database.annotations.DataField;
import br.com.eterniaserver.eternialib.database.annotations.PrimaryKeyField;
import br.com.eterniaserver.eternialib.database.annotations.Table;
import br.com.eterniaserver.eternialib.database.enums.FieldType;
import br.com.eterniaserver.eterniaserver.modules.Constants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;

public final class Entities {

    private Entities() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Table(tableName = "%eternia_server_revision%")
    public static class Revision {

        public Revision(String version, String status, Date date) {
            this.version = version;
            this.status = status;
            this.date = date;
        }

        @PrimaryKeyField(columnName = "id", type = FieldType.INTEGER, autoIncrement = true)
        private Integer id;

        @DataField(columnName = "version", type = FieldType.STRING, notNull = true)
        private String version;

        @DataField(columnName = "status", type = FieldType.STRING, notNull = true)
        private String status;

        @DataField(columnName = "date", type = FieldType.DATE, notNull = true)
        private Date date;

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Table(tableName = "%eternia_server_profile%")
    public static class PlayerProfile {

        @PrimaryKeyField(columnName = "uuid", type = FieldType.UUID, autoIncrement = false)
        private UUID uuid;

        @DataField(columnName = "playerName", type = FieldType.STRING, notNull = true)
        private String playerName;

        @DataField(columnName = "playerDisplay", type = FieldType.STRING)
        private String playerDisplay;

        @DataField(columnName = "firstJoin", type = FieldType.TIMESTAMP)
        private Timestamp firstJoin;

        @DataField(columnName = "lastJoin", type = FieldType.TIMESTAMP)
        private Timestamp lastJoin;

        @DataField(columnName = "playedMinutes", type = FieldType.INTEGER, notNull = true)
        private Integer playedMinutes;

        private boolean afk = false;
        private boolean god = false;

        private long lastMove = System.currentTimeMillis();

    }

}
