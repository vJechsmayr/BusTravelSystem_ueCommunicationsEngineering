package service;

import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.japi.Creator;
import com.google.common.collect.ImmutableList;
import connections.BusConnection;
import connections.BusTerminal;
import util.Time;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jexmaster on 06.01.2016.
 *
 * Klasse für die BusConnections
 * hier wird die Liste der BusConnections mit den eigenen Verbindungen des BusProviders K3 gefüllt
 *
 * @author Viktoria Jechsmayr
 */
public class RoutesServiceActor extends UntypedActor {

    public static final int DAY_MONDAY = 1;
    public static final int DAY_TUESDAY = 2;
    public static final int DAY_WEDNESDAY = 3;
    public static final int DAY_THURSDAY = 4;
    public static final int DAY_FRIDAY = 5;
    public static final int DAY_SATURDAY = 6;
    public static final int DAY_SUNDAY = 7;

    private static final List<BusConnection> availableConnections = new ArrayList<>();

    static{
        //BusTerminals
        BusTerminal terminalBeBru = new BusTerminal ("BE BRU", "Brussel", "Belgium");
        BusTerminal terminalGbLon = new BusTerminal("GB LON", "London", "United Kingdom");
        BusTerminal terminalGbBirm = new BusTerminal("GB BHM", "Birmingham", "United Kingdom");
        BusTerminal terminalGbMan = new BusTerminal("GB MNC", "Manchester", "United Kingdom");
        BusTerminal terminalGbGlas = new BusTerminal("GB GLW", "Glasgow", "United Kingdom");
        BusTerminal terminalGbBel = new BusTerminal("GB BEL", "Belfast", "United Kingdom");
        BusTerminal terminalIeDub = new BusTerminal("IE DUB", "Dublin", "Ireland");

        /**
         * Preiskalkulation für die Fahrtpreise bestehen aus:
         * Preis pro km für Busverschleiß 0.13 * km + Fahrtstunden(Dauer) * Stundenlohn des Busfahrers(10€) + 20€ Gewinnpuffer
         *
         * In den Preis werden keine Tankkosten oder Kosten für die Fähre sowie Steuern gerechnet.
         */
        float priceBruLon = (float) ((0.13 * 373) + (4.32 * 10) + 20);
        float priceBruBirm = (float) ((0.13 * 583) + (5.87 * 10) + 20);
        float priceBruMan = (float) ((0.13 * 716) + (7.4 * 10) + 20);
        float priceBruGlas = (float) ((0.13 * 1043) + (9.95 * 10) + 20);
        float priceBruBel = (float) ((0.13 * 1161) + (13.22 * 10) + 20);
        float priceBruDub = (float) ((0.13 * 975) + (11.48 * 10) + 20);

        //-------------   BUS1   -------------------
        //BRU - LON MO 6:00-10:19
        availableConnections.add(new BusConnection("k3bus1con0", terminalBeBru, new Time(DAY_MONDAY, getMilliSeconds(6,0)), terminalGbLon, new Time(DAY_MONDAY,getMilliSeconds(10,19)), "Bus1", priceBruLon));
        //LON - BRU MO 11:00-15:19
        availableConnections.add(new BusConnection("k3bus1con1", terminalGbLon, new Time(DAY_MONDAY, getMilliSeconds(11,0)), terminalBeBru, new Time(DAY_MONDAY, getMilliSeconds(15,19)), "Bus1", priceBruLon));
        //BRU - MAN MO 16:30-23:54
        availableConnections.add(new BusConnection("k3bus1con2", terminalBeBru, new Time(DAY_MONDAY, getMilliSeconds(16,30)), terminalGbMan, new Time(DAY_MONDAY, getMilliSeconds(23,54)), "Bus1", priceBruMan));
        //MAN-BRU DI 1:00-8:24
        availableConnections.add(new BusConnection("k3bus1con3", terminalGbMan, new Time(DAY_TUESDAY, getMilliSeconds(1,0)), terminalBeBru, new Time(DAY_TUESDAY, getMilliSeconds(8,24)), "Bus1", priceBruMan));
        //BRU-LON
        availableConnections.add(new BusConnection("k3bus1con4", terminalBeBru, new Time(DAY_TUESDAY, getMilliSeconds(9,30)), terminalGbLon, new Time(DAY_TUESDAY,getMilliSeconds(13,49)), "Bus1", priceBruLon));
        //LON-BRU
        availableConnections.add(new BusConnection("k3bus1con5", terminalGbLon, new Time(DAY_TUESDAY, getMilliSeconds(14,30)), terminalBeBru, new Time(DAY_TUESDAY, getMilliSeconds(18,49)), "Bus1", priceBruLon));
        //BRU-DUB
        availableConnections.add(new BusConnection("k3bus1con6", terminalBeBru, new Time(DAY_TUESDAY, getMilliSeconds(20,0)), terminalIeDub, new Time(DAY_WEDNESDAY, getMilliSeconds(7,29)), "Bus1", priceBruDub));
        //DUB-BRU
        availableConnections.add(new BusConnection("k3bus1con7", terminalIeDub, new Time(DAY_WEDNESDAY, getMilliSeconds(8,30)), terminalBeBru, new Time(DAY_WEDNESDAY, getMilliSeconds(19,59)), "Bus1", priceBruDub));
        //BRU-BEL
        availableConnections.add(new BusConnection("k3bus1con8", terminalBeBru, new Time(DAY_WEDNESDAY, getMilliSeconds(20,30)), terminalGbBel, new Time(DAY_THURSDAY, getMilliSeconds(9,43)), "Bus1", priceBruBel));
        //BEL-BRU
        availableConnections.add(new BusConnection("k3bus1con9", terminalGbBel, new Time(DAY_THURSDAY,getMilliSeconds(11,30)), terminalBeBru, new Time(DAY_FRIDAY,getMilliSeconds(0,43)), "Bus1", priceBruBel));
        //BRU-LON
        availableConnections.add(new BusConnection("k3bus1con10", terminalBeBru, new Time(DAY_FRIDAY,getMilliSeconds(1,30)), terminalGbLon, new Time(DAY_FRIDAY,getMilliSeconds(5,49)), "Bus1", priceBruLon));
        //LON-BRU
        availableConnections.add(new BusConnection("k3bus1con11", terminalGbLon, new Time(DAY_FRIDAY,getMilliSeconds(7,00)), terminalBeBru, new Time(DAY_FRIDAY,getMilliSeconds(11,19)), "Bus1", priceBruLon));
        //BRU-BIRM
        availableConnections.add(new BusConnection("k3bus1con12", terminalBeBru, new Time(DAY_FRIDAY,getMilliSeconds(12,30)), terminalGbBirm, new Time(DAY_FRIDAY,getMilliSeconds(18,22)), "Bus1", priceBruBirm));
        //BIRM-BRU
        availableConnections.add(new BusConnection("k3bus1con13", terminalGbBirm, new Time(DAY_FRIDAY,getMilliSeconds(19,00)), terminalBeBru, new Time(DAY_SATURDAY,getMilliSeconds(0,52)), "Bus1", priceBruBirm));
        //BRU-GLAS
        availableConnections.add(new BusConnection("k3bus1con14", terminalBeBru, new Time(DAY_SATURDAY,getMilliSeconds(1,30)), terminalGbGlas, new Time(DAY_SATURDAY,getMilliSeconds(11,27)), "Bus1", priceBruGlas));
        //GLAS-BRU
        availableConnections.add(new BusConnection("k3bus1con15", terminalGbGlas, new Time(DAY_SATURDAY,getMilliSeconds(12,30)), terminalBeBru, new Time(DAY_SATURDAY,getMilliSeconds(22,27)), "Bus1", priceBruGlas));
        //BRU-LON
        availableConnections.add(new BusConnection("k3bus1con16", terminalBeBru, new Time(DAY_SATURDAY,getMilliSeconds(23,30)), terminalGbLon, new Time(DAY_SUNDAY,getMilliSeconds(3,49)), "Bus1", priceBruLon));
        //LON-BRU
        availableConnections.add(new BusConnection("k3bus1con17", terminalGbLon, new Time(DAY_SUNDAY,getMilliSeconds(5,00)), terminalBeBru, new Time(DAY_SUNDAY,getMilliSeconds(9,19)), "Bus1", priceBruLon));
        //BRU-LON
        availableConnections.add(new BusConnection("k3bus1con18", terminalBeBru, new Time(DAY_SUNDAY,getMilliSeconds(11,00)), terminalGbLon, new Time(DAY_SUNDAY,getMilliSeconds(15,19)), "Bus1", priceBruLon));
        //LON-BRU
        availableConnections.add(new BusConnection("k3bus1con19", terminalGbLon, new Time(DAY_SUNDAY,getMilliSeconds(17,00)), terminalBeBru, new Time(DAY_SUNDAY,getMilliSeconds(21,19)), "Bus1", priceBruLon));

        //-------------   BUS2   -------------------
        //BRU - BEL
        availableConnections.add(new BusConnection("k3bus2con20", terminalBeBru, new Time(DAY_MONDAY, getMilliSeconds(2,30)), terminalGbBel, new Time(DAY_MONDAY,getMilliSeconds(15,43)), "Bus2", priceBruBel));
        //BEL-BRU
        availableConnections.add(new BusConnection("k3bus2con21", terminalGbBel, new Time(DAY_MONDAY, getMilliSeconds(16,15)), terminalBeBru, new Time(DAY_TUESDAY,getMilliSeconds(5,28)), "Bus2", priceBruBel));
        //BRU-BIRM
        availableConnections.add(new BusConnection("k3bus2con22", terminalBeBru, new Time(DAY_TUESDAY, getMilliSeconds(6,30)), terminalGbBirm, new Time(DAY_TUESDAY, getMilliSeconds(12,22)), "Bus2", priceBruBirm));
        //BIRM-BRU
        availableConnections.add(new BusConnection("k3bus2con23", terminalGbBirm, new Time(DAY_TUESDAY, getMilliSeconds(13,0)), terminalBeBru, new Time(DAY_TUESDAY, getMilliSeconds(18,52)), "Bus2", priceBruBirm));
        //BRU-LON
        availableConnections.add(new BusConnection("k3bus2con24", terminalBeBru, new Time(DAY_TUESDAY, getMilliSeconds(19,30)), terminalGbLon, new Time(DAY_TUESDAY, getMilliSeconds(23,49)), "Bus2", priceBruLon));
        //LON-BRU
        availableConnections.add(new BusConnection("k3bus2con25", terminalGbLon, new Time(DAY_WEDNESDAY, getMilliSeconds(1,0)), terminalBeBru, new Time(DAY_WEDNESDAY, getMilliSeconds(5,19)), "Bus2", priceBruLon));
        //BRU-GLAS
        availableConnections.add(new BusConnection("k3bus2con26", terminalBeBru, new Time(DAY_WEDNESDAY, getMilliSeconds(6,0)), terminalGbGlas, new Time(DAY_WEDNESDAY, getMilliSeconds(15,57)), "Bus2", priceBruGlas));
        //GLAS-BRU
        availableConnections.add(new BusConnection("k3bus2con27", terminalGbGlas, new Time(DAY_WEDNESDAY, getMilliSeconds(16,40)), terminalBeBru, new Time(DAY_THURSDAY, getMilliSeconds(2,37)), "Bus2", priceBruGlas));
        //BRU-MAN
        availableConnections.add(new BusConnection("k3bus2con28", terminalBeBru, new Time(DAY_THURSDAY, getMilliSeconds(3,40)), terminalGbMan, new Time(DAY_THURSDAY, getMilliSeconds(11,4)), "Bus2", priceBruMan));
        //MAN-BRU
        availableConnections.add(new BusConnection("k3bus2con29", terminalGbMan, new Time(DAY_THURSDAY, getMilliSeconds(11,35)), terminalBeBru, new Time(DAY_THURSDAY, getMilliSeconds(18,59)), "Bus2", priceBruMan));
        //BRU-LON
        availableConnections.add(new BusConnection("k3bus2con30", terminalBeBru, new Time(DAY_THURSDAY, getMilliSeconds(19,30)), terminalGbLon, new Time(DAY_THURSDAY, getMilliSeconds(23,49)), "Bus2", priceBruLon));
        //LON-BRU
        availableConnections.add(new BusConnection("k3bus2con31", terminalGbLon, new Time(DAY_FRIDAY, getMilliSeconds(0,30)), terminalBeBru, new Time(DAY_FRIDAY, getMilliSeconds(4,49)), "Bus2", priceBruLon));
        //BRU-DUB
        availableConnections.add(new BusConnection("k3bus2con32", terminalBeBru, new Time(DAY_FRIDAY, getMilliSeconds(5,30)), terminalIeDub, new Time(DAY_FRIDAY, getMilliSeconds(16,59)), "Bus2", priceBruDub));
        //DUB-BRU
        availableConnections.add(new BusConnection("k3bus2con33", terminalIeDub, new Time(DAY_FRIDAY, getMilliSeconds(17,30)), terminalBeBru, new Time(DAY_SATURDAY, getMilliSeconds(4,59)), "Bus2", priceBruDub));
        //BRU-MAN
        availableConnections.add(new BusConnection("k3bus2con34", terminalBeBru, new Time(DAY_SATURDAY, getMilliSeconds(5,35)), terminalGbMan, new Time(DAY_SATURDAY, getMilliSeconds(12,59)), "Bus2", priceBruMan));
        //MAN-BRU
        availableConnections.add(new BusConnection("k3bus2con35", terminalGbMan, new Time(DAY_SATURDAY, getMilliSeconds(13,30)), terminalBeBru, new Time(DAY_SATURDAY, getMilliSeconds(20,54)), "Bus2", priceBruMan));
        //BRU-MAN
        availableConnections.add(new BusConnection("k3bus2con36", terminalBeBru, new Time(DAY_SATURDAY, getMilliSeconds(21,30)), terminalGbMan, new Time(DAY_SUNDAY, getMilliSeconds(4,54)), "Bus2", priceBruMan));
        //MAN-BRU
        availableConnections.add(new BusConnection("k3bus2con37", terminalGbMan, new Time(DAY_SUNDAY, getMilliSeconds(5,30)), terminalBeBru, new Time(DAY_SUNDAY, getMilliSeconds(12,54)), "Bus2", priceBruMan));
        //BRU-BIRM
        availableConnections.add(new BusConnection("k3bus2con38", terminalBeBru, new Time(DAY_SUNDAY, getMilliSeconds(13,30)), terminalGbBirm, new Time(DAY_SUNDAY, getMilliSeconds(19,22)), "Bus2", priceBruBirm));
        //BIRM-BRU
        availableConnections.add(new BusConnection("k3bus2con39", terminalGbBirm, new Time(DAY_SUNDAY, getMilliSeconds(20,0)), terminalBeBru, new Time(DAY_MONDAY, getMilliSeconds(1,52)), "Bus2", priceBruBirm));
        //-------------   BUS3   -------------------
        //BRU-DUB
        availableConnections.add(new BusConnection("k3bus3con40", terminalBeBru, new Time(DAY_MONDAY, getMilliSeconds(1,0)), terminalIeDub, new Time(DAY_MONDAY, getMilliSeconds(12,29)), "Bus3", priceBruDub));
        //DUB-BRU
        availableConnections.add(new BusConnection("k3bus3con41", terminalIeDub, new Time(DAY_MONDAY, getMilliSeconds(12,59)), terminalBeBru, new Time(DAY_TUESDAY, getMilliSeconds(0,28)), "Bus3", priceBruDub));
        //BRU-BEL
        availableConnections.add(new BusConnection("k3bus3con42", terminalBeBru, new Time(DAY_TUESDAY, getMilliSeconds(1,28)), terminalGbBel, new Time(DAY_TUESDAY, getMilliSeconds(14,41)), "Bus3", priceBruBel));
        //BEl-BRU
        availableConnections.add(new BusConnection("k3bus3con43", terminalGbBel, new Time(DAY_TUESDAY, getMilliSeconds(15,11)), terminalBeBru, new Time(DAY_WEDNESDAY, getMilliSeconds(4,24)), "Bus3", priceBruBel));
        //BRU-BIRM
        availableConnections.add(new BusConnection("k3bus3con44", terminalBeBru, new Time(DAY_WEDNESDAY, getMilliSeconds(5,24)), terminalGbBirm, new Time(DAY_WEDNESDAY, getMilliSeconds(11,16)), "Bus3", priceBruBirm));
        //BIRM-BRU
        availableConnections.add(new BusConnection("k3bus3con45", terminalGbBirm, new Time(DAY_WEDNESDAY, getMilliSeconds(11,46)), terminalBeBru, new Time(DAY_WEDNESDAY, getMilliSeconds(17,38)), "Bus3", priceBruBirm));
        //BRU-LON
        availableConnections.add(new BusConnection("k3bus3con46", terminalBeBru, new Time(DAY_WEDNESDAY, getMilliSeconds(18,8)), terminalGbLon, new Time(DAY_WEDNESDAY, getMilliSeconds(22,27)), "Bus3", priceBruLon));
        //LON-BRU
        availableConnections.add(new BusConnection("k3bus3con47", terminalGbLon, new Time(DAY_WEDNESDAY, getMilliSeconds(22,57)), terminalBeBru, new Time(DAY_THURSDAY, getMilliSeconds(3,16)), "Bus3", priceBruLon));
        //BRU-DUB
        availableConnections.add(new BusConnection("k3bus3con48", terminalBeBru, new Time(DAY_THURSDAY, getMilliSeconds(4,16)), terminalIeDub, new Time(DAY_THURSDAY, getMilliSeconds(15,45)), "Bus3", priceBruDub));
        //DUB-BRU
        availableConnections.add(new BusConnection("k3bus3con49", terminalIeDub, new Time(DAY_THURSDAY, getMilliSeconds(16,15)), terminalBeBru, new Time(DAY_FRIDAY, getMilliSeconds(3,44)), "Bus3", priceBruDub));
        //BRU-GLAS
        availableConnections.add(new BusConnection("k3bus3con50", terminalBeBru, new Time(DAY_FRIDAY, getMilliSeconds(4,44)), terminalGbGlas, new Time(DAY_FRIDAY, getMilliSeconds(14,41)), "Bus3", priceBruGlas));
        //GLAS-BRU
        availableConnections.add(new BusConnection("k3bus3con51", terminalGbGlas, new Time(DAY_FRIDAY, getMilliSeconds(15,11)), terminalBeBru, new Time(DAY_SATURDAY, getMilliSeconds(1,8)), "Bus3", priceBruGlas));
        //BRU-BEL
        availableConnections.add(new BusConnection("k3bus3con52", terminalBeBru, new Time(DAY_SATURDAY, getMilliSeconds(2,8)), terminalGbBel, new Time(DAY_SATURDAY, getMilliSeconds(15,21)), "Bus3", priceBruBel));
        //BEL-BRU
        availableConnections.add(new BusConnection("k3bus3con53", terminalGbBel, new Time(DAY_SATURDAY, getMilliSeconds(15,51)), terminalBeBru, new Time(DAY_SUNDAY, getMilliSeconds(5,4)), "Bus3", priceBruBel));
        //BRU-MAN
        availableConnections.add(new BusConnection("k3bus3con54", terminalBeBru, new Time(DAY_SUNDAY, getMilliSeconds(6,4)), terminalGbMan, new Time(DAY_SUNDAY, getMilliSeconds(13,28)), "Bus3", priceBruMan));
        //MAN-BRU
        availableConnections.add(new BusConnection("k3bus3con55", terminalGbMan, new Time(DAY_SUNDAY, getMilliSeconds(13,58)), terminalBeBru, new Time(DAY_SUNDAY, getMilliSeconds(21,22)), "Bus3", priceBruMan));
    }

    /**
     * Liste der eigenen BusConnections zurück geben
     * @return
     */
    public static List<BusConnection> retrieveConnections()
    {
        return availableConnections;
    }

    /**
     * Hilfsmethode zum berechnen der Millisekunden für leichteres eingeben der Zeiten beim Anlegen einer BusConnection
     *
     * @param hour
     * @param minutes
     * @return
     */
    private static long getMilliSeconds(int hour, int minutes)
    {
        long ms = 0;

        ms = (hour*60 + minutes)* 60 *1000;

        return ms;
    }


    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof RetrievedRoutes)
        {
           getSender().tell(new RetrievedRoutes(ImmutableList.<BusConnection>builder().addAll(retrieveConnections()).build()), getSelf());
        }
    }

    public static Props props()
    {
       return Props.create(new Creator<RoutesServiceActor>(){
           private static final long serialVersionUID = 1L;

           @Override
           public RoutesServiceActor create()
           {
               return new RoutesServiceActor();
           }
       });
    }
}
