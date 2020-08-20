package Utilities;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import Interface.ViewsManager;

/**
 * 
 * @author Ali Abdulhameed
 *
 */
public class TitleBarListener implements MouseListener, MouseMotionListener {

	private static TitleBarListener instance;
	private Point initalLocation = null;

	static {
		instance = new TitleBarListener();
	}

	private TitleBarListener() {
	}

	public static TitleBarListener getInstance() {
		return instance;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		initalLocation = e.getPoint();

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (initalLocation != null) {
			Point currentLocation = e.getLocationOnScreen();
			ViewsManager.getInstance().setLocation(currentLocation.x - initalLocation.x,
					currentLocation.y - initalLocation.y);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
