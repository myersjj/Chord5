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
package com.scvn.chord;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

import javax.swing.*;

import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.scvn.chord.render.*;
import com.scvn.chord.menu.*;
import com.scvn.chord.util.*;
import com.scvn.chord.deft.Deft;

/**
 * @author Martin Leclerc
 * @author James J Myers
 * @version 4.1
 */
public class Chord4 extends JFrame implements  DropTargetListener, ChordConstants, ChordVersion {

	private static final Logger LOGGER =
	        Logger.getLogger(Chord4.class.getName());
	Handler fileHandler = null;
	
	JMenuBar mb;
    JToolBar toolBar;

    FileMenu fileMenu;

    JMenu editMenu;
    ViewMenu viewMenu;

    JMenu processMenu;

    JMenu utilMenu;

    JMenu helpMenu;

    JMenu directiveMenu;

    JLabel status;
    JCheckBoxMenuItem miPasteMode;

    char sortDelimiter;
    File chordrcFile = null;


    OptFrame optFrame;
    Settings settings;
    ChordTextArea ta1;
    C4Renderer rend;
    Producer p;
    KnownChordVector knownChords;
    Parser parser;
    Deft deft;

    /* --------------------------------------------------------------------------------*/
    public Chord4() {
//        int daysLeft = License.countValidDays();
//        if (daysLeft < 0) {
//            JOptionPane.showMessageDialog(this, "No valid license found. Exiting.");
//            System.exit(1);
//        }
//        if (daysLeft < 30) {
//            JOptionPane.showMessageDialog(this, "License valid for " + daysLeft + " days.");
//        }
        initGUI();
        settings = new Settings();
        try {
			fileHandler  = new FileHandler("./chord4.log");
			LOGGER.addHandler(fileHandler);
			fileHandler.setFormatter(new SimpleFormatter());
			fileHandler.setLevel(Level.ALL);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        LOGGER.setLevel(Level.ALL);
        LOGGER.info("Logger Name: "+LOGGER.getName());
        
        if (settings.getShowSplash()) {
        	new SplashWindow("/graphics/splash.jpg", this, 5000);
        }
        setStatus("Building knowledge base");
        //Logger.setDebug(true);
        //Logger.setInfo(true);

        optFrame = new OptFrame(this);
        knownChords = new KnownChordVector();
        setStatus("Setting up the parser");
        parser = new Parser(this);
        parser.setRenderer(getRenderer());


        setStatus("Looking for user startup file");
        readChordrc();
        setStatus("Ready.");
        setGUIEnabled(true);
        // required for proper initialization of D&D
        DropTarget dt = new DropTarget(ta1, this);
    }

    public void setTitle(String title) {
        super.setTitle(CHORDVERSION + " - " + title);
    }

    /* --------------------------------------------------------------------------------*/
    public void doProcess(boolean isList) {
            settings.setIsListOfFiles(isList);
            getRenderer().invalidateRendering();
            getRenderer().setCurrentPage(0);
            getRenderer().setVisible(true);
            return;
        }



    /* --------------------------------------------------------------------------------*/
    /**
     * dragEnter method comment.
     */
    public void dragEnter(java.awt.dnd.DropTargetDragEvent dtde) {
    }
    /* --------------------------------------------------------------------------------*/
    /**
     * dragExit method comment.
     */
    public void dragExit(java.awt.dnd.DropTargetEvent dte) {
    }
    /* --------------------------------------------------------------------------------*/
    /**
     * dragOver method comment.
     */
    public void dragOver(java.awt.dnd.DropTargetDragEvent dtde) {
    }
    /* --------------------------------------------------------------------------------*/
    /**
     * drop method comment.
     */
    public void drop(java.awt.dnd.DropTargetDropEvent dtde) {
        dtde.acceptDrop(DnDConstants.ACTION_MOVE);
        if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
            try {
                java.util.List l = (java.util.List) (dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor));
                fileMenu.fileLoad(l);
                dtde.dropComplete(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    /* --------------------------------------------------------------------------------*/
    /**
     * dropActionChanged method comment.
     */
    public void dropActionChanged(java.awt.dnd.DropTargetDragEvent dtde) {
    }
    /* --------------------------------------------------------------------------------*/
    public KnownChordVector getKnownChords() {
        return knownChords;
    }

    /* --------------------------------------------------------------------------------*/
    public OptFrame getOptFrame() {
        return optFrame;
    }

    /* --------------------------------------------------------------------------------*/
    public Settings getSettings() {
        return settings;
    }

    public ChordTextArea getTextArea() {
        return ta1;
    }

    /* --------------------------------------------------------------------------------*/
    public Deft getDeft() {
        if (deft == null) {
            deft = new Deft(this);
        }
        return deft;
    }

    public ViewMenu getViewMenu() {
        return viewMenu;
    }

    /**
     * Insert the method'frets description here.
     * Creation date: (99-07-20 17:20:07)
     * @return com.scvn.chord.Renderer
     */
    public C4Renderer getRenderer() {
        if (rend == null) {
            rend = new C4Renderer(optFrame, p, this);
        }
        return rend;
    }

    public JToolBar getToolBar() {
        return toolBar;
    }


    /* --------------------------------------------------------------------------------*/
    public void initGUI() {

        String lnf = UIManager.getSystemLookAndFeelClassName();
//        Logger.debug("System LookAndFeel is [" + lnf + "]");
        try {
            UIManager.setLookAndFeel(lnf);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setTitle("(No File)");
        mb = new JMenuBar();
        toolBar = new JToolBar();

        ta1 = new ChordTextArea(this);
        ta1.setMargin(new Insets(5, 5, 5, 5));
        getContentPane().add(new JScrollPane(ta1), BorderLayout.CENTER);
//        getContentPane().setLayout(new BorderLayout());

        fileMenu = new FileMenu(this);
        editMenu = new EditMenu(this);
        viewMenu = new ViewMenu(this);
        processMenu = new ProcessMenu(this);
        utilMenu = new UtilitiesMenu(this);
        directiveMenu = new DirectiveMenu(this);
        helpMenu = new HelpMenu();

        // Build the Menubar
        mb.add(fileMenu);
        mb.add(editMenu);
        mb.add(viewMenu);
        mb.add(processMenu);
        mb.add(directiveMenu);
        mb.add(utilMenu);
        mb.add(helpMenu);
        setJMenuBar(mb);

        // Add the popup
        ta1.setPopupMenu(editMenu.getPopupMenu());
        getContentPane().add(toolBar, BorderLayout.NORTH);

        status = new JLabel("Welcome to Chord !", Label.LEFT);
        status.setForeground(Color.blue);
        status.setBackground(Color.white);
        getContentPane().add(status, BorderLayout.SOUTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 700);
        setGUIEnabled(false);
        setVisible(true);
        this.addWindowListener(new WindowAdapter() {
       	 public void windowClosing(WindowEvent evt) {
       	     onExit();
       	 }
       });
    }
    
    public void onExit() {
  	  System.err.println("Exit");
  	  settings.save();
  	  System.exit(0);
    }

    /* --------------------------------------------------------------------------------*/
    public void setGUIEnabled(boolean state) {
        fileMenu.setEnabled(state);
        processMenu.setEnabled(state);
        utilMenu.setEnabled(state);
    }


    /* --------------------------------------------------------------------------------*/
    public static void main(String[] args) {
        String version = System.getProperty("java.version");
        String vendor = System.getProperty("java.vendor");
        LOGGER.fine("Chord 4 running under java version [" + version + " ] from [" + vendor + "]");
        new Chord4();
    }


    /* --------------------------------------------------------------------------------*/
    /* read the file $HOME/chordrc as a set of directive */
    // look for property set with -DCHORDRC=fullpath
    // then look in directory set in user.home property (usable in 1.2)
    // then look in current directory.
    public void readChordrc() {
        // ?? parser.setRenderer(null);

        parser.getContext().inChordrc = true;

        // locate file only once
        if (chordrcFile == null) {
            chordrcFile = Params.locateChordrcFile();
        }

        if (chordrcFile != null && chordrcFile.exists()) {
            setStatus("Processing chordrc <" + chordrcFile.getAbsolutePath() + ">");
            p = new FileListProducer(chordrcFile.getAbsolutePath());
            parser.setProducer(p);
            //	  System.out.println("Asking for process...");
            parser.processInput();
        } else {
            setStatus("No chordrc found");
        }
        parser.getContext().inChordrc = false;
    }

    /* --------------------------------------------------------------------------------*/
    public void runParser() {

        if (settings.getIsListOfFiles()) {
            p = new FileListProducer(ta1.getText());
        } else {
            p = new TextAreaProducer(ta1);
        }
        setStatus("Starting to parse...");
        parser.setProducer(p);
        LOGGER.log(Level.FINE, "Parser Set up");
        setStatus("Start parse...");
        getRenderer().start();
        setStatus("Parsing...");
        parser.processInput();

        setStatus("Done parsing");
        getRenderer().close();

        if (ParsingErrorSet.getSyntaxErrors().length != 0) {
            String errors = ParsingErrorSet.getSyntaxMessages();
            JOptionPane.showMessageDialog(null, errors);
//            System.out.println(errors);
        }

        int nbundef = ParsingErrorSet.getChordUndefErrors().length;
        if (nbundef != 0) {
            String list = ParsingErrorSet.getNUndefMessages(30);
            String prompt = nbundef + " Chords are not defined:\n" + list + "\nLaunch Deft?";
            int resp = JOptionPane.showConfirmDialog(this, prompt);
            if (resp == JOptionPane.OK_OPTION) {
                Vector undefs = ParsingErrorSet.getUndefList();
                getDeft().setChordList(undefs);
                getDeft().setVisible(true);
            }
        }
    }

    /* --------------------------------------------------------------------------------*/
    public void setErrorStatus(String s) {
        status.setForeground(Color.red);
        status.setText(s);
        Toolkit.getDefaultToolkit().beep();
    }

    /* --------------------------------------------------------------------------------*/
    public void setStatus(String s) {
//        Logger.debug(s);
        status.setForeground(Color.black);
        status.setText(s);
    }
}
