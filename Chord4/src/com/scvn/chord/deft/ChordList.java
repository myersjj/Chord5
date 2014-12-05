package com.scvn.chord.deft;

import javax.swing.*;
import java.util.Vector;

public class ChordList extends AbstractListModel {
    Vector data;

    public ChordList() {
        data = new Vector();
    }

    public Vector getData() {
        return data;
    }

    public void setData(Vector data) {
        this.data = data;
        fireContentsChanged(this, 0, data.size());
    }

    public void remove(String chordName) {
        int idx = data.indexOf(chordName);
        if (idx >= 0) {
            data.removeElementAt(idx);
            fireIntervalRemoved(this, idx, idx);
        }
    }

    public void add(String chordName) {
        int idx = 0;
        while (chordName.compareTo((String) data.elementAt(idx)) > 0 && idx < data.size()) {
            idx++;
        }
        if (idx == data.size()) {
            data.add(chordName);
        } else {
            data.insertElementAt(chordName, idx);
        }
        fireIntervalAdded(this, idx, idx);

    }

    public int getSize() {
        return data.size();
    }

    public Object getElementAt(int i) {
        return data.elementAt(i);
    }


}
