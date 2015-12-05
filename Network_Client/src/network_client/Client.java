/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network_client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author Julien CARMIGNANI
 */
public class Client {
    private static final int SIZE_BUFFER = 1000000;
    private static int nbrPort = 15000;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException{
        SocketChannel client=SocketChannel.open();
        //client.configureBlocking(false);
        boolean status = client.connect(new InetSocketAddress("127.0.0.1", nbrPort));
        System.out.println("Coucou");
        System.out.println("Status = " + status);
        
        System.out.println("Opening client side on port " + nbrPort +"...");
        
        //Getting File
        File file = new File("/Users/starwers13/Documents/kld_sampling.txt");
        //byte[] data = Files.readAllBytes(Paths.get("/Users/starwers13/Pictures/julien.txt"));
        //System.out.println("Size = " + data.length);
        ByteBuffer buffer = ByteBuffer.allocate(SIZE_BUFFER);
        FileChannel fc = FileChannel.open(file.toPath());
        
        //Send the name of the file
        //client.write(ByteBuffer.wrap(file.getName().getBytes("UTF_8")));
        int byteRead = fc.read(buffer);
        System.out.println("Size Buffer = " + byteRead);
        System.out.println("Has Remaining = " + buffer.hasRemaining());

        while((byteRead != 0 && byteRead != -1) || buffer.hasRemaining()){
            buffer.flip();
            client.write(buffer);
            buffer.compact();
            buffer.flip();
            byteRead = client.read(buffer);
            System.out.println("Size Buffer = " + byteRead);
            System.out.println("Has Remaining = " + buffer.hasRemaining());
            System.out.println("Sending...");
        }

        System.out.println("Closing client side...");
        client.close();
    }
}
