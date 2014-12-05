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

import com.scvn.chord.Chord4;
import com.scvn.chord.menu.ProcessMenu.ProcessOptionsAction;


public class ViewMenu extends JMenu implements ActionListener, WindowListener {
    private Chord4 c4;
    
    Action actionViewRenderer;
    JCheckBoxMenuItem miViewRenderer, miViewDeft;

    static String buffer = new String();
    
    public ViewMenu(Chord4 c4) {
        super("View");
        this.c4 = c4;
        add(miViewRenderer = new JCheckBoxMenuItem("View Renderer"));
        actionViewRenderer = new ViewRendererAction("Options", "graphics/Preview_24x24.png");
        c4.getToolBar().add(actionViewRenderer).setToolTipText("Render");
        miViewRenderer.addActionListener(this);
        add(miViewDeft = new JCheckBoxMenuItem("View Deft"));
        miViewDeft.addActionListener(this);

        addActionListener(this);
    }
    public void actionPerformed(ActionEvent e) {
        JMenuItem item = (JMenuItem) e.getSource();
        //	System.out.println(item.getText() + ": " + buffer);
        if (item == miViewRenderer) {
            c4.getRenderer().setVisible(miViewRenderer.isSelected());
            return;
        }
        if (item == miViewDeft) {
            c4.getDeft().setVisible(miViewDeft.isSelected());
            return;
        }
    }
    
    class ViewRendererAction extends AbstractAction {
        public ViewRendererAction(String name, String icon) {
            super(name, new ImageIcon(ClassLoader.getSystemResource(icon)));
        }
        public void actionPerformed(ActionEvent event) {
        	miViewRenderer.setSelected(!miViewRenderer.isSelected());
        	c4.getRenderer().setVisible(miViewRenderer.isSelected());
            return;
         }
     }

    public void windowOpened(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
        if (e.getSource() == c4.getRenderer().getRenderingFrame())
        {
            miViewRenderer.setState(false);
            return;
        }
        if (e.getSource() == c4.getDeft())
        {
            miViewDeft.setState(false);
            return;
        }
        System.out.println("Unexpected source:" + e.getSource());
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }
}
