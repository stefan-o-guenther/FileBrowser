package htw.controls;

// <editor-fold defaultstate="collapsed" desc="Imports">
import htw.*;
import htw.resources.Images;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EventObject;
import javax.swing.CellEditor;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;
// </editor-fold>

/**
 * This class implements a specific JTable
 * a FileBrowserTable
 */
public class FileBrowserTable extends JTable {

    private FileSystem fs;
    private FileBrowserTableModel model;

    private ArrayList<String> clipboardCopyRows;
    private ArrayList<String> clipboardCutRows;

    private static final Object [] directoryUpRow = {
        "", Images.directoryUpIcon(), "", "", "", ""
    };

    private htw.FileBrowserMainView parentFrame;

    private String oldName;

    public FileBrowserTable(FileBrowserMainView parentFrame) {
        super(new FileBrowserTableModel());

        this.parentFrame = parentFrame;

        clipboardCopyRows = new ArrayList<String>();
        clipboardCutRows = new ArrayList<String>();

        setColumnWidths();

        addMouseListener(new MouseAdapterExtended() {
            @Override
            public void mouseDoubleClicked(MouseEvent e) {
                tableMouseDoubleClicked(e);
            }
        });

        setDefaultRenderer(Object.class, new FileBrowserTableCellRenderer());

        model = (FileBrowserTableModel)getModel();

        fs = new FileSystem();
        refreshTable();
    }

    public boolean isRoot() { return fs.isRoot(); }

    private void setColumnWidths() {
        getColumn("").setMaxWidth(20);
        getColumn("").setMinWidth(20);

        getColumn("Name").setPreferredWidth(200);

        getColumn("AbsoluteName").setMaxWidth(0);
        getColumn("AbsoluteName").setMinWidth(0);
        getColumn("AbsoluteName").setPreferredWidth(0);
    }

    @Override
    public void editingStopped(ChangeEvent e) {
        if (e.getSource() instanceof CellEditor) {

            String newName = ((CellEditor)e.getSource()).getCellEditorValue().toString();

            if (!newName.equals(oldName))
                fs.getFile(oldName).renameTo(fs.getFile(newName));
        }
        super.editingStopped(e);
    }

    public void deleteSelectedRows() {
        int[] rows = getSelectedRows();
        for (int i = rows.length - 1; i >= 0; i--) {
            if (fs.deleteFile(model.getValueAt(rows[i], 1).toString()))
                model.removeRow(rows[i]);
        }
    }

    public void copySelectedRows() {
        clipboardCutRows.clear();
        clipboardCopyRows.clear();

        int[] rows = getSelectedRows();
        for (int i = rows.length - 1; i >= 0; i--)
            clipboardCopyRows.add(model.getValueAt(rows[i], 5).toString());        
    }

    public void cutSelectedRows() {
        clipboardCutRows.clear();
        clipboardCopyRows.clear();

        int[] rows = getSelectedRows();
        for (int i = rows.length - 1; i >= 0; i--)
            clipboardCutRows.add(model.getValueAt(rows[i], 5).toString());
    }

    public int pasteRows() {
        try {
            // Kopieren
            if (clipboardCopyRows.size() > 0 && clipboardCutRows.size() == 0)
            {
                for (int i = 0; i < clipboardCopyRows.size(); i++)
                    FileSystem.copy(clipboardCopyRows.get(i), fs.getCurrentPath());

                refreshTable();
                return 0;
            }
            // Einfügen
            else if (clipboardCutRows.size() > 0 && clipboardCopyRows.size() == 0) {
                for (int i = 0; i < clipboardCutRows.size(); i++)
                    FileSystem.move(clipboardCutRows.get(i), fs.getCurrentPath());

                clipboardCutRows.clear();
                refreshTable();
                return 1;
            }
            return -1;
        }
        catch (Exception e) {
            return -1;
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Overridden methods of JTable">

    /**
     * overrides the processKeyEvent and
     * calls the processKeyEvent of parentFrame
     * @param e KeyEvent
     */
    @Override
    protected void processKeyEvent(KeyEvent e) {
        super.processKeyEvent(e);
        parentFrame.processKeyEvent(e);
    }

    @Override
    protected void processMouseEvent(MouseEvent e) {
        super.processMouseEvent(e);
        parentFrame.processMouseEvent(e);
    }

    @Override
    public boolean editCellAt(int row, int column, EventObject e) {
        if (e == null) {
            if (super.editCellAt(row, column, e)) {

                // Das hier wird gemacht um den ganzen Namen
                // der Datei zu markieren
                Object o = getValueAt(row, column);
                TableCellEditor tce = getCellEditor(row, column);

                Component c = tce.getTableCellEditorComponent(this, o, true, row, column);
                if(c instanceof JTextField) {
                    JTextField tf = (JTextField)c;
                    tf.selectAll();
                    tf.requestFocusInWindow();
                }

                oldName = o.toString();
                
                return true;
            }
        }        
        return false;
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="DoubleClick event">
    private void tableMouseDoubleClicked(MouseEvent e) {
        int row = this.rowAtPoint(e.getPoint());
        if (row == -1) return;

        try {
            String filename = model.getValueAt(row, 5).toString();
            if (filename.endsWith(".txt"))
                new FileView(filename);
            else
                fs.open(filename);
        }
        catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "IO Error", JOptionPane.ERROR_MESSAGE);
        }

        refreshTable();
    }
    // </editor-fold>

    private void refreshTable() {
        clearTable();

        parentFrame.setPath(fs.getCurrentDirectoryName());

        File[] f = fs.getList();

        if (!fs.isRoot()) {
            model.addRow(directoryUpRow);
            model.setValueAt(fs.getParentDirectory(), 0, 5);
        }

        if (f == null) return;

        Object[] row;
        for (int i = 0; i < f.length; i++) {
            row = new Object[6];

            row[0] = (f[i].isFile() ? Images.fileIcon() :
                (f[i].isDirectory() ? Images.folderIcon() : Images.deviceIcon()));

            if (fs.isRoot()) {
                row[1] = f[i].getAbsolutePath();
                row[0] = Images.hardDiskIcon();
            }
            else
                row[1] = f[i].getName();

            row[2] = (f[i].length() / 1024) + " KB";
            row[3] = f[i].lastModified();

            String rights = "";
            if (f[i].canRead()) rights += "r";
            if (f[i].canWrite()) rights += "w";
            if (f[i].canExecute()) rights += "x";
            row[4] = rights;
            
            row[5] = f[i].getAbsolutePath();

            model.addRow(row);
        }
    }

    private void clearTable() {
        while (model.getRowCount() > 0)
            model.removeRow(0);
    }

}
