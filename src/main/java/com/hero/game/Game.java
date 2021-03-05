package com.hero.game;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.input.KeyStroke;

import java.io.IOException;
import com.hero.element.*;

public class Game {
    private Screen screen;
    private Arena arena;

    public Game() {
        try {
            Hero hero = new Hero(10, 10);
            this.arena = new Arena("1.txt", hero, 5, 5);

            TerminalSize terminalSize = new TerminalSize(40, 20);
            DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory()
                    .setInitialTerminalSize(terminalSize);
            Terminal terminal = terminalFactory.createTerminal();
            this.screen = new TerminalScreen(terminal);

            this.screen.setCursorPosition(null);   // we don't need a cursor
            this.screen.startScreen();             // screens must be started
            this.screen.doResizeIfNecessary();     // resize screen if necessary
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processKey(KeyStroke key) throws IOException {
        if (key.getKeyType() == KeyType.Character && key.getCharacter() == 'q') {
            this.screen.close();
            return;
        }
        arena.processKey(key);
        if (arena.verifyMonsterCollisions()) { // we don't need to check twice because the monster chases the hero
            if (arena.damageHero()) {
                this.screen.close();
                System.out.println("You died! :(");
                return;
            }
        }
    }

    private void draw() throws IOException{
        this.screen.clear();
        this.arena.draw(this.screen.newTextGraphics());
        this.screen.refresh();
    }

    public void run() {
        while (true) {
            try {
                this.draw();
                KeyStroke key = screen.readInput();
                if (key.getKeyType() == KeyType.EOF) break;
                this.processKey(key);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
