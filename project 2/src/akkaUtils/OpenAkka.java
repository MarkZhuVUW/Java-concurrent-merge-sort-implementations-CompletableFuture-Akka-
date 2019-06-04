package akkaUtils;

import java.util.Collections;
import akka.actor.ActorSystem;
public class OpenAkka{
  public static void main(String[]args) throws InterruptedException {
    ActorSystem s = AkkaConfig.newSystem("OpenAkka",2500,Collections.emptyMap());
    AkkaConfig.keybordClose(s);
  }
}
