package booking;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jexmaster on 24.01.2016.
 *
 * Klasse um die erhaltenen Bookings verwarbeiten
 *
 * @author Viktoria Jechsmayr
 */
public class Booking {
    private String bookingId;
    private String connectionId;
    private String firstName;
    private String lastName;
    private int numOfTickets;
    private Date date;

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @XmlElement(name = "BookingId")
    public String getBookingId() {
        return this.bookingId;
    }

    @XmlElement(name = "ConnectionID")
    public String getConnectionId() {
        return this.connectionId;
    }

    @XmlElement(name = "Firstname")
    public String getFirstName() {
        return this.firstName;
    }

    @XmlElement(name = "Lastname")
    public String getLastName() {
        return this.lastName;
    }

    @XmlElement(name = "Tickets")
    public int getNumOfTickets() {
        return this.numOfTickets;
    }

    @XmlTransient
    public Date getDate() {
        return this.date;
    }

    @XmlElement(name = "Date")
    public String getDateString() {
        if (date == null) {
            return "";
        }
        return format.format(date);
    }


    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setNumOfTickets(int numOfTickets) {
        this.numOfTickets = numOfTickets;
    }

    public void setDate(Date date) {
        this.date = date;
    }



    public void setDateString(String dateString) {
        if (dateString != null) {
            try {
                this.date = format.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            this.date = null;
        }
    }
}
