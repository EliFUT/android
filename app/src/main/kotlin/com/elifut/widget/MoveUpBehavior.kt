package com.elifut.widget

import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.view.View

/**
 * http://alisonhuang-blog.logdown.com/posts/290009-design-support-library-coordinator-layout-and-behavior
 */
class MoveUpBehavior : CoordinatorLayout.Behavior<View>() {
  override fun layoutDependsOn(parent: CoordinatorLayout, child: View, dependency: View) =
      dependency is Snackbar.SnackbarLayout

  override fun onDependentViewChanged(parent: CoordinatorLayout, child: View, dep: View): Boolean {
    child.translationY = Math.min(0f, dep.translationY - dep.height)
    return true
  }
}