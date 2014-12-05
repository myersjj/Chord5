/*
 * DirectiveMenu.java
 *
 * Created on December 29, 2001, 6:54 PM
 */

package com.scvn.chord.menu;

import javax.swing.*;
import java.awt.event.*;

import com.scvn.chord.ChordTextArea;
import com.scvn.chord.Chord4;

/**
 *
 * @author  martin
 */
public class DirectiveMenu extends JMenu implements ActionListener{
    private ChordTextArea ta1;
    JMenuItem miMakeTitle, miMakeSubTitle, miMakeComment;
    JMenuItem miMake2Columns, miMakeColBreak, miMakeDefine;
    JMenuItem miMakeSoC, miMakeEoC;
    JMenuItem miAddCopyright;
    
    /** Creates a new instance of DirectiveMenu */
    public DirectiveMenu(Chord4 c4) {
        super("Directives");
        
        this.ta1 = c4.getTextArea();
        add(miMakeTitle = new JMenuItem("Convert to Title"));
        miMakeTitle.addActionListener(this);
        add(miMakeSubTitle  = new JMenuItem("Convert to Subtitle"));
        miMakeSubTitle.addActionListener(this);
        add(miMakeComment  = new JMenuItem("Convert to Comment"));
        miMakeComment.addActionListener(this);
        add(miMake2Columns  = new JMenuItem("Insert Columns=2"));
        miMake2Columns.addActionListener(this);
        add(miMakeColBreak = new JMenuItem("Insert Column Break"));
        miMakeColBreak.addActionListener(this);
        add(miMakeSoC = new JMenuItem("Insert Start of Chorus"));
        miMakeSoC.addActionListener(this);
        add(miMakeEoC = new JMenuItem("Insert End of Chorus"));
        miMakeEoC.addActionListener(this);
        add(miMakeDefine = new JMenuItem("Insert Define"));
        miMakeDefine.addActionListener(this);
        add(miAddCopyright = new JMenuItem("Add Copyright"));
        miAddCopyright.addActionListener(this);
    }
    
    public void actionPerformed(ActionEvent e) {
        JMenuItem item = (JMenuItem) e.getSource();
        //            JMenuItem miMakeTitle, miMakeSubTitle, miMakeComment, miMake2Columns, miMakeColBreak, miMakeDefine;
        if (item == miMakeTitle) {
            String t = ta1.getSelectedText();
            ta1.replaceSelection("{title:" + (t != null?t:"") + "}");
            return;
        }
        if (item == miMakeSubTitle) {
            String t = ta1.getSelectedText();
            ta1.replaceSelection("{subtitle:" + (t != null?t:"") + "}");
            return;
        }
        if (item == miMakeComment) {
            String t = ta1.getSelectedText();
            ta1.replaceSelection("{comment:" + (t != null?t:"") + "}");
            return;
        }
        if (item == miMake2Columns) {
            ta1.replaceSelection("\n{col: 2}");
            return;
        }
        if (item == miMakeColBreak) {
            ta1.replaceSelection("\n{colb}");
            return;
        }
        if (item == miMakeSoC) {
            ta1.replaceSelection("\n{soc}");
            return;
        }
        if (item == miMakeEoC) {
            ta1.replaceSelection("\n{eoc}");
            return;
        }
        if (item == miMakeDefine) {
            ta1.replaceSelection("\n{define: XYZ base-fret 0 frets E A D G B E}");
            return;
        }
        if (item == miAddCopyright) {
            ta1.setText("# Copyright text here #\n\n" + ta1.getText());
            return;
        }
    }
}
