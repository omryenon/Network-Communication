package bgu.spl.net.impl.BGRSServer.ops;

/**
 * ACK class is a server to client message of a successful operation done by the server.
 * hold the successful opNum and the additional data which needed for the client.
 */

public class Ack implements Operation {
    private  short opNum;
    private String ackMessage;

    public Ack(short num, String additionalData){
        opNum = num;
        if(additionalData!=null)
            ackMessage = additionalData;
    }


    @Override
    public String execute() {
        return ackMessage;
    }

    @Override
    public void setOnlineUserName(String onlineUserName) {

    }

    public String getAckMessage(){
        return ackMessage;
    }


    public short getOpNum() {
        return opNum;
    }
}
