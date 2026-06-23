package com.example.graduation1.data.repository

import com.example.graduation1.data.remote.ApiService
import com.example.graduation1.domain.models.Group
import com.example.graduation1.domain.models.User
import com.example.graduation1.domain.models.requets_response.CommunityMemberItem
import com.example.graduation1.domain.models.requets_response.CommunityMessageResponse
import com.example.graduation1.domain.models.requets_response.CommunityResponse
import com.example.graduation1.domain.models.requets_response.CreateCommunityRequest
import com.example.graduation1.domain.models.requets_response.MembershipResponse
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

class GroupsRepository(private val api : ApiService) {

    suspend fun getCommunities() : List<Group>{
        return api.getCommunities().map {
            Group(
                id = it.communityID.toString(),
                name = it.name,
                image = "",
                members = emptyList()
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