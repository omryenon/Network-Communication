CFLAGS:=-c -Wall -Weffc++ -g -std=c++11 -Iinclude -lpthread -lboost_thread
LDFLAGS:=-lboost_system -lpthread -lboost_thread

all: BGRSclient
	g++ -o bin/BGRSclient bin/connectionHandler.o bin/client.o bin/encoderDecoder.o  $(LDFLAGS)

BGRSclient: bin/connectionHandler.o bin/client.o bin/encoderDecoder.o
	
bin/connectionHandler.o: src/connectionHandler.cpp
	g++ $(CFLAGS) -o bin/connectionHandler.o src/connectionHandler.cpp

bin/client.o: src/client.cpp
	g++ $(CFLAGS) -o bin/client.o src/client.cpp

bin/encoderDecoder.o: src/encoderDecoder.cpp
	g++ $(CFLAGS) -o bin/encoderDecoder.o src/encoderDecoder.cpp


.PHONY: clean
clean:
	rm -f bin/*
