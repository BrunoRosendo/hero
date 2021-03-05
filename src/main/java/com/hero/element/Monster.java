package com.hero.element;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;

import java.util.Random;

import static java.lang.Math.abs;
import com.hero.utils.Position;

public class Monster extends Element{
    public Monster(int x, int y) {
        super(x, y);
    }

    public Position move(Position heroPosition, Position oldPosition) {
        int newX = this.getPosition().getX();
        int newY = this.getPosition().getY();

        // Atack the hero directly
        if (abs(heroPosition.getX() - newX) <= 1
            && abs(heroPosition.getY() - newY) <= 1
            && !oldPosition.equals(this.position) // don't go after again (only if random)
        ) {
            if (heroPosition.getX() > newX) newX++;
            else if (heroPosition.getX() < newX) newX--;

            if (heroPosition.getY() > newY) newY++;
            else if (heroPosition.getY() < newY) newY--;
        } else {
            Random random = new Random();
            newX += random.nextInt(3) - 1;
            newY += random.nextInt(3) - 1;
        }

        return new Position(newX, newY);
    }

    public void draw(TextGraphics graphics) {
        graphics.setForegroundColor(TextColor.Factory.fromString("#8B4513"));
        graphics.enableModifiers(SGR.BOLD);
        graphics.putString(new TerminalPosition(position.getX(), position.getY()), "M");
    }

    public Position getPosition() {
        return position;
    }
}
