package service;

import com.google.common.collect.ImmutableList;
import connections.BusConnection;

import java.util.List;

/**
 * Created by jexmaster on 07.01.2016.
 *
 * @author Viktoria Jechsmayr
 */
public class RetrievedRoutes {

    private List<BusConnection> connections;

    public RetrievedRoutes(ImmutableList<BusConnection> connections) {
        this.connections = connections;
    }

    public RetrievedRoutes() {
    }

    public List<BusConnection> getConnections()
    {
        return connections;
    }
}
