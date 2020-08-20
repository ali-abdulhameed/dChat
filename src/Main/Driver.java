package Main;

import Interface.*;
import Usage.*;

/**
 * 
 * @author Ali Abdulhameed Driver class. Creates the user interface and starts
 *         the server.
 *
 */
public class Driver {

	public static void main(String[] args) {

		UserInterface ui = UserInterface.getInstance();
		Server.getInstance().start();

	}

}
