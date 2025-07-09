package com.dynamicclock.app.utils

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.core.content.ContextCompat
import com.dynamicclock.app.R
import com.dynamicclock.app.data.TimeOfDay
import com.dynamicclock.app.data.WeatherCondition

class BackgroundManager(private val context: Context) {
    
    private val argbEvaluator = ArgbEvaluator()
    
    fun updateBackground(
        backgroundView: View,
        weatherOverlay: View,
        timeOfDay: TimeOfDay,
        weatherCondition: WeatherCondition
    ) {
        val (startColor, endColor) = getTimeBasedColors(timeOfDay)
        val overlayColor = getWeatherOverlayColor(weatherCondition)
        
        animateBackgroundTransition(backgroundView, startColor, endColor)
        animateWeatherOverlay(weatherOverlay, overlayColor)
    }
    
    private fun getTimeBasedColors(timeOfDay: TimeOfDay): Pair<Int, Int> {
        return when (timeOfDay) {
            TimeOfDay.MORNING -> Pair(
                ContextCompat.getColor(context, R.color.morning_start),
                ContextCompat.getColor(context, R.color.morning_end)
            )
            TimeOfDay.AFTERNOON -> Pair(
                ContextCompat.getColor(context, R.color.afternoon_start),
                ContextCompat.getColor(context, R.color.afternoon_end)
            )
            TimeOfDay.EVENING -> Pair(
                ContextCompat.getColor(context, R.color.evening_start),
                ContextCompat.getColor(context, R.color.evening_end)
            )
            TimeOfDay.NIGHT -> Pair(
                ContextCompat.getColor(context, R.color.night_start),
                ContextCompat.getColor(context, R.color.night_end)
            )
        }
    }
    
    private fun getWeatherOverlayColor(weatherCondition: WeatherCondition): Int {
        return when (weatherCondition) {
            WeatherCondition.SUNNY -> ContextCompat.getColor(context, R.color.sunny_overlay)
            WeatherCondition.RAINY -> ContextCompat.getColor(context, R.color.rainy_overlay)
            WeatherCondition.CLOUDY -> ContextCompat.getColor(context, R.color.cloudy_overlay)
            WeatherCondition.UNKNOWN -> ContextCompat.getColor(context, android.R.color.transparent)
        }
    }
    
    private fun animateBackgroundTransition(view: View, startColor: Int, endColor: Int) {
        val currentDrawable = view.background as? GradientDrawable
        val currentColors = currentDrawable?.let { 
            // Try to get current colors, fallback to defaults
            intArrayOf(startColor, endColor)
        } ?: intArrayOf(startColor, endColor)
        
        val targetGradient = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(startColor, endColor)
        )
        
        if (currentDrawable == null) {
            view.background = targetGradient
            return
        }
        
        val animator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 2000
            addUpdateListener { animation ->
                val fraction = animation.animatedValue as Float
                val animatedStartColor = argbEvaluator.evaluate(
                    fraction, 
                    currentColors[0], 
                    startColor
                ) as Int
                val animatedEndColor = argbEvaluator.evaluate(
                    fraction, 
                    currentColors[1], 
                    endColor
                ) as Int
                
                val animatedGradient = GradientDrawable(
                    GradientDrawable.Orientation.TOP_BOTTOM,
                    intArrayOf(animatedStartColor, animatedEndColor)
                )
                view.background = animatedGradient
            }
        }
        animator.start()
    }
    
    private fun animateWeatherOverlay(view: View, targetColor: Int) {
        val currentColor = (view.background as? GradientDrawable)?.let { 
            // Get current color or use transparent as default
            ContextCompat.getColor(context, android.R.color.transparent)
        } ?: ContextCompat.getColor(context, android.R.color.transparent)
        
        val animator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 1500
            addUpdateListener { animation ->
                val fraction = animation.animatedValue as Float
                val animatedColor = argbEvaluator.evaluate(
                    fraction, 
                    currentColor, 
                    targetColor
                ) as Int
                view.setBackgroundColor(animatedColor)
            }
        }
        animator.start()
    }
}