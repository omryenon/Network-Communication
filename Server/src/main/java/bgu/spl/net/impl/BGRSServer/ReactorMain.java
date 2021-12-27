package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.srv.Reactor;

public class ReactorMain {
    public static void main(String[] args) {
        Database database = Database.getInstance();
        int port = Integer.parseInt(args[0]);
        int numThread = Integer.parseInt(args[1]);
        Reactor reactor= new Reactor(numThread, port,()-> new MessagingProtocolImpl(), ()->new MessageEncoderDecoderImpl());
        reactor.serve();
    }

}
