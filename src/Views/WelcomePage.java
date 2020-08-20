package Views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

import Interface.ViewsManager;
import Interface.ViewsManager.View;
import Usage.Server;
import Utilities.TitleBarListener;

/**
 * 
 * @author Ali Abdulhameed
 *
 */
public class WelcomePage {

	private static WelcomePage instance;
	private View view;
	private JLabel errorMessage;
	private JTextField userNameTextField;

	static {
		instance = new WelcomePage();
	}

	private WelcomePage() {

		view = ViewsManager.getInstance().createView("WelcomePage", new Utilities.Dimension(100, 100, 350, 500));
		JPanel panel = new JPanel();
		panel.setBounds(22, 198, 300, 140);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBackground(new Color(26, 28, 32));
		errorMessage = new JLabel();
		errorMessage.setForeground(Color.WHITE);
		errorMessage.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		errorMessage.setVisible(false);
		userNameTextField = new JTextField("Enter a username");
		userNameTextField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
		userNameTextField.setCaretColor(Color.WHITE);
		userNameTextField.setBackground(new Color(21, 20, 25));
		userNameTextField.setForeground(Color.WHITE);
		userNameTextField.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				proccessNewUserName();

			}
		});

		panel.add(errorMessage);
		panel.add(userNameTextField);

		JPanel titleBarPanel = new JPanel();
		titleBarPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		titleBarPanel.setBackground(new Color(26, 28, 32));
		titleBarPanel.setBounds(0, 0, 350, 40);
		titleBarPanel.addMouseListener(TitleBarListener.getInstance());
		titleBarPanel.addMouseMotionListener(TitleBarListener.getInstance());
		ImageIcon exit_button = new ImageIcon(getClass().getResource("/exit_button.png"));
		ImageIcon exit_button_hover = new ImageIcon(getClass().getResource("/exit_button_hover.png"));
		JLabel exit = new JLabel();
		exit.setIcon(exit_button);
		exit.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				ViewsManager.getInstance().dispose();
				System.exit(0);

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				exit.setIcon(exit_button_hover);

			}

			@Override
			public void mouseExited(MouseEvent e) {
				exit.setIcon(exit_button);

			}
		});
		ImageIcon minimize_button = new ImageIcon(getClass().getResource("/minimize_button.png"));
		ImageIcon minimize_button_hover = new ImageIcon(getClass().getResource("/minimize_button_hover.png"));
		JLabel minimize = new JLabel();

		minimize.setIcon(minimize_button);
		minimize.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {

				ViewsManager.getInstance().minimize();

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				minimize.setIcon(minimize_button_hover);

			}

			@Override
			public void mouseExited(MouseEvent e) {
				minimize.setIcon(minimize_button);

			}
		});

		titleBarPanel.add(minimize);
		titleBarPanel.add(exit);

		view.addPanel("titleBarPanel", titleBarPanel);
		view.addPanel("WelcomePage", panel);
	}

	public static WelcomePage getInstance() {
		return instance;
	}

	public View getView() {
		return view;
	}

	private void proccessNewUserName() {
		boolean valid = true;
		errorMessage.setText("<html>");
		String input = userNameTextField.getText();

		if (input.length() == 0) {
			errorMessage.setText(errorMessage.getText() + "You must enter a username<br>");
			valid = false;
		}

		if (input.length() > 16) {
			errorMessage.setText(errorMessage.getText() + "Name cannot exceed 16 characters<br>");
			valid = false;

		}

		if (input.replaceAll("\\s+", "").length() != input.length()) {
			errorMessage.setText(errorMessage.getText() + "Name cannot contain spaces<br>");
			valid = false;
		}

		errorMessage.setText(errorMessage.getText() + "</html>");

		if (!valid) {
			errorMessage.setVisible(true);

		} else {
			Server.getInstance().setName(input);
			ViewsManager.getInstance().setActiveView("Dashboard");
			Dashboard.getInstance().updateUserName(input);
		}

	}

}
