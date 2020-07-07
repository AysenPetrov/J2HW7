package J2HW7.server;

import java.net.*;
import java.io.*;

public class ClientHandler {

    //Служебный класс, которому сервер передаст сокет
    //По сокету будет происходить общение с подключившимся клиентом

    private MServer server; //ссылка на сервер для клиента
    private Socket socket;
    private DataInputStream  in; //поток ввода, используется UTF-кодировка
    private DataOutputStream out;//поток вывода, используется UTF-кодировка
    private String clientName;

    private static int counter = 0;

    public ClientHandler(MServer server, Socket socket)
    {

        try{
            counter++;
            this.clientName = "user" + Integer.toString(counter);
            this.server = server;
            this.socket = socket;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());

            new Thread(()-> {


                try
                {
                    while(true)
                    {
                        String str = in.readUTF();

                        System.out.println("A message from a client: " + str);

                        if(str.equalsIgnoreCase("/end"))
                        {
                            break;
                        }

                        if(str.startsWith("/w"))
                        {
                            String to = str.split(" ")[1];
                            String msg = str.split(" ")[2];
                            server.wisperMsg(this, to, msg); // сообщение участниками диалога

                        } else {
                            server.broadcastMsg("[" + this.clientName + "] " + str);//Cервер разослал сообщение String str = in.readUTF() ВСЕМ подключенным клиентам
                        }
                        out.flush();
                    }
                }
                catch(IOException ex)
                {
                    ex.printStackTrace();
                }
                finally//освобождаем ресурсы
                {
                    try
                    {
                        in.close();
                        //out.close();   //в отдельный блок try - catch
                        //socket.close();//в отдельный блок try - catch
                    }
                    catch(IOException ex)
                    {
                        ex.printStackTrace();
                    }
                    try
                    {
                        out.close();
                    }
                    catch(IOException ex)
                    {
                        ex.printStackTrace();
                    }
                    try
                    {
                        socket.close();
                    }
                    catch(IOException ex)
                    {
                        ex.printStackTrace();
                    }

                    server.remove_client(this);//Клиент отключился и больше не активен

                }//finally

            }).start();

        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }//public ClientHandler

    public String getClientName() {
        return this.clientName;
    }

    //если нужно послать сообщение клиенту
    public void sendMsg(String msg)
    {
        try
        {
            out.writeUTF(msg);//отослать сообщение клиенту
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }
