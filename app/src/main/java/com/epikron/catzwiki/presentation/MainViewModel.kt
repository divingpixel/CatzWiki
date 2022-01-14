package com.epikron.catzwiki.presentation

import android.net.ConnectivityManager
import android.net.Network
import androidx.lifecycle.ViewModel
import com.epikron.catzwiki.model.BreedModel
import com.epikron.catzwiki.model.UserModel
import com.epikron.catzwiki.remote.CatApiRepository
import com.epikron.catzwiki.utils.Write
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.SingleSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainViewModel @Inject constructor(private val catApiRepository: CatApiRepository) : ViewModel() {

    val onlineStatus: BehaviorSubject<Boolean> = BehaviorSubject.create()
    val allBreeds: SingleSubject<List<BreedModel>> = SingleSubject.create()
    val selectedCat: BehaviorSubject<BreedModel> = BehaviorSubject.create()
    val user: BehaviorSubject<UserModel> = BehaviorSubject.create()
    private val disposable: CompositeDisposable = CompositeDisposable()

    fun getAllBreeds() {
        if (!allBreeds.hasValue()) {
            disposable.add(
                catApiRepository.getAllBreeds()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(
                        onSuccess = { breedList -> allBreeds.onSuccess(breedList) },
                        onError = { error -> allBreeds.onError(error) }
                    )
            )
        }
    }

    fun registerConnectivityObserver(connectivityManager: ConnectivityManager) {
        if (!onlineStatus.hasValue()) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            onlineStatus.onNext(capabilities != null)
            connectivityManager.registerDefaultNetworkCallback(
                object : ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        super.onAvailable(network)
                        onlineStatus.onNext(true)
                        Write("Network Available")
                    }

                    override fun onLost(network: Network) {
                        super.onLost(network)
                        onlineStatus.onNext(false)
                        Write("Connection lost")
                    }
                })
        }
    }

    override fun onCleared() {
        super.onCleared()
        if (!disposable.isDisposed) disposable.dispose()
    }
}
