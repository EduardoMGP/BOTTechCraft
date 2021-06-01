//package br.com.techcraftbrasil.app;
//
//import java.io.DataInputStream;
//import java.io.IOException;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
//public class MainBot {
//    public static void main(String[] args) throws IOException {
//
//        URL url = new URL("http://54.39.130.119:8083/clientes");
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setRequestMethod("GET");
//        conn.setRequestProperty("Content-type", "application/json");
//        conn.setRequestProperty( "charset", "utf-8");
//        conn.setUseCaches(false);
//        try(DataInputStream wr = new DataInputStream(conn.getInputStream())){
//            int data = wr.read();
//            while (data != -1) {
//                System.out.print((char) data);
//                data = wr.read();
//            }
//        }
//    }
//
//}
