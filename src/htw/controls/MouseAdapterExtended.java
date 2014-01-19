package htw.controls;

import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

/**
 *
 * @author Tobbe
 */
public abstract class MouseAdapterExtended extends MouseAdapter {
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2)
            mouseDoubleClicked(e);

        super.mouseClicked(e);
    }

    public abstract void mouseDoubleClicked(MouseEvent e);
}
