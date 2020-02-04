package io.github.pashazz.taskmanager;

import java.util.Iterator;

public interface Printable {
    /**
     * Provides a shorter string representation than toString
     * @return
     */
     String shortToString();

    /**
     * Used by us to print data on screen to user (as opposed to logs)
     * @return
     */
    default String printOut() {
        return toString();
    }
}
