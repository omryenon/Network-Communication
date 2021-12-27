using namespace std;
#include "../include/encoderDecoder.h"
#include <boost/lexical_cast.hpp>

/**
 * This class is responsible to encode and the code the messages between the Client and the Server.
 * encode function encodes the message according to the type of message sent.
 * shortToBytes and bytesToShort are sub-functions given to us.
 */


encoderDecoder:: encoderDecoder(){

}

int encoderDecoder::encode(string line , char input[]) {
    char cstr[line.length() - 1];
    string output = "";
    std::strcpy(cstr, line.c_str());
    char *p = std::strtok(cstr, " ");
    string word = "";
    word = p;

    int counter = 2;
    int counter2 = 0;

    if (word == "ADMINREG") {
        short num = 1;
        encoderDecoder::shortToBytes(num,input);
        word =std::strtok(NULL, " ");
        int wordSize = word.length();
        for (int i=0 ; i<wordSize;i++){
             input[i+2]=word[i];
             counter ++;
         }
        input[counter++]='\0';
        word =std::strtok(NULL, " ");
        wordSize = word.length();
        for (; counter2< wordSize;counter2++){
            input[counter2+counter]=word[counter2];
        }

        input[counter+ counter2++]='\0';

    }
    else if (word == "STUDENTREG") {
        short num = 2;
        encoderDecoder::shortToBytes(num,input);
        word =std::strtok(NULL, " ");
        int wordSize = word.length();
        for (int i=0 ; i<wordSize ;i++){
            input[i+2]=word[i];
            counter ++;
        }
        input[counter++]='\0';
        word =std::strtok(NULL, " ");
        wordSize = word.length();
        for (; counter2< wordSize ;counter2++){
            input[counter2+counter]=word[counter2];
        }
        input[counter+ counter2++]='\0';

    }
    else if (word == "LOGIN") {
        short num = 3;
        encoderDecoder::shortToBytes(num,input);
        word =std::strtok(NULL, " ");
        int wordSize = word.length();
        for (int i=0 ; i< wordSize ;i++){
            input[i+2]=word[i];
            counter ++;
        }
        input[counter++]='\0';
        word =std::strtok(NULL, " ");
        wordSize = word.length();
        for (; counter2 < wordSize ;counter2++){
            input[counter2+counter]=word[counter2];
        }
        input[counter+ counter2++]='\0';

    }
    else if (word == "LOGOUT") {
        short num = 4;
        encoderDecoder::shortToBytes(num,input);
    }
    else if (word == "COURSEREG") {
        short num = 5;
        encoderDecoder::shortToBytes(num,input);
        word =std::strtok(NULL, " ");
        short course;
        try {
            course = boost::lexical_cast<short>(word);
        } catch (bad_cast) {
        }

        char coursenum[2];
        encoderDecoder::shortToBytes(course,coursenum);
        input[2] = coursenum[0];
        input[3] = coursenum[1];
        counter += 2;

    } else if (word == "KDAMCHECK") {
        short num = 6;
        encoderDecoder::shortToBytes(num,input);
        word =std::strtok(NULL, " ");
        short course;
        try {
            course = boost::lexical_cast<short>(word);
        } catch (bad_cast) {
        }

        char coursenum[2];
        encoderDecoder::shortToBytes(course,coursenum);
        input[2] = coursenum[0];
        input[3] = coursenum[1];
        counter += 2;

    }
    else if (word == "COURSESTAT") {
        short num = 7;
        encoderDecoder::shortToBytes(num,input);
        word =std::strtok(NULL, " ");
        short course;
        try {
            course = boost::lexical_cast<short>(word);
        } catch (bad_cast) {
        }

        char coursenum[2];
        encoderDecoder::shortToBytes(course,coursenum);
        input[2] = coursenum[0];
        input[3] = coursenum[1];
        counter += 2;

    }
    else if (word == "STUDENTSTAT") {
        short num = 8;
        encoderDecoder::shortToBytes(num,input);
        word =std::strtok(NULL, " ");
        int wordSize = word.length();
        for (int i=0 ; i<wordSize;i++){
            input[i+2]=word[i];
            counter ++;
        }
        input[counter++]='\0';

    }
    else if (word == "ISREGISTERED") {
        short num = 9;
        encoderDecoder::shortToBytes(num,input);
        word =std::strtok(NULL, " ");
        short course;
        try {
            course = boost::lexical_cast<short>(word);
        } catch (bad_cast) {
        }

        char coursenum[2];
        encoderDecoder::shortToBytes(course,coursenum);
        input[2] = coursenum[0];
        input[3] = coursenum[1];
        counter += 2;

    }
    else if (word == "UNREGISTER") {
        short num = 10;
        encoderDecoder::shortToBytes(num,input);
        word =std::strtok(NULL, " ");
        short course;
        try {
            course = boost::lexical_cast<short>(word);
        } catch (bad_cast) {
        }

        char coursenum[2];
        encoderDecoder::shortToBytes(course,coursenum);
        input[2] = coursenum[0];
        input[3] = coursenum[1];
        counter += 2;

    }
    else{
        short num = 11;
        encoderDecoder::shortToBytes(num,input);
    }
    return counter+counter2;

}

void encoderDecoder::shortToBytes(short num, char* bytesArr){
        bytesArr[0] = ((num >> 8) & 0xFF);
        bytesArr[1] = (num & 0xFF);
    }
short encoderDecoder:: bytesToShort(char* bytesArr)
{
    short result = (short)((bytesArr[0] & 0xff) << 8);
    result += (short)(bytesArr[1] & 0xff);
    return result;
}
