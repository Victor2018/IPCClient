package com.victor.ipc.util;

import android.util.Log;

import com.victor.ipc.data.MessType;
import com.victor.ipc.data.Message;
import com.victor.ipc.data.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by victor on 2017/3/20.
 */
public class ChatAction {
    private static String TAG = "ChatAction";
    public static void login (User user) {
        Log.e(TAG,"login()......");
        Socket socket = null;
        try {
            socket = new Socket(user.getServerIp(),1990);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            oos.writeObject(user);
            oos.flush();
            Object res = ois.readObject();
            Log.e(TAG,"res.toString() = " + res.toString());
            if (res instanceof User) {
                Log.e(TAG,"goto chat......");
//                ChatFrame cf = new ChatFrame(socket,(User)res,ois,oos);
            } else {
                Log.e(TAG,"close the chat......");
                oos.close();
                ois.close();
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void regist (User user) {
        Socket socket = null;
        try {
            socket = new Socket(user.getServerIp(),1990);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            oos.writeObject(user);
            oos.flush();
            Object res = ois.readObject();
            if (res.equals("success")) {
                Log.e(TAG, "注册成功.........");
            } else {
                Log.e(TAG, "注册成功.........用户名已经存在");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void chat (User fromUser,User toUser,String message,ObjectOutputStream oos) {
        Message mess = new Message();
        mess.setSendName(fromUser.getName());
        mess.setMessage(message);

        if (toUser == null) {
            mess.setType(MessType.gongliao);
        } else {
            mess.setType(MessType.siliao);
            mess.setToName(toUser.getName());
        }

        try {
            oos.writeObject(mess);
            oos.flush();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
}
