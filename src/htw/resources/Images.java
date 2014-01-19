package htw.resources;

import javax.swing.ImageIcon;

public class Images {
    private static ImageIcon folderIcon;
    private static ImageIcon fileIcon;
    private static ImageIcon deviceIcon;
    private static ImageIcon hardDiskIcon;
    private static ImageIcon directoryUpIcon;

    private static ImageIcon exitIcon;
    private static ImageIcon copyIcon;
    private static ImageIcon cutIcon;
    private static ImageIcon pasteIcon;
    private static ImageIcon deleteIcon;
    private static ImageIcon findIcon;

    static {
        System.out.println("[START] Resources loading...");
        
        folderIcon = new ImageIcon("Folder.gif");
        fileIcon = new ImageIcon("File.gif");
        deviceIcon = new ImageIcon("Device.gif");
        hardDiskIcon = new ImageIcon("HardDisk.gif");
        directoryUpIcon = new ImageIcon("DirectoryUp.gif");

        exitIcon = new ImageIcon("Exit.gif");
        copyIcon = new ImageIcon("Copy.gif");
        cutIcon = new ImageIcon("Cut.gif");
        pasteIcon = new ImageIcon("Paste.gif");
        deleteIcon = new ImageIcon("Delete.gif");
        findIcon = new ImageIcon("Find.gif");

        System.out.println("[END]   Resources loading...");
    }

    public static ImageIcon folderIcon() { return folderIcon; }
    public static ImageIcon fileIcon() { return fileIcon; }
    public static ImageIcon deviceIcon() { return deviceIcon; }
    public static ImageIcon hardDiskIcon() { return hardDiskIcon; }
    public static ImageIcon directoryUpIcon() { return directoryUpIcon; }

    public static ImageIcon exitIcon() { return exitIcon; }
    public static ImageIcon copyIcon() { return copyIcon; }
    public static ImageIcon cutIcon() { return cutIcon; }
    public static ImageIcon pasteIcon() { return pasteIcon; }
    public static ImageIcon deleteIcon() { return deleteIcon; }
    public static ImageIcon findIcon() { return findIcon; }

}
