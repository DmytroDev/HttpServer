package com.infopulse.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdvancedHttpServer {
    final String DIRECTORY = "./content";

    public static void main(String[] args) throws IOException {
        new AdvancedHttpServer().run();
    }

    private void run() throws IOException {
        List<String> listFiles = getListOfFiles(DIRECTORY);
        ServerSocket serverSocket = new ServerSocket(3000);
        System.out.println("Server started: " + (new Date()));

        while (true) {
            try (Socket socket = serverSocket.accept();
                 BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

                String urlPattern = getHeaderFromRequest(bufferedReader);

                if (!"favicon.ico".equals(urlPattern)) {
                    if ("".equals(urlPattern)) {
                        createInitialPage(bufferedWriter, listFiles);
                    } else {
                        for (String file : listFiles) {
                            if (file.equals(urlPattern)) {
                                createFilePage(bufferedWriter, file);
                                break;
                            }
                        }
                    }
                }
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

    // Method for creating initial page
    private void createInitialPage(BufferedWriter bufferedWriter, List<String> fileList) {
        try {
            //List<String> fileList = getListOfFiles(DIRECTORY);

            StringBuilder stringBuilder = new StringBuilder();
            createHeader(stringBuilder);

            stringBuilder.append("<h2>List of files:</h2>\n");
            stringBuilder.append("<ul>");

            for (String fileName : fileList) {
                stringBuilder.append("<li><a href=\"http://localhost:3000/" + fileName + "\">" + fileName + "</a></li>\n");
            }
            stringBuilder.append("</ul>");
            createFooterInitialPage(stringBuilder);

            bufferedWriter.write(stringBuilder.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createFilePage(BufferedWriter bufferedWriter, String fileName){
        StringBuilder stringBuilder = new StringBuilder();
        try {
            createHeader(stringBuilder);
            stringBuilder.append(getInfoFromFile(fileName));
            createFooterFilePage(stringBuilder);
            bufferedWriter.write(stringBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createHeader(StringBuilder stringBuilder) throws IOException {
        stringBuilder.append("HTTP/1.0 200 OK\r\n");
        stringBuilder.append("Content-Type: text/html\r\n");
        stringBuilder.append("\r\n");
        stringBuilder.append("<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>HTTPServer_v.1.0</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<br>\n");
    }

    private void createFooterInitialPage(StringBuilder stringBuilder) throws IOException {
        stringBuilder.append("</body></html>");
    }

    private void createFooterFilePage(StringBuilder stringBuilder) throws IOException {
        stringBuilder.append("\n<br><br><a href=\"http://localhost:3000/\"</a>Back to main page</body></html>");
    }

    // get list of files from folder "resources/content/"
    private List<String> getListOfFiles(String directory) throws IOException {
        List<String> fileList = new ArrayList<>();
        ClassLoader loader = AdvancedHttpServer.class.getClassLoader();

        try (InputStream inputStream = loader.getResourceAsStream(directory);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                fileList.add(line);
            }
        }
        return fileList;

    }
}
