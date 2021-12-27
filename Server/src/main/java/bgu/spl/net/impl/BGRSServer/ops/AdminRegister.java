package bgu.spl.net.impl.BGRSServer.ops;

import bgu.spl.net.impl.BGRSServer.Database;

/**
 * AdminRegister is a class which represent registertion of new admin the the Database.
 */
public class AdminRegister implements Operation {

    private short opNum;
    private String userName;
    private String password;
    private String onlineUserName;

    public AdminRegister(short num,String name, String pass){
        opNum = num;
        userName =name;
        password = pass;
    }

    /**
     * execute the message, if successful register a new user in the database and return ACK operation.
     * else, return ERROR operation
     */
    public Operation execute() {
        Database database = Database.getInstance();
        if (onlineUserName == null)
            if (database.register(userName , password, true)){
                return new Ack(opNum, null);
            }
        return new Error(opNum);
    }

    public void setOnlineUserName(String onlineUserName) {
        this.onlineUserName = onlineUserName;
    }

}
