package com.nhance.assignment.game;

import java.awt.Container;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.nhance.assignment.datastore.GameDetails;

public class Main {

	private JFrame jFrame;
	private Container container;
	private JLabel userIdLabel;
	private JLabel scoreLabel;
	private JLabel gameResultLabel;

	private JTextField scoreTxt;
	private JTextField userIdTxt;
	
	private JButton startBtn;
	private JButton submitBtn;

	private JTextArea resultArea;

	private Game game;
	public static void main(String[] args) throws Exception {

		new Main().createUI();
	}

	private void createUI() {
		jFrame = new JFrame();

		userIdLabel = new JLabel("Enter User Id: ");
		userIdLabel.setBounds(10, 10, 130, 25);

		userIdTxt = new JTextField(10);
		userIdTxt.setBounds(100, 10, 100, 25);

		startBtn = new JButton("Start Game");
		startBtn.setBounds(210, 10, 100, 25);
		
		scoreLabel = new JLabel("Enter Score:");
		scoreLabel.setBounds(10, 50, 130, 25);

		scoreTxt = new JTextField(10);
		scoreTxt.setBounds(100, 50, 100, 25);

		submitBtn = new JButton("Submit");
		submitBtn.setBounds(210, 50, 100, 25);

		gameResultLabel = new JLabel("Next Game Info:");
		gameResultLabel.setBounds(10, 85, 130, 25);

		resultArea = new JTextArea();
		resultArea.setBounds(10, 125, 300, 600);
		resultArea.setLineWrap(true);
		resultArea.setWrapStyleWord(true); // word wrapping enabled
		
		submitBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				long gameScore = Long.parseLong(scoreTxt.getText());
				scoreTxt.setText("");
				try {
					User user = game.submitScore(gameScore);
					GameDetails currentGame = user.getCurrentGame();
					double progressPercentage = user.getProgressPercentage();
					long userScore = user.getScore();
					if(currentGame != null ){
						resultArea.append("Total Score: "+userScore+"\n\n");
						resultArea.append("Progress % : "+progressPercentage+"\n\n");
						resultArea.append("Next Game: "+currentGame+"\n"
								+ "=====================================\n");
						
					}else{
						JOptionPane.showMessageDialog(jFrame,
								"User has completed all the levels.", "INFO",
								JOptionPane.INFORMATION_MESSAGE);
						resultArea.setText("Final Score: "+userScore+"\n\n");
					}
					
				} catch (Exception e) {
					JOptionPane.showMessageDialog(jFrame, e.getMessage(),"ERROR",
							JOptionPane.ERROR_MESSAGE);
				}
				
			}
		});
		
		startBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int userID =  Integer.parseInt(userIdTxt.getText());
				game = Game.getInstance();
				User user = game.startGame(userID);
				userIdTxt.setEditable(false);
				startBtn.setEnabled(false);
				jFrame.setTitle("UserId : "+userID);
				resultArea.append("Next Game: "+user.getCurrentGame()+"\n"
							+ "=====================================\n");
				
			}
		});
		jFrame.setResizable(false);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		container = jFrame.getContentPane();
		container.setLayout(null);
		
		container.add(userIdLabel);
		container.add(userIdTxt);
		container.add(startBtn);
		container.add(scoreLabel);
		container.add(scoreTxt);
		container.add(submitBtn);
		container.add(gameResultLabel);
		container.add(resultArea);
		
		Insets insets = jFrame.getInsets();
		jFrame.setSize(340 + insets.left + insets.right, 800 + insets.top + insets.bottom);
		jFrame.setVisible(true);
		

	}
}
