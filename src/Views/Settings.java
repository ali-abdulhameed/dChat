package Views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicScrollBarUI;

import Interface.ViewsManager;
import Interface.ViewsManager.View;
import Usage.Server;
import Utilities.LabelHover;
import Utilities.TitleBarListener;

/**
 * 
 * @author Ali Abdulhameed
 *
 */
public class Settings {

	private static Settings instance;
	private View view;
	private JPanel leftPanel;
	private JPanel rightPanel;
	private JPanel menuPanel;
	private JScrollPane leftPanelScrollPane;
	private JScrollPane rightPanelScrollPane;

	private JPanel generalPanel = null;
	private JPanel profile = null;
	private JPanel about = null;

	static {
		instance = new Settings();
	}

	private Settings() {

		view = ViewsManager.getInstance().createView("Settings", new Utilities.Dimension(100, 100, 770, 691));
		leftPanel = new JPanel();
		leftPanel.setBounds(0, 0, 185, 691);
		leftPanel.setBackground(new Color(21, 20, 25));
		leftPanel.setLayout(null);

		leftPanelScrollPane = new JScrollPane();
		leftPanelScrollPane.setBounds(0, 0, 185, 670);
		leftPanelScrollPane.setBorder(null);
		leftPanelScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		menuPanel = new JPanel();
		menuPanel.setBackground(new Color(21, 20, 25));
		menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));

		JLabel backButton = new JLabel(new ImageIcon(getClass().getResource("/return_button.png")),
				SwingConstants.CENTER);

		backButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
		backButton.setForeground(Color.WHITE);
		backButton.setBackground(new Color(26, 25, 31));
		backButton.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				ViewsManager.getInstance().setActiveView("Dashboard");

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
				backButton.setOpaque(true);
				backButton.revalidate();
				backButton.repaint();

			}

			@Override
			public void mouseExited(MouseEvent e) {
				backButton.setOpaque(false);
				backButton.revalidate();
				backButton.repaint();

			}
		});

		JLabel general = new JLabel("General", SwingConstants.CENTER);
		general.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
		general.setForeground(Color.WHITE);
		general.setBackground(new Color(45, 8, 62));
		general.setOpaque(true);
		general.addMouseListener(new LabelHover(general, null, true));
		generalPanel = createGeneral();

		JLabel profile = new JLabel("Profile", SwingConstants.CENTER);
		profile.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
		profile.setForeground(Color.WHITE);
		profile.setBackground(new Color(45, 8, 62));
		profile.addMouseListener(new LabelHover(profile, null, false));

		JLabel about = new JLabel("About", SwingConstants.CENTER);
		about.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
		about.setForeground(Color.WHITE);
		about.setBackground(new Color(45, 8, 62));
		about.addMouseListener(new LabelHover(about, "About", false));

		menuPanel.add(backButton);
		menuPanel.add(general);
		// menuPanel.add(profile);
		// menuPanel.add(about);

		leftPanelScrollPane.setViewportView(menuPanel);
		leftPanel.add(leftPanelScrollPane);

		rightPanel = new JPanel();
		rightPanel.setBackground(new Color(26, 28, 32));
		rightPanel.setBounds(185, 40, 583, 650);
		rightPanel.setLayout(null);

		rightPanelScrollPane = new JScrollPane();
		rightPanelScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		rightPanelScrollPane.setBounds(0, 20, 583, 585);
		rightPanelScrollPane.getViewport().setBackground(new Color(26, 28, 32));
		rightPanelScrollPane.setBorder(null);
		rightPanelScrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {

			@Override
			protected JButton createIncreaseButton(int orientation) {
				return createEmptyButton();
			}

			@Override
			protected JButton createDecreaseButton(int orientation) {
				return createEmptyButton();
			}

			@Override
			protected void configureScrollBarColors() {
				this.trackColor = new Color(21, 20, 25);
				this.thumbColor = new Color(45, 8, 62);
			}

			private JButton createEmptyButton() {
				JButton button = new JButton();
				button.setPreferredSize(new Dimension(0, 0));
				;
				button.setMaximumSize(new Dimension(0, 0));
				button.setMinimumSize(new Dimension(0, 0));
				return button;
			}
		});

		rightPanelScrollPane.setViewportView(generalPanel);

		JPanel titleBarPanel = new JPanel();
		titleBarPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		titleBarPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(38, 39, 46)));
		titleBarPanel.setBackground(new Color(26, 28, 32));
		titleBarPanel.setBounds(0, 0, 770, 40);
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

		rightPanel.add(rightPanelScrollPane);
		view.addPanel("titleBarPanel", titleBarPanel);
		view.addPanel("leftPanel", leftPanel);
		view.addPanel("rightPanel", rightPanel);
	}

	public static Settings getInstance() {
		return instance;
	}

	private JPanel createGeneral() {
		if (generalPanel == null) {

			JPanel panel = new JPanel();
			panel.setLayout(null);
			panel.setBackground(new Color(26, 28, 32));

			JTextField publicKeyBroadcastFrequency = new JTextField(
					"Interest signaling frequency. Default: every 3 seconds");
			publicKeyBroadcastFrequency.setBounds(100, 40, 350, 45);

			JTextField numberOfUsersAllowed = new JTextField("Maximum number of users. Default: Infinite");
			numberOfUsersAllowed.setBounds(100, 100, 350, 45);

			JButton submitChanges = new JButton("Update");
			submitChanges.setBounds(220, 160, 75, 35);

			JLabel submissionStatus = new JLabel();
			submissionStatus.setBounds(50, 120, 400, 400);
			submissionStatus.setForeground(Color.WHITE);
			submissionStatus.setVisible(false);

			submitChanges.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					submissionStatus.setText("");
					boolean valid = true;
					String frequencyText = publicKeyBroadcastFrequency.getText();
					String maxNumberText = numberOfUsersAllowed.getText();

					if (frequencyText.replaceAll("[0-9]", "").length() > 0) {
						if (!frequencyText.equals("Interest signaling frequency. Default: every 3 seconds")) {
							submissionStatus.setText("Error: frequency must be a numeric value.<br>");
							valid = false;
						}
					}

					if (maxNumberText.replaceAll("[0-9]", "").length() > 0) {

						if (!maxNumberText.equals("Maximum number of users. Default: Infinite")) {
							submissionStatus.setText(submissionStatus.getText() + "Error: maximum number of"
									+ "users allowed must be<br>a numeric value.");
							valid = false;
						}

					}

					if (!valid) {
						submissionStatus.setText("<html>" + submissionStatus.getText() + "</html>");

					} else {
						boolean changes = false;
						if (!frequencyText.equals("Interest signaling frequency. Default: every 3 seconds")) {
							Server.getInstance().getActiveBroadcasts().get("publicKey")
									.setFrequency(Integer.parseInt(frequencyText) * 1000);
							changes = true;
						}

						if (!maxNumberText.equals("Maximum number of users. Default: Infinite")) {

							Server.getInstance().setMaximumNumberOfUsersAllowed(Integer.parseInt(maxNumberText));
							changes = true;

						}
						if (changes)
							submissionStatus.setText("Settings have been updated successfully.");

					}
					submissionStatus.setVisible(true);

				}
			});

			panel.add(publicKeyBroadcastFrequency);
			panel.add(numberOfUsersAllowed);
			panel.add(submitChanges);
			panel.add(submissionStatus);
			return panel;

		} else
			return generalPanel;
	}

}
