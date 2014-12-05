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

import java.awt.*;

public class ScalingGraphics
{
	private Graphics gc;
	static private double device_scale = 1.0;
/*--------------------------------------------------------------------------------*/
Font createFont(String name, int flags, int size)
{
	return new Font(name, flags, size);
}
/*--------------------------------------------------------------------------------*/
	void dispose ()
			{
			gc.dispose();
			}
/*--------------------------------------------------------------------------------*/
	void drawLine (int x1, int y1, int x2, int y2)
			{
			gc.drawLine(scale(x1),  scale(y1), scale(x2), scale(y2));
			}
/*--------------------------------------------------------------------------------*/
	void drawOval (int x, int y, int w, int h)
			{
			gc.drawOval(scale(x),scale(y),scale(w),scale(h));
			}
/*--------------------------------------------------------------------------------*/
	void drawRect (int x, int y, int w, int h)
			{
			gc.drawRect(scale(x),scale(y),scale(w),scale(h));
			}
/*--------------------------------------------------------------------------------*/
void drawString(String s, int x, int y)
{
	gc.drawString(s, scale(x), scale(y));
    //gc.drawString(frets, x, y);
}
/*--------------------------------------------------------------------------------*/
	void fillOval (int x, int y, int w, int h)
			{
			gc.fillOval(scale(x),scale(y),scale(w),scale(h));
			}
/*--------------------------------------------------------------------------------*/
	void fillRect (int x, int y, int w, int h)
			{
			gc.fillRect(scale(x),scale(y),scale(w),scale(h));
			}
/*--------------------------------------------------------------------------------*/
	FontMetrics getFontMetrics ()
			{
			return gc.getFontMetrics();
			}
/*--------------------------------------------------------------------------------*/
	static public int scale(int x)
		{
		return (int)(x*device_scale);
		}
/*--------------------------------------------------------------------------------*/
	void setColor (Color c)
			{
			gc.setColor(c);
			}
/*--------------------------------------------------------------------------------*/
	static public void setDeviceScale(double s)
		{
		System.out.println("Setting Device scale to " + s);
		device_scale = s;
		}
/*--------------------------------------------------------------------------------*/
void setFont(Font f)
{
	gc.setFont(f);
}
/*--------------------------------------------------------------------------------*/
void setFont(String name, int flags, int size)
{
	gc.setFont(new Font(name, flags, size));
}
/*--------------------------------------------------------------------------------*/ 
	public void setGraphics (Graphics gc)
		{
		this.gc = gc;
		}
/*--------------------------------------------------------------------------------*/
static public int unscale(int x)
{
	return (int) (x / device_scale);
}
}
