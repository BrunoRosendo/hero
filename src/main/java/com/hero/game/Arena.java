package com.hero.game;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import com.hero.element.*;
import com.hero.utils.Position;

public class Arena {
    private int height;
    private int width;
    private boolean over;
    private Hero hero;
    private List<Wall> walls;
    private List<Coin> coins;
    private List<Monster> monsters;
    private Door door;

    public Arena(String file, Hero hero) throws FileNotFoundException {
        this.hero = hero;
        loadLevel(file);
    }

    public void loadLevel(String file) throws FileNotFoundException {
        File obj = new File(file);
        Scanner scanner = new Scanner(obj);
        this.over = false;
        this.width = scanner.nextInt();
        this.height = scanner.nextInt();
        this.door = new Door(scanner.nextInt(), scanner.nextInt(), scanner.nextInt(), scanner.nextInt());
        this.walls = this.createWalls();
        this.coins = this.createCoins(scanner.nextInt());
        this.monsters = this.createMonsters(scanner.nextInt());
        this.hero.setPosition(new Position(10, 10));
    }

    public void restart() {
        this.hero.setScore(0);
        this.hero.setEnergy(5);
    }

    public boolean isOver() {
        return this.over;
    }

    private List<Wall> createWalls() {
        List<Wall> walls = new ArrayList<>();

        for (int c = 0; c < width; c++) {
            walls.add(new Wall(c, 0));
            walls.add(new Wall(c, height - 1));
        }

        for (int r = 1; r < height - 1; r++) {
            walls.add(new Wall(0, r));
            walls.add(new Wall(width - 1, r));
        }

        return walls;
    }

    private List<Coin> createCoins(int num) {
        Random random = new Random();
        ArrayList<Coin> coins = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            Coin newCoin = new Coin(random.nextInt(width - 2) + 1, random.nextInt(height - 2) + 1);

            while (true) {
                boolean repeated = false;
                for (Coin coin : coins) // local coins
                    if (coin.getPosition().equals(newCoin.getPosition())) repeated = true;
                if (!repeated && !newCoin.getPosition().equals(this.hero.getPosition())) break;
                newCoin = new Coin(random.nextInt(width - 2) + 1, random.nextInt(height - 2) + 1);
            }

            coins.add(newCoin);
        }
        return coins;
    }

    private List<Monster> createMonsters(int num) {
        List<Monster> newMonsters = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < num; i++) {
            Monster newMonster = new Monster(random.nextInt(width - 2) + 1, random.nextInt(height - 2) + 1);

            while (true) {
                boolean repeated = false;
                for (Coin coin : coins)
                    if (coin.getPosition().equals(newMonster.getPosition())) repeated = true;
                for (Monster monster : newMonsters)
                    if (monster.getPosition().equals(newMonster.getPosition())) repeated = true;
                if (!repeated && !newMonster.getPosition().equals(this.hero.getPosition())) break;
                newMonster = new Monster(random.nextInt(width - 2) + 1, random.nextInt(height - 2) + 1);
            }

            newMonsters.add(newMonster);
        }
        return newMonsters;
    }

    public void processKey(KeyStroke key) {
        Position oldPos = this.hero.getPosition();
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
        this.moveMonsters(oldPos);
    }

    private void moveMonsters(Position oldPos) {
        for (Monster monster : monsters) {
            Position newPosition = monster.move(this.hero.getPosition(), oldPos);
            if (newPosition.getX() <= 0) newPosition.setX(newPosition.getX()+1);
            if (newPosition.getY() <= 0) newPosition.setY(newPosition.getY()+1);
            if (newPosition.getX() >= width - 1) newPosition.setX(newPosition.getX()-1);
            if (newPosition.getY() >= height - 1) newPosition.setY(newPosition.getY()-1);
            monster.setPosition(newPosition);
        }
    }

    private boolean canHeroMove(Position position) {
        if (door.isOpen() && door.collides(position)) return true;
        if (position.getX() >= this.width || position.getY() >= this.height
           || position.getX() < 0 || position.getY() < 0) return false;

        for (Wall wall : walls)
            if (wall.getPosition().equals(position)) return false;

        return true;
    }

    private void retrieveCoins() {
        for (Coin coin : coins)
            if (coin.getPosition().equals(this.hero.getPosition())) {
                coins.remove(coin);
                this.hero.setScore(this.hero.getScore() + 1);
                return;
            }
    }

    public void moveHero(Position position) {
        if (this.canHeroMove(position)) {
            hero.setPosition(position);
            if (door.collides(position)) this.over = true;
        }
        retrieveCoins();
        if (!door.isOpen() && coins.isEmpty()) door.openDoor();
    }

    public boolean verifyMonsterCollisions() {
        for (Monster monster : monsters)
            if (monster.getPosition().equals(this.hero.getPosition())) return true;
        return false;
    }

    public boolean damageHero() {
        hero.setEnergy(hero.getEnergy() - 1);
        return hero.getEnergy() == 0;
    }

    public void draw(TextGraphics graphics) {
        graphics.setBackgroundColor(TextColor.Factory.fromString("#336699"));
        graphics.fillRectangle(new TerminalPosition(0, 0), new TerminalSize(width, height), ' ');

        for (Wall wall : walls)  wall.draw(graphics);
        door.draw(graphics);
        for (Coin coin : coins) coin.draw(graphics);
        for (Monster monster : monsters) monster.draw(graphics);
        this.hero.draw(graphics);
    }
}
