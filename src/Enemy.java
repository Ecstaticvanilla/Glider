package src;

public class Enemy{
    int health;
    boolean isvisible;
    public Enemy(int health) {
        this.health = health;
        isvisible = false;
    }

    public void bringToLife() {
        isvisible = true;
    }


}