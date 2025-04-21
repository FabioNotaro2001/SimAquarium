// Agent mum in project basics

/* Initial beliefs and rules */

busy(phone, mum).

/* Initial goals */

!call_friend.

/* Plans */

+!call_friend <-
    ?check_phone_available;
    .print("Oh nice! The phone is free")
    !reserve_phone;
    .print("Got the phone, I'll call my friend :D");
    !wait_randomly;
    .print("Done. My friend is so funny XD");
    !release_phone;
    .print("Honey! The phone is free!");
    !wait_randomly;
    !call_friend.

-!call_friend <-
    !wait_randomly;
    !call_friend.

+!reserve_phone : busy(phone, mum) <- true.

+!reserve_phone : ~busy(phone) <-
    .send(pierre, tell, busy(phone, mum));
    +busy(phone, mum).

+!release_phone : busy(phone, mum) <-
  	.send(pierre, tell, ~busy(phone));
  	+~busy(phone).

+busy(phone, _): ~busy(phone) <-
    -~busy(phone).

+~busy(phone) : busy(phone, X) <-
    -busy(phone, X).

+?check_phone_available : busy(phone, mum) | ~busy(phone) <- true.

+!wait_randomly <-
    .random(R);
    .wait(R * 5000).
