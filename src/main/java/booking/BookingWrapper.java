package booking;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by jexmaster on 24.01.2016.
 *
 * Wrapper für den XML Helper zum speichern der Bookings
 *
 * @author Viktoria Jechsmayr
 */
@XmlRootElement(name = "BookingWrapper")
public class BookingWrapper {

    private List<Booking> bookings;

    @XmlElementWrapper(name = "Bookings")
    @XmlElement(name = "Booking")
    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
}
