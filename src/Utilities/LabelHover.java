package Utilities;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;

import Interface.ViewsManager;

/**
 * 
 * @author Ali Abdulhameed
 *
 */
public class LabelHover implements MouseListener {
	private JLabel label;
	private String nextView;
	private boolean selected;

	public LabelHover(JLabel label, String nextView, boolean selected) {
		this.label = label;
		this.nextView = nextView;
		this.selected = selected;
	}

	@Override
	public void mouseClicked(MouseEvent e) {

		if (nextView != null) {
			ViewsManager.getInstance().setActiveView(nextView);
		}

		if (!selected) {
			label.setOpaque(false);
			label.revalidate();
			label.repaint();
		}
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

		if (!selected) {
			label.setOpaque(true);
			label.revalidate();
			label.repaint();
		}

	}

	@Override
	public void mouseExited(MouseEvent e) {
		if (!selected) {
			label.setOpaque(false);
			label.revalidate();
			label.repaint();
		}

	}
}
