package at.newmedialab.ldpath.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Add file description here!
 * <p/>
 * Author: Sebastian Schaffert
 */
public class Collections {

    /**
     * Copies all entries of the parsed collections to an new list
     * @param lists
     * @return
     */
    public static <T> List<T> concat(final Collection<T>... lists) {
        List<T> result = new ArrayList<T>();
        for(Collection<T> list : lists) {
            result.addAll(list);
        }
        return result;
    }
    /**
     * Returns an iterable over all lists without copying any data
     * @param lists the array with the lists
     * @return the plain iterator over all elements of the lists
     */
    public static <T> Iterator<T> iterator(final Collection<T>...lists){
        return iterator(0,lists);
    }
    /**
     * Returns an iterable over all lists starting by the parsed offset.
     * @param offset the offset of the first entry those elements should be
     * included in the returned entries
     * @param lists the array with the lists of elements
     * @return the plain iterator over all elements of the lists starting from
     * index offset
     */
    public static <T> Iterator<T> iterator(final int offset,final Collection<T>...lists){
        if(offset < 0){
            throw new IllegalArgumentException("The parsed Offest MUST NOT be < 0!");
        }
        if(lists == null){
            return null;
        } else if( lists.length <= offset){
            return java.util.Collections.<T>emptyList().iterator();
        }
        return new Iterator<T>() {
            
            private int listsIndex = offset-1;
            private Iterator<T> it;
            
            @Override
            public boolean hasNext() {
                while(it == null || !it.hasNext()){
                    listsIndex++;
                    if(listsIndex < lists.length ){
                        Collection<T> list = lists[listsIndex];
                        if(list != null){
                            it = lists[listsIndex].iterator();
                        }
                    } else {
                        return false;
                    }
                }
                return true;
            }

            @Override
            public T next() {
                if(it == null){
                    hasNext();
                }
                return it.next();
            }

            @Override
            public void remove() {
                if(it != null){
                    it.remove();
                }
            }};
    }
}
