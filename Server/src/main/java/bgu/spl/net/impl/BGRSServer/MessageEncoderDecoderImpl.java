package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.impl.BGRSServer.ops.*;
import bgu.spl.net.impl.BGRSServer.ops.Error;

import java.lang.*;
import java.nio.charset.*;
import java.util.*;

public class MessageEncoderDecoderImpl implements MessageEncoderDecoder<Operation> {

    private byte[] bytes = new byte[1 << 10]; //start with 1k
    private int len = 0;
    private short opNum = 0;
    private int delimterCounter = 0;
    private int[] zeroLocation = new int[2];

    /**
     * firstly decode first to bytes to get the right opNum, then devided to cases by th opNum
     * and get all bytes of the message by its knowen structure.
     * after got all the bytes use popOperation funcation.
     * @param nextByte the next byte to consider for the currently decoded
     * message
     * @return the operation from the popOperation function.
     */

    @Override
    public Operation decodeNextByte(byte nextByte) {
        if (len < 2){
            pushByte(nextByte);
            if (len == 2){
                opNum = bytesToShort(bytes);
            }
            if (opNum != 4 && opNum != 11)
                return null; //not a line yet
        }

        if (opNum == 1 || opNum == 2 || opNum == 3) {
            pushByte(nextByte);
             if (nextByte == '\0') {
                 zeroLocation[delimterCounter] = len-1;
                 delimterCounter++;
             }

            if (delimterCounter == 2)
                return popOperation();

            return null; //not a line yet
        }

        else if (opNum == 4 || opNum == 11){
            return popOperation();
        }

        else if (opNum == 8){
            pushByte(nextByte);
            if (nextByte == '\0') {
                zeroLocation[delimterCounter] = len-1;
                delimterCounter++;
            }

            if (delimterCounter == 1)
                return popOperation();

            return null; //not a line yet
        }

        else {
            pushByte(nextByte);
            if (len == 4)
                return popOperation();
            return null; //not a line yet
        }
    }

    private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }
        bytes[len++] = nextByte;
    }

    /**
     * decode all the byte by the knowen structure by the given opNum.
     * then create a right Operation
     * @return the created Operation
     */

    private Operation popOperation() {
        Operation result = null;

        if (opNum == 1 || opNum == 2 || opNum == 3){
            String userName = new String(bytes, 2, zeroLocation[0]-2, StandardCharsets.UTF_8);
            String password = new String(bytes, zeroLocation[0], zeroLocation[1]-2, StandardCharsets.UTF_8);
            if(opNum == 1)
                result = new AdminRegister(opNum,userName,password);
            else if(opNum == 2)
                result = new StudentRegister(opNum,userName,password);
            else
                result = new Login(opNum,userName,password);

        }

        else if (opNum == 4 || opNum == 11){
            if (opNum == 4 )
                result = new Logout(opNum);
            else
                result = new MyCourses(opNum);
        }

        else if (opNum == 8){
            String userName = new String(bytes, 2, zeroLocation[0]-2, StandardCharsets.UTF_8);
            result = new StudentStatus(opNum, userName);
         }

        else{
            byte[] subBytes = {bytes[2], bytes[3]};
            short courseNum = bytesToShort(subBytes);
            if (opNum == 5)
                result = new CourseReg(opNum , courseNum);
            else if (opNum == 6)
                result = new KdamCheck(opNum , courseNum);
            else if (opNum == 7)
                result = new CourseStatus(opNum , courseNum);
            else if (opNum == 9)
                result = new IsRegister(opNum , courseNum);
            else
                result = new Unregister(opNum , courseNum);
         }
        opNum = 0;
        len = 0;
        delimterCounter = 0;
        return result;
    }


    public short bytesToShort(byte[] byteArr) {
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;
    }


    /**
     * encodes the given message ACK/ERROR + OpNum to bytes array
     * incase of ACK with adittinoal data, encode it as well
     * @param message the operation to encode
     * @return the encoded bytes
     */
    @Override
    public byte[] encode(Operation message) {
        String additionalData="";
        byte[] bytesOutput;
        byte[] temp;
        short myOP;
        short otherOP;

        if( message instanceof Ack) {
            myOP = 12;
            otherOP = ((Ack) message).getOpNum();
            if (((Ack) message).getAckMessage() != null)
                additionalData = "0000" + ((Ack) message).getAckMessage() + "\0"; // keep space for 4 bytes at the start, for 2 op numbers.
            else
                additionalData = "0000" + "\0"; // keep space for 4 bytes at the start, for 2 op numbers.

            bytesOutput = new byte[additionalData.length()];

            //encode all the string to bytes
            bytesOutput = additionalData.getBytes();
        }
        else{
            myOP = 13;
            otherOP = ((Error) message).getOpNum();
            bytesOutput = new byte[4];
        }

        // putting 12 (ACK) in the output
        temp = shortToBytes(myOP);
        bytesOutput[0] = temp[0];
        bytesOutput[1] = temp[1];

        // putting other op number in the output
        temp = shortToBytes(otherOP);
        bytesOutput[2] = temp[0];
        bytesOutput[3] = temp[1];

        return bytesOutput;
    }

    public byte[] shortToBytes(short num) {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }
}
