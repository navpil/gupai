# Plan of which games to implement

## Connecting games

These games are the most similar to the European dominoes.
Main game is JieLong.
DingNiu is the most complex variation - therefore it is done separately.

JieLong, CeDeng and Kkoriput-igi differ only in set composition, player number and points calculation.
Only CeDeng point calculation is missing.

### JieLong

Basic game.
Single arm, discard a tile when cannot move.
All players pay each other according to difference in points.
The one who won becomes a dealer next time.
In case of tie, lower hand has a preference, with dealer counted as lowest hand.

When 3 players play - each get 10 tiles, 2 tiles don't play.
When 2 players play - each get 16 tiles.

Variation - two arm game.

### CeDeng

Additional rules:

Pass, 7-Head-8-Tail (alternatively 6-Head-7-Tail) double the score
8 doubles in a hand - redeal (a very rare thing)

Is played strictly with 4 people.

### Kkoriput-igi

Additional rules:

Scoring - only lowest player pays the highest player. Additionally, all players with score higher than 30 also pay.
When 3 players play - doubles from 3 to 6 are removed, each player gets 8 tiles.
When 2 players play - doubles from 3 to 6 are removed, each player gets 12 tiles.

### DingNiu

Two armed game.
All military tiles except for a bull pair (62 and 63) are removed.
Played by 4 players - each gets 8 tiles.

Scoring - winner gets 6, losers pay -3, -2, -1.
When losers are in a tie, higher hand (last from the dealer) gets a preference.

SuanZhang - special rule, similar to blocking in European dominoes.

## Trick taking games

Already have ShiWuHu, after adjusting a bit, TianJiu can be created. 
Still need to think of which tiles to put down - this moment is not present in ShiWuHu.

### TianJiu (TienGow)

Basic trick taking game.

Trick can be beaten by the same amount of tiles and same suit composition. Otherwise, tiles are played face down.

Classical Military and Civil suits.
1 tile tricks are of two suits - Military and Civil
2 tile tricks can be Military, Civil or Supreme Pair. The last one cannot beat anything, but also cannot be beaten.
3 tile tricks are mixed - 2 Military + 1 Civil or 2 Civil + 1 Military. Only following combinations are possible:
 - Heaven + Nine
 - Earth + Eight
 - Man + Seven
 - Goose + Five 
4 tile tricks are mixed - 2 Military + 2 Civil with the same combinations as 3 tile tricks.

When 3 players play:

 - 30 tiles (no 'Supreme Pair' in play)
 - 24 tiles:
   - Wen + 'Supreme Pair'
   - Wen (without 51) + 'Supreme Pair' + Nines
   - Wen + Nines

When 2 play: the cat and the mouse, described by MinFanXin

### GuPai

Similar to TianJiu, but only single cards can be played - need more info from Macao book.

### Bagchen

Currently not in plans.

Similar to TianJiu, but 32x2 tiles, many other additional rules.

## PaiGow and friends

This can be done as the last game, since it is not so difficult and it's actually popular.

Banker in all of the further games moves around a circle.
All the games use the Modulo 10 rule for hand evaluation.

### PaiGow

4 tiles are received, split into 2 pairs.

Pairs are (from high to low):
 - Supreme Pair (or Gee Goon)
 - Civil Pairs
 - Military Pairs
 - Wongs (Heaven/Earth + 9 point tile)
 - Kongs (Heaven/Earth + 8 point tile)
 - Sum of the points divide by ten remainder (Gee Goon tiles both can be 3 or 6 here)

Equals are split by High domino (only). GeeGoon tiles have no value for evaluating ties (i.e. they are both lower than five).
If High domino cannot be used for a split, then the dealer wins.
If one hand loses and one wins - that's a push, player takes his bet back.

### Tau Ngau

Game of pure luck.
Receive 5 tiles, first discard 3 tiles which cleanly divide by 10.
Then add pips on other 2 tiles and modulo 10.

If a player cannot make a discard, he loses to the banker if the banker discarded.
If a banker cannot make a discard, he loses to all players who discarded.
If both a banker and a player discarded, higher ranking hand wins.
Ties are 'pushes', players take their bets back.

### Kol-ye-si

Players take 1 tile each and place their bets.
Banker takes 2 tiles. If it's a perfect pair (identical tiles) he immediately wins.
Other players take 1 or 2 tiles.
All tiles are exposed now.
Hands are calculated by Modulo 10 rule.

### DaLing

Players put 3 bets - 1 tile bet, 2 tiles bet and 3 tiles bet.
Banker takes 2 and 5 players take 6 tiles each.
Players split their 6 tiles into three groups - 1 tile, 2 tiles and 3 tiles.
Mod 10 calculation, but 0 is highest.
Each group plays against the banker.

## Fishing games

## Combination games

## Single player games

XiangShiFu is already created - but is computer driven.
Probably need to split into actual engine and computer solver.
