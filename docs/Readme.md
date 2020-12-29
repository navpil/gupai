# Plan of which games to implement

Chinese dominoes are called GuPai 骨牌, Korean dominoes are called GolPae 골패

## Connecting games

These games are the most similar to the European dominoes.
Main game is JieLong.
DingNiu is the most complex variation - therefore it is done separately.

JieLong, CeDeng and Kkoriput-igi differ only in set composition, player number and points calculation.
Only CeDeng point calculation is missing.

Source: [Wikipedia](https://zh.wikipedia.org/wiki/%E6%8E%A5%E9%BE%8D_(%E4%B8%AD%E5%9C%8B%E9%AA%A8%E7%89%8C))

### JieLong 接龙 (Connect the dragon)

Basic game.
Single arm, discard a tile when cannot move.
All players pay each other according to difference in points.
The one who won becomes a dealer next time.
In case of tie, lower hand has a preference, with dealer counted as lowest hand.

When 3 players play - each get 10 tiles, 2 tiles don't play.
When 2 players play - each get 16 tiles.

Variation - two arm game.

### CeDeng 斜钉 (Slanted nail)

Additional rules:

Pass, 7-Head-8-Tail (alternatively 6-Head-7-Tail) double the score.
8 doubles in a hand - automatic pass, other players pay the player double of their points (a very rare thing). 
7 doubles in a hand - redeal.

Is played strictly with 4 people.

### Kkoriput-igi 꼬리붙이기 (Sticking the tail)

Korean variant of JieLong.

Additional rules:

Scoring - only lowest player pays the highest player. Additionally, all players with score higher than 30 also pay.
When 3 players play - doubles from 3 to 6 are removed, each player gets 8 tiles.
When 2 players play - doubles from 3 to 6 are removed, each player gets 12 tiles.

Sources: [Culin](https://healthy.uwaterloo.ca/museum/Archives/Culin/Dice1893/kkoripouttchiki.html), [Namu Wiki](https://namu.wiki/w/%EA%B3%A8%ED%8C%A8)

### DingNiu 顶牛 (Bull fight)

Two armed game.
All military tiles except for a bull pair (62 and 63) are removed.
Played by 4 players - each gets 8 tiles.

Scoring - winner gets 6, losers pay -3, -2, -1.
When losers are in a tie, higher hand (last from the dealer) gets a preference.

SuanZhang - special rule, similar to blocking in European dominoes.

Source: [Wenku Baidu](https://wenku.baidu.com/view/ae426c0dfc4ffe473368ab39?pcf=2&bfetype=new#)

## Trick taking games

Already have ShiWuHu, after adjusting a bit, TianJiu can be created. 
Still need to think of which tiles to put down - this moment is not present in ShiWuHu.

### TianJiu (TienGow) 天九 (Heaven Nine)

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

### KuPai 古牌 (Ancient Tiles)

Described in Macao book, information should be treated with caution.

Similar to TianJiu, but only single cards can be played - need more info from Macao book.

### Bagchen

Tibetian game. Currently not in plans.

Similar to TianJiu, but 32x2 tiles, many other additional rules.

## PaiGow and friends

This can be done as the last game, since it is not so difficult and it's actually popular.

Banker in all of the further games moves around a circle.
All the games use the Modulo 10 rule for hand evaluation.

### PaiGow 牌九 (Tile-nine)

4 tiles are received, split into 2 pairs.

Pairs are (from high to low):
 - Supreme Pair (or Gee Joon)
 - Civil Pairs
 - Military Pairs
 - Wongs (Heaven/Earth + 9 point tile)
 - Kongs (Heaven/Earth + 8 point tile)
 - Sum of the points divide by ten remainder (Gee Joon tiles both can be 3 or 6 here)

Equals are split by High domino (only). GeeGoon tiles have no value for evaluating ties (i.e. they are both lower than five).
If High domino cannot be used for a split, then the dealer wins.
If one hand loses and one wins - that's a push, player takes his bet back.

### Tau Ngau 鬥牛 Bullfight

Game of pure luck.
Receive 5 tiles, first discard 3 tiles which cleanly divide by 10.
Then add pips on other 2 tiles and modulo 10.

If a player cannot make a discard, he loses to the banker if the banker discarded.
If a banker cannot make a discard, he loses to all players who discarded.
If both a banker and a player discarded, higher ranking hand wins.
Ties are 'pushes', players take their bets back.

Sources: Macao book, [Pagat](https://www.pagat.com/domino/adding/taungau.html)

### Kol-ye-si

Korean game, probably named 골여시

Players take 1 tile each and place their bets.
Banker takes 2 tiles. If it's a perfect pair (identical tiles) he immediately wins.
Other players take 1 or 2 tiles.
All tiles are exposed now.
Hands are calculated by Modulo 10 rule.

Apparently Banker is at a disadvantage against a perfect player's play.
The 'perfect pair' rule does not beat the player's ability to choose between 1 and 2 tiles.

Implemented. 
Wishlist: 
 
 - extract strategies of choosing the count and a stake
 - use genetic algorithm for finding out best stake strategy
 
Sources: [Culin](https://healthy.uwaterloo.ca/museum/Archives/Culin/Dice1893/kolyesi.html)

### DaLing 打零 (Beat Zeroes)

Players put 3 bets - 1 tile bet, 2 tiles bet and 3 tiles bet.
Banker takes 2 and 5 players take 6 tiles each.
Players split their 6 tiles into three groups - 1 tile, 2 tiles and 3 tiles.
Mod 10 calculation, but 0 is highest.
Each group plays against the banker.

Sources: Macao book, [Video with DaLing shown](https://www.youtube.com/watch?v=PmtH7Ad4ayI&t=1s&ab_channel=%EB%8C%80%ED%95%9C%EB%AF%BC%EA%B5%AD%EB%86%80%EC%9D%B4KoreanGame)

## Fishing games

These games are similar to Korean Hwatu / Japanese Hanafuda.
Purpose of the fishing game is to collect as many tiles as possible by 'fishing' them from the table.

### Tiu-U 釣魚

Translates as 'Catching Fish'.
Uses two sets of dominoes.
If 3 players play, each takes 8 tiles, if 2 - each takes 12.
16 tiles are placed face up on the table, forming a 'pool'.
Rest (64 - 16 - 24 = 24) are in left in a woodpile.

Tiles match when their pip count match, no matter which suit they belong to.
As an exception - Gee Joon tiles (21 and 42) can also match each other.
As an another exception - when a player receives a pair of 6-doubles, he lays them down in front of him immediately 
(which will leave him without a piece in a hand for the last two moves).

On his move a player first plays a tile from his hand.
If he can match his tile to the one on the table, he collects the pair, if he does not, he just puts the tile.
Next he takes a tile from a woodpile and tries to match his tile with the one on the table (same as above).

As an exception for the matching rule - if there are two identical tiles in the pool, a player may place a third identical 
tile next to them, hoping collecting all four of them later.
If there are three identical tiles in the pool, a player may collect all of them by a fourth identical tile.

Game continues until the woodpile is empty.

All taken tiles are divided into small and big fish.
Small fish are tiles with less than 8 pips, they score one point per red pip they have.
Total small fish score is rounded to the nearest 10 up.
Big fish score 2 points per each pip, regardless of a color.

Sources: [Culin](https://healthy.uwaterloo.ca/museum/Archives/Culin/Dice1893/tiuu.html)

#### ShiWuHu (十五湖) Tiu-U variation (84 cards) 

It is very likely that the game originally is played with the deck of 84 domino cards, which includes four of each tile.

The game rules are very similar.

Gee Joon tiles cannot fish each other, as in Tiu-U.
64 and 55 cannot fish each other, even though they both worth 10 points.

Variations:
 - Gee Joon can fish each other
 - Gee Goon and all sixes can fish each other
 - 64 and 55 can fish each other
 - 66 and 11 can fish each other

There is no rule that a third identical card can be put on the table (anyway that doesn't make much sense).
Catching three identical tiles with a fourth one is possible only for 66, 11, 56, 55, 64 and 21 - 
the tiles which only match themselves.

For the purposes of scoring all tiles are divided into three categories: Large, Medium and Small fish.

Large fish:

 - If player has a Heaven and a pair of Nines (black and red), then all Heavens and Nines score as a Big fish, otherwise - small fish 
 - If a Supreme Pair can be found in a harvest, all 42 and 21 are big fish, otherwise they are small fish
 - Four 55 (plum), forming a Jun, each count as a big fish, if less then small
 - Four 56 (axe), forming a Jun, each count as a big fish, if less then small
 - Four 64 (read head ten), forming a Jun, each count as a big fish, if less then small

Middle fish: Earth-Eight, Man-Seven, Goose-Five count as Middle fish under the same condition as Heaven-Nine - 
three different cards should be present.

Small fish: everything else.

Big fish count 30 points each, middle fish count 20 points each, small fish count 10 points each.

Some remove the 1-Wen-2-Wu restriction that both of the Miscellaneous Pair should be present to form big/middle fish - one is enough.

Special combinations, which bring more points are:
 
 - 12 TianJiu
 - 3 Jun (all three Jun collected)
 - 4 Supreme pairs. 
 
In the simplest rules, all the above combinations give additional 300 points.

There are more complex rules, described in the [aloneinthefart](https://aloneinthefart.blogspot.com/2009/03/blog-post_05.html) blog post.

Players pay each other based on the point difference (as in JieLong).

#### Merged Tiu-U rules

Small fish and Big fish in original Tiu-U is too unbalanced.
Small/Middle/Big fish in ShiWuHu variation looks better. 
This rule set with some minor adjustments can be used for the 64 tiles variation.
Proposed changes are: 

 - Remove the '1-Wen-2-Wu' restriction for forming mixed Wen-Wu pairs and counting large/middle fish
 - 8 TianJiu counts as a special combination

### Tsung Shap (對十)

Translates as 'Dispute for tens'.

Game is mostly based on luck.
2 people play, each gets 16 tiles.

On each turn player takes a tile from his pile, turns it face up and places on the table.
If this tile matches a tile on an either end of a row (matching is done only for identical tiles), player collects both 
tiles and places them in front of him. Pairs count 10 points per every pip.
If the played tile forms a modulo 10 triplet with two tiles - two from left, from right or one from left and one from the right,
then player collects a triplet and places it in from of him. Triplets count 1 point per every pip.
If the played tile cannot form a pair nor a triplet, player can put it on the ether end of a row.
If the player tool emptied a table by his move - made a so called 'sweep', he is awarded by a bonus 40 points 
(to indicate it, he piles the taken tile onto each other). After the 'sweep' a player has to put another tile from
his pile, not the table to be empty.

The game ends when one of the players has laid out all of his pieces.

Source: Culin, Macao book

#### 4 tiles in a hand variation

[Spieltdomino](https://spieltdomino.wordpress.com/2013/02/28/tsung-shap-domino-fur-aufmerksame-aus-dem-reich-der-mitte/)
alters the rules to make the game less based on pure luck.

Each player takes 4 tiles into his hand and can decide which tile to put down.
After every move, a player replenishes his hand from the woodpile, unless it is empty.

Other rules are the same.
  
## Draw and discard games

These are the Mahjong kind of games.
Aim is to collect a fixed number of combinations.
Similar to Rummy games, like Canasta.

### KapTaiShap

Called 執十 in Macao book

Many players can play, and many sets are used.
Approximate sets calculation - 2 sets per 5 players.

Aim of a game is to collect one "ngan" - an identical pair (i.e. military mixed pairs do not count) and 4 pairs which divide by 10.
In the beginning each player gets 9 tiles (dealer gets 10, but that's a mere technicality).

Each move consists of:

  - Optionally take any discarded tile into a hand and discard one tile face up.
  - Always take a tile from the wall and discard one tile face up.    

At any point when a player gets 10 tiles and has a winning hand, he can declare a victory.
Victorious player collects same amount of chips from every other player.

42 tile always counts as 3 points 

### KapShap (夾十)

2 player variant of a KapTaiShap which uses 1 set.
Rules are the same, except that a winning hand consists of 8 tiles: 
1 ngan (which means military tiles cannot be used for this purpose) and 3 pairs which divide by 10.

42 tile always counts as 3 points

### Ho-hpai

Korean game. Probably the same as Tuk (톡) which is often mentioned on Korean websites.

Played with 3 or 4 people.
According to Culin when 3 players play, 33, 44, 55 and 66 are taken away from the set.

The aim is to collect 2 triplets.
Triplets are similar to XuanHePaiPu triplets without the "Full 14" and "Five", but with 6-domino straights.
Straight example: `[3:1][3:2][3:3][3:4][3:5][3:6]`.

Each player gets 6 tiles.
Other tiles form a line.
When player moves he first puts a tile on one end of a line and then takes a tile from the other end of a line.

Korean dominoes often use another Military pairs, namely:

        [2:6] [3:6]
        [2:5] [3:5]
        [2:4] [3:4]
        [1:4] [2:3]
        [1:2] [4:5]

Points calculation follow.

#### According to Culin

Mostly all combinations count 1 point with some exceptions.

Full Dragons or ssang-syo:

 - 2 Full Dragons count as 3
 - 2 Full Dragons which pair together counts as 4
 - 2 Full Dragons made purely of military tiles counts as 5
 - 1 Full Dragon counts as 1
 
Coincidence and Five sons, collectively called sok count as 1.

Splits or tai-sam-tong (3 and 3) count as 1.

Mixed:

 - 2 Big dragons made of doubles count 3
 - 2 Small dragons made of doubles count 3
 - 2 2-3-Kao made of doubles count 3
 - 1 Big dragon and 1 Small dragon not made of doubles count 3
 - Otherwise any mixed combination counts as 1 (according to Culin 2-3-Kao not made of doubles does not count, but this looks like an omission)

Straight:
 
 - 2 straight counts as 5
 - 5 straight counts as 4
 - Other straights count as 3  

Since Sok are easy to form sometimes the game is played without them.

#### According to Korean excel

Points combination is often unclear.

Main differences are the following.

Straights:
 
 - 2 straight counts as 7
 - 3 and 4 straights count as 4
 - other straights count as 3

Full dragons Wu count as 6

Certain Sok combinations count as 4.

Other than that many combinations which count 3 in Culin's rules are split between 4, 3 and 2 points with no obvious logic behind.

#### Combined rules

Culin rules are simpler, but sometimes feel wrong and Korean rules are often not clear.
Therefore to make a playable game, Culin's rules will be taken as a basis.

Straights adjustment:
 
 - 2 counts as 5
 - 3 and 4 count as 4
 - other counts as 3 

2-3-Kao combination not made of doubles counts as 1.

Sources: [Korean whsohn12 blog](https://blog.naver.com/whsohn12/100185916542), [Culin](https://healthy.uwaterloo.ca/museum/Archives/Culin/Dice1893/hohpai.html)

### Jjak-mat-chu-gi (짝맞추기)

Korean game. 2-4 players.
This game relies on luck more than other Mahjong-like games.

Aim is to get 3 classic pairs, all players are dealt 5 tiles (except for the dealer who gets 6, but that's a technicality).
On a move a player can take a tile discarded by a previous player - only if a pair can be formed with it, 
or take a tile from the unused tiles.
Player puts all the pairs he has face up in front of him.
If player has not won, he discards one tile face up.

If a player completes a hand from the "wall", everyone pays him.
If a player completes a hand by the tile discarded by the previous player, only that player pays him.
Players can agree that finishing the game by a discarded tile is forbidden.

*Since the game relies mostly on luck, tiles can be put face down to introduce the guessing/memory element into the game.*

### Small Mahjong (打小麻将)

Described in a [HuaBaoLeiDa blog post](http://blog.sina.com.cn/s/blog_62969d840100ucdk.html).

Game for 3-4 players.

Aim of a game is to collect 2 triplets.

For the purpose of this game two terms are defined:

 - Set contains three equal pips (i.e 3-3-3)
 - Straight contains three pips in succession (i.e. 3-4-5)

There are 3 kinds of triplets:
 - Set-Set (双同) triplet, same as XuanHePuPai Split, for example `[6:6][6:4][4:4]` = 4-4-4 + 6-6-6 - counts 3 points
 - Set-Straight (连同) triplet, with no overlapping pips, for example `[1:4][4:4][2:3]` = 4-4-4 + 1-2-3 - counts 2 points
 - Straight-Straight (双连) triplet, for example `[1:4][3:3][2:2]` = 1-2-3 + 2-3-4 - counts 1 point

Players receive 5 cards each.
Then in turn they pick a tile, if they didn't win - then they discard one tile face up.
The currently discarded tile can be picked up:
 
 - In order to form a Straight-Straight triplet - only by the next player
 - In order to from a Set-Straight triplet - by the next and the opposite player (or by anyone in a 3-player game)
 - In order to form a Set-Set triplet - by anyone
 - In order to form any triplet and win - by anyone
 - Hand closer to the discarded player always have a priority.

If no one picks the discarded tile, then the next player takes the tile from the wall.
Same as in Mahjong, players do not play strictly counter-clockwise, because players can be skipped if someone takes 
the currently discarded tile. 

## Single player games

XiangShiFu is already created - but is computer driven.
Probably need to split into actual engine and computer solver.

### XiangShiFu

Combinations are:

Three equal pips: 
 
 1. Five points
 2. Full fourteen
 3. Split (3 and 3) 
 
More than 3 equal pips:

 4. Coincidence (4 same, two other pips add to the same number)
 5. Five sons (Five same)
   
Spreads:

 6. Full dragon (all different)
 7. Mixed:
   - Small dragon (112233)
   - Big dragon (445566)
   - 2-3-Kao (223366)
   