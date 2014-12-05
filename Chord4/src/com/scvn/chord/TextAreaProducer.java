/*
 * TextAreaProducer.java
 *
 * Created on December 29, 2001, 7:19 PM
 */

package com.scvn.chord;

/**
 *
 * @author  martin
 */
public class TextAreaProducer extends StringProducer {
    
    /** Creates a new instance of TextAreaProducer */
    public TextAreaProducer(ChordTextArea ta1) {
        super(ta1.getText());
    }

}
