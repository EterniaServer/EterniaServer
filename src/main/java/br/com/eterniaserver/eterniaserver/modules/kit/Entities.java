package br.com.eterniaserver.eterniaserver.modules.kit;

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
    @Table(tableName = "%eternia_kit_time%")
    public static class KitTime {

        @PrimaryKeyField(columnName = "uuid", type = FieldType.UUID, autoIncrement = false)
        private UUID uuid;

        @DataField(columnName = "kitName", type = FieldType.STRING, notNull = true)
        private String kitName;

        @DataField(columnName = "lastUseTime", type = FieldType.TIMESTAMP, notNull = true)
        private Timestamp lastUseTime;

    }

}
