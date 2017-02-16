import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Name: Alex Oladele
 * Unique ID: OLADELAA
 * Date: 2/13/17
 * Assignment: Project1
 */
public class TCPClient {
    //    public static int arrayLength;
    private static String[] fileNames;


    private Socket socket;

    public TCPClient(String host, int port, String file) {
        try {
//            Create new socket to connect o server
            socket = new Socket(host, port);

//            Sends file to server to be cloned
            sendFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public TCPClient() {
    }

    public void sendFile(String file) throws IOException {
//        DOS to write data out!
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

//        DIS to read in info
        FileInputStream in = new FileInputStream(file);

//        buffer for writing files
        byte[] buffer = new byte[4096];

//        Flushes the output stream to clear out any bad / error data
        out.flush();

        // Writes the fileName to the socket to be used for the server
        out.writeUTF(file);

//        Gets File size and sends it through the socket
        File file1 = new File(file);
        int fileSize = Math.toIntExact(file1.length());

        out.flush();
//        Writes filesize to server through socket
        out.writeInt(fileSize);

        while (in.read(buffer) > 0) {
            out.flush();
//            Writes buffer
            out.write(buffer);
        }

//        Closes both streams
        in.close();
        out.close();
    }

    public static void main(String[] args) throws IOException {
//        Create a new file in order to get the absolute path
        File fileConvert;

//        If statement to make sure that files are actually passed into the program
        if (args.length > 0) {
//            String[] that contains the file names
            fileNames = new String[args.length];


            for (int i = 0; i < fileNames.length; i++) {
//                Create file object  to convert into canonical (abs)  path from relative path
                fileConvert = new File(args[i]);
                String absFileName = fileConvert.getCanonicalPath();

//                Adds filepath name to string[]
                fileNames[i] = absFileName;
            }

            for (String fileName : fileNames) {
//                Passes args through constructor to be cloned
                TCPClient tcpClient = new TCPClient("localhost", 1234, fileName);

            }
        } else {
//            Closes program if no args passed
            System.out.println("No Files passed into program. Exiting....");
            System.exit(0);
        }
    }
}
