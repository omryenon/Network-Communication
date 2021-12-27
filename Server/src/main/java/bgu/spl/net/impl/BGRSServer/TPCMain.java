package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.srv.BaseServer;
import bgu.spl.net.srv.Server;

public class TPCMain {
    public static void main(String[] args) {
        Database database = Database.getInstance();
        int port = Integer.parseInt(args[0]);
        BaseServer server = (BaseServer) Server.threadPerClient(port, ()-> new MessagingProtocolImpl(), ()->new MessageEncoderDecoderImpl());
        server.serve();
    }

}
