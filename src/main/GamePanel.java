package main;

import obj.SuperObject;
import obj.OBJ_Key;
import tile.TileManager;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import entity.Player;

public class GamePanel extends JPanel implements Runnable {

    final int originalTileSize = 16; // 16x16 
    final int scale = 3; 

    public final int tileSize = originalTileSize * scale; // 48x48 
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol; // 768 wide
    public final int screenHeight = tileSize * maxScreenRow; // 576 tall

    //world settings
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int WorldWidth = tileSize * maxWorldCol;
    public final int WorldHeight = tileSize * maxWorldRow;

    // FPS
    int FPS = 60;
    double drawInterval; 

    TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler();
    Thread gameThread; 
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public Player player = new Player(this,keyH);
    public SuperObject obj[] = new SuperObject[10];


    public GamePanel() {
        // size of game panel
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black); // background color
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        
        drawInterval = 1000000000 / FPS;
    }

    public void setupGame() {

        aSetter.setObjects();

    }

    public void startGameThread() {
        gameThread = new Thread(this); 
        gameThread.start(); 
    }

    @Override
    public void run() {
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while(gameThread != null) {
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if(delta >= 1) {
                update();
                repaint(); 
                delta--;    
                drawCount++;
            }

            if (timer >= 1000000000) {
                System.out.println("FPS: " + drawCount);
                timer = 0;
                drawCount = 0;
            }
        }
    }

    public void update() {
        player.update();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        //Tile
        tileM.draw(g2);

        //object
        for(int i = 0; i < obj.length; i++) {
            if(obj[i] != null) {
                obj[i].draw(g2, this);
            }
        }

        //collision check
        cChecker.checkTile(player);

        //player
        player.draw(g2);
        g2.dispose();
    }
}