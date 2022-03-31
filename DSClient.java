import java.net.*;
import java.io.*;
import java.util.concurrent.*;

public class DSClient {
    public static void main(String[] args) {

        try{
            InetAddress aHost = InetAddress.getByName(args[0]); //gets host from input
            int aPort = Integer.parseInt(args[1]); //gets port from input
            Socket s = new Socket(aHost,aPort); //assigns socket

            //variables
            String[] ls = {""}; //Largest Server
            boolean found = false;
            String currentMsg = "";
            CSH(s); //Client/Server Handshake
            
            sendMsg(s, "HELO\n"); //Sends HELO to server
            currentMsg = readMsg(s); //Displays received message
            sendMsg(s, "BYE\n"); //Sends BYE to server
            currentMsg = readMsg(s); //Displays received message

            s.close();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void CSH(Socket s) {
        String currentMsg = "";
        sendMsg(s, "HELO\n"); //Initiate with contact with Server
        currentMsg = readMsg(s); //Display Response from Server
        System.out.println("RCVD: " + currentMsg);
        sendMsg(s, "AUTH Computer\n"); //Authenticate with Username "computer"
        currentMsg = readMsg(s);
        System.out.println("RCVD: " + currentMsg); //Display response from Server
    }


    //This function reads the incoming message and outputs it at the end
    public static synchronized String readMsg(Socket s) {
        String currentMsg = "FAIL";
        try {
            DataInputStream din = new DataInputStream(s.getInputStream());
            byte[] byteArray = new byte[din.available()]; //clears byteArray
            byteArray = new byte[0];
            while (byteArray.length == 0) {
                byteArray = new byte[din.available()];
                din.read(byteArray);
                currentMsg = new String(byteArray, StandardCharsets.UTF_8);
            }
        } 
        catch (IOException e) {e.printStackTrace();}
        System.out.println("RCVD: " + currentMsg);
        return currentMsg; //returns the received message
    }

    //the function below is used to send a message to the server
    public static synchronized void sendMsg(Socket s, String currentMsg) {
        try {
            DataOutputStream dout = new DataOutputStream(s.getOutputStream());
            byte[] byteArray = currentMsg.getBytes();
            dout.write(byteArray);
            System.out.println("SENT: " + currentMsg);
            dout.flush(); //after a message is sent this clears the field ready for a new message
        } 
        catch (IOException e) {e.printStackTrace();}
    }

}