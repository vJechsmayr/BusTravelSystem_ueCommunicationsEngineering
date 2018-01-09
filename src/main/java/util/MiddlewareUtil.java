package util;

import akka.actor.ActorRef;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import java.io.IOException;

/**
 * Created by Florian Krenn on 12/3/15.
 */
public class MiddlewareUtil {

  private static final String middlewareAddress = "http://140.78.196.14:9001/";

  public static String getRemoteAddress(String systemName, String ip, String port, ActorRef ref) {
    System.out.println("akka.tcp://"+systemName+"@"+ip+":"+port+ref.path().toStringWithoutAddress());
    return "akka.tcp://"+systemName+"@"+ip+":"+port+ref.path().toStringWithoutAddress();
  }

  public static boolean register(String address) {

    try {
      Request.Post(middlewareAddress + "ceue/busproviders/register")
          .bodyString("\""+address+"\"", ContentType.APPLICATION_JSON).execute();

      return true;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }

  }

}