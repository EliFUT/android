package com.elifut.widget

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import android.widget.LinearLayout

/** A LinearLayout that can respond to CoordinatorLayout behavior */
@CoordinatorLayout.DefaultBehavior(MoveUpBehavior::class)
class CoordinatorAwareLinearLayout(ctx: Context, attrs: AttributeSet) : LinearLayout(ctx, attrs)