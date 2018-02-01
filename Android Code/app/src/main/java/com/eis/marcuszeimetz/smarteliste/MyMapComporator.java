package com.eis.marcuszeimetz.smarteliste;

import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by marcuszeimetz on 24.01.18.
 */

public class MyMapComporator implements Comparator<HashMap<String, String>>
{


    @Override
    public int compare(HashMap<String, String> o1, HashMap<String, String> o2) {
        int c;

        c = o1.get ("A").compareTo(o2.get ("A"));
        if (c != 0) return c;

        c = o1.get ("B").compareTo(o2.get ("B"));
        if (c != 0) return c;

        return o1.get ("C").compareTo(o2.get ("C"));
    }
}
