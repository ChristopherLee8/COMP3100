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


            // This while loop will run until
            while (!currentMsg.contains("NONE")) {
                
                sendMsg(s, "REDY\n"); //sends message to the server saying its ready for jobs
                currentMsg = readMsg(s);
                
                // Checks to see if the message is a new job
                if (currentMsg.contains("JOBN")) {
                    String[] JOBNSplit = currentMsg.split(" ");
                    //checks which servers are availble
                    sendMsg(s, "GETS Avail " + JOBNSplit[4] + " " + JOBNSplit[5] + " " + JOBNSplit[6] + "\n");
                    //Reads the msg saying what data is about to be sent and responds with "OK"
                    currentMsg = readMsg(s);
                    sendMsg(s, "OK\n");

                    currentMsg = readMsg(s);
                    sendMsg(s, "OK\n");

                    //Checks to see if the largest Server has been found
                    //Used as a flag to see that it was found on the first round
                    if(found == false){
                        ls = findLS(currentMsg);
                        found = true;
                    }
                    currentMsg = readMsg(s); //reads . from the message

                    //Schedule the current job to the largest server (SCHD JobNumber ServerName ServerNumber)
                    sendMsg(s, "SCHD " + JOBNSplit[2] + " " + ls[0] + " " + ls[1] + "\n");

                    //Read the next JOBN
                    currentMsg = readMsg(s);
                    System.out.println("SCHD: " + currentMsg);
                }
                else if (currentMsg.contains("DATA")) {
                    sendMsg(s, "OK\n");
                }
            }
            // Sends "Quit" to the server to end the session which then closes the socket
            sendMsg(s, "QUIT\n");
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

    public static String[] findLS(String currentMsg) {
        // All the servers in the currentMsg being split into an array
        String[] serversAndInfo = currentMsg.split("\n");
        //Sets up variables
        int mostCores = 0;
        String[] currentServer = {""};
        //Searches for the most cores a server holds in the given available servers
        for (int i = 0; i < serversAndInfo.length; i++) {
            currentServer = serversAndInfo[i].split(" ");
            int currentCores = Integer.valueOf(currentServer[4]);
            if (currentCores > mostCores) {
                mostCores = currentCores;
            }
        }
        //Finds and returns the biggest server (The one with the most cores)
        for (int i = 0; i < serversAndInfo.length; i++) {
            currentServer = serversAndInfo[i].split(" ");
            int currentCores = Integer.valueOf(currentServer[4]);
            if (currentCores == mostCores) {
                return currentServer;
            }
        }
        return currentServer;

    }
}