/* Initial beliefs and rules */

/* Initial goals */

!greet.

/* Plans */

+!greet : .my_name(Me) <-
  .print("It's me: ", Me, "!").

+?whosThere(Anyone)[source(Anyone)] <-
  .print(Anyone, ", you found me!").
