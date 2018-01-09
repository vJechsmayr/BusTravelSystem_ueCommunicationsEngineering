package routesprovider;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.dispatch.OnFailure;
import akka.dispatch.OnSuccess;
import akka.japi.Creator;
import akka.pattern.Patterns;
import akka.util.Timeout;
import base.CEUEBaseActor;
import booking.Booking;
import booking.BookingServiceActor;
import connections.BusConnection;
import message.Register;
import messages.booking.BookingRequest;
import messages.booking.BookingResponse;
import messages.search.AvailableRoutesRequest;
import messages.search.AvailableRoutesResponse;
import messages.search.FilteredAvailableRoutesRequest;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import service.RetrievedRoutes;
import util.MiddlewareUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * Created by krenn on 16.11.2015
 *
 *
 *
 * @author Viktoria Jechsmayr
 */
public class BusRoutesProviderActor extends CEUEBaseActor {

  public static final String ACTORNAME = "BusRoutesProvider_K3";
  private final ActorRef routesServiceActor;
  private final ActorRef bookingServiceActor;

  private static final long TIME_PUFFER_BEFORE = 1 * 60 * 60 * 1000; // 1 Stunde in MilliSekunden
  private static final long TIME_PUFFER_AFTER = 2 * 60 * 60 * 1000; // 2 Stunden in MilliSekunden

  public BusRoutesProviderActor(ActorRef routesServiceActor, ActorRef bookingServiceActor)
  {
    this.routesServiceActor = routesServiceActor;
    this.bookingServiceActor = bookingServiceActor;
  }

  /**
   * AvailableRoutesRequest wird in der K3 Suchmaschiene abgefragt und Suchmaschienenintern gefiltert
   * je MessageTyp mit onSuccess und onFailure
   *
   * @param message
   * @throws Exception
     */
  @Override
  public void onReceive(Object message) throws Exception {
    super.onReceive(message);
    if (message instanceof AvailableRoutesRequest) {
      final ActorRef sender = getSender();

      final ExecutionContext ec = getContext().system().dispatcher();
      final Timeout t = new Timeout(Duration.create(10, TimeUnit.SECONDS));
      Future<Object> future = Patterns.ask(routesServiceActor, new RetrievedRoutes(), t);

      future.onSuccess(new OnSuccess<Object>() {
        @Override
        public void onSuccess(Object result) throws Throwable {
          if (result instanceof RetrievedRoutes) {
            sender.tell(new AvailableRoutesResponse(((RetrievedRoutes) result).getConnections()), getSelf());
          } else {
            throw new Exception("Unexpected Response");
          }
        }
      }, ec);

      future.onFailure(new OnFailure() {
        @Override
        public void onFailure(Throwable failure) throws Throwable {
          System.out.println("Error: " + failure.getMessage());
        }
      }, ec);

    } else if (message instanceof FilteredAvailableRoutesRequest) {
      final ActorRef sender = getSender();

      final ExecutionContext ec = getContext().system().dispatcher();
      final Timeout t = new Timeout(Duration.create(10, TimeUnit.SECONDS));
      Future<Object> future = Patterns.ask(routesServiceActor, new RetrievedRoutes(), t);

      future.onSuccess(new OnSuccess<Object>() {
        @Override
        public void onSuccess(Object result) throws Throwable {
          if (result instanceof RetrievedRoutes) {
            FilteredAvailableRoutesRequest request = (FilteredAvailableRoutesRequest)message;
            List<BusConnection> filteredList = new ArrayList<BusConnection>();

            for(BusConnection bc : ((RetrievedRoutes)result).getConnections())
            {
              if (bc.getDepartureTerminal().getLocode().equals(request.getDepartureTerminal().getLocode()) &&
                    bc.getDestination().getLocode().equals(request.getDestination().getLocode()) &&
                    bc.getDepartureTime().getTimeOfDay() >= (request.getDepartureTime().getTimeOfDay() - TIME_PUFFER_BEFORE) &&
                    bc.getDepartureTime().getTimeOfDay() <= (request.getDepartureTime().getTimeOfDay() + TIME_PUFFER_AFTER) &&
                    bc.getArrivalTime().getTimeOfDay() >= (request.getArrivalTime().getTimeOfDay() - TIME_PUFFER_BEFORE) &&
                    bc.getArrivalTime().getTimeOfDay() <= (request.getArrivalTime().getTimeOfDay() + TIME_PUFFER_AFTER))
              {
                filteredList.add(bc);
              }
            }

            sender.tell(new AvailableRoutesResponse(filteredList), getSelf());
          } else {
            throw new Exception("Unexpected Response");
          }
        }
      }, ec);

      future.onFailure(new OnFailure() {
        @Override
        public void onFailure(Throwable failure) throws Throwable {
          System.out.println("Error: " + failure.getMessage());
        }
      }, ec);

    } else if (message instanceof Register) {
      Register registerMsg = (Register) message;
      String remoteAddress = MiddlewareUtil.getRemoteAddress(registerMsg.getSystemName(), registerMsg.getSystemIP(), registerMsg.getSystemPort(), getSelf());
      MiddlewareUtil.register(remoteAddress);
    } else if(message instanceof BookingRequest)
          {
            /**
             * Eingehende Booking Requests werden hier behaltelt
             */
            final ActorRef sender = getSender();
            final ExecutionContext ec = getContext().system().dispatcher();
            final Timeout t = new Timeout(Duration.create(10, TimeUnit.SECONDS));
            Future<Object> future = Patterns.ask(bookingServiceActor, message, t);

            future.onSuccess(new OnSuccess<Object>()
            {
              @Override
              public void onSuccess(Object result) throws Throwable{

                  if(result instanceof Booking)
                  {
                    Booking b = (Booking)result;
                    BookingServiceActor.addBooking(b);
                    sender.tell(new BookingResponse(b.getBookingId(), "Booking successful"), getSelf());
                  }else if(result instanceof String)
                  {
                    String s = (String) result;
                    sender.tell(new BookingResponse("", s), getSelf());
                  }
              }
            },ec);

            future.onFailure(new OnFailure(){
              @Override
              public void onFailure(Throwable failure) throws Throwable {
                System.out.println("Error: " + failure.getMessage());
              }
            },ec);

          }else if (message instanceof String) {
            System.out.println("Remoting Test Successful");
          }
  }

  public static Props props(final ActorRef routesServiceActor,ActorRef bookingServiceActor) {
    return Props.create(new Creator<BusRoutesProviderActor>() {
      private static final long serialVersionUID = 1L;

      @Override
      public BusRoutesProviderActor create() {
        return new BusRoutesProviderActor(routesServiceActor,bookingServiceActor);
      }
    });
  }
}