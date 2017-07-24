package com.elifut.util

import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.PaintDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.view.View

/** Sets a linear gradient as a background of the provided View using the colors array */
fun setLinearBackgroundGradient(colors: IntArray, view: View) {
  val paintDrawable = PaintDrawable().apply {
    shape = RectShape()
    shaderFactory = object : ShapeDrawable.ShaderFactory() {
      override fun resize(width: Int, height: Int): Shader {
        return LinearGradient(0f, 0f, width.toFloat(), height.toFloat(), colors, null,
            Shader.TileMode.CLAMP)
      }
    }
  }
  view.background = LayerDrawable(arrayOf(paintDrawable))
}