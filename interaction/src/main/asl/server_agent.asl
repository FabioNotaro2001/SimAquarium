/* Initial beliefs and rules */

reduce(mult(X, Y), R) :-
  // .print("Reducing ", mult(X, Y)) &
  reduce(X, XR) &
  reduce(Y, YR) &
  R = XR * YR.

reduce(sum(X, Y), R) :-
  // .print("Reducing ", sum(X, Y)) &
  reduce(X, XR) &
  reduce(Y, YR) &
  R = XR + YR.

reduce(sub(X, Y), R) :-
  // .print("Reducing ", sub(X, Y)) &
  reduce(X, XR) &
  reduce(Y, YR) &
  R = XR - YR.

reduce(fract(X, Y), R) :-
  // .print("Reducing ", fract(X, Y)) &
  reduce(X, XR) &
  reduce(Y, YR) &
  R = XR / YR.

reduce(X, X). // :- .print("Reducing ", X).

/* Initial goals */

/* Plans */

+?calculate(Request, Response) <-
  .print("Serving request: ", Request, "...");
  ?reduce(Request, Response);
  .print("... response: ", Response).
