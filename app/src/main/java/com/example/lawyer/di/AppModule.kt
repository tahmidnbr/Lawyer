package com.example.lawyer.di


import androidx.room3.Room
import com.example.lawyer.AppPreferences
import com.example.lawyer.logic.repo.CaseRepository
import com.example.lawyer.logic.room.CaseDatabase
import com.example.lawyer.logic.room.HearingDao
import com.example.lawyer.logic.room.LegalCaseDao
import com.example.lawyer.logic.vm.CaseDetailVm
import com.example.lawyer.logic.vm.CaseFormVm
import com.example.lawyer.logic.vm.CaseListVm
import com.example.lawyer.logic.vm.DateVm
import com.example.lawyer.logic.vm.HearingVm
import com.example.lawyer.logic.vm.HomeVm
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            CaseDatabase::class.java,
            "case_database"
        ).build()
    }

    single<LegalCaseDao> { get<CaseDatabase>().legalCaseDao() }
    single<HearingDao>   { get<CaseDatabase>().hearingDao() }

    single { CaseRepository(get(), get()) }

    single { AppPreferences(androidContext()) }

    viewModel { HomeVm(get()) }
    viewModel { CaseListVm(get()) }
    viewModel { CaseFormVm(get(), get()) }
    viewModel { CaseDetailVm(get(), get()) }
    viewModel { HearingVm(get(), get()) }
    viewModel { DateVm(get()) }
}