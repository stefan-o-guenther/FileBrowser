package htw.controls;

import java.awt.Component;
import java.util.Date;
import java.text.DateFormat;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class FileBrowserTableCellRenderer extends DefaultTableCellRenderer {

    private DateFormat df;

    public FileBrowserTableCellRenderer() {
        df = DateFormat.getDateTimeInstance();
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (value == null) return lbl;

        lbl.setIcon(null);
        lbl.setHorizontalAlignment(LEFT);

        // first column with the icons
        // or first row second column
        // shows the icon in the cell        
        if ((column == 0 || (column == 1 && row == 0))
                && value instanceof ImageIcon) {
            lbl.setText(null);
            lbl.setIcon((ImageIcon) value);
        }

        // third column with the long value of a date
        // converts the long value in a date string
        else if (column == 3 && value instanceof Long) {
            lbl.setText(df.format(new Date((Long)value)));
            lbl.setHorizontalAlignment(RIGHT);
        }
        else if (column == 2 && value instanceof String) {
            lbl.setHorizontalAlignment(RIGHT);
        }
        else {
            lbl.setHorizontalAlignment(LEFT);
        }
        
        return lbl;        
    }
}
