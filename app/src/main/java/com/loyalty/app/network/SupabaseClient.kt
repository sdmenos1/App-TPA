package com.loyalty.app.network

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseClient {
    private const val SUPABASE_URL = "https://wuglirxheotcjylihppe.supabase.co"
    private const val SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Ind1Z2xpcnhoZW90Y2p5bGlocHBlIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzczMzczNDgsImV4cCI6MjA5MjkxMzM0OH0.JdK4rI6zVNFhkcdQYDoUBeNx1BAlxRp3MflRMEbCr-0"

    val client = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_ANON_KEY
    ) {
        install(Auth)
        install(Postgrest)
    }
}
