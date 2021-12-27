package bgu.spl.net.impl.BGRSServer.ops;

import bgu.spl.net.impl.BGRSServer.Database;
/**
 * Logout is a class which represent Logout of a user from the server.
 */
public class Logout implements Operation {
    private short opNum;
    private String onlineUserName;

    public Logout(short opNum) {
        this.opNum = opNum;
    }


    public void setOnlineUserName(String user) {
        this.onlineUserName = user;
    }

    /**
     * execute the message, if successful Logout a user from the server return ACK operation.
     * else, return ERROR operation
     */
    @Override
    public Operation execute() {
        Database database = Database.getInstance();

        if (onlineUserName == null || !database.logout(onlineUserName))
            return new Error(opNum);
        return new Ack(opNum, null);

    }

}
