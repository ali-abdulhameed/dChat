package Views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
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
public class Dashboard {

	Server server = Server.getInstance();
	private static Dashboard instance;
	private View view;
	private JLabel userName;
	private JPanel leftPanel;
	private JPanel rightPanel;
	private JPanel menuPanel;
	private JScrollPane leftPanelScrollPane;
	private JScrollPane rightPanelScrollPane;
	private JPanel statsPanel;
	private JPanel friendsPanel;
	private JPanel groupsPanel;
	private JPanel activeBroadcastsPanel;

	static {
		instance = new Dashboard();
	}

	private Dashboard() {
		view = ViewsManager.getInstance().createView("Dashboard", new Utilities.Dimension(100, 100, 770, 691));
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

		userName = new JLabel("Welcome, " + server.getName());
		userName.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
		userName.setForeground(Color.WHITE);

		JLabel dashboard = new JLabel("Dashboard", SwingConstants.CENTER);
		dashboard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
		dashboard.setForeground(Color.WHITE);
		dashboard.setBackground(new Color(45, 8, 62));
		dashboard.setOpaque(true);
		dashboard.addMouseListener(new LabelHover(dashboard, null, true));

		JLabel friends = new JLabel("Friends", SwingConstants.CENTER);
		friends.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
		friends.setForeground(Color.WHITE);
		friends.setBackground(new Color(45, 8, 62));
		friends.addMouseListener(new LabelHover(friends, "ActivityPage", false));

		JLabel groups = new JLabel("Groups", SwingConstants.CENTER);
		groups.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
		groups.setForeground(Color.WHITE);
		groups.setBackground(new Color(45, 8, 62));
		groups.addMouseListener(new LabelHover(groups, "ActivityGroupsPage", false));

		JLabel settings = new JLabel("Settings", SwingConstants.CENTER);
		settings.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
		settings.setForeground(Color.WHITE);
		settings.setBackground(new Color(45, 8, 62));
		settings.addMouseListener(new LabelHover(settings, "Settings", false));

		menuPanel.add(userName);
		menuPanel.add(dashboard);
		menuPanel.add(friends);
		menuPanel.add(groups);
		menuPanel.add(settings);

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

		statsPanel = new JPanel();
		statsPanel.setLayout(null);
		statsPanel.setBackground(new Color(26, 28, 32));

		friendsPanel = new JPanel();
		friendsPanel.setLayout(new BoxLayout(friendsPanel, BoxLayout.Y_AXIS));
		friendsPanel.setBackground(new Color(21, 20, 25));
		friendsPanel.setBounds(15, 0, 265, 150);

		JLabel numberOfFriends = new JLabel(
				"<html><br><span style='font-size: 10px; color:#1484ff'>•</span><font color='white'>"
						+ " Friends    </font><font size='2' color='#7d7f85'>#" + server.getClients().size()
						+ "</font><html>");

		JLabel friendsDesc = new JLabel("<html><br><font color='#7d7f85'>This displays the number of active users that"
				+ " have signaled their intertest in communicating on the given" + " network.</font></html>");

		friendsPanel.add(numberOfFriends);
		friendsPanel.add(friendsDesc);

		groupsPanel = new JPanel();
		groupsPanel.setLayout(new BoxLayout(groupsPanel, BoxLayout.Y_AXIS));
		groupsPanel.setBackground(new Color(21, 20, 25));
		groupsPanel.setBounds(295, 0, 265, 150);

		JLabel numberOfGroups = new JLabel(
				"<html><br><span style='font-size: 10px; color:#1484ff'>•</span> <font color='white'>"
						+ "Groups    </font><font size='2' color='#7d7f85'>#" + server.getGroups().size()
						+ "</font><html>");
		JLabel groupsDesc = new JLabel(
				"<html><br><font color='#7d7f85'>This displays the number of groups you're" + "a member of</html>");

		groupsPanel.add(numberOfGroups);
		groupsPanel.add(groupsDesc);

		activeBroadcastsPanel = new JPanel();
		activeBroadcastsPanel.setLayout(new BoxLayout(activeBroadcastsPanel, BoxLayout.Y_AXIS));
		activeBroadcastsPanel.setBackground(new Color(21, 20, 25));
		activeBroadcastsPanel.setBounds(15, 165, 265, 200);

		JLabel numberOfActiveBroadcasts = new JLabel("<html><span style='font-size: 10px; color:#1484ff'>•</span> "
				+ "<font color='white'>Broadcasts    </font><font size='2' color='#7d7f85'>#"
				+ server.getActiveBroadcasts().size() + "</font><html>");
		JLabel activeBroadcastsDesc = new JLabel(
				"<html><br><font color='#7d7f85'>This displays the number active broadcasts."
						+ "<br>A broadcast sends a message to all devices connected on a given network.<br>This app contains 1 defualt"
						+ "broadcast which signals interest of communication with all devices"
						+ "running<br>the same app on the network.</html>");

		activeBroadcastsPanel.add(numberOfActiveBroadcasts);
		activeBroadcastsPanel.add(activeBroadcastsDesc);

		groupsPanel.setLayout(new BoxLayout(groupsPanel, BoxLayout.Y_AXIS));
		groupsPanel.setBackground(new Color(21, 20, 25));
		statsPanel.add(friendsPanel);
		statsPanel.add(groupsPanel);
		statsPanel.add(activeBroadcastsPanel);
		rightPanelScrollPane.setViewportView(statsPanel);

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

	public static Dashboard getInstance() {
		return instance;
	}

	public View getView() {
		return view;
	}

	public void updateUserName(String name) {
		SwingUtilities.invokeLater(() -> {
			userName.setText("Welcome, " + name);
		});
	}

	public void updateNumberOfFriends() {
		SwingUtilities.invokeLater(() -> {

			JLabel numberOfFriends = (JLabel) friendsPanel.getComponent(0);
			numberOfFriends
					.setText("<html><br><span style='font-size: 10px; color:#1484ff'>•</span><font color='white'>"
							+ " Friends    </font><font size='2' color='#7d7f85'>#" + server.getClients().size()
							+ "</font><html>");

		});
	}

	public void updateNumberOfGroups() {
		SwingUtilities.invokeLater(() -> {

			JLabel numberOfgroups = (JLabel) groupsPanel.getComponent(0);
			numberOfgroups
					.setText("<html><br><span style='font-size: 10px; color:#1484ff'>•</span> <font color='white'>"
							+ "Groups    </font><font size='2' color='#7d7f85'>#" + server.getGroups().size()
							+ "</font><html>");
		});
	}

	public void updateNumberOfActiveBroadcasts() {
		SwingUtilities.invokeLater(() -> {

			JLabel numberOfActiveBroadcasts = (JLabel) activeBroadcastsPanel.getComponent(0);
			numberOfActiveBroadcasts.setText("<html><span style='font-size: 10px; color:#1484ff'>•</span> "
					+ "<font color='white'>Broadcasts    </font><font size='2' color='#7d7f85'>#"
					+ server.getActiveBroadcasts().size() + "</font><html>");
		});
	}

}
