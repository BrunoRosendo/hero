package com.hero.element;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.hero.utils.Position;

public class Door extends Element{
    boolean open;
    Position posFinal;
    public Door(int x, int y, int xFinal, int yFinal) {
        super(x, y);
        this.posFinal = new Position(xFinal, yFinal);
        this.open = false;
    }

    public void openDoor() {
        this.open = true;
    }

    public boolean isOpen() {
        return this.open;
    }

    public boolean collides(Position heroPos) {
        for (int x = position.getX(); x <= posFinal.getX(); ++x)
            for (int y = position.getY(); y <= posFinal.getY(); ++y)
                if (heroPos.equals(new Position(x, y))) return true;
        return false;
    }

    @Override
    public void draw(TextGraphics graphics) {
        if (posFinal.getX() == position.getX()) { // vertical
            for (int i = position.getY(); i <= posFinal.getY(); ++i) {
                if (open)
                    graphics.setForegroundColor(TextColor.Factory.fromString("#336699"));
                else
                    graphics.setForegroundColor(TextColor.Factory.fromString("#654321"));
                graphics.enableModifiers(SGR.BOLD);
                graphics.putString(new TerminalPosition(position.getX(), i), "■");
            }
        } else { // horizontal
            for (int i = position.getX(); i <= posFinal.getX(); ++i) {
                if (open)
                    graphics.setForegroundColor(TextColor.Factory.fromString("#336699"));
                else
                    graphics.setForegroundColor(TextColor.Factory.fromString("#654321"));
                graphics.enableModifiers(SGR.BOLD);
                graphics.putString(new TerminalPosition(i, position.getY()), "■");
            }
        }
    }
}
