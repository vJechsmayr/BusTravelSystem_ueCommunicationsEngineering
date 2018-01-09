import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import messages.search.AvailableRoutesRequest;
import org.apache.http.client.fluent.Request;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by jexmaster on 06.01.2016.
 *
 * @author Viktoria Jechsmayr
 */
public class test {
    public static void main(String[] args)
    {
        try{
            String jsonResponse = Request.Get("http://140.78.196.14:9001/ceue/busproviders").execute().returnContent().asString();

            ActorSystem testSystem = ActorSystem.create("TestSystem");

            Gson gson = new Gson();
            Type listOfRefs = new TypeToken<List<RefsResponse>>(){}.getType();
            Object listofBusProviders = gson.fromJson(jsonResponse, listOfRefs);

            System.out.println(jsonResponse);

            ((List<RefsResponse>)listofBusProviders).get(0);
            ActorSelection actorSelection = testSystem.actorSelection(((List<RefsResponse>) listofBusProviders).get(3).getRef());

            ActorRef testActor = testSystem.actorOf(ResponseTestActor.props());
            actorSelection.tell(new AvailableRoutesRequest(), testActor);

        }catch(IOException e)
            {
                e.printStackTrace();
            }
    }
}
