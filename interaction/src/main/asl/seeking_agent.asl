/* Initial beliefs and rules */

/* Initial goals */

!seek_others.

/* Plans */

+!seek_others <-
  +found(0);
  .broadcast(askOne, whosThere(_));
  .wait(found(3));
  -found(_);
  .print("I found you all!").

+whosThere(_)[source(Sender)] : found(X) <-
  -+found(X + 1);
  .print("I found you, ", Sender, "!").
