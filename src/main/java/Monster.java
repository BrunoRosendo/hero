import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;

public class Monster extends Element{
    public Monster(int x, int y) {
        super(x, y);
    }

    public Position move(Position heroPosition) {
        int newX = this.getPosition().getX();
        int newY = this.getPosition().getY();

        if (heroPosition.getX() > newX) newX++;
        else if (heroPosition.getX() < newX) newX--;

        if (heroPosition.getY() > newY) newY++;
        else if (heroPosition.getY() < newY) newY--;

        return new Position(newX, newY);
    }

    public void draw(TextGraphics graphics) {
        graphics.setForegroundColor(TextColor.Factory.fromString("#8B4513"));
        graphics.enableModifiers(SGR.BOLD);
        graphics.putString(new TerminalPosition(position.getX(), position.getY()), "M");
    }
}
