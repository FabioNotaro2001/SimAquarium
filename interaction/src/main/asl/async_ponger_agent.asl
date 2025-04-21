/* Initial beliefs and rules */

turn(other).
other(ping).

/* Initial goals */

/* Plans */

+ball[source(Sender)] : turn(other) & other(Sender) <-
  -+turn(me);
  -ball[source(Sender)];
  .print("Received ball from ", Sender);
  !sendMessageTo(ball, Sender);
  !handle_ping.

+!handle_ping <-
  -+turn(other);
  .print("Done").

+!sendMessageTo(Message, Receiver) <-
  .print("Sending ", Message, " to ", Receiver);
  .send(Receiver, tell, Message).
