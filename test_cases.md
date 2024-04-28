# Game Issues and Responses

| Case NO | Description | Steps | Expected | Actual | Response |
|---------|-------------|-------|----------|--------|----------|
| 1       | Game does not stop after 1 player wins | 1-win as player1<br> 2-try moving player2’s pieces<br> 3-try moving player1’s piece’s after he has won! | Game stops and announces winner | Game continues normally even letting other player win afterwards | Fixed |
| 2       | There is no way to clear selection | 1- click on one piece that is off the board, green light appears around the chosen piece<br> 2- try  clicking away to toggle the green light off , essentially calling off this move. | The move is called off when you click away from the board | No way to toggle it off | Reopened |
| 3       | Expressive messages to explain to the user illegal moves when doing them | 1-try doing any illegal move like placing a smaller gobblet on top of a bigger one | Expressive message showing this is illegal to do | Nothing happens, the user has to figure it out for himself | Reopened |
| 4       | Gameplay issues | 1-touch gobblet that is on the board 2- touch another gobblet that is larger than it | Once you touch gobblet that is on the board, you must play it. If there are no possible moves for it, you lose the game. Control changes to the bigger gobblet | Semi-fixed (second part in “expected” needs to be thought about and clarified) |
| 5       | Draw scenario | 1-repeat the same move for player1 three times 2-also repeat the same move for player2 three times | Game should be declared as draw | Game continues normally | Delayed until we get confirmation |
| 6       | Piece motion | 1-gobble up 2 pieces 2-try to lift the bigger piece | Bigger piece should be lifted to ensure one piece is moved at a time | Piece is not lifted |
| 7       | Gobbling issues | 1-align three white pieces in a row/column 2-put a black piece on one of them <br>3-put a white piece in the 4th slot (so that they become 4 in a row but the black piece is above one of them) <br>4-select the black piece and don’t move it | Game wait until the black piece is moved, perhaps black gobblet will cover another white gobblet in the row. Game declares game over and doesn’t wait to move the black piece. |
| 8       | Gameplay issues | 1-select a piece <br>2-try to select another one to play | Game should allow the player to change selection | Game does not allow player to change selection |
| 9       | Game user interaction | 1-open the game and play | Game should display whose turn it is | Game doesn’t display whose turn it is |
| 10      | Display game recommended age | 1-open the game | Recommended age should be displayed in the options menu | Options menu does not have recommended age. |
| 11      | Losing conditions | 1-open the game <br>2-touch a gobblet that cannot be played at the moment | You should automatically lose the game | Game continues |
