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
 package com.scvn.chord.grop;

import java.awt.*;
import java.awt.image.*;

/**
 * Insert the type's description here.
 * Creation date: (99-07-17 16:38:56)
 * @author
 */
public class ImageDrawer implements GraphicsOperation, ImageObserver
{
	int x, y;
	Image im;
/**
 * Insert the method's description here.
 * Creation date: (99-07-17 17:55:13)

 */
public ImageDrawer(Image im, int x, int y)
{
	this.im = im;
	this.x = x;
	this.y = y;
}
/**
 * Insert the method's description here.
 * Creation date: (99-07-17 16:42:22)
 * @param g java.awt.Graphics
 */
public void apply(java.awt.Graphics g)
{
	g.drawImage(im, x, y, this);
}
/**
 * imageUpdate method comment.
 */
public boolean imageUpdate(java.awt.Image img, int infoflags, int x, int y, int width, int height)
{
	return false;
}
}
