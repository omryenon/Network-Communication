package bgu.spl.net.impl.BGRSServer.ops;

import bgu.spl.net.impl.BGRSServer.Database;
/**
 * KdamCheck is a class which send back all the Kdam courses of a given course.
 */
public class KdamCheck implements Operation {
    private short opNum;
    private short courseNum;
    private String onlineUserName;

    public KdamCheck(short opNum, short courseNum) {
        this.opNum = opNum;
        this.courseNum = courseNum;
    }

    /**
     * execute the message, if successful return all the Kdam courses with the ACK operation.
     * else, return ERROR operation
     */
    @Override
    public Operation execute() {
        Database database = Database.getInstance();
        if (onlineUserName != null)
            if(database.isCourseExist(courseNum)){
                Integer[] kdamCourses = database.getKdam(courseNum);
                String output ="[";
                if (kdamCourses != null)
                    for (int i = 0; i < kdamCourses.length; i++) {
                        if (i != kdamCourses.length-1)
                            output += kdamCourses[i] + ",";
                        else
                            output += kdamCourses[i];
                }
                output += "]";
                return new Ack(opNum, output);
            }
        return new Error(opNum);
    }


    public void setOnlineUserName(String onlineUserName) {
        this.onlineUserName = onlineUserName;
    }
}
