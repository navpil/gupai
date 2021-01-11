package io.github.navpil.gupai.rummy.hohpai;

import io.github.navpil.gupai.fishing.tsungshap.RunManySimulations;
import io.github.navpil.gupai.jielong.Stats;
import io.github.navpil.gupai.ChineseDominoSet;
import io.github.navpil.gupai.dominos.Domino;

import java.util.List;

public class HoHpai {

    public static void main(String[] args) {

        //3-4 players according to Culin
        //up to 5 can play with DISCARD_FIRST
        //up to 6 can play with DRAW_FIRST rule
        //2 can also play, but same 24-tiles deck as for 3 players will apply
        final List<Player> players = List.of(
            new RandomPlayer("Comp-1"),
            new RandomPlayer("Comp-2"),
            new RandomPlayer("Comp-3"),
//            new RandomPlayer("Comp-4"),
//            new RandomPlayer("Comp-5"),
            new RealPlayer("Jim")
        );
        final List<Domino> deck = ChineseDominoSet.create();

        //This is a rule according to Culin. Can be played with a full deck though.
        //One source mentioned 64 tiles, which may mean that the game could be played with 2 sets
        if (players.size() <= 3) {
            deck.removeAll(Domino.ofList(33, 44, 55, 66));
        }

        final Stats stats = new RunManySimulations().runManySimulations(
                deck,
                players,
                RuleSet.optimal(),
                10,
                Simulation::runSimulation
        );

        System.out.println(stats);
    }


}