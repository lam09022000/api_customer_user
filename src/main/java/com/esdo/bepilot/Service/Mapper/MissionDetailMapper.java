package com.esdo.bepilot.Service.Mapper;

import com.esdo.bepilot.Model.Entity.MissionDetail;
import com.esdo.bepilot.Model.Response.MissionDetailResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MissionDetailMapper {

    public List<MissionDetailResponse> mapToListResponse(List<MissionDetail> missions) {
        List<MissionDetailResponse> missionDetailResponses = new ArrayList<>();
        missions.forEach(missionDetail -> missionDetailResponses.add(mapToMissionDetailResponse(missionDetail)));
        return missionDetailResponses;
    }

    public MissionDetailResponse mapToMissionDetailResponse(MissionDetail missionDetail) {
        MissionDetailResponse missionDetailResponse = new MissionDetailResponse();
        missionDetailResponse.setId(missionDetail.getId());
        missionDetailResponse.setMissionId(missionDetail.getMissionId().getId());
        missionDetailResponse.setUserId(missionDetail.getUserId().getId());
        missionDetailResponse.setStatus(missionDetail.getStatus());
        return missionDetailResponse;
    }


}
