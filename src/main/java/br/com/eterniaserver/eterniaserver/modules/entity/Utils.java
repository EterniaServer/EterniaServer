package br.com.eterniaserver.eterniaserver.modules.entity;

import br.com.eterniaserver.eterniaserver.modules.Constants;
import lombok.Getter;
import lombok.Setter;

public class Utils {

    private Utils() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    @Getter
    @Setter
    public static class EntityControl {

        private final int clearAmount;
        private final int spawnLimit;
        private final int breedingLimit;
        private final boolean editorState;

        private double health;
        private double attackDamage;
        private double speed;

        public EntityControl(int clearAmount, int spawnLimit, int breedingLimit, boolean editorState) {
            this.clearAmount = clearAmount;
            this.spawnLimit = spawnLimit;
            this.breedingLimit = breedingLimit;
            this.editorState = editorState;
        }
    }

}
