import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client{

   public static void main(String[] args){

    try{
        System.out.println("Client Started...\n");
        Socket socket = new Socket ("192.168.143.203", 6000);
        while(true){
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Client: ");
            String string = userInput.readLine();
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            output.println(string);

            BufferedReader serverInputBuffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String serverString = serverInputBuffer.readLine();
            System.out.println("Server: "+serverString);
            if(string.equals("bye")){
                break;
            }

        }
        socket.close();
    }
    catch(Exception e){
        e.printStackTrace();
    }

   }


}
