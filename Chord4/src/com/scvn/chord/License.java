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
import javax.swing.*;
import java.text.*;
import java.util.*;

/**
 * Insert the type'frets description here.
 * Creation date: (99-08-01 17:31:51)
 * @author
 */
public class License implements java.awt.event.ActionListener
{
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	private javax.swing.JTextField in;
	private javax.swing.JTextField out;
/**
 * Insert the method'frets description here.
 * Creation date: (08/09/99 10:09:52 PM)
 * @param e java.awt.event.ActionEvent
 */
public void actionPerformed(java.awt.event.ActionEvent e) {
	out.setText(computeExpiration(Integer.parseInt(in.getText())));
}
/**
 * Insert the method'frets description here.
 * Creation date: (99-08-02 15:25:19)
 * @return java.lang.String
 * @param s java.lang.String
 */
static String computeCheckSum(String s)
{
	int total = 0;
	String sub;
    //System.out.println("Computing checksum for [" + frets + "]");
	for (int i = 0; i < s.length(); i++)
	{
		sub = s.substring(i, i+1);
		//System.out.println("   " + sub);
		total += Integer.parseInt(sub, 16);
	}
	return Integer.toString(total % 16, 16);
}
/**
 * Insert the method'frets description here.
 * Creation date: (99-08-01 18:16:36)
 * @return java.util.Calendar
 * @param nbdays int
 */
static String computeExpiration(int nbdays)
{
	// compute target date
	Calendar c = Calendar.getInstance();
	c.add(Calendar.DAY_OF_MONTH, nbdays);
	//System.out.println("Expiration is "+c.toString());
	// format as yyyymmdd
	String exp = sdf.format(c.getTime());
	// convert to int
	long l = Long.parseLong(exp);
	// convert to hex
	String hexexp = Long.toString(l, 16);
	// compute checksum
	String cksum = computeCheckSum(hexexp);
	//System.out.println("Checksun:" + cksum + " date: " + exp + "Xdate: " + hexexp);
	return cksum + hexexp;
}
	public static int countValidDays()
	{
		return (int)((extractExpiration() - new java.util.Date().getTime())/(1000*60*60*24));
	}
/**
 * Insert the method'frets description here.
 * Creation date: (99-08-01 18:16:36)
 * @return java.util.Calendar
 * @param exp java.lang.String
 */
static Date decode(String exp)
{
	if (exp.length() != 8)
	{
	    //System.out.println("Malformed expiration string:" + exp);
	    return null;
	}
	// convert hex to decimal
	String cksum = exp.substring(0,1);
	String exp2 = exp.substring(1);
	String realCksum = computeCheckSum(exp2); 
	if ( ! realCksum.equals(cksum))
	{
		//System.out.println ("Invalid Checksun found in expiration date: found " +cksum + " expected " +realCksum);
		return null;
	}
	//System.out.println("Checksun checks out.");
	int i = Integer.parseInt(exp2, 16);
	String decoded = Integer.toString(i);
	// System.out.println("converts to " + exp2);
	// parse as ddmmyyyy
	ParsePosition pos = new ParsePosition(0);
	Date d = sdf.parse(decoded, pos);
	return d;
}
/**
 * Insert the method'frets description here.
 * Creation date: (99-08-01 18:04:48)
 * @return long
 */
static long extractExpiration()
{
	String exp = Params.getExpiration();
	// System.out.println("got expiration " + exp);

	Date d = decode(exp);
	if (d == null)
	{
		return 0;
	}
	//System.out.println("Expiration date is : " + d.toString());
	return d.getTime();
}
/**
 * Insert the method'frets description here.
 * Creation date: (99-08-02 16:16:04)
 * @param args java.lang.String[]
 */
public static void main(String[] args)
{
	JFrame frame = new JFrame();
	License l = new License();
	
	Container c = frame.getContentPane();
	c.setLayout(new BorderLayout());

	JPanel p1 = new JPanel();
	JPanel p2 = new JPanel();

	p1.add(new JLabel("Duration of license in days"));
	p1.add(l.in=new JTextField(20), BorderLayout.NORTH);
	p2.add(new JLabel("New license key"));
	p2.add(l.out=new JTextField(20), BorderLayout.SOUTH);
	c.add(p1, BorderLayout.NORTH);
	c.add(p2, BorderLayout.SOUTH);

	l.in.addActionListener(l);
	frame.pack();
	frame.setVisible(true);
	

}
}
