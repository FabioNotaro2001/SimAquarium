/* Initial beliefs and rules */

turn(me).
other(pong).

/* Initial goals */

!send_ping.

/* Plans */

+!send_ping : turn(me) & other(Receiver) <-
  -+turn(other);
  !sendMessageTo(ball, Receiver).

+ball[source(Sender)] : turn(other) & other(Sender) <-
  -+turn(me);
  -ball[source(Sender)];
  .print("Received ball from ", Sender);
  .print("Done").

+!sendMessageTo(Message, Receiver) <-
  .print("Sending ", Message, " to ", Receiver);
  .send(Receiver, tell, Message).
