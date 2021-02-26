import javax.swing.*;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;

import java.io.IOException;

public class Arena {
    private int height;
    private int width;
    private Hero hero;

    public Arena(int h, int w, Hero hero) {
        this.height = h;
        this.width = w;
        this.hero = hero;
    }

    public void processKey(KeyStroke key) {
        switch (key.getKeyType()) {
            case ArrowUp:
                this.moveHero(this.hero.moveUp());
                break;
            case ArrowDown:
                this.moveHero(this.hero.moveDown());
                break;
            case ArrowLeft:
                this.moveHero(this.hero.moveLeft());
                break;
            case ArrowRight:
                this.moveHero(this.hero.moveRight());
                break;
            default:
                break;
        }
    }

    private boolean canHeroMove(Position position) {
        return position.getX() < this.width && position.getY() < this.height
                && position.getX() >= 0 && position.getY() >= 0;
    }

    public void moveHero(Position position) {
        if (this.canHeroMove(position))
            hero.setPosition(position);
    }

    public void draw(TextGraphics graphics) {
        graphics.setBackgroundColor(TextColor.Factory.fromString("#336699"));
        graphics.fillRectangle(new TerminalPosition(0, 0), new TerminalSize(width, height), ' ');
        this.hero.draw(graphics);
    }
}
