package com.burachevsky.rssfeedreader.ui.base

interface Renderer<STATE, EFFECT> {
    fun renderState(state: STATE)
    fun renderEffect(effect: EFFECT)
}