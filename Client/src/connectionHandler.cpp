#include "../include/connectionHandler.h"

/**
 * The connection handler manages the connection between the Client and the Server:
 * getLine gets an empty String and appends him the Server's message by other sub-function: "getServerMessage"
 * getServerMessage is actually build the String according to the type od the message the Server sent.
 * getBytes reads every char from the socket - helps getServerMessage append the chars to the String
 * sendLine gets the message String and the relevant buffer which contains the message that should be
 * forwarded to the server. The message is encoded by the encode function and then sent to the Server
 * via the socket by sendBytes function.
*/

using boost::asio::ip::tcp;
using std::cin;
using std::cout;
using std::cerr;
using std::endl;
using std::string;

ConnectionHandler::ConnectionHandler(string host, short port): host_(host), port_(port), io_service_(),
                                                                 socket_(io_service_), encDecode_(){}

ConnectionHandler::~ConnectionHandler() {
    close();
}

bool ConnectionHandler::connect() {
    std::cout << "Starting connect to "
              << host_ << ":" << port_ << std::endl;
    try {
        tcp::endpoint endpoint(boost::asio::ip::address::from_string(host_), port_); // the server endpoint
        boost::system::error_code error;
        socket_.connect(endpoint, error);
        if (error)
            throw boost::system::system_error(error);
    }
    catch (std::exception& e) {
        return false;
    }
    return true;
}

// Close down the connection properly.
void ConnectionHandler::close() {
    try {
        socket_.close();
    } catch (...) {
    }
}


bool ConnectionHandler::getLine(std::string& line) {

    char delimeter = '\0';
    return getServerMessage(line, delimeter);

}

bool ConnectionHandler::sendLine(std::string& line,char* input) {
    int size = encDecode_.encode(line, input);
    return sendBytes(input, size);
}

bool ConnectionHandler::sendBytes(const char bytes[], int bytesToWrite) {
    int tmp = 0;
    boost::system::error_code error;
    try {
        while (!error && bytesToWrite > tmp ) {
            tmp += socket_.write_some(boost::asio::buffer(bytes + tmp, bytesToWrite - tmp), error);
        }
        if(error)
            throw boost::system::system_error(error);
    } catch (std::exception& e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}

bool ConnectionHandler::getBytes(char bytes[], unsigned int bytesToRead) {
    size_t tmp = 0;
    boost::system::error_code error;
    try {
        while (!error && bytesToRead > tmp ) {
            tmp += socket_.read_some(boost::asio::buffer(bytes+tmp, bytesToRead-tmp), error);
        }
        if(error)
            throw boost::system::system_error(error);
    } catch (std::exception& e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}



bool ConnectionHandler::getServerMessage(std::string& frame, char delimiter) {
    char ch;
    // Stop when we encounter the null character.
    // Notice that the null character is not appended to the frame string.
    try {
        char opCode[2];   /// create an array to get the opCode
        char opCode2[2];  /// create an array to get the type of the message
        for (int i = 0; i < 4; i++) {
            if (!getBytes(&ch, 1)) {
                return false;
            }
            if (i < 2)
                opCode[i] = ch;
            else
                opCode2[i - 2] = ch;
        }
        short opNum = encDecode_.bytesToShort(opCode);
        short opNum2 = encDecode_.bytesToShort(opCode2);

        if (opNum == 13) { /// this is an Error
            frame = "ERROR " + to_string(opNum2) + "\n" ;
        }

        else {
            frame = "ACK " + to_string(opNum2)  +"\n" ;
            do {
                if (!getBytes(&ch, 1)) {
                    return false;
                }
                if (ch != '\0')
                    frame.append(1, ch);
            } while (delimiter != ch);
            if (opNum == 12 && (opNum2 == 11 || opNum2 == 9 || opNum2 == 8 || opNum2 == 7 || opNum2 == 6))
                frame.append("\n");
        }} catch (std::exception & e)
        {
            std::cerr << "recv failed2 (Error: " << e.what() << ')' << std::endl;
            return false;
        }

    return true;
}
