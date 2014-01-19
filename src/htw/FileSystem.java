package htw;

import java.io.File;
import java.io.IOException;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

/**
 *
 * @author Tobbe
 */
public class FileSystem {
    private File currentDirectory;
    private File[] currentList;

    public FileSystem() {
        currentList = File.listRoots();
    }

    public boolean isRoot() {
        return (currentDirectory == null);
    }

    public File[] getList() {
        return currentList;
    }

    public String getCurrentPath() {
        return (currentDirectory == null ? "" : currentDirectory.getAbsolutePath());
    }

    public String getParentDirectory() {
        File tmp;
        if (currentDirectory != null) {
            if ((tmp = currentDirectory.getParentFile()) != null)
                return tmp.getAbsolutePath();
            else
                return "";
        }
        else
            return "";
    }

    public File getCurrentDirectory() {
        return currentDirectory;
    }
    
    public String getCurrentDirectoryName() {
        return (currentDirectory == null ? "" : currentDirectory.getAbsolutePath());
    }

    public File getFile(String file) {
        return new File(currentDirectory.getAbsolutePath() + File.separator + file);
    }

    public boolean deleteFile(String file) {
        if (file.equals("..")) return false;
        File tmp = getFile(file);
        if (tmp == null) return false;
        if (tmp.isDirectory())
            return deleteSubs(tmp);

        return tmp.delete();
    }

    public static void move(String quelle, String ziel) throws IOException {
        File q = new File(quelle);
        File z = new File(ziel);

        z = new File(z.getAbsolutePath() + File.separator + q.getName());
        q.renameTo(z);
    }

    public static void copy(String quelle, String ziel) throws IOException {
        File q = new File(quelle);
        File z = new File(ziel);

        if (q.getAbsolutePath().indexOf(z.getAbsolutePath()) != -1)
            throw new IOException("Der Zielordner ist dem Quellordner untergeordnet!");

        if (q.isDirectory()) {
            File dir = new File(z.getAbsolutePath() + File.separator + q.getName());
            if (dir.mkdir()) {
                for (File f : q.listFiles()) {
                    copy(f.getAbsolutePath(), z.getAbsolutePath() + File.separator + q.getName());
                }
            }
        }
        else {
            FileInputStream in = new FileInputStream(q);
            z = new File(z.getAbsolutePath() + File.separator + q.getName());
            FileOutputStream out = new FileOutputStream(z);

            FileChannel source = in.getChannel();
            if (!z.exists()) z.createNewFile();
            FileChannel dest = out.getChannel();

            source.transferTo(0, source.size(), dest);

            in.close();
            out.close();
        }
    }

    public static ArrayList<File> searchForFileName(File directory, String pattern) {
        ArrayList<File> result = new ArrayList<File>();

        for (File f : directory.listFiles()) {
            if (f.isDirectory()) {
                ArrayList<File> ret = searchForFileName(f, pattern);
                if (ret != null && ret.size() > 0)
                    result.addAll(ret);
            }
            else {
                if (f.getName().matches(pattern))
                    result.add(f);
            }                
        }
        return result;
    }

    public static ArrayList<File> searchForTextInFile(File directory, String pattern) {
        ArrayList<File> result = new ArrayList<File>();

        for (File f : directory.listFiles()) {
            if (f.isDirectory()) {
                ArrayList<File> ret = searchForTextInFile(f, pattern);
                if (ret != null && ret.size() > 0)
                    result.addAll(ret);
            }
            else {
                try {
                    BufferedReader br = new BufferedReader(new FileReader(f));
                    StringBuilder sb = new StringBuilder();

                    String s;
                    while ((s = br.readLine()) != null)
                        sb.append(s);

                    if (sb.toString().matches(pattern))
                        result.add(f);
                }
                catch (Exception ex) {

                }
            }
        }
        return result;
    }

    private boolean deleteSubs(File f) {
    	File[] files = f.listFiles();
    	if (files != null) {
    		for (int i = 0; i < files.length; i++) {
    			if (files[i].isDirectory()) {
    				deleteSubs(files[i]); // Verzeichnis leeren und anschließend löschen
    			} else {
    				files[i].delete(); // Datei löschen
    			}                
    		}
    		return f.delete();
    	}
    	return false;
    }

    public void open(String directory) throws IOException {
        File tmp = currentDirectory;

        if (directory.equals("")) {
            currentList = File.listRoots();
            currentDirectory = null;
            return;
        } else {
        	currentDirectory = new File(directory);
        }
        
        if (currentDirectory.isFile()) {
            try { Desktop.getDesktop().open(currentDirectory); }
            catch (IOException e) { throw e; }
            currentDirectory = tmp;
        } else if (currentDirectory.isDirectory()) {
        	currentList = currentDirectory.listFiles();
        } else {
            currentDirectory = tmp;
            throw new IOException("Can't open device!");
        }
    }
}
