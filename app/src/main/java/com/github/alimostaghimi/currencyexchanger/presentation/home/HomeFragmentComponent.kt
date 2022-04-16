package com.github.alimostaghimi.currencyexchanger.presentation.home

import com.github.alimostaghimi.currencyexchanger.di.FragmentScope
import com.github.alimostaghimi.currencyexchanger.presentation.application.ApplicationComponent
import dagger.BindsInstance
import dagger.Component

@FragmentScope
@Component(
    modules = [HomeFragmentModule::class],
    dependencies = [ApplicationComponent::class]
)
interface HomeFragmentComponent {
    fun inject(fragment: HomeFragment)

    @Component.Builder
    interface Builder {
        fun build(): HomeFragmentComponent

        @BindsInstance
        fun fragment(fragment: HomeFragment): Builder

        fun applicationComponent(applicationComponent: ApplicationComponent): Builder
    }
}