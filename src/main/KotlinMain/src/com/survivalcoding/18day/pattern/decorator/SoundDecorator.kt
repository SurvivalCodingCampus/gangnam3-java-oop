package com.survivalcoding.`18day`.pattern.decorator

import com.survivalcoding.`18day`.audio.SoundManager

/**
 * [GoF Decorator Pattern] 사운드 매니저 데코레이터 인터페이스.
 *
 * SoundManager에 책임(볼륨 제어, 루프, 페이드 인/아웃 등)을 동적으로 추가합니다.
 * 상속이 아닌 조합(composition)을 통해 기능을 확장하므로 OCP를 준수합니다.
 *
 * 예:
 *   val sound = SoundManager as ISoundPlayer
 *   val withVolume = VolumeDecorator(sound, 0.5)
 *   val withLoop = LoopDecorator(withVolume, true)
 *   withLoop.play("sounds/pikachu_cry.wav")
 */
interface ISoundPlayer {

    fun play(soundPath: String?, loop: Boolean)

    fun stop()

    fun setVolume(volume: Double)
}

/**
 * [GoF Decorator Pattern] 기본 구현체: SoundManager를 ISoundPlayer로 적응.
 */
class SoundManagerAdapter : ISoundPlayer {

    override fun play(soundPath: String?, loop: Boolean) {
        SoundManager.play(soundPath, loop = loop)
    }

    override fun stop() {
        SoundManager.stop()
    }

    override fun setVolume(volume: Double) {
        SoundManager.setVolume(volume)
    }
}

/**
 * [GoF Decorator Pattern] 데코레이터 기본 클래스.
 */
abstract class SoundDecorator(protected val wrapped: ISoundPlayer) : ISoundPlayer {

    override fun play(soundPath: String?, loop: Boolean) {
        wrapped.play(soundPath, loop)
    }

    override fun stop() {
        wrapped.stop()
    }

    override fun setVolume(volume: Double) {
        wrapped.setVolume(volume)
    }
}

/**
 * 볼륨 제어 데코레이터.
 * 재생 전 볼륨을 설정합니다.
 */
class VolumeDecorator(
    wrapped: ISoundPlayer,
    private val volume: Double
) : SoundDecorator(wrapped) {

    override fun play(soundPath: String?, loop: Boolean) {
        wrapped.setVolume(volume)
        wrapped.play(soundPath, loop)
    }
}

/**
 * 루프 강제 데코레이터.
 * 모든 재생을 루프 모드로 강제합니다.
 */
class LoopDecorator(
    wrapped: ISoundPlayer,
    private val forceLoop: Boolean
) : SoundDecorator(wrapped) {

    override fun play(soundPath: String?, loop: Boolean) {
        wrapped.play(soundPath, forceLoop)
    }
}

/**
 * 페이드 인 데코레이터.
 * 볼륨을 0에서 설정 볼륨까지 점진적으로 올리며 재생을 시뮬레이션합니다.
 * (실제 페이드는 시간이 필요하므로 여기서는 볼륨만 낮게 설정해 시작합니다.)
 */
class FadeInDecorator(
    wrapped: ISoundPlayer,
    private val targetVolume: Double = 0.7,
    private val startVolume: Double = 0.1
) : SoundDecorator(wrapped) {

    override fun play(soundPath: String?, loop: Boolean) {
        wrapped.setVolume(startVolume)
        wrapped.play(soundPath, loop)
        // 실제 페이드 인 효과는 애니메이션과 연동하여 별도로 구현할 수 있음
    }
}
