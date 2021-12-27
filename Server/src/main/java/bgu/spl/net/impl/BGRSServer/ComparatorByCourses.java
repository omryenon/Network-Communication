package bgu.spl.net.impl.BGRSServer;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * Comparator which get list of courses and by the order they are in the course.txt
 * and compare two courses by the index in the list.
 */
public class ComparatorByCourses implements Comparator<Integer> {
    private LinkedList list;


public ComparatorByCourses(LinkedList list1){
    list = list1;
    }

    @Override
    public int compare(Integer o1, Integer o2) {
      return list.indexOf(o1) - list.indexOf(o2);
    }
}
