/*
 * HelpMenu.java
 *
 * Created on December 29, 2001, 7:03 PM
 */

package com.scvn.chord.menu;

import javax.swing.*;
import java.awt.event.*;
import java.io.*;

import com.scvn.chord.Logger;
import com.scvn.chord.util.BrowserLauncher;

/**
 *
 * @author  martin
 */
public class HelpMenu extends JMenu implements ActionListener{
    JMenuItem miSyntaxHelp;

    /** Creates a new instance of HelpMenu */
    public HelpMenu() {
        super("Help");
        add(miSyntaxHelp = new JMenuItem("Syntax Help"));
        miSyntaxHelp.addActionListener(this);

    }

    public void actionPerformed(ActionEvent e) {
        JMenuItem item = (JMenuItem) e.getSource();
        if (item == miSyntaxHelp) {
            try {
                //look for a chord home
                String basePath = System.getProperty("chord.home", "..");
                String url = basePath + "/doc/index.html";
                //Logger.debug("Help file is ["+url+"]");
                File f = new File(url);
                if (f.exists()) {
                    BrowserLauncher.openURL(url);
                }
                else
                {
                    JOptionPane.showMessageDialog(this, "Cannot locate help file <"+url+">");
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            return;
        }
    }
    
}
