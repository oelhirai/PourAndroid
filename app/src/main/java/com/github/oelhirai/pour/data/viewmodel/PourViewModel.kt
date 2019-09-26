package com.github.oelhirai.pour.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.github.oelhirai.pour.data.model.Pour
import com.github.oelhirai.pour.data.model.PourOver

class PourViewModel : ViewModel() {

    private val _isReadyForNextStep = MutableLiveData(false)
    private val _pourIndex = MutableLiveData(-1)
    private val _currentWeightGoal = MutableLiveData(0)
    private val pourover = PourOver(
        arrayListOf(
            Pour(60, 45, 45),
            Pour(60, 30, 30),
            Pour(90, 40, 45),
            Pour(90, 40, 45)
        )
    )

    val isReadyForNextStep: LiveData<Boolean> =
        _isReadyForNextStep
    val currentPourPeriod: LiveData<Long> =
        Transformations.map(_pourIndex) { getCurrentPourPeriod(pourover.pours[it]) }
    val pourToMessage: LiveData<String> =
        Transformations.map(_currentWeightGoal) { getPourToDescription(it) }
    val stepDescription: LiveData<String> =
        Transformations.map(_pourIndex) {
            getStepDescription(it + 1, pourover.numberOfPours, pourover.pours[it])
        }

    init {
        moveToNextPour()
    }

    /**
     * Move on to the next pour.
     */
    fun moveToNextPour() {
        with(_pourIndex.value ?: return) {
            val newIndex = this + 1
            if (hasFinishedPouring(newIndex).not()) {
                _isReadyForNextStep.value = false
                _pourIndex.value = newIndex
                _currentWeightGoal.value = (_currentWeightGoal.value ?: 0 ) + pourover.pours[newIndex].weight
            }
        }
    }

    fun setFinishedCurrentPour() {
        with(_pourIndex.value ?: return) {
            val newIndex = this + 1
            if (hasFinishedPouring(newIndex).not()){
                _isReadyForNextStep.value = true
            }
        }
    }

    fun hasFinishedPouring(currentStep: Int): Boolean {
        return currentStep >= pourover.numberOfPours
    }

    fun getCurrentPourPeriod(pour: Pour): Long {
        return pour.minPeriod * 1000L
    }

    fun getPourToDescription(weightGoal: Int): String {
        return "pour to $weightGoal"
    }

    fun getStepDescription(currentStep: Int, totalSteps: Int, pour: Pour): String {
        return "Pour $currentStep/$totalSteps | ${pour.weight}mg"
    }
}