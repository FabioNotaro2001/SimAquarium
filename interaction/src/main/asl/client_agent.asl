/* Initial beliefs and rules */

/* Initial goals */

!start.

/* Plans */

+!start <-
  Expression = mult(sum(1, 2), 3);
  .print("Calculating ", Expression, " ...");
  !call(calculate(Expression, Result), service);
  .print("... result: ", Result).


+!call(Request, ServiceName) <-
  .print("Asking ", Request, " to ", ServiceName);
  .send(ServiceName, askOne, Request, Response);
  .print("Service '", ServiceName, "' answered ", Response);
  Request = Response.
