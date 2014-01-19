package htw;

// <editor-fold defaultstate="collapsed" desc="Imports">
import htw.controls.FileBrowserTable;
import htw.resources.Images;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
// </editor-fold>

/**
 * The main frame of the FileBrowser application
 * with the menu and the table
 */
public class FileBrowserMainView extends JFrame {

    // <editor-fold defaultstate="collapsed" desc="Declaration">
    private FileBrowserTable tb;

    private JTextField txtFullPath;

    private JMenuItem menuItemExit;
    private JMenuItem menuItemCut;
    private JMenuItem menuItemCopy;
    private JMenuItem menuItemPaste;
    private JMenuItem menuItemDelete;
    private JMenuItem menuItemRename;
    private JMenuItem menuItemSearch;

    private JMenuItem contextMenuItemCut;
    private JMenuItem contextMenuItemCopy;
    private JMenuItem contextMenuItemPaste;
    private JMenuItem contextMenuItemDelete;
    private JMenuItem contextMenuItemRename;

    private JPopupMenu contextMenu;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ctor">
    public FileBrowserMainView() {
        super("Filebrowser");
        initMenu();
        initTable();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Initialization">
    /**
     * Creates the FileBrowser table
     */
    private void initTable() {
        txtFullPath = new JTextField();
        txtFullPath.setEditable(false);
        tb = new FileBrowserTable(this);
        BorderLayout bl = new BorderLayout();
        getContentPane().setLayout(bl);
        add(new JScrollPane(txtFullPath), BorderLayout.PAGE_START);
        add(new JScrollPane(tb), BorderLayout.CENTER);
        //this.getContentPane().add(new JScrollPane(tb));
    }

    /**
     * Creates the MainMenu in this Frame
     */
    private void initMenu() {
        // Creates a ActionListener
        // it calls the menuItemClicked event
        ActionListener action = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                menuItemClicked(e);
            }
        };

        JMenuBar menuBar;
        JMenu menu;

        //Create the menu bar.
        menuBar = new JMenuBar();

        //Build the file menu.
	menu = new JMenu("File");
	menuBar.add(menu);

        // menuItemExit
        menuItemExit = new JMenuItem("Exit");
        menuItemExit.setIcon(Images.exitIcon());
        menuItemExit.addActionListener(action);
        menu.add(menuItemExit);

        //Build the edit menu.
	menu = new JMenu("Edit");
	menuBar.add(menu);
        
        // menuItemCut
        menuItemCut = new JMenuItem("Cut");
        menuItemCut.setIcon(Images.cutIcon());
        menuItemCut.addActionListener(action);
        menu.add(menuItemCut);

        // menuItemCopy
        menuItemCopy = new JMenuItem("Copy");
        menuItemCopy.setIcon(Images.copyIcon());
        menuItemCopy.addActionListener(action);
        menu.add(menuItemCopy);

        // menuItemPaste
        menuItemPaste = new JMenuItem("Paste");
        menuItemPaste.setIcon(Images.pasteIcon());
        menuItemPaste.addActionListener(action);
        menuItemPaste.setEnabled(false);
        menu.add(menuItemPaste);

        menu.addSeparator();

        // menuItemDelete
        menuItemDelete = new JMenuItem("Delete");
        menuItemDelete.setIcon(Images.deleteIcon());
        menuItemDelete.addActionListener(action);
        menu.add(menuItemDelete);

        // menuItemRename
        menuItemRename = new JMenuItem("Rename");
        menuItemRename.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
        menuItemRename.addActionListener(action);
        menu.add(menuItemRename);

        menu.addSeparator();

        // menuItemSearch
        menuItemSearch = new JMenuItem("Search");
        menuItemSearch.setIcon(Images.findIcon());
        menuItemSearch.addActionListener(action);
        menu.add(menuItemSearch);

        this.setJMenuBar(menuBar);

        contextMenu = new JPopupMenu();

        // menuItemCut
        contextMenuItemCut = new JMenuItem("Cut");
        contextMenuItemCut.setIcon(Images.cutIcon());
        contextMenuItemCut.addActionListener(action);
        contextMenu.add(contextMenuItemCut);

