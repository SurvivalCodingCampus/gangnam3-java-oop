package com.survivalcoding.game.entity;

import com.survivalcoding.game.engine.GameState;
import com.survivalcoding.game.input.InputHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("게임 엔티티 기본 클래스: 생명체·피해·좌표")
class GameEntityTest {

    private static final double DT = 1.0 / 60.0;

    /** updatePosition이 protected이므로 테스트에서 직접 검증하기 위한 최소 구현체. */
    private static class TestEntity extends GameEntity {
        TestEntity(double x, double y, double width, double height, int hp) {
            super(x, y, width, height, hp);
        }

        @Override
        public void update(double deltaTime, InputHandler input, GameState gameState) {
        }

        void move(double deltaTime, GameState gameState) {
            updatePosition(deltaTime, gameState);
        }
    }

    private static TestEntity entity(double x, double y, int hp) {
        return new TestEntity(x, y, 40, 60, hp);
    }

    @Test
    @DisplayName("생성자 값이 그대로 저장되고 maxHp는 hp와 같다")
    void constructorStoresValues() {
        TestEntity e = entity(10, 20, 80);
        assertEquals(10, e.getX());
        assertEquals(20, e.getY());
        assertEquals(40, e.getWidth());
        assertEquals(60, e.getHeight());
        assertEquals(80, e.getHp());
        assertEquals(80, e.getMaxHp());
        assertTrue(e.isAlive());
    }

    @Test
    @DisplayName("기본 이동 속도는 200이며 setSpeed로 변경된다")
    void speedIsConfigurable() {
        TestEntity e = entity(0, 0, 10);
        assertEquals(200, e.getSpeed());
        e.setSpeed(350);
        assertEquals(350, e.getSpeed());
    }

    @Test
    @DisplayName("피해를 받으면 hp가 감소하고 히트 플래시가 켜진다")
    void takeDamageReducesHpAndFlashes() {
        TestEntity e = entity(0, 0, 100);
        e.takeDamage(30);
        assertEquals(70, e.getHp());
        assertTrue(e.isHitFlashing());
    }

    @Test
    @DisplayName("피해량은 hp를 0 아래로 내리지 않는다")
    void hpNeverGoesBelowZero() {
        TestEntity e = entity(0, 0, 10);
        e.takeDamage(9999);
        assertEquals(0, e.getHp());
    }

    @Test
    @DisplayName("hp가 0이 되면 죽는다")
    void zeroHpKillsEntity() {
        TestEntity e = entity(0, 0, 50);
        e.takeDamage(50);
        assertFalse(e.isAlive());
    }

    @Test
    @DisplayName("heal은 maxHp를 넘지 않는다")
    void healIsCappedAtMaxHp() {
        TestEntity e = entity(0, 0, 100);
        e.heal(500);
        assertEquals(100, e.getHp());

        e.takeDamage(40);
        e.heal(30);
        assertEquals(90, e.getHp());
    }

    @Test
    @DisplayName("넉백은 속도에 더해진다")
    void knockbackAddsVelocity() {
        TestEntity e = entity(0, 0, 10);
        e.applyKnockback(120, -80);
        assertEquals(120, e.getVelocityX());
        assertEquals(-80, e.getVelocityY());
    }

    @Test
    @DisplayName("updatePosition은 속도 * deltaTime 만큼 이동시킨다")
    void updatePositionAppliesVelocity() {
        GameState gs = new GameState();
        TestEntity e = entity(500, 500, 10);
        e.setVelocity(600, -300);
        e.move(DT, gs);
        assertEquals(500 + 600 * DT, e.getX(), 1e-9);
        assertEquals(500 - 300 * DT, e.getY(), 1e-9);
    }

    @Test
    @DisplayName("updatePosition은 매번 속도를 0.9배로 줄인다")
    void updatePositionAppliesFriction() {
        GameState gs = new GameState();
        TestEntity e = entity(500, 500, 10);
        e.setVelocity(100, 200);
        e.move(DT, gs);
        assertEquals(90, e.getVelocityX(), 1e-9);
        assertEquals(180, e.getVelocityY(), 1e-9);
    }

