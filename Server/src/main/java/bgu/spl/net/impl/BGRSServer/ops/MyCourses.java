package bgu.spl.net.impl.BGRSServer.ops;

import bgu.spl.net.impl.BGRSServer.Database;
/**
 * MyCourses is a class which return the list of courses of the logged in user.
 */
public class MyCourses implements Operation {
    private short opNum;
    private String onlineUserName;

    public MyCourses(short opNum) {
        this.opNum = opNum;
    }

    /**
     * execute the message, if successful return the list of courses with the ACK operation.
     * else, return ERROR operation
     */
    @Override
    public Operation execute() {
        Database database = Database.getInstance();
        if (onlineUserName != null)
            if (!database.isAdmin(onlineUserName)) {
                String output = "[" + database.getCourseList(onlineUserName) +"]";
                return new Ack(opNum, output);
            }
                    return new Error(opNum);
    }


    public void setOnlineUserName(String onlineUserName) {
        this.onlineUserName = onlineUserName;
    }
}
