package bgu.spl.net.impl.BGRSServer.ops;

import bgu.spl.net.impl.BGRSServer.Database;
/**
 * CourseReg is a class which represent registertion of a student to a new course.
 */
public class CourseReg implements Operation {

    private short opNum;
    private short courseNum;
    private String onlineUserName;

    public CourseReg(short opNum, short courseNum) {
        this.opNum = opNum;
        this.courseNum = courseNum;
    }

    /**
     * execute the message, if successful register a student to a course and return ACK operation.
     * else, return ERROR operation
     */
    @Override
    public Operation execute() {
        Database database = Database.getInstance();
        if (onlineUserName != null)
            if (database.registerToCourse(onlineUserName, courseNum)) {
                return new Ack(opNum, null);
            }
        return new Error(opNum);
    }

    public void setOnlineUserName(String onlineUserName) {
        this.onlineUserName = onlineUserName;
    }
}
