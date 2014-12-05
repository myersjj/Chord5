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
 
package com.scvn.chord.render;

import java.awt.print.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;

/**
 * Insert the type's description here.
 * Creation date: (99-07-20 17:05:49)
 * @author
 */
public class RenderingPane extends JPanel {
    C4Renderer rend;
    int cpage;
    BufferedImage bi;
    Graphics2D big2d;
    int width, height;
    /**
     * Insert the method's description here.
     * Creation date: (99-07-20 17:08:15)
     */
    public RenderingPane(C4Renderer rend) {
        this.rend = rend;
        cpage = 0;
    }
    /**
     * Insert the method's description here.
     * Creation date: (99-07-20 07:33:48)
     * @param g java.awt.Graphics
     */
    public void paintComponent(Graphics g) {
        //System.out.println("bi="+bi+"\ng="+g);
        if (bi != null) {
            g.drawImage(bi, 0, 0, this);
        }
    }
/* --------------------------------------------------------------------------------*/
    public Graphics2D buildGraphics()
    {
            PageFormat pf = rend.getUserPageFormat();
            width = (int)pf.getWidth();
            height = (int)pf.getHeight();
            Dimension d = new Dimension(width, height);
            // create new Image with new size
            setSize(width, height);
            setMinimumSize(d);
            setPreferredSize(d);
            bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            big2d = bi.createGraphics();
            big2d.setColor(Color.white);
            big2d.fillRect(0, 0, width, height);
            return big2d;
    }
/* --------------------------------------------------------------------------------*/
    public void setCurrentPage(int p) {
        if (!rend.isRenderingDone() || big2d == null) {
            buildGraphics();
            rend.validateRendering(big2d);
        }
        cpage = p;
        big2d.setColor(Color.white);
        big2d.fillRect(0, 0, width, height);
        rend.print(big2d, null, cpage);
        invalidate();
        repaint();
    }
}
