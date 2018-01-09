import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.Creator;
import base.CEUEBaseActor;
import connections.BusConnection;
import messages.search.AvailableRoutesResponse;
import util.Time;

/**
 * Created by jexmaster on 07.01.2016.
 *
 * @author Viktoria Jechsmayr
 */
public class ResponseTestActor extends CEUEBaseActor {

    @Override
    public void onReceive(Object message) throws Exception {
        super.onReceive(message);
        if (message instanceof AvailableRoutesResponse) {
            AvailableRoutesResponse tmp = (AvailableRoutesResponse)message;
            final ActorRef sender = getSender();
            for(BusConnection bc : tmp.getRouteDefinitions())
            {
                System.out.println(bc.getDepartureTerminal().getCityName() + " to " + bc.getDestination().getCityName());
                System.out.println(" " + getTimeString(bc.getDepartureTime()) + " - " + getTimeString(bc.getArrivalTime()));
                System.out.println("     - " + bc.getBusId() + " Preis: " + bc.getPrice() + "€ ");
            }
        }else
        {
            System.out.println("Error ResponseTestActor");
        }
    }

    private String getTimeString(Time t)
    {
        String day = t.getDay(t.getIndexDayOfWeek());
        int h,m;
        double time = t.getTimeOfDay();
        h = (int)(time/3600000.0);
        double tmp = ((time/3600000.0) - h)*60;
        m = (int)tmp;
        day = day + " - " + h + ":" + m;
        return day;
    }

    public static Props props() {
        return Props.create(new Creator<ResponseTestActor>() {
            private static final long serialVersionUID = 1L;

            @Override
            public ResponseTestActor create() {
                return new ResponseTestActor();
            }
        });
    }
}