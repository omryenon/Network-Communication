package bgu.spl.net.impl.BGRSServer.ops;

import bgu.spl.net.impl.BGRSServer.Database;
/**
 * CourseStatus is a class which used by the admin and get information of a course from the database.
 */
public class CourseStatus implements Operation {
    private short opNum;
    private short courseNum;
    private String onlineUserName;

    public CourseStatus(short opNum, short courseNum) {
        this.opNum = opNum;
        this.courseNum = courseNum;
    }
    /**
     * execute the message, if successful the admin get all the information needed on the course with the ACK operation.
     * else, return ERROR operation
     */
    @Override
    public Operation execute() {
        Database database = Database.getInstance();
        if(onlineUserName !=null)
            if (database.isAdmin(onlineUserName))
                if(database.isCourseExist(courseNum)){
                    String output = database.getCourseStatus(courseNum);
                    return new Ack(opNum , output);
                }
        return new Error(opNum);
    }
    public void setOnlineUserName(String onlineUserName) {
        this.onlineUserName = onlineUserName;
    }
}
