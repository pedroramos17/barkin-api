package com.barkin.BarkinAPI.services;

import com.barkin.BarkinAPI.entities.Gateway;
import com.barkin.BarkinAPI.repositories.GatewayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GatewayService {

    @Autowired
    GatewayRepository gatewayRepository;

    public Gateway insertGateway(Gateway gateway) {
        return gatewayRepository.save(gateway);
    }
    public Gateway updateGateway(UUID gatewayId, Gateway updatedGateway) {
        Optional<Gateway> gateway = gatewayRepository.findById(gatewayId);

        if (gateway.isPresent()) {
            Gateway tempGateway = gateway.get();
            tempGateway.setInputDate(updatedGateway.getInputDate());
            tempGateway.setOutputDate(updatedGateway.getOutputDate());
            tempGateway.setPermanence(updatedGateway.isPermanence());
            tempGateway.setDriver(updatedGateway.getDriver());

            return gatewayRepository.save(tempGateway);
        }

        return null;
    }
    public  boolean deleteGateway(UUID gatewayId) {
        Optional<Gateway> gateway = gatewayRepository.findById(gatewayId);

        if(gateway.isPresent()) {
            gatewayRepository.delete(gateway.get());
            return true;
        }

        return false;
    }

    public Gateway getGatewayById(UUID gatewayId) {
        Optional<Gateway> gateway = gatewayRepository.findById(gatewayId);

        return gateway.orElse(null);

    }

    public List<Gateway> listGateways(int page, int size) {
        return  gatewayRepository.findAll(PageRequest.of(page, size, Sort.by("id").ascending())).toList();
    }
}
