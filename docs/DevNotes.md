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

Already have ShiWuHu, after adjusting a bit, TianJiu can be created. 
Still need to think of which tiles to put down - this moment is not present in ShiWuHu.

Todo list:

 - TianJiu: classic, 3 players variant, cat and mice
 - KuPai - same as TianJiu, but single cards only (can be rules restriction)
 
ShiWuHu can be adjusted to have a human player

Not in plans:

 - Bagchen

## Gambling games

Kol-ye-si seems to give a preference to a player, not a banker, maybe there is no point in improving the AI. 
Same probably goes for DaLing.

Make all games to use RunGamblingGame

Todo list:

 - PaiGow - This can be done as the last game, since it is not so difficult and it's actually popular.
 - DaLing (probability calculation included already)

Included games:
 
 - Kol-ye-si, wishlist:
     - extract strategies of choosing the count and a stake
     - use genetic algorithm for finding out best stake strategy
 - Tau Ngau

## Fishing games

Provide more AI players for Tiu-U - now there is only one.
As for TsungShap (classic) - it is difficult to provide more players - there are almost no choices to make.

Todo list:
  
 - TsungShap Spieltdomino (4 cards) variation

Included games:

 - Tiu-U (two rule sets, plus mixed)
 - Tsung Shap
 
## Draw and discard games

Small MahJong looks like an invented game and I'm not sure people actually play it.
Nevertheless, I have included it, and maybe I will include more AIs in the future.
Need to add a calculation of a discard for a computer player.
Also there can be different strategies in which discard to take.
Winning hands are picked at random - can it happen that the same set of dominoes yield different winning hands?
Actually the game can last very few rounds - maybe the given Computer players are good enough already.

Try including more strategy for KapShap computer player.

Ho-hpai now has only Random Computer player and only an EYE fix for broken game.
Should include more AI variation and probably other fixes (maybe include those into a separate game - Tok)

Todo list:

 - Jjak-mat-chu-gi (3 classic pairs) + blind game variation(?)
 - Tok (?): MahJong variation of Ho-hpai 
 
Included games:

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
   