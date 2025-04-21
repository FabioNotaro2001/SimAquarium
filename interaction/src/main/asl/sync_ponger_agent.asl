/* Initial beliefs and rules */

turn(other).
other(ping).

/* Initial goals */

!ping_pong.

/* Plans */

+!ping_pong <-
  !receive_ping;
  !send_pong.

+!send_pong : turn(me) & other(Receiver) <-
   -+turn(other);
  !sendMessageTo(ball, Receiver).

+!receive_ping : turn(other) & other(Sender) <-
  .wait({ +ball[source(Sender)] });
  -+turn(me);
  -ball[source(Sender)];
  .print("Received ball from ", Sender);
  .print("Done").

+!sendMessageTo(Message, Receiver) <-
  .print("Sending ", Message, " to ", Receiver);
  .send(Receiver, tell, Message).
