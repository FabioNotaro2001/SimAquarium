// Agent fanboy in project basics

/* Initial beliefs */

likes("Radiohead").
phone("Glasgow Arena","05112345").
concert("Radiohead", "22/12/2014", "Glasgow Arena"). // still triggers belief addition event

busy(phone, mum).

/* Initial goals */

!start.

/* Plans */

+!start : true <- .print("Hello world!").

+concert(Artist, Date, Venue) // triggering event (belief addition)
	: likes(Artist) // plan context
	<- !book_tickets(Artist, Date, Venue). // plan body

+!book_tickets(A, D, V) <-
		?phone(V, N); // test goal (to retrieve a belief)
		!call(N);
		!choose_seats(A, D, V);
		!done.

+!call(N) <-
		?check_phone_available;
		.print("Thanks God! The phone is available!");
		!reserve_phone;
		.print("Finally I got the phone!");
		.print("Calling ", N, "..."); !wait_randomly; .print("... called ", N, " :D");
		.print("Releasing the phone...");
		!release_phone;
		.print("Mum, the phone is free: it's your turn!'").

-!call(N) <-
		.print("C'mon Mum! You're keeping the phone busy! I need it!");
		!wait_randomly;
		!call(N).

+!reserve_phone : ~busy(phone) <-
		.send(mum, tell, busy(phone, pierre));
		+busy(phone, pierre).

+!release_phone : busy(phone, pierre) <-
		.send(mum, tell, ~busy(phone));
		+~busy(phone).

+busy(phone, _) : ~busy(phone) <-
		-~busy(phone).

+~busy(phone) : busy(phone, X) <-
		-busy(phone, X).

+?check_phone_available : busy(phone, fanboy) | ~busy(phone) <- true.

+!wait_randomly <-
		.random(R);
		.wait(R * 5000).

+!choose_seats(A, D, V) : true <- .print("choosing seats for ", A, " at ", V, "...").

+!done : true <- .print("done!").
