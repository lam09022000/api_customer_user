package com.esdo.bepilot.Service.Implement;

import com.esdo.bepilot.Exception.CustomException;
import com.esdo.bepilot.Model.Entity.Customer;
import com.esdo.bepilot.Model.Entity.Mission;
import com.esdo.bepilot.Model.Request.MissionRequest;
import com.esdo.bepilot.Model.Response.MissionResponse;
import com.esdo.bepilot.Repository.CustomerRepository;
import com.esdo.bepilot.Repository.MissionRepository;
import com.esdo.bepilot.Service.Mapper.MissionMapper;
import com.esdo.bepilot.Service.MissionService;
import com.esdo.bepilot.Service.Validate.MissionValidate;
import com.esdo.bepilot.Specification.MissionSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MissionServiceImpl implements MissionService {

    @Autowired
    MissionRepository missionRepository;

    @Autowired
    MissionMapper missionMapper;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    MissionValidate missionValidate;


    @Override
    public List<MissionResponse> getListMission(int pageIndex, int pageSize) {
        Pageable paging = PageRequest.of(pageIndex, pageSize);
        Page<Mission> page = missionRepository.findAll(paging);
        List<Mission> missions = page.getContent();
        return missionMapper.mapToListResponse(missions);
    }

    @Override
    public Mission createMission(MissionRequest missionRequest) {
        missionValidate.validate(missionRequest);
        Mission mission = missionMapper.mapToMission(missionRequest);
        return missionRepository.save(mission);
    }


    @Override
    public MissionResponse updateMissionById(Long id, MissionRequest missionRequest) {
        Mission mission = missionMapper.mapToMission(missionRequest);
        mission.setId(id);
        return saveAndReturnResponse(mission);
    }

    @Override
    public List<MissionResponse> searchMission(String name, String communication, int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        Page<Mission> page = missionRepository.findAll(MissionSpecification.filterMission(name, communication), pageable);
        List<Mission> missions = page.getContent();
        return missionMapper.mapToListResponse(missions);
    }

    @Override
    public Customer findCustomerById(Long id) {
        Optional<Customer> opt = customerRepository.findById(id);
        if (opt.isPresent()) {
            return opt.get();
        }
        throw new CustomException("Customer not found");
    }

    @Override
    public List<Mission> findByStatus(String status, Long customerId) {
        List<MissionResponse> mission = missionRepository.findByStatus(status, customerId);
        return missionMapper.map(mission);
    }

    private MissionResponse saveAndReturnResponse(Mission mission) {
        Mission saveMission = missionRepository.save(mission);
        return missionMapper.mapToMissionResponse(saveMission);
    }

    @Override
    public void deleteMissionById(Long id) {
        missionRepository.deleteById(id);
    }

}