package com.survivalcoding.`18day`.pattern.factory

import com.survivalcoding.`18day`.Data_source.FakePokemonDataSource
import com.survivalcoding.`18day`.Data_source.PokemonDataSource
import com.survivalcoding.`18day`.repostory.IPokemonRepository
import com.survivalcoding.`18day`.repostory.PokemonRepositoryImpl

/**
 * [GoF Factory Pattern] 포켓몬 저장소 팩토리.
 *
 * 클라이언트는 구체 클래스( PokemonRepositoryImpl, FakePokemonDataSource )를
 * 직접 인스턴스화할 필요 없이, 팩토리 메서드를 통해 저장소를 생성합니다.
 *
 * OCP 준수: 새로운 데이터 소스가 추가되어도 클라이언트 코드 수정 없이
 * 팩토리 메서드만 확장하면 됩니다.
 *
 * 원격 데이터 소스(RemotePokemonDataSource)는 Ktor 의존성으로 인해
 * 현재 빌드 환경에서 컴파일 제외되어 있으므로, 팩토리에서는 Fake 데이터 소스만
 * 직접 생성하고, 원격은 추후 확장 가능하도록 구성합니다.
 */
object PokemonRepositoryFactory {

    enum class DataSourceKind {
        FAKE, REMOTE
    }

    /**
     * 지정한 데이터 소스 종류에 따른 IPokemonRepository 인스턴스를 생성합니다.
     * REMOTE는 현재 컴파일 제외 상태이므로 예외를 발생시킵니다.
     */
    fun createRepository(kind: DataSourceKind): IPokemonRepository {
        return when (kind) {
            DataSourceKind.FAKE -> PokemonRepositoryImpl(FakePokemonDataSource())
            DataSourceKind.REMOTE -> error("RemotePokemonDataSource는 현재 빌드 환경에서 사용할 수 없습니다. Ktor 의존성 확인 필요.")
        }
    }

    /**
     * 주어진 PokemonDataSource 인스턴스를 감싸는 IPokemonRepository를 생성합니다.
     */
    fun createRepository(dataSource: PokemonDataSource): IPokemonRepository =
        PokemonRepositoryImpl(dataSource)

    fun createFake(): IPokemonRepository = createRepository(DataSourceKind.FAKE)
}
