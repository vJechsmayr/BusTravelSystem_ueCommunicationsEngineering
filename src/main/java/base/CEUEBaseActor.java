package base;

import akka.actor.Actor;
import akka.actor.UntypedActor;

/**
 * Created by krenn on 11/16/15.
 */
public class CEUEBaseActor extends UntypedActor {

  @Override
  public void onReceive(Object message) throws Exception {
    if(message.equals("alive?")) {
      getSender().tell(true, getSelf());
    }
  }
}