package br.com.eterniaserver.eterniaserver.objects;

public class EntityControl {

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

    public void setHealth(double health) {
        this.health = health;
    }
    
    public void setAttackDamage(double attackDamage) {
        this.attackDamage = attackDamage;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getHealth() {
        return health;
    }

    public double getAttackDamage() {
        return attackDamage;
    }

    public double getSpeed() {
        return speed;
    }

    public int getClearAmount() {
        return clearAmount;
    }

    public int getSpawnLimit() {
        return spawnLimit;
    }

    public int getBreedingLimit() {
        return breedingLimit;
    }

    public boolean getEditorState() {
        return editorState;
    }

}
