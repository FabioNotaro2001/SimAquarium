/* TODO */
turn(other).
other(ping).

+ball[source(Sender)] : turn(other) & other(Sender) <-
  -+turn(me);
  -ball[source(Sender)];
  .print("Received ball from ", Sender);
  !sendMessageTo(ball, Sender);
  -+turn(other);
  .print("Received ball").

+!sendMessageTo(Message, Receiver) <-
  .print("Sending ", Message, " to ", Receiver);
  .send(Receiver, tell, Message).
