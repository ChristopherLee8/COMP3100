import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Client {
//Declaring variables
//Assigning localhost
public static String hostName = "localhost";

//Assigning server port number
public static int serverPort = 50000;

//Assigning username
public static String userName = "user.name";

//Authentication username on the system
public static String AUTHusername = "AUTH " + System.getProperty(userName);

//Server variables
public static String[] arrMsg;
public static String[] oriServer;
public static ArrayList<Server> serverList;

public static Socket socket;
public static BufferedReader buffered;
public static DataOutputStream outputStream;

public Client (String address, int port) throws Exception{
    serverList = new ArrayList<Server>();
    oriServer = new String [7];
    socket = new Socket(address, port);
    buffered = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    outputStream = new DataOutputStream(socket.getOutputStream());
}

//Read messages
public static String[] read() throws Exception {
    var line = buffered.readLine();

    //Assigning variables to String
    String readMsg = line.toString();
    arrMsg = readMsg.split("\\s+");

    return arrMsg;
}

//Send messages
public static void send(String msg) throws IOException{
    //Adding mesaage with new line
    String sendMsg = msg + "\n";
    byte[] message = sendMsg.getBytes();

    outputStream.write(message);
    outputStream.flush();
}

//New algorithm
public static void lowCost(Job job) throws IOException{
    for(Server server:serverList){
        if(server.coreCount >= job.core && server.memory >= job.memory &&
            server.disk >= job.disk && job.startTime >= job.runTime){
                Client.send("SCHD " + job.jobID + " " + server.type + " " + server.serverID);
                return;
            }
    }
    Client.send("SCHD " + job.jobID + " " + oriServer[0] + " " + oriServer[1]);
}

//Executing main function
public static void main(String[] args) {
    try{
    //Creating new instance
    Client server = new Client(hostName, serverPort);
    server.send("HELO");
    server.read();

    //Authenticate with username
    server.send(AUTHusername);

    //This function will send data when ready
    while(server.read()[0] != "error"){
        if(arrMsg[0].equals("OK")){
            server.send("REDY");
        }

        switch (arrMsg[0]){
            //Scheduling job
            case "JOBN":
                //Creating object to store job details
                Job desc = new Job (Integer.parseInt(arrMsg[1]),Integer.parseInt(arrMsg[2]),
                                Integer.parseInt(arrMsg[3]),Integer.parseInt(arrMsg[4]),
                                Integer.parseInt(arrMsg[5]),Integer.parseInt(arrMsg[6]));

                //Server will run when recieving the job
                server.send("GETS " + "Capable " + desc.core + " " + desc.memory + " " + desc.disk);

                String[] msg = server.read();
                server.send("OK");

                //Adding new servers to the list
                for(int i = 0; i < Integer.parseInt(msg[1]); i++){
                    String[] newServer = server.read();
                    if(i == 0){
                        oriServer = newServer;
                    }

                    Server s = new Server(newServer[0],Integer.parseInt(newServer[1]),
                                            newServer[2],Integer.parseInt(newServer[3]),
                                            Integer.parseInt(newServer[4]),Integer.parseInt(newServer[5]),
                                            Integer.parseInt(newServer[6]));
                            
                                serverList.add(s);
                }

                server.send("OK");
                server.read();
                
                //Checking if there is any data before scheduling
                if(arrMsg[0].equals(".")){
                    lowCost(desc);
                    serverList.clear();
                }
                break;

                //Job Completion
                case "JCPL":
                server.send("REDY");
                break;

                //Otherwise, it will quit the program
                case "NONE":
                server.send("QUIT");
                server.read();
                server.buffered.close();
                server.outputStream.close();
                server.socket.close();
                break;
            }
        }
    }
        catch(Exception e){

        }
    }
}