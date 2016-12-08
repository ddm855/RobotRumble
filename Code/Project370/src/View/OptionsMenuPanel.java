package View;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.google.gson.*;

import Model.Robot;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.io.*;
import java.net.*;


public class OptionsMenuPanel extends JPanel{
	
	private final String hostName = "gpu0.usask.ca";
	private final String request = "{ \"list-request\" : { \"data\" : \"brief\" }}";
	private final int port = 20001;
	
	/** @public Initializes Options menu. */
	public OptionsMenuPanel(){
		super();
		setSize(1400,800);
		setLayout(new GridLayout(2,2));
		String serverData = "";
		
		try {
			Socket socket = new Socket(hostName, port);
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			out.println(request);
			
			serverData = in.readLine();
			serverData = "{ \"robots\":" + serverData + "}";
			socket.close();
			
			JsonElement jElement = new JsonParser().parse(serverData);
			
			JsonObject jObject = jElement.getAsJsonObject();
			JsonArray briefInfo = jObject.getAsJsonArray("robots");

			String robotTeam = "";
			String robotClass = "";
			String robotName = "";
			int robotMatches = 0;
			int robotWins = 0;
			int robotLosses = 0;
			
			JPanel[] robotPanels = new JPanel[briefInfo.size()];
			
			for(int i = 0; i < briefInfo.size(); i++){
				JsonObject curObj = briefInfo.get(i).getAsJsonObject().get("script").getAsJsonObject();
				robotTeam = curObj.get("team").getAsString();
				robotClass = curObj.get("class").getAsString();
				robotName = curObj.get("name").getAsString();
				robotMatches = curObj.get("matches").getAsInt();
				robotWins = curObj.get("wins").getAsInt();
				robotLosses = curObj.get("losses").getAsInt();
				
				robotPanels[i] = new RobotInfoPanel(briefInfo.get(i).getAsJsonObject(), robotTeam, 
										robotClass, robotName, robotMatches, robotWins, robotLosses);
				this.add(robotPanels[i]);
				robotPanels[i].setVisible(true);
			}
		} catch (IOException e) {
			System.err.println("Could not find host. Robot Librarian is disconnected from system.");
		} finally {
			repaint();
			revalidate();
		}
	}
	
	public static void main(String args[]){
		JFrame testFrame = new JFrame("IN-GAME-MENU-PANEL-TEST");
    	testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    	testFrame.setVisible(true);
    	testFrame.setSize(1200, 1000);
    	testFrame.setResizable(false);
    	
    	testFrame.add(new OptionsMenuPanel());
	}
	
	/** JPanel that displays information on a robot to be imported from the robot librarian server */
	private class RobotInfoPanel extends JPanel{
		
		private static final long serialVersionUID = 1L;
		
		/** The JSON object representing the current robot */
		JsonObject script;
		
		/** The name of the robot */
		String name;
		
		/** @public Initializes Options menu. */
		public RobotInfoPanel(JsonObject robotScript, String team, String robotClass, 
								String robotName, int matches, int wins, int losses){
			super();
			setLayout(new GridLayout(8,1));
			setSize(100,100);
			script = robotScript;
			name = robotName;
			add(new JLabel("Team: " + team));
			add(new JLabel("Class: " + robotClass));
			add(new JLabel("Name: " + robotName));
			add(new JLabel("Matches: " + matches));
			add(new JLabel("Wins: " + wins));
			add(new JLabel("Losses: " + losses));
			this.setBorder(BorderFactory.createLineBorder(Color.black));
			JButton importButton = new JButton("Import");
			importButton.addActionListener(new ImportButtonListener(importButton));
			add(importButton);
			revalidate();
		}
		
		private class ImportButtonListener implements ActionListener{
			JButton button;
			
			public ImportButtonListener(JButton _button){
				super();
				button = _button;
			}
	    	public void actionPerformed(ActionEvent e){
	    		button.setText("Imported!");
	    		button.setEnabled(false);
	    		
	    		try{
	    			Writer writer = new FileWriter("src/Model/scripts/" + name + ".json");
	    			writer.write(script.toString());
	    			writer.close();
	    		} catch (IOException e1) {
					e1.printStackTrace();
				} finally {
	    			;
	    		}
	    	}
	    }
	}
}




