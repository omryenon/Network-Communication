package bgu.spl.net.impl.BGRSServer.ops;

/**
 * Error class is a server to client message of a failure operation done by the server.
 * hold the opNum of the failed operation.
 */
public class Error implements Operation {
    private  short opNum;
    private String errorMessage;

    public Error(short num){
        opNum = num;
        errorMessage = "";
    }

    @Override
    public String execute() {
        errorMessage += "ERROR "+opNum;
        return errorMessage;
    }

    public void setOnlineUserName(String onlineUserName) {
    }
    public String getErrorMessage (){
        return errorMessage;
    }

    public short getOpNum() {return opNum;}
}
