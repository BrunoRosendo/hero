import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.screen.Screen;

public class Hero {
    private Position position;

    public Hero(int x, int y) {
        this.position = new Position(x, y);
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public Position moveUp() {
        return new Position(this.position.getX(), this.position.getY() - 1);
    }

    public Position moveDown() {
        return new Position(this.position.getX(), this.position.getY() + 1);
    }

    public Position moveLeft() {
        return new Position(this.position.getX() - 1, this.position.getY());
    }

    public Position moveRight() {
        return new Position(this.position.getX() + 1, this.position.getY());
    }

    public void draw(Screen screen) {
        screen.setCharacter(this.getPosition().getX(), this.getPosition().getY(), TextCharacter.fromCharacter('X')[0]);
    }
}
