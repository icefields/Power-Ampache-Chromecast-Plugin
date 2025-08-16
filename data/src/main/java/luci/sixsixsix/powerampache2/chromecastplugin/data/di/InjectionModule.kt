package luci.sixsixsix.powerampache2.chromecastplugin.data.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import java.lang.ref.WeakReference
import javax.inject.Singleton

typealias WeakContext = WeakReference<Context>

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    /**
     * Provides application managed coroutine scope for usage in data layer.
     */
    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(Dispatchers.IO + SupervisorJob())

    @Provides
    @Singleton
    fun provideWeakApplicationContext(application: Application) =
        WeakContext(application.applicationContext)
}