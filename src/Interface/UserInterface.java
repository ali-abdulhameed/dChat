package Interface;

/**
 * 
 * @author Ali Abdulhameed Class used to handle the user interface and its
 *         different views.
 *
 */
public class UserInterface {

	private static UserInterface instance;
	private ViewsManager viewsManager;
	private final String ENTRY_VIEW = "WelcomePage";

	static {
		instance = new UserInterface();
	}

	public UserInterface() {
		viewsManager = ViewsManager.getInstance();
		viewsManager.setActiveView(ENTRY_VIEW);
		viewsManager.setVisible(true);

	}

	public static UserInterface getInstance() {
		return instance;
	}

}
