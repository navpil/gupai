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

Player has preference over banker.
If min-max stakes are different at least x10 simplest computer player always wins.
If stakes are fixed, then player has to deploy some cleverer techniques, but still wins.

Random player loses.

**DaLing** rules are not clear at all. 
The interpreted ruls are the *exact point* game where players should match the banker's hand exactly.
It can be played in both casino and friendly way.
This game already uses the RED/BLACK game type evaluation.
Rules do not specify what should happen if no one matches banker's hands or what happens if several players are in a tie.
Because of these uncertainties I had to create the rules I saw fit.

**TauNgau** has two sets of rules.
*pagat* rules as in Pagat.com website. There is no advantage over the player nor banker.
*macao* rules as in Macao book give a slight preference to the player.

Since Casino should always win, I've fixed the *macao* rules.

In a friendly game *pagat* rules should be used - and the banker will be changed round-robin.

Make all games to use RunGamblingGame

Included games:
 
 - DaLing
 - Kol-ye-si, wishlist:
     - extract strategies of choosing the count and a stake
     - use genetic algorithm for finding out best stake strategy
 - Tau Ngau
 - PaiGow (+ Small PaiGow)

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

**Ho-hpai** has only an EYE fix for broken game.
There are four strategies for chossing a discard, including random strategy.
Players may get into a zugzwang when no one accepts an offered tile.
This does not happen often, less than 1 time per 1000 games, but still a fix is needed for the situation.
To avoid a never ending game, a cutoff parameter was introduced - it will stop a game after some number of rounds.

I decided that the mysterious *tăi-să-ttai* combination is a single combination of the 3 pairs. 

Wishlist:

 - Introduce collecting straights and 3 pairs - current AI does not do that

**Tok** reuses as much of the Ho-Hpai code as possible.
Maybe it makes sense to introduce the Kap-Shap like rule 'offer-any-tile'?

**Jjak-mat-chu-gi** is too restrictive, only 14% of games are won when 4 players play, 53% when 3 play and 88% when 2 play
With the 'offer-to-all' rule, meaning that all the players are offered a discard, winning rate for 4 players is 90%.
Should I add the KapShap-like rule 'offer-any-tile'?

Included games:

 - Jjak-mat-chu-gi (3 classic pairs)
 - KapTaiShap
 - KapShap
 - Small Mahjong
 - Ho-hpai
 - Tok: MahJong variation of Ho-hpai 
  
## Single player games

**XiangShiFu** can be configured to use 2-3-6 or mixed dragon.
If the 2-2-3-3-6-6 is used, then player should not get a dead pair in the layout.
Should include other winning conditions, such as Five dragons figthing for throne etc.
Should add the PuzzleCreator which will create a set of triplets which definitely has a solution.
For Five dragons start with an end posiiton and then create a start position from it. 

Todo list:

 - Five rows (strict, 5 generals, 2:1 escape)

Done: 

 - XiangShiFu 

Not in plans:

 - Open the pagoda
 - Turtle
 - Memento (create pairs out of the tiles)
