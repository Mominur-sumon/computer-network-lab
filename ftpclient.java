import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ftpclient {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        String message, receivedMsg;

        Socket socket = new Socket("127.0.0.1", 7777);
        DataOutputStream dsOut = new DataOutputStream(socket.getOutputStream());
        DataInputStream dsIn = new DataInputStream(socket.getInputStream());

        while (true) {
            System.out.print(">");
            message = scanner.nextLine();
            dsOut.writeUTF(message);

            if (message.length() >= 5 && message.substring(0, 4).equals("get ")) {
                if (dsIn.readBoolean()) {
                    String fileName = message.substring(4);
                    receiveFile(dsIn, fileName);
                    System.out.println("File received: " + fileName);
                }
                else {
                    System.out.println("File not found.");
                }
            }

            else if (message.length() >= 5 && message.substring(0, 4).equals("put ")) {
                String fileName = message.substring(4).trim();
                File fileToSend = new File(fileName);
                if (fileToSend.exists() && fileToSend.isFile()) {
                    dsOut.writeBoolean(true);
                    FileInputStream fileInputStream = new FileInputStream(fileToSend);
                    byte[] buffer = new byte[16777216];
                    int bytesRead;
                    while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                        dsOut.write(buffer, 0, bytesRead);
                    }
                    fileInputStream.close();
                } else {
                    dsOut.writeBoolean(false);
                    System.out.println("File not found.");
                }
            }
            
            else if(message.equalsIgnoreCase("exit")) {
                break;
            }
            else {
                receivedMsg = dsIn.readUTF();
                System.out.println(receivedMsg);
            }
        }
        socket.close();
        scanner.close();
    }

    private static void receiveFile(DataInputStream dsIn, String fileName) throws Exception {
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        byte[] buffer = new byte[16777216];
        int bytesRead;
        bytesRead = dsIn.read(buffer);
        fileOutputStream.write(buffer, 0, bytesRead);
        fileOutputStream.close();
        
    }
}
