package booking;

import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.japi.Creator;
import connections.BusConnection;
import messages.booking.BookingRequest;
import service.RoutesServiceActor;
import util.Time;
import util.XMLHelper;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by jexmaster on 24.01.2016.
 *
 * Klasse mit dem Actor füt die Bookings
 * Aufruf um die eingegangenen Bookings im XML zu speichern
 *
 * @author Viktoria Jechsmayr
 */
public class BookingServiceActor extends UntypedActor{

    private static final  String FILE_BOOKINGS = "bookings.xml";

    private static List<Booking> bookings;


    public BookingServiceActor()
    {
    }

    private static List<Booking> getBookings()
    {
        if(bookings == null)
        {
            if(new File(FILE_BOOKINGS).exists())
            {
                try {
                    BookingWrapper wrapper = XMLHelper.load(BookingWrapper.class, FILE_BOOKINGS);
                    bookings = wrapper.getBookings();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else
            {
                bookings = new ArrayList<>();
            }
        }
        return bookings;
    }

    private static void saveBookings() {
        BookingWrapper wrapper = new BookingWrapper();
        wrapper.setBookings(bookings);
        try {
            XMLHelper.save(wrapper, FILE_BOOKINGS);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public synchronized static void addBooking(Booking b)
    {
        getBookings().add(b);
        saveBookings();
    }

    public synchronized static Booking createBooking(BookingRequest request)
    {
        Booking b = new Booking();

        b.setConnectionId(request.getConnectionId());
        b.setFirstName(request.getFirstName());
        b.setLastName(request.getLastName());
        b.setNumOfTickets(request.getNumOfTickets());
        b.setDate(request.getDate());
        b.setBookingId(request.getConnectionId() + getBookings().size());

        return b;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof BookingRequest)
        {
            BookingRequest br = (BookingRequest) message;
            BusConnection b = null;

            for(BusConnection bc : RoutesServiceActor.retrieveConnections())
            {
                if(bc.getConnectionId().equals(br.getConnectionId()))
                {
                    b = bc;
                    break;
                }
            }

            if(b == null)
            {
                getSender().tell("Booking failed - ConnectionID not found", getSelf());

            }else
            {
                Calendar cal = GregorianCalendar.getInstance();
                cal.setTime(br.getDate());
                if(checkDayofWeek(b.getDepartureTime(), cal))
                {
                    getSender().tell(createBooking(br), getSelf());
                }else
                {
                    getSender().tell("Booking failed - Wrong Day", getSelf());
                }

            }
        }
    }

    public static Props props()
    {
        return Props.create(new Creator<BookingServiceActor>(){
            private static final long serialVersionUID = 1L;

            @Override
            public BookingServiceActor create()
            {
                return new BookingServiceActor();
            }
        });
    }


    private boolean checkDayofWeek(Time t, Calendar cal)
    {
        if(t.getIndexDayOfWeek()==7)
        {
            if(cal.get(cal.DAY_OF_WEEK) == cal.SUNDAY)
            {
                return true;
            }
        }else if((t.getIndexDayOfWeek() + 1) == cal.get(cal.DAY_OF_WEEK))
        {
            return true;
        }
        return false;
    }

}
