package soccersim.gui;

import soccersim.base.Field;
import soccersim.base.DefendingSide;
import soccersim.team.Player;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: frank
 * Date: Sep 18, 2007
 * Time: 9:25:48 AM
 * To change this template use File | Settings | File Templates.
 */
public class FieldPanel extends JPanel {

    private Image fieldImage;
    private Image ballImage;
    private Image eastPlayerImage;
    private Image westPlayerImage;
    private Field field;

    public FieldPanel() {
        getImages();
        initializeProperties();
    }

    private void getImages() {
        try {
            fieldImage = ImageIO.read(new URL(getClass().getResource("images/field.png"), "field.png"));
            ballImage = ImageIO.read(new URL(getClass().getResource("images/ball.png"), "ball.png"));
            eastPlayerImage = ImageIO.read(new URL(getClass().getResource("images/eastPlayer.png"), "eastPlayer.png"));
            westPlayerImage = ImageIO.read(new URL(getClass().getResource("images/westPlayer.png"), "westPlayer.png"));
        } catch (IOException e) {
            e.printStackTrace(); 
        }
    }

    private void initializeProperties() {
        Dimension size = new Dimension(fieldImage.getWidth(null), fieldImage.getHeight(null));
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setSize(size);
        setLayout(null);
    }

    public void paintComponent(Graphics g) {
        g.drawImage(fieldImage, 0, 0, null);
        drawBall(g);
        drawPlayers(g);
    }

    private void drawBall(Graphics g) {
        if (field != null) {
            g.drawImage(ballImage, getXFrom(field.getBallXCoordinate()), getYFrom(field.getBallYCoordinate()), null);
        }
    }

    private void drawPlayers(Graphics g) {
        if (field != null) {
            for(Player player : field.getPlayers()) {
                if (player.getDefendingSide() == DefendingSide.East) {
                    g.drawImage(eastPlayerImage, getXFrom(player.getXCoordinate()), getYFrom(player.getYCoordinate()), null);
                } else {
                    g.drawImage(westPlayerImage, getXFrom(player.getXCoordinate()), getYFrom(player.getYCoordinate()), null);
                }
            }
        }
    }

    private int getXFrom(int x) {
        return (x * 10) - 5;
    }

    private int getYFrom(int y) {
        return (y * 20) - 10;
    }

    public void updateField(Field field) {
        this.field = field; 
    }


}
