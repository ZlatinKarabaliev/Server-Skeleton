package org.softuni.javache;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;


public class RequestHandlerLoader {
    private static  final  String SERVER_PACKAGE_PATH = "org/softuni/javache/" ;
    private static  final  String LIB_FOLDER_PATH =
            Server.class
            .getResource("")
            .getPath()
            .replace(SERVER_PACKAGE_PATH,"lib");
    public HashSet<RequestHandler> loadRequestHandlers(){
        HashSet<RequestHandler> requestHandlers = new HashSet<>();

        this.loadFile(LIB_FOLDER_PATH, requestHandlers);


        return requestHandlers;
    }
    public  void loadFile( String path,HashSet<RequestHandler> requestHandlers){
        File currentFileOrDirectory = new File(path);
        if (!currentFileOrDirectory.exists()){
            return;
        }

        for (File childFileOrDirectory : currentFileOrDirectory.listFiles()) {
            if (childFileOrDirectory.isDirectory()){
                this.loadFile(
                        path + "/"+ childFileOrDirectory.getName(),
                        requestHandlers);
            }else{
                try {
                   URL fileUrl = (childFileOrDirectory.getParentFile().toURI().toURL());
                    URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{fileUrl});

                    Class<?> clazz =urlClassLoader
                            .loadClass(childFileOrDirectory.getName()
                                    .replace(".class",""));

                    if (RequestHandler.class.isAssignableFrom(clazz)){
                        RequestHandler clazzInstance =
                                (RequestHandler) clazz
                                        .getConstructor(String.class)
                                        .newInstance(WebConstants.WEB_SERVER_FOLDER_PATH);
                        requestHandlers.add(clazzInstance);
                    }

                } catch (ClassNotFoundException
                        | IllegalAccessException
                        | InstantiationException
                        | NoSuchMethodException
                        | MalformedURLException
                        | InvocationTargetException e) {
                    e.printStackTrace();

                }
            }
        }
    }
}
