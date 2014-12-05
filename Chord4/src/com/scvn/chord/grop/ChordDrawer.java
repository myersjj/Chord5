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
import com.scvn.chord.*;

/**
 * Insert the type's description here.
 * Creation date: (99-07-17 16:38:56)
 * @author
 */
public class ChordDrawer implements GraphicsOperation
{ 
	KnownChord kc;
	int hpos, vpos;
/**
 * Insert the method's description here.
 * Creation date: (99-07-17 17:55:13)
 */
public ChordDrawer(KnownChord kc, int hpos, int vpos)
{
	this.kc = kc;
	this.hpos = hpos;
	this.vpos = vpos;
}
/**
 * Insert the method's description here.
 * Creation date: (99-07-17 16:42:22)
 * @param g java.awt.Graphics
 */
public void apply(java.awt.Graphics g)
{
	kc.paint((Graphics2D)g, hpos, vpos);
}
}
