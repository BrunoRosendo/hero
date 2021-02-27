import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Arena {
    private int height;
    private int width;
    private Hero hero;
    private List<Wall> walls;
    private List<Coin> coins;
    private List<Monster> monsters;

    public Arena(int h, int w, Hero hero) {
        this.height = h;
        this.width = w;
        this.hero = hero;
        this.walls = this.createWalls();
        this.coins = this.createCoins();
        this.monsters = this.createMonsters();
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

    private List<Coin> createCoins() {
        Random random = new Random();
        ArrayList<Coin> coins = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Coin newCoin = new Coin(random.nextInt(width - 2) + 1, random.nextInt(height - 2) + 1);

            while (true) {
                boolean repeated = false;
                for (Coin coin : coins)
                    if (coin.position.equals(newCoin.position)) repeated = true;
                if (!repeated && !newCoin.position.equals(this.hero.position)) break;
                newCoin = new Coin(random.nextInt(width - 2) + 1, random.nextInt(height - 2) + 1);
            }

            coins.add(newCoin);
        }
        return coins;
    }

    private List<Monster> createMonsters() {
        List<Monster> newMonsters = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            Monster newMonster = new Monster(random.nextInt(width - 2) + 1, random.nextInt(height - 2) + 1);

            while (true) {
                boolean repeated = false;
                for (Coin coin : coins)
                    if (coin.position.equals(newMonster.position)) repeated = true;
                for (Monster monster : newMonsters)
                    if (monster.position.equals(newMonster.position)) repeated = true;
                if (!repeated && !newMonster.position.equals(this.hero.position)) break;
                newMonster = new Monster(random.nextInt(width - 2) + 1, random.nextInt(height - 2) + 1);
            }

            newMonsters.add(newMonster);
        }
        return newMonsters;
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
        this.moveMonsters();
    }

    private void moveMonsters() {
        for (Monster monster : monsters)
            monster.setPosition(monster.move(this.hero.position));
    }

    private boolean canHeroMove(Position position) {
        if (position.getX() >= this.width || position.getY() >= this.height
           || position.getX() < 0 || position.getY() < 0) return false;

        for (Wall wall : walls)
            if (wall.getPosition().equals(position)) return false;

        return true;
    }

    private void retrieveCoins() {
        for (Coin coin : coins)
            if (coin.position.equals(this.hero.position)) {
                coins.remove(coin);
                return;
            }
    }

    public void moveHero(Position position) {
        if (this.canHeroMove(position))
            hero.setPosition(position);
        retrieveCoins();
    }

    public boolean verifyMonsterCollisions() {
        for (Monster monster : monsters)
            if (monster.getPosition().equals(this.hero.getPosition())) return true;
        return false;
    }

    public void draw(TextGraphics graphics) {
        graphics.setBackgroundColor(TextColor.Factory.fromString("#336699"));
        graphics.fillRectangle(new TerminalPosition(0, 0), new TerminalSize(width, height), ' ');

        for (Wall wall : walls)  wall.draw(graphics);
        for (Coin coin : coins) coin.draw(graphics);
        for (Monster monster : monsters) monster.draw(graphics);
        this.hero.draw(graphics);
    }
}
