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
import javax.swing.text.WrappedPlainView;
import java.awt.event.*;

import com.scvn.chord.ChordTextArea;
import com.scvn.chord.Chord4;


public class EditMenu extends JMenu implements ActionListener {
    private ChordTextArea ta;
    
    //JMenuItem miGrab, miStrip, miEnd;
    Action actionGrab, actionEndGrab;
    JMenuItem  miStrip;
    JMenuItem miCopy, miCut, miPaste;
    JMenuItem miMerge;
    JCheckBoxMenuItem miSyntaxHighlight;

    static String buffer = new String();

    Chord4 c4;

    public EditMenu(Chord4 aC4) {
        super("Edit");
        this.c4 = aC4;
        this.ta = c4.getTextArea();
        add(miCopy = new JMenuItem("Copy"));
        miCopy.addActionListener(this);
        add(miCut = new JMenuItem("Cut"));
        miCut.addActionListener(this);
        add(miPaste = new JMenuItem("Paste"));
        miPaste.addActionListener(this);
        addSeparator();
        add(actionGrab = new GrabAction("Grab Chords","graphics/Grab24.gif"));
        // miGrab.addActionListener(this);
        add(actionEndGrab = new EndGrabAction("Exit Chord Paste Mode", "graphics/EndGrab24.gif"));
        // miEnd.addActionListener(this);
        add(miStrip = new JMenuItem("Strip chords"));
        miStrip.addActionListener(this);
        addSeparator();
        add(miMerge = new JMenuItem("Merge (a2crd)"));
        miMerge.addActionListener(this);
//        addSeparator();
//        add(miSyntaxHighlight = new JCheckBoxMenuItem("Syntax Highlight"));
//        miSyntaxHighlight.setState(true);
//        miSyntaxHighlight.addActionListener(this);

        JToolBar toolBar = c4.getToolBar();
        JButton b = toolBar.add(actionGrab);
        b.setToolTipText("Grab Chords");
        b = toolBar.add(actionEndGrab);
        b.setToolTipText("Exit Chord Paste Mode");
         //toolBar.add(miStrip);

        addActionListener(this);
    }
    public void actionPerformed(ActionEvent e) {
        JMenuItem item = (JMenuItem) e.getSource();
        //	System.out.println(item.getText() + ": " + buffer);
        /*
        if (item == miGrab) {
            ta.grab();
            ta.setMode(ChordTextArea.PASTE_CHORD);
            return;
        }
        if (item == miEnd) {
            ta.setMode(ChordTextArea.NORMAL);
            return;
        }
        */
        if (item == miStrip) {
            ta.strip();
            return;
        }
        if (item == miMerge) {
            ta.merge();
            return;
        }
        if (item == miCopy) {
            //		  buffer = ta.getSelectedText();
            ta.copy();
            return;
        }
        if (item == miCut) {
            //		  buffer = ta.getSelectedText();
            //      ta.replaceSelection("");
            ta.cut();
            return;
        }
        if (item == miPaste) {
            //      ta.insert(buffer, ta.getCaretPosition());
            //    	ta.replaceRange(buffer, ta.getSelectionStart(), ta.getSelectionEnd());
            ta.paste();
            return;
        }
        if (item == miSyntaxHighlight) {
//            ta.setSyntaxHighlighting(miSyntaxHighlight.isSelected());
        }
    }
    class GrabAction extends AbstractAction {
        public GrabAction(String name, String icon) {
            super(name, new ImageIcon(ClassLoader.getSystemResource(icon)));
        }
     public void actionPerformed(ActionEvent event) {
         ta.grab();
         ta.setMode(ChordTextArea.PASTE_CHORD);
         return;
         }
     }
    class EndGrabAction extends AbstractAction {
        public EndGrabAction(String name, String icon) {
            super(name, new ImageIcon(ClassLoader.getSystemResource(icon)));
        }
     public void actionPerformed(ActionEvent event) {
         ta.grab();
         ta.setMode(ChordTextArea.NORMAL);
         return;
         }
     }

}
