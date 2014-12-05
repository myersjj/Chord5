package com.scvn.chord.util;

/*

	Sample program to show color coding

    P. Vinod Kiran ( vinodkiran@usa.net )
    May 30, 2000

*/
import javax.swing.text.*;

public class ColorEditorKit extends DefaultEditorKit implements ViewFactory
{
	public ColorEditorKit()
    {
    	super();
	}

	public ViewFactory getViewFactory()
    {
		return this;
	}

	/* Create Color Views */
	public View create (Element e)
    {
		return new ColorView(e);
	}
}
