package com.romariomkk.urltextparser.util.annotation

import com.romariomkk.urltextparser.view.base.AbsViewModel
import kotlin.reflect.KClass

@MustBeDocumented
@Target(allowedTargets = [AnnotationTarget.CLASS])
@Retention(value = AnnotationRetention.RUNTIME)
annotation class RequiresViewModel(val value : KClass<out AbsViewModel>)
