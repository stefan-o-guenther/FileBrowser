package htw.controls;

import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;

public class FileBrowserTableModel extends DefaultTableModel {

    private static String[] columnNames =
        {"", "Name", "Size", "Timestamp", "Rights", "AbsoluteName"};

    private Class [] columnClasses =
        {ImageIcon.class, String.class, String.class, String.class, String.class, String.class};

    public FileBrowserTableModel() {
        super(new Object[0][0], columnNames);
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex].toString();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex != 1) return false;

        if (getValueAt(rowIndex, columnIndex).equals("..")) return false;
        else return true;
    }

    @Override
    public Class getColumnClass(int columnIndex) {
        return columnClasses[columnIndex];
    }

}
