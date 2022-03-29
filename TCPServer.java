import java.net.*;
import java.io.*;
import java.util.concurrent.TimeUnit;

public class TCPCLient {
    public static void main(String[] args) throws IOExecption {

        int aPort = Integer.parseInt(args[0]);
        System.out.println("Port Number: " + aPort);
        ServerSocket ss = new ServerSocket(aPort);
        while(true){
            try{
                Socket s = ss.accept(); //established connection
                DataInputStream din = new DataInputStream(s.getInputStream());
                DataOutputStream dout = new DataOutputStream(s.getOutputStream());

                System.out.println("Target IP: " _ s.getInetAddress() + " Target Port: " + s.getPort());
                System.out.println("Local IP: " + s.getLocalAddress() + " Local Port: " +s.getLocalPort());
                
                String str = (String)din.readUTF();
                System.out.println("RCVD: " +str);

                dout.writeUTF("G'DAY");
                System.out.println("SENT: G'DAY");

                str = (String)din.readUTF();
                System.out.println("RCVD: "+str);

                dout.writeUTF("BYE");
                System.out.println("SENT: BYE");

                din.close();
                dout.close();
                s.close();

            }
            catch(Exception e){System.out.println(e);}
        }
    }
}
