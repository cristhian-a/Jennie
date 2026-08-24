package com.next.game.ui;

import com.next.engine.data.Registry;
import com.next.engine.graphics.Sprite;
import com.next.engine.system.Input;
import com.next.game.Game;
import com.next.engine.graphics.Layer;
import com.next.engine.graphics.RenderPosition;
import com.next.engine.graphics.RenderQueue;
import com.next.game.model.*;
import com.next.game.rules.data.Inventory;
import com.next.game.util.Colors;
import com.next.game.util.Fonts;
import com.next.game.util.Inputs;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StatsViewUIState implements UIState {

    // Box rectangles and stroke info
    private static final int arc = 25;
    private static final int thickness = 5;
    private static final int x = 50, y = 100;
    private static final int w = 400, h = 558;
    private static final int bx = x + (thickness >> 1), by = y + (thickness >> 1);
    private static final int bw = w - thickness + 1, bh = h - thickness + 1;
    private static final int bArc = Math.max(0, arc - thickness);

    // Static text coordinates
    private static final int stX = bx + 125, stY = by + 40;

    private static final int tx = bx + 20;
    private static final int ty = by + 80;
    private static final int incrY = 35;

    private static final int t1Y = ty;
    private static final int t2Y = ty + (incrY);
    private static final int t3Y = ty + (incrY * 2);
    private static final int t4Y = ty + (incrY * 3);
    private static final int t5Y = ty + (incrY * 4);
    private static final int t6Y = ty + (incrY * 5);
    private static final int t7Y = ty + (incrY * 6);
    private static final int t8Y = ty + (incrY * 7);
    private static final int t9Y = ty + (incrY * 8);
    private static final int t10Y = ty + (incrY * 9);
    private static final int t11Y = ty + (incrY * 10);
    private static final int t12Y = ty + (incrY * 11);

    private static final int displayX = tx + 300;

    private static final String EMPTY_TXT = "";
    private static final String STATS = "- YOUR STATS -";
    private static final String HP = "Health: ";
    private static final String GEAR = "Gear: ";
    private static final String STRENGTH = "Strength: ";
    private static final String RESISTANCE = "Resistance: ";
    private static final String ATTACK = "Attack: ";
    private static final String DEFENSE = "Defense: ";
    private static final String LEVEL = "Level: ";
    private static final String XP = "XP: ";
    private static final String NEXT_LEVEL = "Next Level: ";
    private static final String COIN = "Coins: ";
    private static final String WEAPON_MIGHT = "Weapon's Might: ";
    private static final String SHIELD_RESIS = "Shield's Resistance: ";

    private int hp = -999;
    private int strength = -999;
    private int resistance = -999;
    private int attack = -999;
    private int defense = -999;
    private int level = -999;
    private int xp = -999;
    private int nextLevel = -999;
    private int coins = -999;
    private int weaponsMight = -999;
    private int shieldsResistance = -999;
    private String displayedHp = EMPTY_TXT;
    private String displayedStrength = EMPTY_TXT;
    private String displayedResistance = EMPTY_TXT;
    private String displayedAttack = EMPTY_TXT;
    private String displayedDefense = EMPTY_TXT;
    private String displayedLevel = EMPTY_TXT;
    private String displayedXp = EMPTY_TXT;
    private String displayedNextLevel = EMPTY_TXT;
    private String displayedCoins = EMPTY_TXT;
    private String displayedMight = EMPTY_TXT;
    private String displaydResistance = EMPTY_TXT;

    private final Game game;
    private final Player player;

    // ** Inventory related stuff **
    private final ViewInventory inventoryView;
    private final ViewItemInfo itemInfoView;
    private final List<Item> inventoryItems = new ArrayList<>();
    private final Inventory inventory;

    // Info panel stuff
    private static final String OPT_EQUIP = "> Equip";
    private static final String OPT_USE =   "> Use";
    private static final String OPT_DROP =  "> Drop";
    private static final String OPT_BACK =  "> Back";
    private String[] options;
    private int optCursor = 0;

    // cursor
    private int cursorColumn;
    private int cursorRow;
    private Item cursorItem;
    private boolean itemSelected;

    public StatsViewUIState(Game game) {
        this.game = game;
        this.player = game.getPlayer();

        itemInfoView = new ViewItemInfo();
        inventoryView = new ViewInventory(this.player);
        inventory = player.getInventory();
        player.getInventory().forEach(inventoryItems::add);
    }

    @Override
    public void update(double delta) {
        var attributes = player.getAttributes();
        var gear = player.getActiveGear();

        if (player.getHealth() != hp) displayedHp = player.getHealth() + "/" + player.getMaxHealth();
        if (attributes.strength != strength) displayedStrength = String.valueOf(attributes.strength);
        if (attributes.resistance != resistance) displayedResistance = String.valueOf(attributes.resistance);
        if (player.getAttack() != attack) displayedAttack = String.valueOf(player.getAttack());
        if (player.getDefense() != defense) displayedDefense = String.valueOf(player.getDefense());
        if (attributes.level != level) displayedLevel = String.valueOf(attributes.level);
        if (attributes.xp != xp) displayedXp = String.valueOf(attributes.xp);
        if (attributes.lupXP != nextLevel) displayedNextLevel = String.valueOf(attributes.lupXP);
        if (attributes.coin != coins) displayedCoins = String.valueOf(attributes.coin);
        if (gear.weapon.getMight() != weaponsMight) displayedMight = String.valueOf(gear.weapon.getMight());
        if (gear.shield.getResistance() != shieldsResistance) displaydResistance = String.valueOf(gear.shield.getResistance());

        hp = player.getHealth();
        strength = attributes.strength;
        resistance = attributes.resistance;
        attack = player.getAttack();
        defense = player.getDefense();
        level = attributes.level;
        xp = attributes.xp;
        nextLevel = attributes.lupXP;
        coins = attributes.coin;

        Input input = game.getInput();

        int cursorIndex = cursorRow * 4 + cursorColumn;
        cursorItem = inventory.get(cursorIndex).orElse(null);

        if (itemSelected) {
            handleItemSelectedInput(input);
        } else {
            if (input.isTyped(Inputs.RIGHT)) {
                cursorColumn++;
                cursorColumn = cursorColumn % 4;
            } else if (input.isTyped(Inputs.LEFT)) {
                cursorColumn--;
                if (cursorColumn < 0) cursorColumn = 3;
                cursorColumn = cursorColumn % 4;
            } else if (input.isTyped(Inputs.DOWN)) {
                cursorRow++;
                cursorRow = cursorRow % 4;
            } else if (input.isTyped(Inputs.UP)) {
                cursorRow--;
                if (cursorRow < 0) cursorRow = 3;
                cursorRow = cursorRow % 4;
            } else if (input.isTyped(Inputs.TALK)) {
                if (cursorItem != null) {
                    itemSelected = true;
                    optCursor = 0;

                    if (cursorItem == player.getActiveGear().weapon || cursorItem == player.getActiveGear().shield) {
                        options = new String[] { OPT_BACK };
                    } else if (cursorItem instanceof Equip) {
                        options = new String[]{OPT_EQUIP, OPT_DROP, OPT_BACK};
                    } else if (cursorItem instanceof Consumable) {
                        options = new String[] {OPT_USE, OPT_DROP, OPT_BACK};
                    } else {
                        options = new String[] { OPT_DROP, OPT_BACK };
                    }
                }
            }
        }
    }

    private void handleItemSelectedInput(Input input) {
        if (input.isTyped(Inputs.TALK)) {
            if (Objects.equals(options[optCursor], OPT_BACK)) {
                this.itemSelected = false;
            } else if (Objects.equals(options[optCursor], OPT_EQUIP)) {
                if (cursorItem instanceof Weapon weapon) {
                    player.getActiveGear().weapon = weapon;
                } else if (cursorItem instanceof Armor armor) {
                    player.getActiveGear().shield = armor;
                }
                this.itemSelected = false;
            } else if (Objects.equals(options[optCursor], OPT_DROP)) {
                player.getInventory().pop(cursorItem);
                inventoryItems.remove(cursorItem);
                this.itemSelected = false;
                cursorItem = null;
            } else if (Objects.equals(options[optCursor], OPT_USE)) {
                if (cursorItem instanceof Consumable consumable) {
                    consumable.use();
                }
                player.getInventory().pop(cursorItem);
                inventoryItems.remove(cursorItem);
                this.itemSelected = false;
                cursorItem = null;
            }
        } else if (input.isTyped(Inputs.RIGHT)) {
            optCursor++;
            optCursor %= options.length;
        } else if (input.isTyped(Inputs.LEFT)) {
            optCursor--;
            if (optCursor < 0) optCursor = options.length - 1;
            optCursor = optCursor % options.length;
        }
    }

    @Override
    public void submitRender(RenderQueue queue) {
        queue.roundStrokeRect(
                Layer.UI_SCREEN,
                x, y,
                w, h,
                thickness,
                Colors.WHITE,
                arc
        );
        queue.fillRoundRect(
                Layer.UI_SCREEN,
                bx, by,
                bw, bh,
                Colors.FADED_BLACK,
                bArc
        );

        Layer l = Layer.UI_SCREEN;
        String f = Fonts.DEFAULT;
        int c = Colors.WHITE;
        RenderPosition rp = RenderPosition.AXIS;
        int fr = 0;

        queue.submit(l, STATS,          f, c, stX, stY, rp, fr);
        queue.submit(l, HP,             f, c, tx, t1Y, rp, fr);
        queue.submit(l, LEVEL,          f, c, tx, t2Y, rp, fr);
        queue.submit(l, STRENGTH,       f, c, tx, t3Y, rp, fr);
        queue.submit(l, RESISTANCE,     f, c, tx, t4Y, rp, fr);
        queue.submit(l, ATTACK,         f, c, tx, t5Y, rp, fr);
        queue.submit(l, DEFENSE,        f, c, tx, t6Y, rp, fr);
        queue.submit(l, XP,             f, c, tx, t7Y, rp, fr);
        queue.submit(l, NEXT_LEVEL,     f, c, tx, t8Y, rp, fr);
        queue.submit(l, COIN,           f, c, tx, t9Y, rp, fr);
        queue.submit(l, WEAPON_MIGHT,   f, c, tx, t11Y, rp, fr);
        queue.submit(l, SHIELD_RESIS,   f, c, tx, t12Y, rp, fr);

        queue.submit(l, displayedHp,            f, c, displayX, t1Y, rp, fr);
        queue.submit(l, displayedLevel,         f, c, displayX, t2Y, rp, fr);
        queue.submit(l, displayedStrength,      f, c, displayX, t3Y, rp, fr);
        queue.submit(l, displayedResistance,    f, c, displayX, t4Y, rp, fr);
        queue.submit(l, displayedAttack,        f, c, displayX, t5Y, rp, fr);
        queue.submit(l, displayedDefense,       f, c, displayX, t6Y, rp, fr);
        queue.submit(l, displayedXp,            f, c, displayX, t7Y, rp, fr);
        queue.submit(l, displayedNextLevel,     f, c, displayX, t8Y, rp, fr);
        queue.submit(l, displayedCoins,         f, c, displayX, t9Y, rp, fr);
        queue.submit(l, displayedMight,         f, c, displayX, t11Y, rp, fr);
        queue.submit(l, displaydResistance,     f, c, displayX, t12Y, rp, fr);

        renderInventory(queue);
    }

    private void renderInventory(RenderQueue queue) {
        inventoryView.render(queue);    // frame/view

        final int startX = 152;
        final int startY = 38;
        final int incr = 16 + 6;
        final int cursorWidth = 16 + 2;
        final int cursorHeight = 16 + 2;

        int cursorX = -1 + startX + (incr * cursorColumn);
        int cursorY = -1 + startY + (incr * cursorRow) ;

        queue.roundStrokeRect(Layer.UI_SCR_SCALED, cursorX, cursorY, cursorWidth, cursorHeight, 1, Colors.WHITE, 4);

        int row = 0, col = 0;
        for (int i = 0; i < inventoryItems.size(); i++) {
            Item item = inventoryItems.get(i);

            int x = startX + (incr * row);
            int y = startY + (incr * col);

            if (item == player.getActiveGear().weapon || item == player.getActiveGear().shield) {
                queue.fillRoundRect(Layer.UI_SCR_SCALED, x - 1, y - 1, cursorWidth, cursorHeight, Colors.GOLDEN, 4);
            }

            Sprite s = Registry.sprites[item.getIcon()];
            x += Math.round(s.pivotX());
            y += Math.round(s.pivotY());
            queue.submit(Layer.UI_SCR_SCALED, x, y, item.getIcon());

            row++;
            row %= 4;
            if (row == 0) col++;
        }

        itemInfoView.update(cursorItem, itemSelected, options, optCursor);
        itemInfoView.render(queue);
    }
}
