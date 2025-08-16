package luci.sixsixsix.powerampache2.chromecastplugin.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import luci.sixsixsix.powerampache2.chromecastplugin.data.MusicFetcherImpl
import luci.sixsixsix.powerampache2.chromecastplugin.domain.MusicFetcher

@Module
@InstallIn(SingletonComponent::class)
abstract class AbstractModule {
    @Binds abstract fun bindMusicFetcher(musicFetcherImpl: MusicFetcherImpl): MusicFetcher
}
