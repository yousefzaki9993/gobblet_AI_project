# Game Issues and Responses

| Case NO | Description | Steps | Expected | Actual | Response |
|---------|-------------|-------|----------|--------|----------|
| 1       | Game does not stop after 1 player wins | 1-win as player1 <br> 2-try moving player2's pieces <br> 3-try moving player1’s pieces after he has won! | Game stops and announces winner | Game continues normally even letting other player win afterwards | Fixed |
| 2       | There is no way to clear selection   | 1-click on one piece that is on the board, green light appears around the chosen piece <br> 2-clicking away to toggle the green light off , essentially calling off this move.   | The move is called off when u click away from the board    | no way to toggle it off    | Fixed |
| 3       | Expressive messages to explain to the user illegal moves when doing them    | try doing any illegal move like placing a smaller gobblet on top of a bigger one   | Expressive message showing this is illegal to do    | Nothing happens, the user has to figure it out for himself    	| Fixed |
| 4      	| Gameplay issues  	| 1-touch gobblet that is on the board <br>2- touch another gobblet that is larger than it    	| Once you touch gobblet that is on the board... you must play it further are no moves for it, you lose the game    	| Control changes to be bigger gobblet    	| Semi-fixed (second part in "expected" needs to be thought about and clarified) |
| 5       | Draw scenario      | repeat	the same	move	for	player1 three	times then	repeat	the	same	move	for	player2 three	times | Game should	be declared	as	draw | Game continues	normally | Delayed until we get confirmation |
