package Interface;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Utilities.Dimension;

/**
 * 
 * @author Ali Abdulhameed Class used for managing the user interface views.
 *
 */
public class ViewsManager extends JFrame {

	private static ViewsManager instance;

	private JPanel contentPane;
	private Map<String, View> views;
	private View activeView;

	static {
		instance = new ViewsManager();
	}

	private ViewsManager() {
		views = new HashMap<>();
		activeView = null;
		setDefaultCloseOperation(this.EXIT_ON_CLOSE);
		setResizable(false);
		setUndecorated(true);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(26, 28, 32));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
	}

	public static ViewsManager getInstance() {
		return instance;
	}

	public View createView(String desc, Dimension deminiosn) {
		View view = new View(desc, deminiosn);
		views.put(desc, view);
		return view;
	}

	public View getView(String desc) {

		return views.get(desc);
	}

	public Map<String, View> getViews() {
		return views;
	}

	public void setActiveView(String desc) {
		View view;
		if (!views.containsKey(desc)) {
			try {
				Class cls = Class.forName("Views." + desc);
				cls.getDeclaredMethod("getInstance", null).invoke(null);
			} catch (Exception e) {

				e.printStackTrace();
			}
		}
		view = views.get(desc);

		view.activateView();
		Point currentLocation = getLocation();
		Dimension deminsion = view.getDemsions();
		if (activeView == null || !deminsion.equals(activeView.getDemsions())) {
			setBounds(deminsion.X, deminsion.Y, deminsion.W, deminsion.H);
			if (activeView != null)
				setLocation(currentLocation);
		}
		activeView = view;
	}

	public void minimize() {
		setState(Frame.ICONIFIED);
	}

	public class View {

		private final Map<String, JPanel> panels;
		private String desc;
		private final Dimension deminsion;

		private View(String desc, Dimension deminsion) {
			this.desc = desc;
			this.deminsion = deminsion;
			panels = new HashMap<>();

		}

		public String getViewDescription() {
			return desc;
		}

		public Dimension getDemsions() {
			return deminsion;
		}

		public void addPanel(String desc, JPanel panel) {
			panels.put(desc, panel);
		}

		public JPanel getPanel(String desc) {
			if (panels.containsKey(desc))
				return panels.get(desc);
			return null;
		}

		public Map<String, JPanel> getAllPanels() {
			return panels;
		}

		public void activateView() {
			contentPane.removeAll();
			for (JPanel panel : new ArrayList<JPanel>(panels.values())) {
				contentPane.add(panel);
			}
			contentPane.revalidate();
			contentPane.repaint();
		}

	}
}
