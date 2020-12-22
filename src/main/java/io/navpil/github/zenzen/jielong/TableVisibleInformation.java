package io.navpil.github.zenzen.jielong;

/**
 * Currently it only contains the Dragon, but if more complex strategies should arise, then other information can be added.
 * For example - who moved first, how many tiles a player put down, what was the move history and how players reacted etc.
 */
public class TableVisibleInformation {

    private Dragon dragon;

    public TableVisibleInformation(Dragon dragon) {
        this.dragon = dragon;
    }

    public Dragon getDragon() {
        return dragon;
    }
}
