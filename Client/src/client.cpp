#include <stdlib.h>
#include "../include/connectionHandler.h"
#include <iostream>
#include <thread>
#include <mutex>
#include <condition_variable>

/**
 * The client is devided for 2 parts:
 * The main thread: receiving messages from the server and prints them.
 * The second thread: responsible for sending messages to the server( using the class: "sendMessage" and the function: "run")
 * Both of threads are keep running until the message "LOGOUT" is sent. In this point the second thread waits.
 * When "Ack 4" message receives the main thread interrupts the second thread and makes him to terminate gracefully.
*/


class sendMessage {
private:
    ConnectionHandler& connectionHandler;
    bool canTerminate = false;
    mutex & mtx;
    condition_variable & cv;
public:
    sendMessage(ConnectionHandler &connectionHandler1, mutex &mtx1 , condition_variable &cv1):
                                connectionHandler(connectionHandler1), mtx(mtx1), cv(cv1){
    }

     void run() {
        while (!canTerminate) {
            const short bufsize = 1024;
            char buf[bufsize];
            std::cin.getline(buf, bufsize);
            std::string line(buf);
            if (!connectionHandler.sendLine(line, buf)) {
                break;
            }
            unique_lock<mutex> lck{mtx};
            if (line == "LOGOUT"){
                cv.wait(lck);
            }
        }

    }
    void setTerminate(){
        canTerminate = true;
    }
};

int main (int argc, char *argv[]) {
    if (argc < 3) {
        std::cerr << "Us6age: " << argv[0] << " host port" << std::endl << std::endl;
        return -1;
    }
    std::string host = argv[1];
    short port = atoi(argv[2]);

    ConnectionHandler connectionHandler(host, port);
    if (!connectionHandler.connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }

    /// this part defines a second thread ("the sender") and gives him the corrisponding function.

    mutex mtx ;
    condition_variable cv;

    sendMessage sendMessage (connectionHandler, mtx, cv);
    std:: thread sender (&sendMessage:: run , &sendMessage );

while (1) {

        std::string answer;
        if (!connectionHandler.getLine(answer)) {
            break;
        }

        int len=answer.length();
        answer.resize(len-1);
        std::cout <<answer << std::endl;

        if (answer == "ACK 4") {
            sendMessage.setTerminate();
            cv.notify_one();
            sender.join();
            break;
        }
        else if (answer == "ERROR 4")
            cv.notify_one();

}
    return 0;
}

