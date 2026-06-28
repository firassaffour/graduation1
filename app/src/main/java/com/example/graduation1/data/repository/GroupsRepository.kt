package com.example.graduation1.data.repository

import com.example.graduation1.data.remote.ApiService
import com.example.graduation1.domain.models.Group
import com.example.graduation1.domain.models.User
import com.example.graduation1.domain.models.requets_response.CommunityMessageResponse
import com.example.graduation1.domain.models.requets_response.CommunityResponse
import com.example.graduation1.domain.models.requets_response.CreateCommunityRequest
import com.example.graduation1.domain.models.requets_response.MembershipResponse

class GroupsRepository(private val api : ApiService) {

    private val BASE_URL = "https://graduation-project-backend-production-bc68.up.railway.app"

    suspend fun getCommunities() : List<Group>{
        return api.getCommunities().map {
            Group(
                id = it.communityID.toString(),
                name = it.name,
                image = "$BASE_URL${it.imageUrl}",
                members = emptyList(),
                admin = it.createdBy.toString()
            )
        }
    }

    suspend fun createCommunity(createCommunityRequest: CreateCommunityRequest) : CommunityResponse{
        return api.createCommunity(createCommunityRequest)
    }

    suspend fun joinCommunity(id: Int): CommunityMessageResponse {return api.joinCommunity(id)}

    suspend fun leaveCommunity(id: Int): CommunityMessageResponse {return api.leaveCommunity(id)}

    suspend fun getCommunityMembers(id: Int): List<User>{
        return api.getCommunityMembers(id).map {
            User(
                id = it.userID.toString(),
                name = it.firstName,
                userName = it.lastName
            )
        }
    }

    suspend fun getCommunityMembership(id: Int): MembershipResponse {return api.getCommunityMembership(id)}
}