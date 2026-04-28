package com.example.birdy.data

/**
 * App configuration — mirrors iOS BirdyKit/Config.swift
 */
object Config {
    // const val API_BASE_URL = "http://10.0.2.2:3030"           // Android Emulator → Local
    const val API_BASE_URL = "https://tcdlm857gf.execute-api.us-east-1.amazonaws.com/dev"  // AWS Development

    // Stripe — matches iOS BirdyKit/Config.swift
    const val STRIPE_PUBLISHABLE_KEY = "pk_live_51SFypI0MYmEMIsHRC9TByy2moodTCLKiIQJi4rR8fBbh57vTStvOTpHiFGsMsQ6B4GRYMW6RnvEzx2JPGpL4tfDi003HADHoC0"
}
