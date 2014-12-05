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

import javax.swing.text.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.util.*;

import com.scvn.chord.a2crd.A2crd;
import com.scvn.chord.util.ColorEditorKit;

public class ChordTextArea extends JEditorPane implements MouseListener {
//public class ChordTextArea extends JTextArea implements MouseListener, Runnable {
    public final static int PASTE_CHORD = 1;
    public final static int NORMAL = 2;
    private int mode = NORMAL;
    private Vector chords, edits;
    private Chord4 c4;
    private JPopupMenu editMenu;
    private boolean modified = false;
    int invalidPos = 0;
    final static int S_DEF = 0;
    final static int S_CHORD = 1;
    final static int S_DIRECTIVE = 2;
    final static int S_COMMENT = 3;

    public ChordTextArea(Chord4 c4) {
        this.c4 = c4;
        addMouseListener(this);


        addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent ke) {
                //if (delimChars.indexOf(ke.getKeyChar()) >= 0)
                {
                    modified = true;
                }
            }
        });

        setSyntaxHighlighting(true);

        reset();
    }


    /**
     * This is broken and does not work if active is true.
     * @param active
     */
    public void setSyntaxHighlighting(boolean active) {
        if (active) {
            setEditorKit(new ColorEditorKit());
        } else {
            setEditorKit(new DefaultEditorKit());
        }
    }

    public void replaceSelection(String content) {
        super.replaceSelection(content);
    }


    public void grab() {
        chords = new Vector();
        String s = getSelectedText();
        StringBuffer grabbed = new StringBuffer("Grabbed: ");
        String sub;
        if (s == null || s.length() == 0) {
            c4.setStatus("Nothing Selected");
            return;
        }
        int i, j = 0;
        while (j >= 0 && (i = s.indexOf("[", j)) >= 0) {
            if ((j = s.indexOf("]", i + 1)) > 0) {
                sub = s.substring(i + 1, j);
                grabbed.append(sub + " ");
                chords.addElement(sub);
            }
        }
        c4.setStatus(grabbed.toString());
    }


    public void mouseClicked(MouseEvent e) {
        if ((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0) {
            editMenu.show((JComponent) e.getSource(), e.getX(), e.getY());
            return;
        }
        if (mode == PASTE_CHORD) {
            if ((e.getModifiers() & InputEvent.SHIFT_MASK) != 0) {
                if (edits.size() == 0) {
                    return;
                }
                Edit ed = (Edit) (edits.elementAt(0));
                chords.insertElementAt(ed.getString(), 0);
                try {
                    // account for the pair of []
                    getDocument().remove(ed.getPos(), ed.getString().length() + 2);
                } catch (BadLocationException ble) {
                    ble.printStackTrace();
                }
                edits.removeElementAt(0);
            } else if ((e.getModifiers() & InputEvent.BUTTON1_MASK) != 0) {
                String c = (String) (chords.elementAt(0));
                edits.insertElementAt(new Edit(getCaretPosition(), c), 0);
                try {
                    getDocument().insertString(getCaretPosition(), "[" + c + "]", null);
                } catch (BadLocationException ble) {
                    ble.printStackTrace();
                }
                chords.removeElementAt(0);
                if (chords.size() == 0) {
//					setEditable(false);
                    setMode(NORMAL);
                }
            }
            showChords();
            return;
        }
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void reset() {
        edits = new Vector();
    }

    public void setMode(int mode) {
        this.mode = mode;
        if (mode == PASTE_CHORD) {
            setEditable(false);
            showChords();
            return;
        }
        if (mode == NORMAL) {
            setEditable(true);
            this.getCaret().setVisible(true);
            c4.setStatus("Editor in Normal Mode");
        }
    }

    /**
     * Insert the method'frets description here.
     * Creation date: (99-08-04 15:28:54)
     * @param s java.lang.String
     */
    public void setText(String s) {
        super.setText(s);
    }

    public void showChords() {
        if (chords.size() == 0) {
            c4.setStatus("No chords grabbed");
            return;
        }
        StringBuffer msg = new StringBuffer("In the buffer:");
        for (int i = 0; i < chords.size(); i++) {
            msg.append(" " + chords.elementAt(i));
        }
        c4.setStatus(msg.toString());
    }

    public void merge() {
        String s = getSelectedText();
        A2crd a2crd = new A2crd();
        String merged;
        if (s == null || s.length() == 0) {
            c4.setStatus("Nothing Selected");
            return;
        }
        merged = a2crd.merge(1.0, s);
        try {
            getDocument().remove(getSelectionStart(), getSelectionEnd() - getSelectionStart());
            getDocument().insertString(getSelectionStart(), merged, null);
        } catch (BadLocationException ble) {
            ble.printStackTrace();
        }
    }

    public void strip() {
        String s = getSelectedText();
        StringBuffer stripped = new StringBuffer();
        String sub;
        if (s == null || s.length() == 0) {
            c4.setStatus("Nothing Selected");
            return;
        }
        int i, j = 0;
        while ((i = s.indexOf("[", j)) >= 0) {
            sub = s.substring(j, i);
            stripped.append(sub);
            j = s.indexOf("]", i + 1) + 1;
        }
        stripped.append(s.substring(j, s.length()));
        try {
            getDocument().remove(getSelectionStart(), getSelectionEnd() - getSelectionStart());
            getDocument().insertString(getSelectionStart(), stripped.toString(), null);
            //fix replaceRange(stripped.toString(), getSelectionStart(), getSelectionEnd());
        } catch (BadLocationException ble) {
            ble.printStackTrace();
        }
    }

    /**
     * Insert the method'frets description here.
     * Creation date: (99-08-05 11:57:38)
     * @param uee javax.swing.event.DocumentEvent
     */
    public void undoableEditHappened(UndoableEditEvent uee) {
        System.out.println(uee.toString());
    }

    /**
     * Whether the text has been modified or not.
     * @return True if the text has been modified.
     */
    public boolean isModified() {
        return modified;
    }

    /**
     * Set the modified status of the text. Normally, should only be used
     * after saving.
     * @param modified New value of property modified.
     */
    public void setModified(boolean modified) {
        this.modified = modified;
    }

    // FIXME: We should be usiong the add() method instead. But when I first implemented
    // it, I got a NPE. So there's some extra setup to do.
    public void setPopupMenu(JPopupMenu jPopupMenu) {
        editMenu = jPopupMenu;
    }

}
