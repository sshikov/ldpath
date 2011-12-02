package at.newmedialab.ldpath.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Add file description here!
 * <p/>
 * Author: Sebastian Schaffert
 */
public class Collections {

    
    public static <T> List<T> concat(Collection<T>... lists) {
        List<T> result = new ArrayList<T>();
        for(Collection<T> list : lists) {
            result.addAll(list);
        }
        return result;
    }
}
