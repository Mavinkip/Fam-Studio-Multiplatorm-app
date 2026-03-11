package com.famstudio.app.domain.repository

import com.famstudio.app.domain.model.Artwork
import kotlinx.coroutines.flow.Flow

interface ArtworkRepository {
    fun getArtworkFeed(lastId: String? = null, limit: Int = 20): Flow<List<Artwork>>
    fun getArtworksByArtist(artistId: String): Flow<List<Artwork>>
    fun getSavedArtworks(userId: String): Flow<List<Artwork>>
    suspend fun getArtworkById(id: String): Result<Artwork>
    suspend fun uploadArtwork(artwork: Artwork, imageBytes: ByteArray): Result<Artwork>
    suspend fun updateArtwork(artwork: Artwork): Result<Artwork>
    suspend fun deleteArtwork(artworkId: String): Result<Unit>
    suspend fun saveArtwork(userId: String, artworkId: String): Result<Unit>
    suspend fun unsaveArtwork(userId: String, artworkId: String): Result<Unit>
    suspend fun incrementViewCount(artworkId: String)
    suspend fun getDownloadUrl(artworkId: String): Result<String>
}
