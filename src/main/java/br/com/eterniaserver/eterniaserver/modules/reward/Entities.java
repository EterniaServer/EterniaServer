package br.com.eterniaserver.eterniaserver.modules.reward;

import br.com.eterniaserver.eternialib.database.annotations.DataField;
import br.com.eterniaserver.eternialib.database.annotations.PrimaryKeyField;
import br.com.eterniaserver.eternialib.database.annotations.Table;
import br.com.eterniaserver.eternialib.database.enums.FieldType;
import br.com.eterniaserver.eterniaserver.modules.Constants;
import lombok.Getter;
import lombok.Setter;

public final class Entities {

    private Entities() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    @Getter
    @Setter
    @Table(tableName = "%eternia_server_reward%")
    public static class RewardGroup {

        public RewardGroup(String keyCode, String groupName) {
            this.keyCode = keyCode;
            this.groupName = groupName;
        }

        @PrimaryKeyField(columnName = "id", type = FieldType.INTEGER, autoIncrement = true)
        private Integer id;

        @DataField(columnName = "keyCode", type = FieldType.STRING, notNull = true)
        private String keyCode;

        @DataField(columnName = "groupName", type = FieldType.STRING, notNull = true)
        private String groupName;

    }

}
