import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Name: Alex Oladele
 * Unique ID: OLADELAA
 * Date: 2/13/17
 * Assignment: Project1
 */
public class TCPServer implements Runnable {

    private ServerSocket welcomeSocket;

    public TCPServer(int port) {
        try {
            welcomeSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //    Runs process on new thread
    @Override
    public void run() {

        while (true) {
            try {
//                Starts accepting new files
                System.out.println("Receiving File(s).......");
                Socket socket = welcomeSocket.accept();

//                Save file method
                saveDaFile(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    private void saveDaFile(Socket socket) throws IOException {
//        DIS for receiving files
        DataInputStream in = new DataInputStream(socket.getInputStream());

//        buffer for writing files
        byte[] buffer = new byte[4096];

        for (int i = 0; i < 1; i++) {
            //            Reads in passed in fileName and extracts the parts of it in order to make cloned name
            String readIn = in.readUTF(),
                    fileName = readIn.substring(0, readIn.lastIndexOf("/") + 2),
                    mimeType = readIn.substring(readIn.lastIndexOf("."));

            int fileSize = in.readInt(),
                    read = 0,
                    totalRead = 0,
                    remaining = fileSize;

            System.out.println("Creating new file " + (i + 1));

//            Creates new file and appends "-cloned" to file name
            FileOutputStream out = new FileOutputStream(fileName + "-cloned" + mimeType);


//            Flushes outStream in case of any bad data in it
            out.flush();

//            Writes the file until all bytes are written
            while ((read = in.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
                totalRead += read;
                remaining -= read;
                System.out.println("read " + totalRead + " bytes.");
                out.write(buffer, 0, read);
            }

//            Closes input and output stream
            in.close();
            out.close();
        }
    }

    public static void main(String[] args) throws IOException {
//        Creates and runs server
        TCPServer server = new TCPServer(1234);
        server.run();
    }
}
