package com.next.game.model;

import com.next.engine.animation.*;
import com.next.engine.data.Registry;
import com.next.engine.event.EventCollector;
import com.next.engine.event.TriggerRules;
import com.next.engine.graphics.Layer;
import com.next.engine.graphics.RenderQueue;
import com.next.engine.model.Actor;
import com.next.engine.model.HitboxSpec;
import com.next.engine.model.ProjectileSpec;
import com.next.engine.physics.*;
import com.next.engine.scene.Direction;
import com.next.engine.debug.DebugChannel;
import com.next.engine.debug.Debugger;
import com.next.game.visual.AnimationState;
import com.next.game.event.HitEvent;
import com.next.game.model.factory.HitboxFactory;
import com.next.game.rules.Layers;
import com.next.game.rules.data.ActiveGear;
import com.next.game.rules.data.Attributes;
import com.next.engine.system.Input;
import com.next.game.rules.data.Inventory;
import com.next.game.util.Inputs;
import lombok.Getter;
import lombok.Setter;

public final class Player extends Actor implements Combatant {

    private final Dresser<AnimationState> costume;

    @Getter private final Inventory inventory = new Inventory();
    @Setter private Input input;
    private final HitboxFactory hitboxFactory;

    @Getter @Setter boolean talking;

    private float speed = 1;
    @Getter @Setter private int maxHealth = 6;
    @Getter private int health = maxHealth;
    private Direction direction = Direction.DOWN;

    private float dx;
    private float dy;

    // Combat-related stuff
    private boolean attacking = false;
    private int attackingFrames = 0;
    private int invincibilityFrames = 0;
    private boolean invincible = false;
    @Getter private final ActiveGear activeGear;
    @Getter private final Attributes attributes;

    public Player(float worldX, float worldY, Wardrobe<AnimationState> animations, CollisionBox collisionBox,
                  HitboxFactory hitboxFactory
    ) {
        this.worldX = worldX;
        this.worldY = worldY;
        this.mass = 1f;

        layer = Layers.PLAYER;
        collisionMask = Layers.NPC | Layers.WALL | Layers.ITEM | Layers.ENEMY;
        this.collisionBox = collisionBox;
        this.collisionType = CollisionType.SOLID;
        setPosition(worldX, worldY);

        costume = new Dresser<>(animations);
        costume.wear(AnimationState.IDLE_DOWN);

        this.hitboxFactory = hitboxFactory;

        // Gear and stuff
        this.activeGear = new ActiveGear();
        activeGear.weapon = new WeaponSword();
        activeGear.shield = new ArmorShieldWood();

        this.attributes = new Attributes();
        attributes.strength = 1;
        attributes.resistance = 1;
        attributes.coin = 0;
        attributes.level = 1;
        attributes.xp = 0;

        inventory.add(activeGear.weapon);
        inventory.add(activeGear.shield);
    }

    @Override
    public Costume getCostume() {
        return costume;
    }

    @Override
    public void update(double delta) {

        if (invincible) invincibilityFrames--;
        invincible = invincibilityFrames > 0;

        attacking = attackingFrames > 0;
        handleAttackAnimation();
        if (attacking) {
            attackingFrames--;
            switch (direction) {
                case UP -> costume.wear(AnimationState.ATTACK_UP);
                case DOWN -> costume.wear(AnimationState.ATTACK_DOWN);
                case LEFT -> costume.wear(AnimationState.ATTACK_LEFT);
                case RIGHT -> costume.wear(AnimationState.ATTACK_RIGHT);
            }
        }

        if (input.isTyped(Inputs.TALK) && !talking && !attacking) {
            attacking = true;
            attackingFrames = 39;
        } else if (input.isTyped(Inputs.FIRE) && !talking && !attacking) {
            shot();
        } else if (!talking & !attacking) {
            if (input.isDown(Inputs.UP)) {
                dy -= speed;
                costume.wear(AnimationState.WALK_UP);
                direction = Direction.UP;
            } else if (input.isDown(Inputs.DOWN)) {
                dy += speed;
                costume.wear(AnimationState.WALK_DOWN);
                direction = Direction.DOWN;
            } else if (input.isDown(Inputs.LEFT)) {
                dx -= speed;
                costume.wear(AnimationState.WALK_LEFT);
                direction = Direction.LEFT;
            } else if (input.isDown(Inputs.RIGHT)) {
                dx += speed;
                costume.wear(AnimationState.WALK_RIGHT);
                direction = Direction.RIGHT;
            }
        }

        if (dx != 0 || dy != 0)
            context.mailbox().motionQueue.submit(this.id, dx, dy, 0f);
        else {
            if (!attacking) {
                switch (direction) {
                    case UP -> costume.wear(AnimationState.IDLE_UP);
                    case LEFT -> costume.wear(AnimationState.IDLE_LEFT);
                    case DOWN -> costume.wear(AnimationState.IDLE_DOWN);
                    case RIGHT -> costume.wear(AnimationState.IDLE_RIGHT);
                }
            }
        }

        costume.update(delta);
        dx = 0;
        dy = 0;

        Debugger.publish("PLAYER", new Debugger.DebugText("X: " + worldX + ", Y: " + worldY), 10, 90, DebugChannel.INFO);
    }

