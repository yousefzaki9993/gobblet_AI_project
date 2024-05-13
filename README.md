

| **Name** | **ID** |
| --- | --- |
| Hebatuallah Ayman Mohamed Mokhtar Abdulaziz | 1900022 |
| Ghaith Bassam Zaza | 1901458 |
| Abdelrhman Rady Hassan Ahmed | 1900725 |
| Ahmed Ehab Fathy Abdelwahaab | 1901073 |
| Andrew Samir Kamel Gayed | 1900242 |
| Islam Zidan Mohammed Alalamy | 1900146 |
| Mina Mounir Farid Gendi | 1901384 |
| Abdelrahman Mohammed Abdelaziz Ibrahim | 1900829 |

This project was done by Senior 2 CSE students for the course: CSE472s (UG2018) - Artificial Intelligence

**Chosen Programming language:**

![image](https://github.com/Mina-Mounir-Farid/GobbletGame/assets/105249158/ab0cf558-1805-478c-b2a2-39e5dfe35203)


**Chosen Framework:**

Java Swing

**User manual and features:**

To use the application, simply open the provided exe file:

![image](https://github.com/Mina-Mounir-Farid/GobbletGame/assets/105249158/520c2b7d-d46b-4ee4-b258-344f496dcc1a)


Then when faced with the interface, choose an option from the many options provided and then press the play icon

![image](https://github.com/Mina-Mounir-Farid/GobbletGame/assets/105249158/de71408b-c1bf-43e9-b64b-63c7bc0068c1)


CPU represents the computer or the ai we are playing against, where there are 3 difficulties varying from easy (easily beaten) to medium (a fair challenge) to hard (almost impossible to beat)

Where the player option represents human player where us -humans- take control and try to beat an ai or another human player

Let's take Human vs Human for an example to illustrate some game features!

![image](https://github.com/Mina-Mounir-Farid/GobbletGame/assets/105249158/8bc38afd-6c02-4daf-8d73-e4aa4c3fd85a)


After we press the play icon, the pieces we use appear, whereby default the white always starts playing first as in chess.

In order to select a piece, click on it with the mouse, and it should appear on the top right corner indicating that it has been chosen and revealing the piece underneath it:

![image](https://github.com/Mina-Mounir-Farid/GobbletGame/assets/105249158/4f6ba002-94a5-4ac0-a997-774048085ceb)


And in order to play it, simply click anywhere on the board.

After that, the status in the right bottom corner changes indicating that it's black's turn to play.

![image](https://github.com/Mina-Mounir-Farid/GobbletGame/assets/105249158/c92e9ae0-c50b-42e9-8aec-d66bc7bb22df)


All the game rules were considered when the application was made and any attempt to violate it, causes a notification message to appear the bottom left of the GUI and rejects the move,

For example, if we tried to cover a larger piece that ours this is what we would be shown:

![image](https://github.com/Mina-Mounir-Farid/GobbletGame/assets/105249158/d80f5f3e-5686-457d-b0c1-9ba839b36bac)

Now let's also see what happens when a player wins by achieving 4 connected pieces:

![image](https://github.com/Mina-Mounir-Farid/GobbletGame/assets/105249158/aeef3370-f7ef-443b-931f-4e1fabb63d0e)


The notifier and status bar shows that someone won (white in this case) and all GUI interactions inside the boards are rejected unless the player starts a new game which clears the board and pieces.

The next feature we have is the AI (or CPU) player, before we show gameplay, we will discuss how it was made.

The first step was to make a board tree, the board tree represents all possible moves branching from our current move, and all the moves branching from these moves and so on until it reaches a certain depth.

This was our initial step, but for means of optimizing and eliminating unnecessary options, we used a game heuristic covered in our lectures called"Tapered search", this heuristic in short, sorts board nodes in the tree according to their evaluation (we will discuss the evaluation further on), and then board nodes are given ranks based on their indexing after sorting, where the higher rank nodes are given higher branching, which means more children can result out of this node because, while lower rank nodes are given lower branching, and all according to this function:

![image](https://github.com/Mina-Mounir-Farid/GobbletGame/assets/105249158/979aca3b-8759-4621-8d6e-10dd2bfd9f65)


And this concludes our tree, the next step is to know what move we should pick according to the tree, this is done by evaluating every node.

Evaluating nodes takes into account all pieces on board and off board and which turn it is and gives a score which represents how well the player is doing.

Then when all pieces are evaluated, we decide which move to pick by pruning the tree using alpha-beta pruning, which gives us which move we should take in order to achieve victory.

**Difficulty and iterative deepening:**

The last part to cover is difficulty, and what makes the easy CPU easy, or the hard CPU hard, the answer would be: depth.

We used iterative deepening in our algorithm, which in short, decides how much time should a tree spend generating children, and depending on the level of difficulty, we allow the tree to generate more depths, which in turn, gives a further insight into the game which allows CPU to take better decisions.

And now at last, when we try to play against ai, it's the same procedure for human, but instead of a second player making a move, the move is automatically taken by the AI.

Here's an example of a match of a player (White) against a hard AI (Black)

![image](https://github.com/Mina-Mounir-Farid/GobbletGame/assets/105249158/4ecb5257-fa99-4726-86bb-4aef9b7c0a74)