        // menuItemCopy
        contextMenuItemCopy = new JMenuItem("Copy");
        contextMenuItemCopy.setIcon(Images.copyIcon());
        contextMenuItemCopy.addActionListener(action);
        contextMenu.add(contextMenuItemCopy);

        // menuItemPaste
        contextMenuItemPaste = new JMenuItem("Paste");
        contextMenuItemPaste.setIcon(Images.pasteIcon());
        contextMenuItemPaste.addActionListener(action);
        contextMenuItemPaste.setEnabled(false);
        contextMenu.add(contextMenuItemPaste);

        contextMenu.addSeparator();

        // menuItemDelete
        contextMenuItemDelete = new JMenuItem("Delete");
        contextMenuItemDelete.setIcon(Images.deleteIcon());
        contextMenuItemDelete.addActionListener(action);
        contextMenu.add(contextMenuItemDelete);

        // menuItemRename
        contextMenuItemRename = new JMenuItem("Rename");
        contextMenuItemRename.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
        contextMenuItemRename.addActionListener(action);
        contextMenu.add(contextMenuItemRename);
        
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Sonstiges">

    public void setPath(String path) {
        txtFullPath.setText(path);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Menu Events">

    private void mouseButtonRightClicked(MouseEvent e) {
        if (tb.getSelectedRows().length >= 1 && !tb.isRoot())
            contextMenu.show(e.getComponent(), e.getX(), e.getY());
    }

    @Override
    public void processMouseEvent(MouseEvent e) {
        if (e.isPopupTrigger())
            mouseButtonRightClicked(e);
    }

    /**
     * this method overrides the processKeyEvent
     * and handles the Shortcuts for the MenuActions
     * @param e a KeyEvent
     */
    @Override
    public void processKeyEvent(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_F2) 
            menuItemClicked(new ActionEvent(menuItemRename, 0, null));
        else if(e.getKeyCode() == KeyEvent.VK_DELETE)
            menuItemClicked(new ActionEvent(menuItemDelete, 0, null));
        else if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_C)
            menuItemClicked(new ActionEvent(menuItemCopy, 0, null));
        else if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_X)
            menuItemClicked(new ActionEvent(menuItemCut, 0, null));
        else if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_V)
            menuItemClicked(new ActionEvent(menuItemPaste, 0, null));

        //super.processKeyEvent(e);
    }

    /**
     * Handles the menuItem click events
     * @param e The ActionEvent data
     */
    private void menuItemClicked(ActionEvent e) {
        if (menuItemExit == e.getSource()) {
            System.exit(0);
            return;
        }

        if (menuItemRename == e.getSource() || contextMenuItemRename == e.getSource()) {
            if (tb.getSelectedRowCount() == 1) {
                tb.editCellAt(tb.getSelectedRow(), 1);
                return;
            }
        }

        if (menuItemDelete == e.getSource() || contextMenuItemDelete == e.getSource()) {
            tb.deleteSelectedRows();
            menuItemPaste.setEnabled(false);
            return;
        }

        if (menuItemCopy == e.getSource() || contextMenuItemCopy == e.getSource()) {
            tb.copySelectedRows();
            menuItemPaste.setEnabled(true);
            return;
        }

        if (menuItemCut == e.getSource() || contextMenuItemCut == e.getSource()) {
            tb.cutSelectedRows();
            menuItemPaste.setEnabled(true);
            return;
        }

        if (menuItemPaste == e.getSource() || contextMenuItemPaste == e.getSource()) {
            if (tb.pasteRows() == 1)
                menuItemPaste.setEnabled(false);
            return;
        }

        if (menuItemSearch == e.getSource()) {
            FileSearcher f = new FileSearcher(new File(txtFullPath.getText()));
            f.setVisible(true);
            f.setDefaultCloseOperation( DISPOSE_ON_CLOSE );
            return;
        }

        // Throw a UnsupportedOperationException when a MenuItem
        // has no action. When you add a new MenuItem to the MainMenu
        // you musst add here a handling method
        throw new UnsupportedOperationException("Not handled in menuItemClicked()");
    }

    // </editor-fold>
}