    @Override
    public void onCollision(Body other, EventCollector collector) {
//        if (other instanceof NpcDummy dummy) {
//            if (input.isTyped(Input.Action.TALK)) {
//                collector.post(() -> new DialogueEvent(this, dummy));
//            }
//        }
    }

    @Override
    public void takeDamage(int damage) {
        if (invincible) return;

        damage = Math.clamp(damage, 0, health);
        health -= damage;
        invincible = true;
        invincibilityFrames = 60;
    }

    @Override
    public boolean isDead() {
        return false;
    }

    public void boostSpeed(float boost) {
        speed += boost;
    }

    @Override
    public void setHealth(int health) {
        health = Math.clamp(health, 0, maxHealth);
        this.health = health;
    }

    @Override
    public int getAttack() {
        return attributes.strength * activeGear.weapon.getMight();
    }

    @Override
    public int getDefense() {
        return attributes.resistance * activeGear.shield.getResistance();
    }

    private void handleAttackAnimation() {
        if (attacking && attackingFrames > 29) {
            if (direction == Direction.UP) dy -= speed;
            if (direction == Direction.DOWN) dy += speed;
            if (direction == Direction.LEFT) dx -= speed;
            if (direction == Direction.RIGHT) dx += speed;
        }

        if (attackingFrames == 30) {
            var spec = activeGear.weapon.getSpec(direction);
            var rule = TriggerRules
                    .when((s, other) -> other instanceof Hittable)
                    .then((s, other) -> new HitEvent(this, (Hittable) other, spec));
            hitboxFactory.spawnHitbox(this, spec, rule);
        }
    }

    private void shot() {
        var hbBuilder = HitboxSpec.builder()
                .width(8).height(8)
                .durationSeconds(1d)
                .damage(1).knockbackX(0).knockbackY(0)
                .collisionLayer(Layers.ENEMY | Layers.WALL).oneHitPerTarget(true)
                .followOwner(false)
                .offsetX(-4).offsetY(-4);

        float offsetX = 0f;
        float offsetY = 0f;

        var projBuilder = ProjectileSpec.builder()
                .penetrable(false)
                .vx(0f).vy(0f);

        switch (direction) {
            case UP -> projBuilder.vy(-180f);
            case DOWN -> projBuilder.vy(180f);
            case LEFT -> projBuilder.vx(-180f);
            case RIGHT -> projBuilder.vx(180f);
        }

        projBuilder.hitboxSpec(hbBuilder.build());

        var sprite = Registry.textureIds.get("spell-shot.png");
        hitboxFactory.projectile(worldX + offsetX, worldY + offsetY, this, projBuilder.build(), sprite);
    }

    @Override
    public void collectRender(RenderQueue queue) {
        float z = 1f;

        long sortKey = ((long) Layer.ACTORS.ordinal() << 48)
                | ((long) ((int) z & 0xFFFF) << 32)
                | ((long) ((int) worldY & 0xFFFF) << 16)
                | (id & 0xFFFF);

        queue.draw(Layer.ACTORS, sortKey, worldX, worldY, costume);
    }
}
