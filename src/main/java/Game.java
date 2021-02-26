import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.input.KeyStroke;

import javax.swing.*;
import java.io.IOException;

import static com.googlecode.lanterna.input.KeyType.ArrowUp;

public class Game {
    private Screen screen;
    private Hero hero;

    public Game() {
        try {
            this.hero = new Hero(10, 10);

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

    private void moveHero(Position position) {
        this.hero.setPosition(position);
    }

    private void processKey(KeyStroke key) throws IOException {
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
            case Character:
                if (key.getCharacter() == 'q') this.screen.close();
                break;
            default:
                break;
        }
    }

    private void draw() throws IOException{
        this.screen.clear();
        this.hero.draw(this.screen);
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
