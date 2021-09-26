package com.epikron.catzwiki.utils

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.schedulers.TestScheduler
import javax.inject.Inject

interface BaseSchedulerProvider {
	fun io(): Scheduler
	fun computation(): Scheduler
	fun ui(): Scheduler
}

class SchedulerProvider @Inject constructor(): BaseSchedulerProvider {
	override fun computation(): Scheduler = Schedulers.computation()
	override fun ui(): Scheduler = AndroidSchedulers.mainThread()
	override fun io(): Scheduler = Schedulers.io()
}

class TrampolineSchedulerProvider @Inject constructor() : BaseSchedulerProvider {
	override fun computation(): Scheduler = Schedulers.trampoline()
	override fun ui(): Scheduler = Schedulers.trampoline()
	override fun io(): Scheduler = Schedulers.trampoline()
}

class TestSchedulerProvider @Inject constructor(private val scheduler: TestScheduler) : BaseSchedulerProvider {
	override fun computation() = scheduler
	override fun ui() = scheduler
	override fun io() = scheduler
}