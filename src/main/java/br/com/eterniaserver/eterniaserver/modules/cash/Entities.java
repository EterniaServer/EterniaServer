package br.com.eterniaserver.eterniaserver.modules.cash;

import br.com.eterniaserver.eternialib.database.annotations.DataField;
import br.com.eterniaserver.eternialib.database.annotations.PrimaryKeyField;
import br.com.eterniaserver.eternialib.database.annotations.Table;
import br.com.eterniaserver.eternialib.database.enums.FieldType;
import br.com.eterniaserver.eterniaserver.modules.Constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

public final class Entities {

    private Entities() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Table(tableName = "%eternia_server_cash%")
    public static class CashBalance {

        @PrimaryKeyField(columnName = "uuid", type = FieldType.UUID, autoIncrement = false)
        private UUID uuid;

        @DataField(columnName = "balance", type = FieldType.INTEGER, notNull = true)
        private Integer balance;

    }
}
