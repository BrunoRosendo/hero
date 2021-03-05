package com.hero.element;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.hero.utils.Position;

public class Hero extends Element{
    private int energy;
    private int score;

    public Hero(int x, int y) {
        super(x, y);
        this.energy = 5;
        this.score = 0;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
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

    public void draw(TextGraphics graphics) {
        graphics.setForegroundColor(TextColor.Factory.fromString("#FFFF33"));
        graphics.enableModifiers(SGR.BOLD);
        graphics.putString(new TerminalPosition(position.getX(), position.getY()), "X");
        graphics.putString(new TerminalPosition(position.getX(), position.getY() + 1), String.valueOf(this.energy));
        graphics.putString(new TerminalPosition(0, 0), String.valueOf(this.score));
    }
}
