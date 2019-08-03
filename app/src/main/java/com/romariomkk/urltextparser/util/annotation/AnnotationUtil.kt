package com.romariomkk.urltextparser.util.annotation

import androidx.annotation.LayoutRes
import com.romariomkk.urltextparser.view.base.AbsViewModel

object AnnotationUtil {

    @JvmStatic
    fun findViewModelClass(any: Any): Class<out AbsViewModel> {
        return any.javaClass.getAnnotation(RequiresViewModel::class.java).value.java
    }

    @JvmStatic
    @LayoutRes
    fun findViewId(any: Any): Int {
        return if (any.javaClass.isAnnotationPresent(RequiresView::class.java))
            any.javaClass.getAnnotation(RequiresView::class.java).value
        else
            -1
    }

    @JvmStatic
    fun hasViewModel(any: Any) = any.javaClass.isAnnotationPresent(RequiresViewModel::class.java)

}