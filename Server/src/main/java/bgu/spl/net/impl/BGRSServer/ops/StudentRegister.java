package bgu.spl.net.impl.BGRSServer.ops;

import bgu.spl.net.impl.BGRSServer.Database;
/**
 * StudentRegister is a class which represent registertion of new admin the the Database.
 */
public class StudentRegister implements Operation {

    private short opNum;
    private String userName;
    private String password;
    private String onlineUserName;

    public StudentRegister(short num,String name, String pass){
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
//            if (!database.isRegistered(userName)){
//                database.register(userName, password, false);
//                return new Ack(opNum, null);
//            }
            if (database.register(userName, password, false)){
                return new Ack(opNum, null);
            }

        return new Error(opNum);
    }

    @Override
    public void setOnlineUserName(String onlineUserName) {
        this.onlineUserName = onlineUserName;

    }
}
