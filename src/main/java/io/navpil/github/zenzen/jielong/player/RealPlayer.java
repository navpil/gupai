package io.navpil.github.zenzen.jielong.player;

import io.navpil.github.zenzen.dominos.Domino;
import io.navpil.github.zenzen.jielong.Dragon;
import io.navpil.github.zenzen.jielong.Move;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class RealPlayer implements Player {

    private final List<Domino> dominoes;
    private final List<Domino> down = new ArrayList<>();
    final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    private final String name;

    public RealPlayer(String name, List<Domino> dominoes) {
        this.dominoes = dominoes;
        this.name = name;
    }

    @Override
    public Move firstMove() {
        while (true) {
            showDominoes();
            System.out.println("Please choose which one to lead");
            try {
                final int index = readInt() - 1;
                if (index >= 0 && index < dominoes.size()) {
                    final Domino domino = dominoes.get(index);
                    int side;
                    if (!isDouble(domino)) {
                        System.out.println("Which side is open for " + domino + "?");
                        side = readInt();
                    } else {
                        side = 1;
                    }
                    if (side == 1 || side == 2) {
                        final Domino remove = dominoes.remove(index);
                        if (side == 1) {
                            return new Move(0, remove.getPips()[1], remove.getPips()[0]);
                        } else {
                            return new Move(0, remove.getPips()[0], remove.getPips()[1]);
                        }
                    } else {
                        System.out.println("Illegal side");
                    }
                } else {
                    System.out.println("Illegal index " + index);
                }

            } catch (IOException e) {
                throw new RuntimeException("Sorry, could not do it");
            }

        }
    }

    private boolean isDouble(Domino domino) {
        final int[] pips = domino.getPips();
        return pips[0] == pips[1];
    }

    private int readInt() throws IOException {
            final String s = bufferedReader.readLine();
            return Integer.parseInt(s);
    }



    @Override
    public Move extractMove(Dragon dragon) {
        while (true) {
            showDominoes();
            System.out.println("Please choose which one to move");
            try {
                final int index = readInt() - 1;
                if (index >= 0 && index < dominoes.size()) {
                    final int shouldMove;
                    final List<Integer> matchingEnds = getMatchingEnds(dragon, index);
                    if (matchingEnds.isEmpty()) {
                        shouldMove = 0;
                    } else {
                        System.out.println("Put down [0] or move [1]?");
                        shouldMove = readInt();
                    }
                    if (shouldMove == 1) {
                        int side;
                        if (matchingEnds.size() > 1) {
                            System.out.println("Which side to put the " + dominoes.get(index) + "?");
                            side = readInt();
                        } else {
                            side = matchingEnds.get(0);
                        }
                        if (side == 1 || side == 2) {
                            List<Move> moves = MoveFinder.getAvailableMoves(dragon.getOpenEnds(), dominoes);
                            final int suanZhangMovesSize = (int) moves.stream().filter(m -> dragon.suanZhang().willSuanZhang(m)).count();
                            //SuanZhang only counts when user can choose between suanzhang and no suanzhang. IF he has no choice - SuanZhang won't count
                            boolean maySuanZhang = !(suanZhangMovesSize == 0 || suanZhangMovesSize == moves.size());
                            final Domino remove = dominoes.remove(index);
                            Move move;
                            if (dragon.getOpenEnds().get(side - 1) == remove.getPips()[0]) {
                                move = new Move(side, remove.getPips()[0], remove.getPips()[1]);
                            } else {
                                move = new Move(side, remove.getPips()[1], remove.getPips()[0]);
                            }
                            if (maySuanZhang && dragon.suanZhang().willSuanZhang(move)) {
                                move.setSuanZhang(true);
                            }
                            return move;
                        } else {
                            System.out.println("Illegal side");
                        }
                    } else {
                        System.out.println("You've put down the domino");
                        final Domino remove = dominoes.remove(index);
                        down.add(remove);
                        return Move.none(remove);
                    }
                } else {
                    System.out.println("Illegal index " + index);
                }

            } catch (IOException e) {
                throw new RuntimeException("Sorry, could not do it");
            }
        }

    }

    private List<Integer> getMatchingEnds(Dragon dragon, int index) {
        final Domino domino = dominoes.get(index);
        final List<Integer> openEnds = dragon.getOpenEnds();
        List<Integer> matchingEnds = new ArrayList<>();
        int matchinEndIndex = 0;
        for (Integer openEnd : openEnds) {
            matchinEndIndex++;
            final int[] pips = domino.getPips();
            if (openEnd == pips[0] || openEnd == pips[1]) {
                matchingEnds.add(matchinEndIndex);
            }
        }
        return matchingEnds;
    }

    private void showDominoes() {
        System.out.println("You have these dominoes: ");
        int index = 0;
        for (Domino dominoe : dominoes) {
            index++;
            System.out.println("  (" + index + ") " + dominoe);
        }
    }

    @Override
    public int getPoints() {
        int score = 0;
        for (Domino domino : dominoes) {
            score += (domino.getPips()[0] + domino.getPips()[1]);
        }
        for (Domino domino : down) {
            score += (domino.getPips()[0] + domino.getPips()[1]);
        }
        return score;
    }

    @Override
    public String getName() {
        return name;
    }


}
