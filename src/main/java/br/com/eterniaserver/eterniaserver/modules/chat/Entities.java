package br.com.eterniaserver.eterniaserver.modules.chat;

import br.com.eterniaserver.eternialib.database.annotations.DataField;
import br.com.eterniaserver.eternialib.database.annotations.PrimaryKeyField;
import br.com.eterniaserver.eternialib.database.annotations.Table;
import br.com.eterniaserver.eternialib.database.enums.FieldType;
import br.com.eterniaserver.eterniaserver.modules.Constants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @Table(tableName = "%eternia_server_chat%")
    public static class ChatInfo {

        @PrimaryKeyField(columnName = "uuid", type = FieldType.UUID, autoIncrement = false)
        private UUID uuid;

        @DataField(columnName = "chatColor", type = FieldType.STRING)
        private String chatColor;

        @DataField(columnName = "mutedUntil", type = FieldType.TIMESTAMP)
        private Timestamp mutedUntil;

    }

}
