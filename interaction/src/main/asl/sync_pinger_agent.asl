/* Initial beliefs and rules */

turn(me).
other(pong).

/* Initial goals */

!ping_pong.

/* Plans */

+!ping_pong <-
  !send_ping;
  !receive_pong.

+!send_ping : turn(me) & other(Receiver) <-
   -+turn(other);
  !sendMessageTo(ball, Receiver).

+!receive_pong : turn(other) & other(Sender) <-
  .wait({ +ball[source(Sender)] });
  -+turn(me);
  -ball[source(Sender)];
  .print("Received ball from ", Sender);
  .print("Done").

+!sendMessageTo(Message, Receiver) <-
  .print("Sending ", Message, " to ", Receiver);
  .send(Receiver, tell, Message).
