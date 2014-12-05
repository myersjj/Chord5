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


public class ProcessMenu extends JMenu  {
    private Chord4 c4;
    Action actionProcessOptions, actionProcessInput, actionProcessList;

    public ProcessMenu(Chord4 c4) {
        super("Process");
        this.c4 = c4;
        add(actionProcessInput = new ProcessAsChordAction("As chord input..."));
        add(actionProcessList = new ProcessAsListAction("As list of files..."));
        add(actionProcessOptions = new ProcessOptionsAction("Options", "graphics/Preferences24.gif"));
        c4.getToolBar().add(actionProcessOptions).setToolTipText("Options");

    }

    public void setEnabled(boolean state) {
        actionProcessOptions.setEnabled(state);
        actionProcessInput.setEnabled(state);
        actionProcessList.setEnabled(state);
     }

    class ProcessAsChordAction extends AbstractAction {
        public ProcessAsChordAction(String name) {
            super(name);
        }
     public void actionPerformed(ActionEvent event) {
         c4.doProcess(false);
             return;
         }
     }

    class ProcessAsListAction extends AbstractAction {
        public ProcessAsListAction(String name) {
            super(name);
        }
     public void actionPerformed(ActionEvent event) {
             c4.doProcess(true);
         return;
         }
     }


    class ProcessOptionsAction extends AbstractAction {
        public ProcessOptionsAction(String name, String icon) {
            super(name, new ImageIcon(ClassLoader.getSystemResource(icon)));
        }
     public void actionPerformed(ActionEvent event) {
             c4.getOptFrame().setVisible(true);
             return;
         }
     }

}