    @Test
    @DisplayName("updatePosition은 월드 경계 안으로 좌표를 제한한다")
    void updatePositionClampsToWorldBounds() {
        GameState gs = new GameState();

        TestEntity right = entity(2900, 500, 10);
        right.setVelocity(100000, 0);
        right.move(1.0, gs);
        assertTrue(right.getX() <= gs.getWorldWidth() - right.getWidth() / 2);

        TestEntity left = entity(20, 500, 10);
        left.setVelocity(-100000, 0);
        left.move(1.0, gs);
        assertTrue(left.getX() >= left.getWidth() / 2);

        TestEntity top = entity(500, 30, 10);
        top.setVelocity(0, -100000);
        top.move(1.0, gs);
        assertTrue(top.getY() >= top.getHeight() / 2);

        TestEntity bottom = entity(500, 1950, 10);
        bottom.setVelocity(0, 100000);
        bottom.move(1.0, gs);
        assertTrue(bottom.getY() <= gs.getWorldHeight() - bottom.getHeight() / 2);
    }

    @Test
    @DisplayName("getBounds는 중심이 (x, y)이고 크기가 width x height이다")
    void boundsAreCenteredOnPosition() {
        TestEntity e = entity(100, 200, 40);
        var b = e.getBounds();
        assertEquals(80, b.getMinX(), 1e-9);
        assertEquals(170, b.getMinY(), 1e-9);
        assertEquals(40, b.getWidth(), 1e-9);
        assertEquals(60, b.getHeight(), 1e-9);
    }

    @Test
    @DisplayName("범위가 겹치면 충돌하고 떨어져 있으면 충돌하지 않는다")
    void collisionFollowsBoundsOverlap() {
        TestEntity a = entity(100, 100, 40);
        TestEntity near = entity(120, 100, 40);
        TestEntity far = entity(900, 900, 40);

        assertTrue(a.collidesWith(near));
        assertFalse(a.collidesWith(far));
    }

    @Test
    @DisplayName("distanceTo는 두 점 사이의 거리를 반환한다")
    void distanceToUsesEuclideanDistance() {
        TestEntity a = entity(0, 0, 10);
        TestEntity b = entity(3, 4, 10);
        assertEquals(5.0, a.distanceTo(b), 1e-9);
        assertEquals(5.0, b.distanceTo(a), 1e-9);
    }

    @Test
    @DisplayName("angleTo는 방향을 라디안으로 반환한다")
    void angleToReturnsRadians() {
        TestEntity origin = entity(0, 0, 10);
        TestEntity right = entity(1, 0, 10);
        TestEntity up = entity(0, 1, 10);

        assertEquals(0.0, origin.angleTo(right), 1e-9);
        assertEquals(Math.PI / 2, origin.angleTo(up), 1e-9);
    }

    @Test
    @DisplayName("setPosition·setAlive·setFacingRight가 상태를 바꾼다")
    void settersMutateState() {
        TestEntity e = entity(0, 0, 10);
        e.setPosition(300, 400);
        assertEquals(300, e.getX());
        assertEquals(400, e.getY());

        e.setFacingRight(false);
        assertFalse(e.isFacingRight());

        e.setAlive(false);
        assertFalse(e.isAlive());

        e.setVelocity(11, 22);
        assertEquals(11, e.getVelocityX());
        assertEquals(22, e.getVelocityY());
    }

    @Test
    @DisplayName("getCenter는 현재 좌표를 Point2D로 반환한다")
    void centerReturnsCurrentPosition() {
        TestEntity e = entity(55, 66, 10);
        assertEquals(55, e.getCenter().getX(), 1e-9);
        assertEquals(66, e.getCenter().getY(), 1e-9);
    }
}