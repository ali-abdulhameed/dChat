package Views;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicScrollBarUI;

import Interface.ViewsManager;
import Interface.ViewsManager.View;
import Usage.Client;
import Usage.Server;
import Utilities.TitleBarListener;

/**
 * 
 * @author Ali Abdulhameed
 *
 */
public class ActivityPage {
	private static ActivityPage instance;
	private View view;
	private Map<InetAddress, ActiveClient> activeClients;
	private Client currentClient = null;
	private JLabel currentLabel = null;
	private JPanel leftPanel;
	private JPanel rightPanel;
	private JPanel clientListPanel;
	private JScrollPane leftPanelScrollPane;
	private JScrollPane rightPanelScrollPane;
	private JTextArea userMessageTextField;
	private JScrollPane userMessageTextFieldPane;
	private boolean activeScrolling = false;

	static {
		instance = new ActivityPage();
	}

	private ActivityPage() {
		activeClients = new HashMap<>();
		view = ViewsManager.getInstance().createView("ActivityPage", new Utilities.Dimension(100, 100, 770, 691));
		leftPanel = new JPanel();
		leftPanel.setBounds(0, 0, 185, 691);
		leftPanel.setBackground(new Color(21, 20, 25));
		leftPanel.setLayout(null);

		leftPanelScrollPane = new JScrollPane();
		leftPanelScrollPane.setBounds(0, 0, 185, 670);
		leftPanelScrollPane.setBorder(null);
		leftPanelScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		clientListPanel = new JPanel();
		clientListPanel.setBackground(new Color(21, 20, 25));
		clientListPanel.setLayout(new BoxLayout(clientListPanel, BoxLayout.Y_AXIS));

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

		clientListPanel.add(backButton);

		leftPanelScrollPane.setViewportView(clientListPanel);
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

				sendMessageToClient();
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

	public static ActivityPage getInstance() {
		return instance;
	}

	public View getView() {
		return view;
	}

	public void sendMessageToClient() {
		if (currentClient == null)
			return;
		String message = userMessageTextField.getText();
		if (message.replaceAll("\\s+", "").length() > 0)
			currentClient.sendMessage(message);
	}

	public void updateClientList(Client client) {

		SwingUtilities.invokeLater(() -> {

			String clientIPAddress = client.get_IP_ADDRESS().toString();
			JLabel label = new JLabel(!client.getName().equals("Anonymous") ? client.getName() : clientIPAddress,
					SwingConstants.CENTER);
			label.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
			label.setForeground(Color.WHITE);
			label.setBackground(new Color(45, 8, 62));

			JPanel panel = new JPanel();
			panel.setBackground(new Color(26, 28, 32));
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

			ActiveClient newActiveClient = new ActiveClient(client);
			newActiveClient.setName(label);
			newActiveClient.setPanel(panel);

			activeClients.put(client.get_IP_ADDRESS(), newActiveClient);
			clientListPanel.add(label);
			clientListPanel.revalidate();
			clientListPanel.repaint();
			label.addMouseListener(new MouseListener() {

				@Override
				public void mouseClicked(MouseEvent e) {
					if (!label.equals(currentLabel) && currentLabel != null) {
						currentLabel.setOpaque(false);
						currentLabel.revalidate();
						currentLabel.repaint();
					}

					label.setOpaque(true);
					label.revalidate();
					label.repaint();
					userMessageTextField.setVisible(true);
					userMessageTextField.setEditable(true);
					userMessageTextField.getCaret().setVisible(true);
					userMessageTextFieldPane.setVisible(true);
					userMessageTextFieldPane.requestFocus();
					userMessageTextField.requestFocus();
					currentClient = client;
					currentLabel = label;
					JPanel p = activeClients.get(client.get_IP_ADDRESS()).getPanel();
					rightPanelScrollPane.setViewportView(p);
					updateActiveMessagesPanel(client);

				}

				@Override
				public void mousePressed(MouseEvent e) {
				}

				@Override
				public void mouseReleased(MouseEvent e) {
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					label.setOpaque(true);
					label.revalidate();
					label.repaint();
				}

				@Override
				public void mouseExited(MouseEvent e) {
					if (!label.equals(currentLabel)) {
						label.setOpaque(false);
						label.revalidate();
						label.repaint();
					}

				}

			});

		});

	}

	public void updateActiveMessagesPanel(Client client) {

		SwingUtilities.invokeLater(() -> {

			JPanel p = activeClients.get(client.get_IP_ADDRESS()).getPanel();
			for (int i = client.getStartingMessageIndex(); i < client.getMessages().size(); i++) {
				StringBuilder message = new StringBuilder(client.getMessages().get(i));
				for (int j = 66; j < message.length(); j += 70) {
					message.insert(j, "<br>");
				}

				SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
				String clientIPAddress = client.get_IP_ADDRESS().toString();
				JLabel messageLabel = new JLabel("<html><font " + (message.charAt(message.length() - 1) == '0'
						? "color='aqua'>" + Server.getInstance().getName()
						: "color='red'>" + (client.getName().equals("ME") || client.getName().equals("Anonymous")
								? clientIPAddress.substring(1, clientIPAddress.length())
								: client.getName()))
						+ "</font> <font color='#585960' size='1'>" + dateFormat.format(new Date()) + "</font><br>"
						+ "<font color='white'>" + message.substring(0, message.length() - 1)
						+ "</font><br><br></html>");
				messageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
				p.add(messageLabel);
			}
			p.revalidate();
			p.repaint();

			if (!activeScrolling) {
				JScrollBar vertical = rightPanelScrollPane.getVerticalScrollBar();
				rightPanelScrollPane.validate();
				vertical.setValue(vertical.getMaximum());
			}

		});

	}

	public void changeClientName(Client client, String name) {
		SwingUtilities.invokeLater(() -> {

			if (activeClients.containsKey(client.get_IP_ADDRESS())) {
				activeClients.get(client.get_IP_ADDRESS()).setName(name);
				activeClients.get(client.get_IP_ADDRESS()).getName().revalidate();
				activeClients.get(client.get_IP_ADDRESS()).getName().repaint();

			}

		});
	}

	private class ActiveClient {

		private Client client;
		private JLabel label = null;
		private JPanel panel = null;

		private ActiveClient(Client client) {
			this.client = client;
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
