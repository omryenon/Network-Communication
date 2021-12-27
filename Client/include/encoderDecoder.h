

#ifndef CLIENT2_ENCODERDECODER_H
#define CLIENT2_ENCODERDECODER_H
#include <string>
#include <iostream>
#include <cstring>
using namespace std;

class encoderDecoder {

public :

    encoderDecoder();
    int encode (string str, char* input);
    void shortToBytes(short num, char* bytesArr);
    char* test(string str, char* input);
    short bytesToShort(char *bytesArr);

};


#endif //CLIENT2_ENCODERDECODER_H
