package com.hero.game;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.input.KeyStroke;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import com.googlecode.lanterna.terminal.swing.TerminalScrollController;
import com.hero.element.*;

public class Game {
    private Screen screen = null;
    private Arena arena;
    private int currentLevel;
    private int maxLevel;

    public Game() {
        try {
            this.currentLevel = 1;
            this.maxLevel = 2;

            Hero hero = new Hero(10, 10);
            this.arena = new Arena("1.txt", hero);
            resizeScreen("1.txt");

            this.screen.setCursorPosition(null);   // we don't need a cursor
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void resizeScreen(String file) throws IOException {
        File obj = new File(file);
        Scanner scanner = new Scanner(obj);
        TerminalSize terminalSize = new TerminalSize(scanner.nextInt(), scanner.nextInt());
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory()
                .setInitialTerminalSize(terminalSize);
        Terminal terminal = terminalFactory.createTerminal();
        if (this.screen != null) this.screen.close();
        this.screen = new TerminalScreen(terminal);
        this.screen.startScreen();             // screens must be started
        this.screen.doResizeIfNecessary();     // resize screen if necessary
    }

    private boolean checkEndAndLoadLevel() throws IOException {
        if (currentLevel == maxLevel) return true;
        currentLevel++;
        try {
            String file = String.valueOf(currentLevel) + ".txt";
            arena.loadLevel(file);
            resizeScreen(file);
        } catch (FileNotFoundException e) {
            System.out.println("Error opening file");
            this.screen.close();
        }
        return false;
    }

    private void processKey(KeyStroke key) throws IOException {
        if (key.getKeyType() == KeyType.Character && key.getCharacter() == 'q') {
            this.screen.close();
            return;
        }
        arena.processKey(key);
        if (arena.verifyMonsterCollisions()) { // we don't need to check twice because the monster chases the hero
            if (arena.damageHero()) endOfGame(false);
        }
    }

    private void draw() throws IOException{
        this.screen.clear();
        this.arena.draw(this.screen.newTextGraphics());
        this.screen.refresh();
    }

    private void endOfGame(boolean won) throws IOException {
        this.screen.newTextGraphics().setForegroundColor(TextColor.Factory.fromString("#FFFF33"));
        this.screen.newTextGraphics().enableModifiers(SGR.BOLD);
        if (won)
            this.screen.newTextGraphics().putString(new TerminalPosition(10, 10), "Congrats! You Won");
        else
            this.screen.newTextGraphics().putString(new TerminalPosition(10, 10), "You died! :(");
        this.screen.newTextGraphics().putString(new TerminalPosition(10, 11), "Type 'r' to restart and 'q' to quit");
        this.screen.refresh();
        while (true) {
            KeyStroke key = screen.readInput();
            if (key.getKeyType() == KeyType.Character && key.getCharacter() == 'q') {
                this.screen.close();
                return;
            }
            else if (key.getKeyType() == KeyType.Character && key.getCharacter() == 'r') {
                this.currentLevel = 0;
                checkEndAndLoadLevel();
                this.arena.restart();
                return;
            }
        }
    }

    public void run() {
        while (true) {
            try {
                this.draw();
                KeyStroke key = screen.readInput();
                if (key.getKeyType() == KeyType.EOF) break;
                this.processKey(key);
                if (arena.isOver() && checkEndAndLoadLevel()) endOfGame(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
