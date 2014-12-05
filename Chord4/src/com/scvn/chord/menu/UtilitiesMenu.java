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
import java.text.Collator;
import java.util.TreeMap;
import java.util.Iterator;
import java.io.*;

import com.scvn.chord.ChordTextArea;
import com.scvn.chord.Chord4;


public class UtilitiesMenu extends JMenu  {
    private Chord4 c4;
    private ChordTextArea ta1;

    Action actionUtilPrintGrids, actionUtilSortLines;


    public UtilitiesMenu(Chord4 c4) {
        super("Utilities");
        this.c4 = c4;
        ta1 = c4.getTextArea();
        add(actionUtilPrintGrids = new UtilPrintGridsAction("Print all known chords as grids..."));
        add(actionUtilSortLines = new UtilSortLinesAction("Sort this list of files..."));

  }

    public void setEnabled(boolean state) {
        actionUtilPrintGrids.setEnabled(state);
        actionUtilSortLines.setEnabled(state);
      }

    /* --------------------------------------------------------------------------------*/
    public void sortLines(String delim) {
        Collator coll = Collator.getInstance();
//        Logger.debug("Sorting names according to locale:" + Locale.getDefault().getDisplayName());
        coll.setStrength(Collator.SECONDARY);
        TreeMap tm = new TreeMap(coll);
        //        TreeMap tm = new TreeMap(String.CASE_INSENSITIVE_ORDER);
        ByteArrayInputStream bais = new ByteArrayInputStream(ta1.getText().getBytes());
        InputStreamReader isr = new InputStreamReader(bais);
        BufferedReader fr = new BufferedReader(isr);
        String line, key;
        int pos;
        try {
            while ((line = fr.readLine()) != null) {
                pos = line.lastIndexOf(delim) + delim.length();
                key = line.substring(pos);
                tm.put(key, line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuffer sb = new StringBuffer();
        Iterator iter = tm.keySet().iterator();
        while (iter.hasNext()) {
            key = (String) (iter.next());
            sb.append((String) (tm.get(key)));
            sb.append("\n");
        }
        ta1.setText(sb.toString());
        ta1.setModified(true);
    }


    class UtilPrintGridsAction extends AbstractAction {
         public UtilPrintGridsAction(String name) {
             super(name);
         }
      public void actionPerformed(ActionEvent event) {
              c4.getRenderer().printGrids();
              return;
          }
      }

     class UtilSortLinesAction extends AbstractAction {
         public UtilSortLinesAction(String name) {
             super(name);
         }
      public void actionPerformed(ActionEvent event) {
              String delimiter;
              delimiter = JOptionPane.showInputDialog("Enter the string delimiting the field to be sorted");
              if (delimiter != null) {
                  if (delimiter.trim().length() == 0) {
                      // assume user want sort on the full line
                      delimiter = "===SoMeImPrObAbLeStRiNg===";
                  }
                  sortLines(delimiter);
              }
              return;
          }
      }

}
