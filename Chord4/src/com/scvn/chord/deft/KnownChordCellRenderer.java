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
/*
 * Created by IntelliJ IDEA.
 * User: martin
 * Date: 02-05-20
 * Time: 14:05:06
 * To change template for new class use
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package com.scvn.chord.deft;

import com.scvn.chord.KnownChord;

import javax.swing.*;
import java.awt.*;

public class KnownChordCellRenderer extends JComponent implements ListCellRenderer {
    KnownChord kc;
    int gridSize;

    public KnownChordCellRenderer() {
        setOpaque(true);
        gridSize = KnownChord.getGridSize() * 2;
        setPreferredSize(new Dimension(gridSize, gridSize));
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        kc = (KnownChord) value;
        setBackground(isSelected ? Color.lightGray : Color.white);
        setToolTipText(kc.toString());
        return this;
    }

    public void paint(Graphics g) {
        g.setColor(getBackground());
        g.fillRect(0, 0, getSize().width, getSize().height);

        g.setColor(Color.black);
        kc.setDrawName(false);
        kc.paint((Graphics2D) g, gridSize / 2, gridSize / 4);
    }
}
