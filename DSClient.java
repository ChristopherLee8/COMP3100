import java.net.*;
import java.io.*;
import java.util.concurrent.*;

public class DSClient {
    public static void main(String[] args) {
        while(true) {
            try{
                InetAddress aHost = InetAddress.getByName(args[0]);
                int aPort = Integer.parseInt(args[1]);
                Socket s = new Socket(aHost,aPort);
                DataOutputStream dout = new DataOutputStream(s.getOutputStream());
                BufferedReader din = new BufferedReader(new InputStreamReader(s.getInputStream()));

                System.out.println("Target IP: " + s.getInetAddress() + " Target Port: "+ s.getPort());
                System.out.println("Local IP: " + s.getLocalAddress() + " Local Port: " + s.getLocalPort());
                
                dout.flush();
                dout.write(("HELO\n").getBytes());
                System.out.println("SENT: HELO");
                
                dout.flush();
                String str = (String)din.readLine();
                System.out.println("RCVD: "+str);
                dout.write(("BYE\n").getBytes());
                System.out.println("SENT: BYE");

                dout.flush();
                str = (String)din.readLine();
                System.out.println("RCVD: "+str);

                din.close();
                dout.close();
                s.close();
            }
            catch(Exception e){System.out.println(e);}
            try {TimeUnit.SECONDS.sleep(1);} catch(InterruptedException e){System.out.println(e);}
        }
    }
}