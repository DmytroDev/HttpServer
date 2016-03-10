package com.infopulse.server;

import com.infopulse.films.Films;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class AdvancedHttpServer {

    public static void main(String[] args) throws IOException {
        new AdvancedHttpServer().run();
    }

    private void run() throws IOException {
        ServerSocket serverSocket = new ServerSocket(3000);

        while (true) {
            try (Socket socket = serverSocket.accept();
                 BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

                String urlPattern = getHeaderFromRequest(bufferedReader);
                if (!"favicon.ico".equals(urlPattern)) {
                    switch (urlPattern) {
                        case "300":
                            getFile(Films.THREE_HUNDRED);
                            break;
                        case "green":
                            getFile(Films.GREEN_BEAUTIFUL);
                            break;
                        case "truth":
                            getFile(Films.UGLY_TRUTH);
                            break;
                        default:
                            getFile(Films.INCORRECT);
                            break;
                    }
                }
                out.write("HTTP/1.0 200 OK\r\n");
                out.write("Server: Apache/0.8.4\r\n");
                out.write("Content-Type: text/html\r\n");
                out.write("\r\n");  // content bellow
                out.write("<TITLE>Example</TITLE>");
                out.write("<P>Hello from server</P>");

            }
        }
    }

    // TODO: need change type value that return
    private void getFile(String fileName) throws IOException {
        try (InputStream inputStream = this.getClass().getResourceAsStream("/content/" + fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            String lineFromFile = null;
            while ((lineFromFile = reader.readLine()) != null) {
                System.out.println(lineFromFile);
            }
        }
    }

    // Method get url pattern  from request header (part of first line)
    private String getHeaderFromRequest(BufferedReader bufferedReader) {
        String firstLineFromHeader, urlPattern = null;
        try {
            firstLineFromHeader = bufferedReader.readLine();
            urlPattern = (firstLineFromHeader != null) ? firstLineFromHeader.split(" ")[1].substring(1) : "invalid";
        } catch (IOException e) {
            e.getMessage();
        }
        return urlPattern;
    }
}
