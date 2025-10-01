public class Player {
    private int lives = 3;
    private int x, y;
    private PlayerState state;

    //Can Add States Later
    public enum PlayerState { ALIVE, DEAD }

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        this.state = PlayerState.ALIVE;
    }

    // Getters
    public int[] getCoordinates() {
        return new int[] { this.x, this.y };
    }

    public PlayerState getState() {
        return this.state;
    }    

    public int getLives() {
        return this.lives;
    }

    // Stat Changers
    public void oneUp() {
        this.lives++;
    }    

    public boolean death() {
        this.lives--;
        if (this.lives <= 0) {
            this.lives = 0;
            this.state = PlayerState.DEAD;
            return true;
        }
        return false;
    }

    public void changePosition(int xchange, int ychange) {
        this.x += xchange;
        this.y += ychange;
    }

    public void changeState(PlayerState newState) {
        this.state = newState;
    }
}
