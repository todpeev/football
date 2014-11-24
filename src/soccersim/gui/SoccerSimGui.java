package soccersim.gui;

import soccersim.base.DefendingSide;
import soccersim.base.GameHandler;
import soccersim.team.Team;
import javax.swing.*;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.Date;
import soccersim.team.brute.*;
/**
 * Creates a GUI to play through the Soccer Simulation.
 * @author frank hadder, mofications by Todor Peev
 * @version 1.0
 */
public class SoccerSimGui extends JApplet implements Runnable {
    private JPanel mainPanel;
    private FieldPanel fieldPanel;
    private JPanel buttonPanel;
    private JButton startButton;
    private JButton pauseButton;
    private JButton stopButton;
    private JLabel westTeamScore;
    private JLabel eastTeamScore;
    private JComboBox speedComboBox;
    private Thread paintThread;

	Team eastTeam = null;
	Team westTeam = null;
	
	private GameHandler game;
	private int speed = 50;
    private boolean isPaused = false;
    private boolean shouldStop = false;

    public SoccerSimGui() throws ClassNotFoundException, SQLException {
    	initializeComponents();
        addActionListeners();
        initializeThread();
        initializeComboBoxes();
        loadTeamDropDown(3, DefendingSide.East);
        loadTeamDropDown(3, DefendingSide.West);
    }
    
    private void initializeComponents() {
    	// setup GridLayout
    	GridLayout layout = new GridLayout();
    	layout.setRows(1);
    	layout.setColumns(1);
    	layout.setHgap(-1);
    	layout.setVgap(-1);
    	mainPanel = new JPanel();
    	mainPanel.setLayout(layout);
    	mainPanel.setBounds(20, 20, 804, 540);
    	mainPanel.setEnabled(true);
    	
    	// setup Field (top most Panel)
    	fieldPanel = new FieldPanel();
    	
    	// setup Button Panel (middle Panel)
    	FlowLayout buttonPanelLayout = new FlowLayout(FlowLayout.CENTER);
    	buttonPanelLayout.setHgap(10);
    	buttonPanelLayout.setVgap(5);
    	buttonPanel = new JPanel(buttonPanelLayout);
    	
    	westTeamScore = new JLabel("West Team: 0");
    	buttonPanel.add(westTeamScore);
    	
    	startButton = new JButton("Start");
    	buttonPanel.add(startButton);
    	
    	pauseButton = new JButton("Pause");
    	buttonPanel.add(pauseButton);
    	
    	stopButton = new JButton("Stop");
    	buttonPanel.add(stopButton);
    	
    	speedComboBox = new JComboBox();
    	buttonPanel.add(speedComboBox);
    	
    	eastTeamScore = new JLabel("East Team: 0");
    	buttonPanel.add(eastTeamScore);

    	JSplitPane mainSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, fieldPanel, buttonPanel);
    	mainSplit.setDividerSize(0);
    	mainSplit.setOrientation(0);
    	mainSplit.setEnabled(false);
    	mainPanel.add(mainSplit);
    }

    private void initializeThread() throws ClassNotFoundException, SQLException {
        if (eastTeam != null && westTeam != null) {
            game = new GameHandler(eastTeam, westTeam);
            fieldPanel.updateField(game.getField());
            fieldPanel.repaint();
            updateScoreLabels();
        }
        paintThread = new Thread(this);
    }

    private void initializeComboBoxes() {
        speedComboBox.addItem("Slower");
        speedComboBox.addItem("Slow");
        speedComboBox.addItem("Normal");
        speedComboBox.addItem("Fast");
        speedComboBox.addItem("Faster");
        speedComboBox.addItem("Instant");
        speedComboBox.setSelectedItem("Normal");


    }

    private void addActionListeners() {
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!paintThread.isAlive() && eastTeam != null && westTeam != null) {
                    shouldStop = false;
                    try {
						initializeThread();
					} catch (ClassNotFoundException e1) {
						e1.printStackTrace();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
                    paintThread.start();
                }
            }
        });

        pauseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                isPaused = !isPaused;
                if (isPaused) {
                    pauseButton.setText("Resume");
                } else {
                    pauseButton.setText("Pause");
                }
            }
        });

        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                shouldStop = true;
            }
        });

        speedComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selected = (String)speedComboBox.getSelectedItem();
                if (selected.equals("Normal")) {
                    speed = 75;
                } else if (selected.equals("Slower")) {
                    speed = 275;
                } else if (selected.equals("Slow")) {
                    speed = 125;
                } else if (selected.equals("Fast")) {
                    speed = 50;
                } else if (selected.equals("Faster")) {
                    speed = 25;
                } else if (selected.equals("Instant")) {
                	speed = 1;
                }
            }
        });
    }

    private void loadTeamDropDown(int selectedIndex, DefendingSide side) throws ClassNotFoundException, SQLException {
        Team team = null;

       
        switch (selectedIndex) {
            // brutal brutes
            case 3 : team = new BruteTeam(side); break;
            default : break;
        }
        if (side == DefendingSide.East) {
            eastTeam = team;
        } else {
            westTeam = team;
        }

        if (eastTeam != null && westTeam != null) {
            initializeThread();
        }
    }

    

    public void run() {
        while (game.gameInProgress() && !shouldStop) {
            while (isPaused) {
                try {paintThread.wait();} catch(Exception ex) {}
            }

            try {
				game.executeStep();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}

            updateScoreLabels();
            
            fieldPanel.updateField(game.getField());

            fieldPanel.repaint();
            
            if(game.hasScored()){
            	pause(3);
            	game.fieldReset();
            }
            try {java.lang.Thread.sleep(this.speed); } catch (Exception ex) {}
        }
    }

    public static void pause(int seconds){
        Date start = new Date();
        Date end = new Date();
        while(end.getTime() - start.getTime() < seconds * 1000){
            end = new Date();
        }
    }
    private void updateScoreLabels() {
        westTeamScore.setText("West team" + ": " + game.getScore().getScore(DefendingSide.West));
        eastTeamScore.setText("East team" + ": " + game.getScore().getScore(DefendingSide.East));
    }

    public void init() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setSize(new Dimension(800,500));
        try {
			add(new SoccerSimGui().mainPanel);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
        setVisible(true);
    }

}

