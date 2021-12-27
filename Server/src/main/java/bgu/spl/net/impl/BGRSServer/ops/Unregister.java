package bgu.spl.net.impl.BGRSServer.ops;

import bgu.spl.net.impl.BGRSServer.Database;
/**
 * Unregister is a class which unregister a student from a given course.
 */
public class Unregister implements Operation{
    private short opNum;
    private short courseNum;
    private String onlineUserName;

    public Unregister(short opNum, short courseNum) {
        this.opNum = opNum;
        this.courseNum = courseNum;
    }

    /**
     * execute the message, if successful unregister a user from the course and return ACK operation.
     * else, return ERROR operation
     */
    @Override
    public Operation execute() {
        Database database = Database.getInstance();
        if (onlineUserName != null)
            if (!database.isAdmin(onlineUserName))
                if (database.isCourseExist(courseNum))
                    if (database.isUserRegToCourse(onlineUserName, courseNum)) {
                        database.unrgisterCourse(onlineUserName, courseNum);
                        return new Ack(opNum, null);
                    }


        return new Error(opNum);
    }


    public void setOnlineUserName(String onlineUserName) {
        this.onlineUserName = onlineUserName;
    }
}
