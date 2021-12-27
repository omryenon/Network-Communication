package bgu.spl.net.impl.BGRSServer.ops;

import bgu.spl.net.impl.BGRSServer.Database;
/**
 * IsRegister is a class which used by student and check if the student is register to a given course.
 */
public class IsRegister implements Operation {
    private short opNum;
    private short courseNum;
    private String onlineUserName;

    public IsRegister(short opNum, short courseNum) {
        this.opNum = opNum;
        this.courseNum = courseNum;
    }

    /**
     * execute the message, if successful return NOT REGISTERED / REGISTERED with the ACK operation.
     * else, return ERROR operation
     */
    @Override
    public Operation execute() {
        Database database = Database.getInstance();
        if (onlineUserName != null)
            if (!database.isAdmin(onlineUserName))
                if (database.isCourseExist(courseNum))
                    if (database.isUserRegToCourse(onlineUserName, courseNum))
                        return new Ack(opNum, "REGISTERED");
                    else
                        return new Ack(opNum, "NOT REGISTERED");

        return new Error(opNum);
    }


    public void setOnlineUserName(String onlineUserName) {
        this.onlineUserName = onlineUserName;
    }
}
