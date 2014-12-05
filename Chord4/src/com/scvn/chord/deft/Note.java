package com.scvn.chord.deft;

public class Note {

    private String name;
    private int halftone; // always relative to C

    /**
     * this constructor assume the halftone is absolute, that is
     * relative to C.
     */
    public Note(int halftone, String name) {
        this.halftone = halftone;
        this.name = name;
    }

    public boolean equals(Object o) {

        return o instanceof Note
                && ((Note) o).getHalftone() == halftone
                && ((Note) o).getName().equals(name);
    }

    public int getHalftone() {
        return halftone % 12;
    }

    public int getRole() {
       return halftone;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name; // + ":" + halftone;
    }
}
