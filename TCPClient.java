import java.net.*;
import java.io.*;
import java.util.concurrent.TimeUnit;

public class TCPCLient {
    public static void main(String[] args) {
        while(true) {
            try{
                InetAddress aHost = InetAddress.getByName(args[0]);
                int aPort = Integer.parseInt(args[1]);
                Socket s = new Socket(aHost,Aport);
                DataOutputStream dout = new DataOutputStream(s.getOutputStream());
                DataInputStream din = new DataInputStream(s.getInputStream());

                System.out.println("Target IP: " + s.getInetAddress() + " Target Port: "+ s.getPort());
                System.out.println("Local IP: " + s.getLocalAddres() + " Local Port: " + s.getLocalPort());
                
                dout.writeUTF("HELO");
                System.out.println("SENT: HELO");
                
                String str = (String)din.readUTF();
                System.out.println("RCVD: "+str);
                dout.writeUTF("BYE");
                System.out.println("SENT: BYE");

                str = (String)din.readUTF();
                System.out.println("RCVD: "+str);

                din.close();
                dout.close();
                s.close();
            }
            catch(Exceptione){System.out.println(e);}
            try {TimeUnit.SECONDS.sleep(1);} catch(InterruptedException e){system.out.println(e);}
        }
    }
}