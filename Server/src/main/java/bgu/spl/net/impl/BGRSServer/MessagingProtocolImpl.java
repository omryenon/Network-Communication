package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.api.MessagingProtocol;
import bgu.spl.net.impl.BGRSServer.ops.*;



public class MessagingProtocolImpl implements MessagingProtocol<Operation> {

    /**
     * @param onlineUserName is null if no user is login.
     * @param shouldTerminate change to true only after success logout message
     */

    private String onlineUserName;
    private boolean shouldTerminate = false;

    /**
     * process the given message
     *firstly check if the user is login, if so give to the message the name of the user
     * and if a Logout msg recived active should terminate
     * if not logged in after a login msg update the onlineUserName
     *@param msg the received message
     *@return the response to send, ACK if the process success or ERROR else.
     */
    @Override
    public Operation process(Operation msg) {

        Operation output;

        //user is login
        if (onlineUserName != null) {
            msg.setOnlineUserName(onlineUserName);
            output = (Operation) msg.execute();

            if (msg instanceof Logout) {
                shouldTerminate = true;
            }

        }
        //user is not login
        else{
            output = (Operation) msg.execute();

            //after login sucssfuly update the online use name
            if (msg instanceof Login && output instanceof Ack)
                onlineUserName = ((Login) msg).getUserName();
        }
        return output;
    }

    /**
     * @return true if the connection should be terminated
     */
    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }
}
