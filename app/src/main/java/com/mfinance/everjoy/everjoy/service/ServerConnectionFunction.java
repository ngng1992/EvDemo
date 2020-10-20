package com.mfinance.everjoy.everjoy.service;

import android.util.Log;


import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ServerConnectionFunction {

    private static final String TAG = ServerConnectionFunction.class.getSimpleName();

    private String sUrl;
    private int iPort;
    private String sendMsg;

    public ServerConnectionFunction(String sUrl, int iPort, String sendMsg) {
        this.sUrl = sUrl;
        this.iPort = iPort;
        this.sendMsg = sendMsg;
    }


    public void connection() {
        try {
            // 与服务端建立连接
            Socket socket = new Socket(sUrl, iPort);
            // 建立连接后获得输出流
            OutputStream outputStream = socket.getOutputStream();
            socket.getOutputStream().write(sendMsg.getBytes("UTF-8"));
            //通过shutdownOutput高速服务器已经发送完数据，后续只能接受数据
            socket.shutdownOutput();

            InputStream inputStream = socket.getInputStream();
            byte[] bytes = new byte[1024];
            int len;
            StringBuilder sb = new StringBuilder();
            while ((len = inputStream.read(bytes)) != -1) {
                //注意指定编码格式，发送方和接收方一定要统一，建议使用UTF-8
                sb.append(new String(bytes, 0, len, "UTF-8"));
            }
            Log.e(TAG, "接收服务器的消息 = " + sb.toString());

            inputStream.close();
            outputStream.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//    private WebSocket webSocket;


    public void startConnection() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                connection();
            }
        });
        thread.start();

//        try {
//            String url = "ws://" + sUrl + ":" + iPort;
//            WebSocketFactory webSocketFactory = new WebSocketFactory();
////            ProxySettings proxySettings = webSocketFactory.getProxySettings();
////            proxySettings.setHost(sUrl);
////            proxySettings.setPort(iPort);
//            webSocketFactory.setConnectionTimeout(5000);
//            webSocket = webSocketFactory.createSocket(url);
////            webSocket.addExtension(WebSocketExtension.PERMESSAGE_DEFLATE);
//            webSocket.addListener(new WebSocketAdapter() {
//                @Override
//                public void onTextMessage(WebSocket websocket, String text) throws Exception {
//                    super.onTextMessage(websocket, text);
//                    // 接收消息
//                    Log.e("socket", "onTextMessage = " + text);
//                }
//
//                @Override
//                public void onError(WebSocket websocket, WebSocketException cause) throws Exception {
//                    super.onError(websocket, cause);
//                    // 错误1 An I/O error occurred while a frame was being read from the web socket: Software caused connection abort
//                    // 从web套接字读取帧时发生I/O错误：软件导致连接中止
//
//                    // 错误2  Flushing frames to the server failed: Broken pipe
//                    // 对服务器的冲洗帧失败:已断开的管道
//
//                    // 当关闭网络时，1次错误1,2次错误2，1次onDisconnected
//                    Log.e("socket", "onError = " + cause.getMessage());
//                }
//
//                @Override
//                public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
//                    super.onConnected(websocket, headers);
//                    // 连接成功
//                    Log.e("socket", "onConnected =========== ");
//                }
//
//                @Override
//                public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
//                    super.onDisconnected(websocket, serverCloseFrame, clientCloseFrame, closedByServer);
//                    Log.e("socket", "onDisconnected =========== ");
//                }
//            });
//            // 只能调用一次，出错NetworkOnMainThreadException
//            webSocket.connect();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }


    private void sendMsg(String message) {
//        webSocket.sendText(message);
    }
}
