import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Server{
    static String getCurrentDate(){
        LocalDate localDate = LocalDate.now();
        return DateTimeFormatter.ofPattern("yyyy/MM/dd").format(localDate);
    }

    static String getCurrentTime(){
        LocalTime localTime = LocalTime.now();
        return DateTimeFormatter.ofPattern("HH:mm:ss").format(localTime);
    }

    public static void main(String[] args){
        try{
            System.out.println("Waiting for client request...\n");
            ServerSocket serverSocket = new ServerSocket(9096);
            Socket socket = serverSocket.accept();
            System.out.println("Connection Established\n");

            while(true){
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String socketString = bufferedReader.readLine();
                System.out.println("Client: "+socketString);
                String serverReplyString = "";
                if(socketString.equals("date")){
                    serverReplyString = getCurrentDate();

                }else if(socketString.equals("time")){
                    serverReplyString = getCurrentTime();
                }else{
                    serverReplyString = socketString;
                }
                PrintWriter socketPrintWriter = new PrintWriter(socket.getOutputStream(), true);
                socketPrintWriter.println(serverReplyString);
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
