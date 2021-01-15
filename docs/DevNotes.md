# Plan of which games to implement

In general need to add some Game log, so that not all information will be output to the user immediately, but after 
the game only.

There are some approaches which were crystalized only in the end of a developer's process, such as a multiple run 
strategy, etc. 
Should include them into the other games whenever possible.

## Connecting games

Included games:

 - JieLong
 - Kkoriput-igi
 - CeDeng
 - DingNiu


## Trick taking games

The games here are mostly modification of a TianJiu game.

All next games will benefit from a better AI, but not all information is available for the players.
For example in the TianJiu players do not have information about dominoes already played face up.

**ShiWuHu** uses a 84 (4x21) deck of cards, so it's not strictly a GuPai game and made mostly for research purposes.
ShiWuHu points calculation is quite basic.
I'm not sure how to exactly calculate points - so for the trick version I calculate the number of Hu, but for the 
get-rid version I simply give 1 point to a winner.

**TianJiu** can have better AI, but random strategy seems to work very well against an unexperienced player.
I'm leaving AI as is for now, since I don't know what the strategy should be anyway.

**KuPai** is made as a TianJiu rules modification.

Done:

 - TianJiu 2 players Mouse Moving
 - TianJiu, KuPai
 - ShiWuHu

Not in plans:

 - Bagchen

## Gambling games

**Kol-ye-si** seems to give a preference to a player, not a banker, maybe there is no point in improving the AI. 
Same probably goes for DaLing.

**DaLing** rules are not clear at all. 
Therefore, at least two variations were created. 
In a first one players should outrank the banker's hand. 
Since banker is at a disadvantage in this game, game is better played as a non-casino (friendly) with a round-robin banker.
There is no use of RED/BLACK game type described in the rules.
The second variation is an *exact point* game where players should match the banker's hand exactly.
It can be played in both casino and friendly way.
This game already uses the RED/BLACK game type evaluation.
Rules do not specify what should happen if no one matches banker's hands or what happens if several players are in a tie.
Because of these uncertainties I had to create the rules I saw fit.

Make all games to use RunGamblingGame

Todo list:

 - PaiGow - This can be done as the last game, since it is not so difficult and it's actually popular.

Included games:
 
 - DaLing
 - Kol-ye-si, wishlist:
     - extract strategies of choosing the count and a stake
     - use genetic algorithm for finding out best stake strategy
 - Tau Ngau

## Fishing games

Provide more AI players for **Tiu-U** - now there is only one.

As for **TsungShap** (classic) - it is difficult to provide more players - there are almost no choices to make.
The discard tile is pretty arbitrary but is not random - maybe this should be changed.

**SpielDomino TsungShap** lacks the strategy of discarding a tile.
I'm not sure what the strategy should actually be, so I'm leaving it as is for now.

Included games:

 - Tiu-U (two rule sets, plus mixed)
 - Tsung Shap
 - TsungShap Spieltdomino (4 cards) variation
  
## Draw and discard games

**Small MahJong** looks like an invented game and I'm not sure people actually play it.
Nevertheless, I have included it, and maybe I will include more AIs in the future.
Need to add a calculation of a discard for a computer player.
Also there can be different strategies in which discard to take.
Winning hands are picked at random - can it happen that the same set of dominoes yield different winning hands?
Actually the game can last very few rounds - maybe the given Computer players are good enough already.

Try including more strategy for **KapShap** computer player.

**Ho-hpai** now has only Random Computer player and only an EYE fix for broken game.
Should include more AI variation and probably other fixes (maybe include those into a separate game - Tok)

**Jjak-mat-chu-gi** is too restrictive, only 14% of games are won when 4 players play, 53% when 3 play and 88% when 2 play
With the 'offer-to-all' rule, meaning that all the players are offered a discard, winning rate for 4 players is 90%.
Should I add the KapShap-like rule 'offer-any-tile'?

Todo list:

 - Tok (?): MahJong variation of Ho-hpai 
 
Included games:

 - Jjak-mat-chu-gi (3 classic pairs)
 - KapTaiShap
 - KapShap
 - Small Mahjong
 - Ho-hpai
 
## Single player games

Todo list:

 - Five rows (strict, 5 generals, 2:1 escape)

In progress: 

 - XiangShiFu - is computer driven, need to add a Player interaction 

Not in plans:

 - Open the pagoda
 - Turtle
 - Memento (create pairs out of the tiles)
