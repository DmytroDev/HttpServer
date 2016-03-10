package com.infopulse.server;

import com.infopulse.films.Movies;

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
                 BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

                // header bellow
                bufferedWriter.write("HTTP/1.0 200 OK\r\n");
                bufferedWriter.write("Server: Apache/0.8.4\r\n");
                bufferedWriter.write("Content-Type: text/html\r\n");
                bufferedWriter.write("\r\n");
                // content bellow
                bufferedWriter.write("<TITLE>Movie description</TITLE>");

                String fileConsistents = null;
                String urlPattern = getHeaderFromRequest(bufferedReader);
                if (!"favicon.ico".equals(urlPattern)) {
                    switch (urlPattern) {
                        case "300":
                            fileConsistents = getInfoFromFile(Movies.THREE_HUNDRED);
                            break;
                        case "green":
                            fileConsistents = getInfoFromFile(Movies.GREEN_BEAUTIFUL);
                            break;
                        case "truth":
                            fileConsistents = getInfoFromFile(Movies.UGLY_TRUTH);
                            break;
                        default:
                            fileConsistents = getInfoFromFile(Movies.INCORRECT);
                            break;
                    }
                }
                bufferedWriter.write("<P>" + fileConsistents + "</P>");
            }
        }
    }

    // Method get text from concrete file
    private String getInfoFromFile(String fileName) throws IOException {
        StringBuffer fileContents = null;
        try (InputStream inputStream = this.getClass().getResourceAsStream("/content/" + fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            fileContents = new StringBuffer();
            String temporaryLine = null;
            while ((temporaryLine = reader.readLine()) != null) {
                fileContents.append(temporaryLine);
            }
        }
        return fileContents.toString();
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
