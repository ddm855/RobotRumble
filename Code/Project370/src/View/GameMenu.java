package View;

import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.*;

/**
 * This GameMenu class contains the GUI for the main entry point
 * of the program. This menu moves the player between the options,
 * the game, and exiting the application.
 */
public class GameMenu extends JFrame{
	
	private static final long serialVersionUID = 1L;

	public static void main(String[] args){
		try {
            // Set System L&F
        UIManager.setLookAndFeel(
            UIManager.getSystemLookAndFeelClassName());
	    } 
	    catch (UnsupportedLookAndFeelException e) {
	       // handle exception
	    }
	    catch (ClassNotFoundException e) {
	       // handle exception
	    }
	    catch (InstantiationException e) {
	       // handle exception
	    }
	    catch (IllegalAccessException e) {
	       // handle exception
	    }
		
		
	}
	
	/** Constructs the gmenu = new GameMenu();ame menu, the system's entry point */
    public GameMenu(ActionListener actionListener) {
    	
    	super("The Legend of Dutchyn 370: A Program is Born");
    	
    	this.setSize(400, 400);
    	this.setVisible(true);
    	
    	//Initialize the menu panel
    	JPanel menuPanel;
    	menuPanel = new JPanel(new GridLayout(3, 1));
    	menuPanel.setSize(400,  400);
    	menuPanel.setVisible(true);
    	menuPanel.doLayout();
    	this.add(menuPanel);
    	
    	//Initialize the play button and add it to the menu panel
    	JButton playButton;
    	playButton = new JButton("Play");
    	playButton.setSize(50, 50);
    	playButton.setVisible(true);
    	playButton.setActionCommand("play");
    	playButton.addActionListener(actionListener);
    	
    	menuPanel.add(playButton);
    	
    	//Initialize the option button and add it to the menu panel
    	JButton optionButton;
    	optionButton = new JButton("Options");
    	optionButton.setSize(50, 50);
    	optionButton.setVisible(true);
    	optionButton.setActionCommand("options");
    	optionButton.addActionListener(actionListener);
    	
    	menuPanel.add(optionButton);
    	
    	//Initialize the exit button and add it to the menu panel
    	JButton exitButton;
    	exitButton = new JButton("Exit");
    	exitButton.setSize(50, 50);
    	exitButton.setVisible(true);
    	exitButton.setActionCommand("exit");
    	exitButton.addActionListener(actionListener);
    	
    	menuPanel.add(exitButton);
    }
    
    

    
}
