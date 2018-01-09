package util;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import booking.BookingServiceActor;
import message.Register;
import routesprovider.BusRoutesProviderActor;
import service.RoutesServiceActor;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Created by jexmaster on 07.01.2016.
 *
 * @author Viktoria Jechsmayr
 */
public class Startup {
    public static void main(String[] args)
    {
        try {
            PrintWriter writer = new PrintWriter("/tmp/busprovider.log");
            writer.println("Startup busprovider");
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        String systemName = "CEK3BusProvider";
        String systemIP = args.length > 0 ? args[0] : "140.78.196.17";
        String systemPort = args.length > 1 ? args[1] : "2552";

        ActorSystem system = ActorSystem.create(systemName);
        ActorRef routesServiceActorRef = system.actorOf(RoutesServiceActor.props());
        ActorRef bookingServiceActorRef = system.actorOf(BookingServiceActor.props());
        ActorRef busProvider = system.actorOf(BusRoutesProviderActor.props(routesServiceActorRef, bookingServiceActorRef), "CEK3BusProvider");

        busProvider.tell(new Register(systemName,systemIP,systemPort), ActorRef.noSender());
    }

}
