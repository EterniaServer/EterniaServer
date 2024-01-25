package br.com.eterniaserver.eterniaserver.modules.economy;

import br.com.eterniaserver.eternialib.database.annotations.DataField;
import br.com.eterniaserver.eternialib.database.annotations.PrimaryKeyField;
import br.com.eterniaserver.eternialib.database.annotations.ReferenceField;
import br.com.eterniaserver.eternialib.database.annotations.Table;
import br.com.eterniaserver.eternialib.database.enums.FieldType;
import br.com.eterniaserver.eternialib.database.enums.ReferenceMode;
import br.com.eterniaserver.eterniaserver.modules.Constants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

final class Entities {

    private Entities() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Table(tableName = "%eternia_server_bank%")
    public static class BankBalance {

        @PrimaryKeyField(columnName = "name", type = FieldType.STRING, autoIncrement = false)
        private String name;

        @DataField(columnName = "balance", type = FieldType.DOUBLE, notNull = true)
        private Double balance;

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Table(tableName = "%eternia_server_bank_member%")
    public static class BankMember {

        @PrimaryKeyField(columnName = "id", type = FieldType.INTEGER, autoIncrement = true)
        private Integer id;

        @ReferenceField(columnName = "bankName",
                        referenceTableName = "%eternia_server_bank%",
                        referenceColumnName = "name",
                        notNull = true,
                        mode = ReferenceMode.CASCADE)
        @DataField(columnName = "bankName", type = FieldType.STRING, notNull = true)
        private String bankName;

        @DataField(columnName = "uuid", type = FieldType.UUID, notNull = true)
        private UUID uuid;

        @DataField(columnName = "role", type = FieldType.STRING, notNull = true)
        private String role;

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Table(tableName = "%eternia_server_economy%")
    public static class EcoBalance {

        @PrimaryKeyField(columnName = "uuid", type = FieldType.UUID, autoIncrement = false)
        private UUID uuid;

        @DataField(columnName = "balance", type = FieldType.DOUBLE, notNull = true)
        private Double balance;

    }

}
