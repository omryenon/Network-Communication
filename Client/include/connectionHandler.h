//
// Created by spl211 on 03/01/2021.
//

#ifndef CLIENT2_CONNECTIONHANDLER_H
#define CLIENT2_CONNECTIONHANDLER_H

#include <string>
#include <iostream>
#include <boost/asio.hpp>
#include "encoderDecoder.h"

using boost::asio::ip::tcp;

class ConnectionHandler {
private:
    const std::string host_;
    const short port_;
    boost::asio::io_service io_service_;   // Provides core I/O functionality
    tcp::socket socket_;
    encoderDecoder encDecode_;

public:
    ConnectionHandler(std::string host, short port);
    virtual ~ConnectionHandler();


    void close();

    bool connect();

    bool getLine(string &line );

    bool sendLine(string &line, char* input);

    bool sendBytes(const char *bytes, int bytesToWrite);

    bool getBytes(char *bytes, unsigned int bytesToRead);

    bool getServerMessage(string &frame, char delimiter);

};

#endif //CLIENT2_CONNECTIONHANDLER_H
