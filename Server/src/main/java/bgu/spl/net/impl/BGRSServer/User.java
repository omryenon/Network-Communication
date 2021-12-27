package bgu.spl.net.impl.BGRSServer;

import java.util.*;

/**
 * Passive object which hold and manage each user data.
 */
public class User {
    private String userName;
    private String password;
    private Vector<Integer> courses;
    private boolean isAdmin;
    private boolean isLogin;


    public User(String name, String pass, boolean admin){
        userName = name;
        password = pass;
        isAdmin  = admin;
        courses = new Vector<>();
        isLogin = false;
    }


    public void setLogin(boolean login) {
        isLogin = login;
    }

    public boolean isLogin(){
        return isLogin;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public String getPassword() {
        return password;
    }

    public String getUserName() {
        return userName;
    }

    public boolean isRegtoCourse(short courseNum) {
        return courses.contains((int)courseNum);
    }

    public synchronized void addCourse(int num){
        courses.add(num);
    }

    public synchronized void removeCourse(short courseNum) {
        Integer course = (int)courseNum;
        courses.remove(course);
    }

    public synchronized String getCourses(ComparatorByCourses comparator){
        courses.sort(comparator);
        String output="";
        for (int i = 0; i <courses.size() ; i++) {
            if(i<courses.size()-1)
                output += courses.get(i) + ",";
            else
                output += courses.get(i);
        }
        return output;
    }

    public boolean haveKdam(short[] kdamList) {
        for (int i = 0; i < kdamList.length; i++) {
            if (!courses.contains((int)kdamList[i]))
                return false;
        }
        return true;
    }

}
