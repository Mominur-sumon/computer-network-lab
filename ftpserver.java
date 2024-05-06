import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ftpserver {
    public static void main(String[] args) throws Exception{
        ServerSocket ss = new ServerSocket(7777);
        Socket socket = ss.accept();
        DataInputStream dsIn = new DataInputStream(socket.getInputStream());
        DataOutputStream dsOut = new DataOutputStream(socket.getOutputStream());

        while (true) {
            String msg = dsIn.readUTF();
            System.out.println("Received command: " + msg);

            if(msg.equals("ls")) {
                File currentDir = new File(".");
                File[] files = currentDir.listFiles();
                StringBuilder response = new StringBuilder();
                response.append("Contents of current directory:\n");
                for (File file : files) {
                    response.append(file.getName()).append("\n");
                }
                dsOut.writeUTF(response.toString());
            }

            else if (msg.length() >= 5 && msg.substring(0, 4).equals("get ")) {
                String fileName = msg.substring(4).trim();
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
                    // System.out.println("Not found.");
                }
            }

            else if (msg.length() >= 5 && msg.substring(0, 4).equals("put ")) {
                if (dsIn.readBoolean()) {
                    String fileName = msg.substring(4);
                    receiveFile(dsIn, fileName);
                    System.out.println("File received: " + fileName);
                }
                else {
                    // System.out.println("Incorrect file name");
                }
            }

            else if (msg.equalsIgnoreCase("exit")) {
                System.out.println("Connection Terminated.");
                dsOut.writeUTF("Connection Terminated.");
                break;
            }

            else {
                dsOut.writeUTF("Server: Unknown Command");
            }
        }

        ss.close();
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