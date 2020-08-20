package Views;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicScrollBarUI;

import Interface.ViewsManager;
import Interface.ViewsManager.View;
import Usage.Client;
import Usage.Group;
import Usage.Server;

import Utilities.TitleBarListener;

/**
 * 
 * @author Ali Abdulhameed
 *
 */
public class ActivityGroupsPage {

	private static ActivityGroupsPage instance;
	private View view;
	private Map<String, ActiveGroup> activeGroups;
	private Group currentGroup = null;
	private JLabel currentLabel = null;
	private JPanel leftPanel;
	private JPanel rightPanel;
	private JPanel groupsListPanel;
	private JScrollPane leftPanelScrollPane;
	private JScrollPane rightPanelScrollPane;
	private JTextArea userMessageTextField;
	private JScrollPane userMessageTextFieldPane;

	private JPanel createGroupPanel;
	private JPanel joinGroupPanel;

	private boolean activeScrolling = false;

	static {
		instance = new ActivityGroupsPage();
	}

	private ActivityGroupsPage() {

		activeGroups = new HashMap<>();
		view = ViewsManager.getInstance().createView("ActivityGroupsPage", new Utilities.Dimension(100, 100, 770, 691));
		leftPanel = new JPanel();
		leftPanel.setBounds(0, 0, 185, 691);
		leftPanel.setBackground(new Color(21, 20, 25));
		leftPanel.setLayout(null);

		leftPanelScrollPane = new JScrollPane();
		leftPanelScrollPane.setBounds(0, 0, 185, 670);
		leftPanelScrollPane.setBorder(null);
		leftPanelScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		groupsListPanel = new JPanel();
		groupsListPanel.setBackground(new Color(21, 20, 25));
		groupsListPanel.setLayout(new BoxLayout(groupsListPanel, BoxLayout.Y_AXIS));

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

		JLabel createGroup = new JLabel("Create Group", SwingConstants.CENTER);
		createGroupPanel = new JPanel();
		createGroupPanel.setBackground(new Color(26, 28, 32));
		createGroupPanel.setLayout(null);

		JTextField groupName = new JTextField("Enter group name");
		groupName.setBounds(100, 40, 350, 45);

		JButton submitNewGroup = new JButton("Create");
		submitNewGroup.setBounds(220, 115, 75, 35);

		JTextArea groupInformation = new JTextArea();
		groupInformation.setBounds(50, 220, 300, 400);
		groupInformation.setForeground(Color.WHITE);
		groupInformation.setBackground(new Color(26, 28, 32));
		groupInformation.setLineWrap(true);
		groupInformation.setForeground(Color.WHITE);
		groupInformation.setVisible(false);

		createGroupPanel.add(groupName);
		createGroupPanel.add(submitNewGroup);
		createGroupPanel.add(groupInformation);

		submitNewGroup.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (groupName.getText().replaceAll("\\s+", "").length() > 0) {
					String groupInfo = Server.getInstance().createGroup(groupName.getText());
					groupInformation.setText(groupInfo);
					groupInformation.setVisible(true);
				}

			}

		});

		createGroup.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
		createGroup.setForeground(Color.WHITE);
		createGroup.setBackground(new Color(45, 8, 62));
		createGroup.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {

				if (!createGroup.equals(currentLabel) && currentLabel != null) {
					currentLabel.setOpaque(false);
					currentLabel.revalidate();
					currentLabel.repaint();
				}
				userMessageTextField.setVisible(false);
				userMessageTextField.setEditable(false);
				userMessageTextFieldPane.setVisible(false);
				currentLabel = createGroup;
				rightPanelScrollPane.setViewportView(createGroupPanel);

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

				createGroup.setOpaque(true);
				createGroup.revalidate();
				createGroup.repaint();

			}

			@Override
			public void mouseExited(MouseEvent e) {
				if (!createGroup.equals(currentLabel)) {
					createGroup.setOpaque(false);
					createGroup.revalidate();
					createGroup.repaint();
				}

			}

		});

		JLabel joinGroupLabel = new JLabel("Join Group", SwingConstants.CENTER);
		joinGroupPanel = new JPanel();
		joinGroupPanel.setBackground(new Color(26, 28, 32));
		joinGroupPanel.setLayout(null);

		JTextField nameOfGroupToJoin = new JTextField("Enter group name");
		JTextField idOfGroupToJoin = new JTextField("Enter group ID");
		JTextField secretKeyOfGroupToJoin = new JTextField("Enter group secret key");

		nameOfGroupToJoin.setBounds(100, 40, 350, 45);
		idOfGroupToJoin.setBounds(100, 100, 350, 45);
		secretKeyOfGroupToJoin.setBounds(100, 160, 350, 45);

		JButton joinNewGroup = new JButton("Join");
		joinNewGroup.setBounds(220, 230, 75, 35);

		JLabel statusOfJoin = new JLabel();
		statusOfJoin.setBounds(50, 100, 300, 400);
		statusOfJoin.setForeground(Color.WHITE);
		statusOfJoin.setVisible(false);

		joinGroupPanel.add(nameOfGroupToJoin);
		joinGroupPanel.add(idOfGroupToJoin);
		joinGroupPanel.add(secretKeyOfGroupToJoin);
		joinGroupPanel.add(joinNewGroup);
		joinGroupPanel.add(statusOfJoin);

		joinNewGroup.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String groupName = nameOfGroupToJoin.getText();
				String groupID = idOfGroupToJoin.getText();
				String groupSecretKey = secretKeyOfGroupToJoin.getText();

				if ((groupName + idOfGroupToJoin + groupSecretKey).replaceAll("\\s+", "").length() > 0) {

					statusOfJoin.setText(Server.getInstance().joinGroup(groupName, groupID, groupSecretKey));
					statusOfJoin.setVisible(true);

				}

			}

		});

		joinGroupLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
		joinGroupLabel.setForeground(Color.WHITE);
		joinGroupLabel.setBackground(new Color(45, 8, 62));
		joinGroupLabel.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (!joinGroupLabel.equals(currentLabel) && currentLabel != null) {
					currentLabel.setOpaque(false);
					currentLabel.revalidate();
					currentLabel.repaint();
				}

				userMessageTextField.setVisible(false);
				userMessageTextField.setEditable(false);
				userMessageTextFieldPane.setVisible(false);
				currentLabel = joinGroupLabel;
				rightPanelScrollPane.setViewportView(joinGroupPanel);

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

				joinGroupLabel.setOpaque(true);
				joinGroupLabel.revalidate();
				joinGroupLabel.repaint();

			}

			@Override
			public void mouseExited(MouseEvent e) {
				if (!joinGroupLabel.equals(currentLabel)) {
					joinGroupLabel.setOpaque(false);
					joinGroupLabel.revalidate();
					joinGroupLabel.repaint();
				}

			}

		});

		groupsListPanel.add(backButton);
		groupsListPanel.add(createGroup);
		groupsListPanel.add(joinGroupLabel);

		leftPanelScrollPane.setViewportView(groupsListPanel);
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
		rightPanelScrollPane.getVerticalScrollBar().addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {

				activeScrolling = true;
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				activeScrolling = false;
			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {

			}

		});

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

		userMessageTextField = new JTextArea() {
			@Override
			public void setBorder(Border border) {
			}
		};

		userMessageTextField.setCaretColor(Color.WHITE);

		userMessageTextField.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "sendMessage");
		userMessageTextField.getActionMap().put("sendMessage", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {

				sendMessageToClients();
				userMessageTextField.setText("");

			}

		});
		userMessageTextField.setVisible(false);
		userMessageTextField.setEditable(false);
		userMessageTextField.setBackground(new Color(34, 33, 40));
		userMessageTextField.setForeground(Color.WHITE);
		userMessageTextField.setLineWrap(true);

		userMessageTextFieldPane = new JScrollPane(userMessageTextField) {
			@Override
			public void setBorder(Border border) {
			}
		};
		userMessageTextFieldPane.setVisible(false);
		userMessageTextFieldPane.setBounds(4, 610, 576, 35);
		userMessageTextFieldPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {

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
		rightPanel.add(userMessageTextFieldPane);
		view.addPanel("titleBarPanel", titleBarPanel);
		view.addPanel("leftPanel", leftPanel);
		view.addPanel("rightPanel", rightPanel);
	}

	private void sendMessageToClients() {

		if (currentGroup == null)
			return;

		String message = userMessageTextField.getText();

		if (message.replaceAll("\\s+", "").length() > 0)
			currentGroup.sendMessage(message);
	}

	public void updateClientList(Group group) {

		SwingUtilities.invokeLater(() -> {

			ActiveGroup activeGroup = new ActiveGroup(group);
			activeGroups.put(group.getGroupID(), activeGroup);
			JLabel groupLabel = new JLabel(group.getName(), SwingConstants.CENTER);
			groupLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
			groupLabel.setForeground(Color.WHITE);
			groupLabel.setBackground(new Color(45, 8, 62));

			JPanel groupPanel = new JPanel();
			groupPanel.setBackground(new Color(26, 28, 32));
			groupPanel.setLayout(new BoxLayout(groupPanel, BoxLayout.Y_AXIS));

			activeGroup.setName(groupLabel);
			activeGroup.setPanel(groupPanel);

			groupsListPanel.add(groupLabel);
			groupsListPanel.revalidate();
			groupsListPanel.repaint();

			groupLabel.addMouseListener(new MouseListener() {

				@Override
				public void mouseClicked(MouseEvent e) {
					if (!groupLabel.equals(currentLabel) && currentLabel != null) {
						currentLabel.setOpaque(false);
						currentLabel.revalidate();
						currentLabel.repaint();
					}

					userMessageTextField.setVisible(true);
					userMessageTextField.setEditable(true);
					userMessageTextField.getCaret().setVisible(true);
					userMessageTextFieldPane.setVisible(true);
					userMessageTextFieldPane.requestFocus();
					userMessageTextField.requestFocus();
					currentGroup = group;
					currentLabel = groupLabel;
					JPanel p = activeGroups.get(group.getGroupID()).getPanel();
					rightPanelScrollPane.setViewportView(p);
					updateActiveMessagesPanel(group);

				}

				@Override
				public void mousePressed(MouseEvent e) {
				}

				@Override
				public void mouseReleased(MouseEvent e) {
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					groupLabel.setOpaque(true);
					groupLabel.revalidate();
					groupLabel.repaint();
				}

				@Override
				public void mouseExited(MouseEvent e) {
					if (!groupLabel.equals(currentLabel)) {
						groupLabel.setOpaque(false);
						groupLabel.revalidate();
						groupLabel.repaint();
					}

				}

			});

		});

	}

	public void updateActiveMessagesPanel(Group group) {

		SwingUtilities.invokeLater(() -> {

			JPanel groupPanel = activeGroups.get(group.getGroupID()).getPanel();

			Object[] messagesLog = group.getMessages();
			ArrayList<String> messages = (ArrayList<String>) messagesLog[0];
			ArrayList<Client> clients = (ArrayList<Client>) messagesLog[1];

			for (int i = group.getStartingMessageIndex(); i < messages.size(); i++) {

				StringBuilder message = new StringBuilder(messages.get(i));

				for (int j = 66; j < message.length(); j += 70) {
					message.insert(j, "<br>");
				}
				SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
				String clientIPAddress = clients.get(i) != null ? clients.get(i).get_IP_ADDRESS().toString() : "";
				JLabel messageLabel = new JLabel("<html><font "
						+ (clients.get(i) == null ? "color='aqua'>" + Server.getInstance().getName()
								: ("color='red'>" + (clients.get(i).getName().equals("ME")
										|| clients.get(i).getName().equals("Anonymous")
												? clientIPAddress.substring(1, clientIPAddress.length())
												: clients.get(i).getName())))
						+ "</font> <font color='#585960' size='1'>" + dateFormat.format(new Date()) + "</font><br>"
						+ "<font color='white'>" + message + "</font><br><br></html>");
				messageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
				groupPanel.add(messageLabel);

			}

			groupPanel.revalidate();
			groupPanel.repaint();

			if (!activeScrolling) {
				JScrollBar vertical = rightPanelScrollPane.getVerticalScrollBar();
				rightPanelScrollPane.validate();
				vertical.setValue(vertical.getMaximum());
			}

		});

	}

	public static ActivityGroupsPage getInstance() {
		return instance;
	}

	private class ActiveGroup {

		private Group group;
		private JLabel label = null;
		private JPanel panel = null;

		private ActiveGroup(Group group) {
			this.group = group;
		}

		private JLabel getName() {
			return label;
		}

		private JPanel getPanel() {
			return panel;
		}

		private void setName(JLabel label) {
			this.label = label;
		}

		private void setName(String name) {
			label.removeAll();
			label.setText(name);
		}

		private void setPanel(JPanel panel) {
			this.panel = panel;
		}

	}

}
