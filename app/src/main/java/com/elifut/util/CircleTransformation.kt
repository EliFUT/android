/*
 * Copyright 2014 Julian Shen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.elifut.util

import android.graphics.*

import com.squareup.picasso.Transformation

class CircleTransform : Transformation {
  override fun transform(source: Bitmap): Bitmap {
    val size = Math.min(source.width, source.height)
    val x = (source.width - size) / 2
    val y = (source.height - size) / 2

    val squaredBitmap = Bitmap.createBitmap(source, x, y, size, size)
    if (squaredBitmap != source) {
      source.recycle()
    }

    val bitmap = Bitmap.createBitmap(size, size, source.config)
    val canvas = Canvas(bitmap)
    val paint = Paint()
    val shader = BitmapShader(squaredBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
    paint.shader = shader
    paint.isAntiAlias = true
    val r = size / 2f
    canvas.drawCircle(r, r, r, paint)
    squaredBitmap.recycle()
    return bitmap
  }

  override fun key(): String {
    return "circle"
  }
}