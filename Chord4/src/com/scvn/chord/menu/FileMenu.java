/*
 * The CHORD utility for guitar players is distributed under the terms of the
 * general GNU license.
 *
 * Copyright (C) 2001  Martin Leclerc & Mario Dorion
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.scvn.chord.menu;

import javax.swing.*;

import java.awt.event.*;
import java.awt.*;
import java.io.*;

import com.scvn.chord.ChordTextArea;
import com.scvn.chord.Chord4;
import com.scvn.chord.Settings;


public class FileMenu extends JMenu {

    Action actionFileNew, actionFileOpen, actionFileSaveAs, actionFileSave, actionFileQuit;
    JCheckBoxMenuItem miSyntaxHighlight;
    Chord4 c4;
    JFileChooser chooser;
    ChordTextArea ta1;
    File myfile;


    static String buffer = new String();
    
    public FileMenu(Chord4 c4) {
        super("File");
        this.c4 = c4;
        ta1 = c4.getTextArea();
        add(actionFileNew = new FileNewAction("New", "graphics/New24.gif")).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Event.CTRL_MASK));
        add(actionFileOpen = new FileOpenAction("Open...", "graphics/Open24.gif")).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Event.CTRL_MASK));
        add(actionFileSave = new FileSaveAction("Save", "graphics/Save24.gif")).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK));
        actionFileSave.setEnabled(false);
        add(actionFileSaveAs = new FileSaveAsAction("Save as...", "graphics/SaveAs24.gif"));
        addSeparator();
        add(actionFileQuit = new FileQuitAction("Quit")).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, Event.CTRL_MASK));

        JToolBar toolBar = c4.getToolBar();
        JButton b = toolBar.add(actionFileNew);
        b.setToolTipText("New");
        b = toolBar.add(actionFileOpen);
        b.setToolTipText("Open...");
        b = toolBar.add(actionFileSave);
        b.setToolTipText("Save");
        b = toolBar.add(actionFileSaveAs);
        b.setToolTipText("Save As...");

        chooser = new JFileChooser("Select a file");


    }

    public void setEnabled(boolean state) {
        actionFileNew.setEnabled(state);
        actionFileOpen.setEnabled(state);
        actionFileSaveAs.setEnabled(state);
        actionFileSave.setEnabled(state);
        actionFileQuit.setEnabled(state);

    }

    /* --------------------------------------------------------------------------------*/
    /**
     * Insert the method'frets description here.
     * Creation date: (30/10/99 1:41:32 PM)
     * @param o java.util.List
     * @param buf java.lang.StringBuffer
     */
    public void explodeList(String[] o, StringBuffer buf) {
        for (int i = 0; i < o.length; i++) {
            File f = new File(o[i]);
            if (f.isDirectory()) {
                String l[] = f.list();
                for (int j = 0; j < l.length; j++) {
                    l[j] = f.getAbsolutePath() + System.getProperty("file.separator") + l[j];
                }
                explodeList(l, buf);
            } else {
                buf.append(f.getAbsolutePath() + "\n");
            }
        }
    }

    /* --------------------------------------------------------------------------------*/
    /**
     * Insert the method'frets description here.
     * Creation date: (99-07-24 09:12:18)
     * @param f java.io.File
     */
    public void fileLoad(File f) {
        StringBuffer buf = new StringBuffer();
        try {
            FileReader is = new FileReader(f);
            BufferedReader bis = new BufferedReader(is);
            String b;
            while ((b = bis.readLine()) != null) {
                buf.append(b);
                //buf.append(System.getProperty("line.separator"));
                buf.append("\n");
            }
            bis.close();
            is.close();
            actionFileSave.setEnabled(true);
            myfile = f;
        } catch (IOException e) {
            c4.setErrorStatus("Failed to open file " + f + " : " + e.toString());
            e.printStackTrace();
        }
        c4.setTitle(f.getAbsolutePath());
        ta1.setText(buf.toString());
        ta1.setModified(false);
        c4.getRenderer().invalidateRendering();
    }
    /* --------------------------------------------------------------------------------*/
    /**
     * Insert the method'frets description here.
     * Creation date: (99-07-24 09:12:18)
     * @param l java.util.List
     */
    public void fileLoad(java.util.List l) {
        StringBuffer buf = new StringBuffer();
        File f = (File) (l.get(0));
        if (l.size() == 1 && (!f.isDirectory())) {
            fileLoad((File) (l.get(0)));
            return;
        } else {
            String names[] = new String[l.size()];
            for (int i = 0; i < l.size(); i++) {
                names[i] = ((File) (l.get(i))).getAbsolutePath();
            }
            explodeList(names, buf);
            c4.setTitle("File List");
            actionFileSave.setEnabled(false);
            ta1.setText(buf.toString());
            ta1.setModified(false);
            c4.getRenderer().invalidateRendering();
        }
    }

    /* --------------------------------------------------------------------------------*/
    public void fileSave(File f) {
        //System.out.println("Got a File:Save:" + f.getAbsolutePath());
        try {
            FileOutputStream os = new FileOutputStream(f.getAbsolutePath());
            DataOutputStream dos = new DataOutputStream(os);
            dos.writeBytes(ta1.getText());
            dos.close();
            os.close();
            myfile = f;
            actionFileSave.setEnabled(true);
            ta1.setModified(false);
            c4.setTitle(myfile.getAbsolutePath());


        } catch (IOException e) {
            c4.setErrorStatus("Failed to save file " + f.getAbsolutePath() + ": " + e.toString());
        }
        //	System.out.println(" Done");
    }

    /**
     * Ask the user for confirmation if saving a file will overwrite
     * an existing one.
     *
     * @return True if the file should be overwritten or if the file
     * doesn't exist.
     */
    private boolean overwriteExistingFile(File fileChosen) {
        if (fileChosen.exists()) {
            int choice = JOptionPane.showConfirmDialog(this, "Overwrite file?", "Overwrite file?", JOptionPane.YES_NO_OPTION);

            return (choice == JOptionPane.YES_OPTION);
        } else {
            return true;
        }
    }

    /**
     * Determines if modifications (if any) should be discarded. If the text
     * has been modified, a Yes/No popup requests confirmation before discarding
     * changes.
     *
     * @return True if the text has not been changed or if the user chooses to
     * discard his changes. False otherwise.
     */
    public boolean discardModifications() {
        // No modifications, keep going
        if (!ta1.isModified()) {
            return true;
        }

        int choice = JOptionPane.showConfirmDialog(this, "Discard modifications?", "Discard modifications?", JOptionPane.YES_NO_OPTION);

        return (choice == JOptionPane.YES_OPTION);
    }


    //
    // Action inner classes
    //

    class FileOpenAction extends AbstractAction {
        public FileOpenAction(String name, String icon) {
            super(name, new ImageIcon(ClassLoader.getSystemResource(icon)));
        }
        public void actionPerformed(ActionEvent event) {
            if (!discardModifications()) {
                return;
            }
            int returnVal = chooser.showOpenDialog(c4);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                fileLoad(chooser.getSelectedFile());
            }
            return;
        }
    }

    class FileSaveAction extends AbstractAction {
        public FileSaveAction(String name, String icon) {
            super(name, new ImageIcon(ClassLoader.getSystemResource(icon)));
        }
        public void actionPerformed(ActionEvent event) {
            if (myfile == null) {
                int returnVal;
                boolean writeFile = false;

                do {
                    returnVal = chooser.showSaveDialog(c4);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File fileChosen = chooser.getSelectedFile();

                        writeFile = overwriteExistingFile(fileChosen);
                        if (writeFile) {
                            fileSave(fileChosen);
                        }
                    } else {
                        return;
                    }
                } while (!writeFile);

                return;
            }
            fileSave(myfile);
            return;
        }

}

       class FileSaveAsAction extends AbstractAction {
           public FileSaveAsAction(String name, String icon) {
               super(name, new ImageIcon(ClassLoader.getSystemResource(icon)));
           }
        public void actionPerformed(ActionEvent event) {
                int returnVal;
                boolean writeFile = false;
                do {
                    returnVal = chooser.showSaveDialog(c4);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File fileChosen = chooser.getSelectedFile();

                        writeFile = overwriteExistingFile(fileChosen);
                        if (writeFile) {
                            fileSave(fileChosen);
                        }
                    } else {
                        return;
                    }
                } while (!writeFile);

                return;
            }
        }
    class FileNewAction extends AbstractAction {
        public FileNewAction(String name, String icon) {
            super(name, new ImageIcon(ClassLoader.getSystemResource(icon)));
        }
     public void actionPerformed(ActionEvent event) {
             if (!discardModifications()) {
                 return;
             }
             ta1.setText("");
             ta1.setModified(false);
             c4.setTitle("(New File)");
             myfile = null;
             c4.getRenderer().invalidateRendering();
             return;
         }
     }

    class FileQuitAction extends AbstractAction {
        public FileQuitAction(String name) {
            super(name);
        }
     public void actionPerformed(ActionEvent event) {
             if (!discardModifications()) {
                 return;
             }
             c4.getSettings().save();
             System.exit(0);
             return;
         }
     }

}
