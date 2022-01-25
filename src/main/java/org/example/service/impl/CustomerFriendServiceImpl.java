package org.example.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.response.CustomerPage;
import org.example.entity.CustomerFriend;
import org.example.repository.CustomerFriendRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.common.dto.kafka.FollowMessage;
import org.common.exception.BusinessException;
import org.common.helper.kafka.PineKafkaProperties;
import org.common.jpa.SearchInput;
import org.example.service.CustomerFriendService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.example.util.Message.Error.ERROR_UNFOLLOW_OTHER;

@Slf4j
@Service
public class CustomerFriendServiceImpl extends BaseServiceImpl implements CustomerFriendService {

    private static Logger logger = LoggerFactory.getLogger(CustomerFriendServiceImpl.class);

    @Autowired
    private CustomerFriendRepository customerFriendRepository;
    @Autowired
    private KafkaTemplate<String, FollowMessage> followKafkaTemplate;


    @Override
    public List<Long> getListIdCustomerFollowing(Long idCustomer) throws BusinessException {
        try {
            return customerFriendRepository.findByIdCustomer(idCustomer);
        } catch (Exception ex) {
            logger.error("Error during: getMapAllFollowing: " + idCustomer);
        }
        return new ArrayList<>();
    }

    @Override
    public List<Long> getListFollowingByListFriends(Long idCustomer, List<Long> idFriends) throws BusinessException {
        try {
            return customerFriendRepository.findByIdCustomerAndListFriends(idCustomer, idFriends);
        } catch (Exception ex) {
            logger.error("Error during: getMapAllFollowing: " + idCustomer);
        }
        return new ArrayList<>();
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void saveCustomerFollow(Long idCustomer, Long idFriend, String source) throws BusinessException {
        try {
            CustomerFriend existedCustomerFriend = customerFriendRepository.findByIdCustomerAndIdFriend(idCustomer, idFriend);
            if (existedCustomerFriend == null) {
                existedCustomerFriend = CustomerFriend.builder()
                        .idCustomer(idCustomer)
                        .idFriend(idFriend)
                        .source(source)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();
                customerFriendRepository.save(existedCustomerFriend);

                try {
                    followKafkaTemplate.send(PineKafkaProperties.TOPIC_FOLLOW, FollowMessage.builder()
                            .currentCustomerId(idCustomer)
                            .followedCustomerId(idFriend)
                            .follow(true)
                            .timestamp(System.currentTimeMillis())
                            .build());
                } catch(Exception ex) {
                    //TODO:
                }
            }
        } catch (Exception ex) {
            logger.error("Error during: " + idCustomer + " follow other customer: " + idFriend);
            throw new BusinessException(HttpStatus.BAD_REQUEST, ERROR_UNFOLLOW_OTHER);
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean unfollowFriend(Long idCustomer, Long idFriend) throws BusinessException {
        try {
            CustomerFriend existedCustomerFriend = customerFriendRepository.findByIdCustomerAndIdFriend(idCustomer, idFriend);
            if (existedCustomerFriend != null) {
                existedCustomerFriend.setDeletedAt(LocalDateTime.now());
                customerFriendRepository.save(existedCustomerFriend);

                try {
                    followKafkaTemplate.send(PineKafkaProperties.TOPIC_FOLLOW, FollowMessage.builder()
                            .currentCustomerId(idCustomer)
                            .followedCustomerId(idFriend)
                            .follow(false)
                            .timestamp(System.currentTimeMillis())
                            .build());
                } catch(Exception ex) {
                    //TODO:
                }

                return true;
            }
            return false;
        } catch (Exception ex) {
            logger.error("Error during: " + idCustomer + " unfollow other customer: " + idFriend);
            throw new BusinessException(HttpStatus.BAD_REQUEST, ERROR_UNFOLLOW_OTHER);
        }
    }

    @Override
    public List<CustomerFriend> findFriendListByCustomerId(Long idCustomer) {
        return customerFriendRepository.findFriendListByCustomerId(idCustomer);
    }

    @Override
    public CustomerPage getSuggestedCustomer(List<Long> idFriends, Long idCustomer, SearchInput searchInput) {
        return customerFriendRepository.getSuggestedCustomer(idFriends, idCustomer, searchInput);
    }

    @Override
    public int countFriendRelationShip(Long idCustomer, Long idFriend) {
        return customerFriendRepository.countFriendRelationShip(idCustomer, idFriend);
    }
}
