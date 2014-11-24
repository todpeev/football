package soccersim.gui;

import soccersim.base.Field;
import soccersim.base.DefendingSide;
import soccersim.team.Player;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.net.URL;

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

    int counter;
    
    public void paintComponent(Graphics g) {
        g.drawImage(fieldImage, 0, 0, null);
        drawBall(g);
        drawPlayers(g);
        if(field!=null && field.getHasShot()){
        	if(counter < 6){
        		drawShot(g, field.getShooter().getDefendingSide());
        		
        	} else {
        		field.setHasShot(false);
        		counter = -1;
        	}
        	
        	counter++;
        }
        if(field!=(null) && field.playerHasScored()){
        	drawGoal(g, field.getSideThatScored());
        	
    	}
    }
    public void drawShot(Graphics g, DefendingSide side){
    	String text;  
    	if(side== DefendingSide.East ){
    		g.setColor(Color.blue);
    		text = "blue player shoots!"; 
    	} else {
    		g.setColor(Color.red);
    		text = "red player shoots!"; 
    	}
    	g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
    	if(side== DefendingSide.East ){
    		 g.drawString(text,210 , 180);
    	} else {
    		g.drawString(text,420 , 180); 
    	}
    	
    	
    }
    
    public void drawGoal(Graphics g, DefendingSide side){
    	String text; 
    	if(side== DefendingSide.East ){
    		g.setColor(Color.blue);
    		text = "Blue team scored!";
    	} else {
    		g.setColor(Color.red);
    		text = "Red team scored!";
    	}
    	
    	
    	g.setFont(new Font("TimesRoman", Font.PLAIN, 50));
    	g.drawString(text, 210, 230);
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
