package bgu.spl.net.impl.BGRSServer.ops;

import bgu.spl.net.impl.BGRSServer.Database;
/**
 * Login is a class which represent Login of new user/admin to the server.
 */
public class Login implements Operation {
    private short opNum;
    private String userName;
    private String password;
    private String onlineUserName;

    public Login(short opNum, String userName, String password) {
        this.opNum = opNum;
        this.userName = userName;
        this.password = password;
    }

    /**
     * execute the message, if successful login a user to the server and return ACK operation.
     * else, return ERROR operation
     */
    @Override
    public Operation execute() {
        Database database = Database.getInstance();
        if (onlineUserName == null)
            if (database.login(userName, password))
                return new Ack(opNum, null);
        return new Error(opNum);
    }

    public String getUserName() {
        return userName;
    }

    public void setOnlineUserName(String onlineUserName) {
        this.onlineUserName = onlineUserName;
    }
}
