package com.example.little_lemon_android

interface Distinations {
    val route : String
}

object Home : Distinations{
    override val route = "Home"
}
object Onboarding : Distinations{
    override val route = "Onboarding"
}
object Profile : Distinations{
    override val route = "Profile"
}