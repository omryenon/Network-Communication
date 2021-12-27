package bgu.spl.net.impl.BGRSServer;

import java.util.*;
import java.util.concurrent.atomic.*;

/**
 * Passive object which hold and manage each course data.
 */
public class CourseInfo {

    private short courseNum;
    private String courseName;
    private short[] courseKdam;
    private int courseCapacity;
    private AtomicInteger numOfStudents;
    private Vector<String> regStudents;


    public CourseInfo(short courseNum, String courseName, int[] courseKdam, int courseCapacity) {
        this.courseNum = courseNum;
        this.courseName = courseName;
        if (courseKdam != null) {
            this.courseKdam = new short[courseKdam.length];
            for (int i = 0; i < courseKdam.length; i++)
                this.courseKdam[i] = (short) courseKdam[i];
        }
        this.courseCapacity = courseCapacity;
        numOfStudents = new AtomicInteger(0);
        this.regStudents = new Vector<>();
    }

    public String getCourseName() {
        return courseName;
    }

    public short[] getCourseKdam() {
        return courseKdam;
    }

    public int getCourseCapacity() {
        return courseCapacity;
    }

    public void increaseNumOfStudents(){
        if (numOfStudents.intValue() < courseCapacity)
            numOfStudents.compareAndSet(numOfStudents.intValue(), numOfStudents.intValue()+1);
    }

    public boolean isFull(){
        return courseCapacity == numOfStudents.intValue();
    }

    public int getNumOfStudents(){
        return numOfStudents.intValue();
    }

    public void add(String studentName){
        regStudents.add(studentName);
    }

    public String getRegStudents(){
        regStudents.sort(Comparator.naturalOrder());
        String output="";
        for (int i = 0; i <regStudents.size() ; i++) {
            if(i<regStudents.size()-1)
                output += regStudents.get(i) + ",";
            else
                output += regStudents.get(i);
        }
        return output;
    }

    public void removeStudent(String userName) {
        numOfStudents.getAndDecrement();
        regStudents.remove(userName);
    }

}


