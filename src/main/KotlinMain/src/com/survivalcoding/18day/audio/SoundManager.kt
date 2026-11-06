package com.survivalcoding.`18day`.audio

import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import java.io.File

/**
 * 포켓몬 게임 사운드 매니저
 *
 * 포켓몬 출현음, 공격음, 배틀 배경음 등을 재생합니다.
 * PokeAPI는 사운드 파일을 직접 제공하지 않으므로,
 * 로컬 리소스 파일(sounds/ 폴더) 또는 외부 URL에서 사운드를 로드합니다.
 *
 * 사운드 파일이 없거나 재생에 실패해도 게임이 중단되지 않도록
 * 모든 예외를 안전하게 처리합니다.
 */
object SoundManager {

    /** 현재 재생 중인 MediaPlayer (배틀 BGM 등) */
    private var currentPlayer: MediaPlayer? = null

    /** 사운드 볼륨 (0.0 ~ 1.0) */
    var volume: Double = 0.7
        private set

    /**
     * 볼륨을 설정합니다.
     */
    fun setVolume(volume: Double) {
        this.volume = volume.coerceIn(0.0, 1.0)
        currentPlayer?.volume = this.volume
    }

    /**
     * 사운드 파일이 실제 존재하는지 확인합니다.
     */
    private fun soundFileExists(soundPath: String): Boolean {
        if (soundPath.isBlank()) return false
        // http/https URL인 경우 파일 존재 확인 불가 (재생 시도만 함)
        if (soundPath.startsWith("http://") || soundPath.startsWith("https://")) return true
        return File(soundPath).exists()
    }

    /**
     * 사운드 파일을 재생합니다 (한 번만 재생, 효과음용).
     *
     * @param soundPath 사운드 파일 경로 (클래스패스 리소스 또는 절대 경로/URL)
     * @param loop 반복 재생 여부 (배틀 BGM용)
     */
    fun play(soundPath: String?, loop: Boolean = false) {
        if (soundPath.isNullOrBlank()) {
            playDefaultBeep()
            return
        }

        // 로컬 파일인 경우 존재 여부 확인
        if (!soundFileExists(soundPath)) {
            System.err.println("[SoundManager] 사운드 파일을 찾을 수 없음: $soundPath (기본 효과음 사용)")
            playDefaultBeep()
            return
        }

        try {
            val media = Media(soundPath)
            val player = MediaPlayer(media)
            player.volume = volume
            player.setCycleCount(if (loop) MediaPlayer.INDEFINITE else 1)
            player.play()

            if (!loop) {
                // 효과음은 재생 완료 후 자동 해제
                player.setOnEndOfMedia {
                    player.dispose()
                }
            } else {
                currentPlayer = player
            }
        } catch (e: Exception) {
            // 사운드 파일이 없거나 재생 실패 시 게임 중단 없음
            System.err.println("[SoundManager] 사운드 재생 실패: $soundPath (${e.message})")
            playDefaultBeep()
        }
    }

    /**
     * 시스템 기본 비프음을 재생합니다.
     */
    private fun playDefaultBeep() {
        try {
            java.awt.Toolkit.getDefaultToolkit().beep()
        } catch (e: Exception) {
            System.err.println("[SoundManager] 기본 효과음 재생 실패: ${e.message}")
        }
    }

    /**
     * 현재 재생 중인 배틀 BGM을 정지합니다.
     */
    fun stop() {
        currentPlayer?.stop()
        currentPlayer?.dispose()
        currentPlayer = null
    }

    /**
     * 포켓몬 출현 효과음을 재생합니다.
     */
    fun playPokemonCry(soundPath: String?) {
        if (soundPath.isNullOrBlank() || !soundFileExists(soundPath ?: "")) {
            // 사운드가 없으면 시스템 비프음으로 대체
            playDefaultBeep()
            return
        }
        play(soundPath, loop = false)
    }

    /**
     * 배틀 배경음을 반복 재생합니다.
     */
    fun playBattleBgm(soundPath: String?) {
        if (soundPath.isNullOrBlank()) return
        stop()
        play(soundPath, loop = true)
    }
}
