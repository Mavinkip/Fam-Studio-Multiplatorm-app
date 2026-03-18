package com.famstudio.app.presentation.screens.order

data class ArtistInfo(
    val id:        String,
    val name:      String,
    val avatar:    String,
    val bio:       String,
    val location:  String,
    val email:     String,
    val instagram: String,
    val phone:     String
)

val FAKE_ARTISTS_PUBLIC = mapOf(
    "a1" to ArtistInfo("a1", "Amara Osei",  "https://i.pravatar.cc/150?img=1",
        "Contemporary African artist based in Nairobi. Works in acrylic, oil and mixed media.",
        "Nairobi, Kenya", "amara@famstudio.app", "@amara_art", "+254 712 345 678"),
    "a2" to ArtistInfo("a2", "Zuri Mwangi", "https://i.pravatar.cc/150?img=2",
        "Watercolour specialist inspired by the Great Rift Valley landscapes.",
        "Nakuru, Kenya", "zuri@famstudio.app", "@zuri_creates", "+254 723 456 789"),
    "a3" to ArtistInfo("a3", "Kofi Asante", "https://i.pravatar.cc/150?img=3",
        "Oil painter known for wide panoramic landscapes of the African savanna.",
        "Accra, Ghana", "kofi@famstudio.app", "@kofi_canvas", "+233 244 567 890"),
)