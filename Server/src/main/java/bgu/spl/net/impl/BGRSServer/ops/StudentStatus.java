package bgu.spl.net.impl.BGRSServer.ops;

import bgu.spl.net.impl.BGRSServer.Database;
/**
 * StudentStatus is a class used by  admin and get information of a user from the database.
 */
public class StudentStatus implements Operation {
    private short opNum;
    private String userToCheck;
    private String onlineUserName;

    public StudentStatus(short opNum, String userToCheck) {
        this.opNum = opNum;
        this.userToCheck = userToCheck;
    }

    /**
     * execute the message, if successful the admin get all the information needed on the user with the ACK operation.
     * else, return ERROR operation
     */
    @Override
    public Operation execute() {
        Database database = Database.getInstance();
        if(onlineUserName !=null)
            if (database.isAdmin(onlineUserName))
                if(database.isRegistered(userToCheck)){
                    String output = database.getStudentStatus(userToCheck);
                    return new Ack(opNum , output);
                }
        return new Error(opNum);
    }
    public void setOnlineUserName(String onlineUserName) {
        this.onlineUserName = onlineUserName;
    }
}
