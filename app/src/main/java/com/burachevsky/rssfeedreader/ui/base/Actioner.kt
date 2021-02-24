package com.burachevsky.rssfeedreader.ui.base

interface Actioner<ACTION> {
    fun submit(action: ACTION)
}