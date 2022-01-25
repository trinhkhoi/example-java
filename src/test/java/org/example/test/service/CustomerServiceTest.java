package org.example.test.service;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.request.SearchCustomerFollowRequest;
import org.example.dto.response.CustomerPage;
import org.example.entity.CustomerFriend;
import org.example.repository.CustomerFriendRepository;
import org.example.service.CustomerService;
import org.example.test.GlobalTestCase;
import org.example.util.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.common.exception.BusinessException;
import org.common.jpa.SearchInput;
import org.common.security.AppContext;
import org.common.security.CustomerAuthentication;
import org.common.security.RequestContextHolder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockStatic;

/**
 * Author: khoitd
 * Date: 2021-04-17 16:11
 * Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class CustomerServiceTest extends GlobalTestCase {
    private static MockedStatic<AppContext> appContextMockedStatic;
    // TODO: 7/16/21 consider to use @Before @After to setup/clean data after running tests
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerFriendRepository customerFriendRepository;
    private final SearchCustomerFollowRequest searchInputRequest = new SearchCustomerFollowRequest();
    private final SearchInput searchInput = new SearchInput();

    @Test
    public void empty() {
    }

    // @Test
    @DatabaseSetup(value = "/datasets/init_data_for_customer.xml", type = DatabaseOperation.CLEAN_INSERT)
    public void test_save_customer_follow() throws Exception {
        appContextMockedStatic = mockStatic(AppContext.class);
        appContextMockedStatic.when(AppContext::getRequestContextHolder).thenReturn(RequestContextHolder.builder()
                .authentication(CustomerAuthentication.builder()
                        .cif("cif_test_1")
                        .userId(1L)
                        .build())
                .build());

        log.info("test_save_customer_follow");
        Long idFriend = 5L;
        customerService.saveCustomerFollow(idFriend);
        CustomerFriend customerFriend = customerFriendRepository.findByIdCustomerAndIdFriend(1L, idFriend);
        assertThat(customerFriend).isNotNull();

        appContextMockedStatic.close();
    }

    // @Test
    @DatabaseSetup(value = "/datasets/init_data_for_customer.xml", type = DatabaseOperation.CLEAN_INSERT)
    public void test_save_customer_follow_existed() throws Exception {
        appContextMockedStatic = mockStatic(AppContext.class);
        appContextMockedStatic.when(AppContext::getRequestContextHolder).thenReturn(RequestContextHolder.builder()
                .authentication(CustomerAuthentication.builder()
                        .cif("cif_test_1")
                        .userId(1L)
                        .build())
                .build());

        Long idFriend = 6L;
        customerService.saveCustomerFollow(idFriend);
        CustomerFriend customerFriend = customerFriendRepository.findByIdCustomerAndIdFriend(1L, idFriend);
        assertThat(customerFriend).isNotNull();

        appContextMockedStatic.close();
    }
    // @Test
    @DatabaseSetup(value = "/datasets/init_data_for_customer.xml", type = DatabaseOperation.CLEAN_INSERT)
    public void test_save_customer_follow_itself_fail() throws Exception {
        appContextMockedStatic = mockStatic(AppContext.class);
        appContextMockedStatic.when(AppContext::getRequestContextHolder).thenReturn(RequestContextHolder.builder()
                .authentication(CustomerAuthentication.builder()
                        .cif("cif_test_1")
                        .userId(1L)
                        .build())
                .build());

        Long idFriend = 1L;
        try {
            customerService.saveCustomerFollow(idFriend);
        } catch (Exception ex) {
            assertThat(ex instanceof BusinessException).isTrue();
            assertThat(((BusinessException) ex).getMessageCode()).isEqualTo(Message.Error.ERROR_FOLLOW_ITSELF);
        }
        CustomerFriend customerFriend = customerFriendRepository.findByIdCustomerAndIdFriend(1L, idFriend);
        assertThat(customerFriend).isNull();

        appContextMockedStatic.close();
    }

    // @Test
    @DatabaseSetup(value = "/datasets/init_data_for_customer.xml", type = DatabaseOperation.CLEAN_INSERT)
    public void test_get_following() throws Exception {
        appContextMockedStatic = mockStatic(AppContext.class);
        appContextMockedStatic.when(AppContext::getRequestContextHolder).thenReturn(RequestContextHolder.builder()
                .authentication(CustomerAuthentication.builder()
                        .cif("cif_test_1")
                        .userId(1L)
                        .build())
                .build());

        searchInputRequest.setIdCustomer(1L);
        CustomerPage customerPage = customerService.getFollowing(searchInputRequest);
        assertThat(customerPage.getData().size()).isEqualTo(4);
        assertThat(customerPage.getData().get(0).getCif()).isEqualTo("cif_test_2");
        assertThat(customerPage.getData().get(1).getCif()).isEqualTo("cif_test_4");
        assertThat(customerPage.getData().get(2).getCif()).isEqualTo("cif_test_6");
        assertThat(customerPage.getData().get(3).getCif()).isEqualTo("cif_test_7");

        // assert that is followed is true
        assertThat(customerPage.getData().get(0).getIsFollowed()).isTrue();
        assertThat(customerPage.getData().get(1).getIsFollowed()).isTrue();
        assertThat(customerPage.getData().get(2).getIsFollowed()).isTrue();
        assertThat(customerPage.getData().get(3).getIsFollowed()).isTrue();

        appContextMockedStatic.close();
    }


    // @Test
    @DatabaseSetup(value = "/datasets/init_data_for_customer.xml", type = DatabaseOperation.CLEAN_INSERT)
    public void test_get_follower_with_zero_follower() throws Exception {
        appContextMockedStatic = mockStatic(AppContext.class);
        appContextMockedStatic.when(AppContext::getRequestContextHolder).thenReturn(RequestContextHolder.builder()
                .authentication(CustomerAuthentication.builder()
                        .cif("cif_test_1")
                        .userId(1L)
                        .build())
                .build());

        searchInputRequest.setIdCustomer(1L);
        CustomerPage customerPage = customerService.getFollower(searchInputRequest);
        assertThat(customerPage.getData().size()).isEqualTo(0);

        appContextMockedStatic.close();
    }

    // @Test
    @DatabaseSetup(value = "/datasets/init_data_for_customer.xml", type = DatabaseOperation.CLEAN_INSERT)
    public void test_get_follower_with_two_followers() throws Exception {
        appContextMockedStatic = mockStatic(AppContext.class);
        appContextMockedStatic.when(AppContext::getRequestContextHolder).thenReturn(RequestContextHolder.builder()
                .authentication(CustomerAuthentication.builder()
                        .cif("cif_test_1")
                        .userId(6L)
                        .build())
                .build());

        searchInputRequest.setIdCustomer(6L);
        CustomerPage customerPage = customerService.getFollower(searchInputRequest);
        assertThat(customerPage.getData().size()).isEqualTo(2);
        assertThat(customerPage.getData().get(0).getCif()).isEqualTo("cif_test_1");
        assertThat(customerPage.getData().get(1).getCif()).isEqualTo("cif_test_2");

        // assert that is followed is true and false`
        assertThat(customerPage.getData().get(0).getIsFollowed()).isFalse();
        assertThat(customerPage.getData().get(1).getIsFollowed()).isTrue();

        appContextMockedStatic.close();
    }

    // @Test
    @DatabaseSetup(value = "/datasets/init_data_for_customer.xml", type = DatabaseOperation.CLEAN_INSERT)
    public void test_un_follow_other_sucess() throws Exception {
        Long idCustomer = 1L;
        Long idFriend = 7L;

        appContextMockedStatic = mockStatic(AppContext.class);
        appContextMockedStatic.when(AppContext::getRequestContextHolder).thenReturn(RequestContextHolder.builder()
                .authentication(CustomerAuthentication.builder()
                        .cif("cif_test_1")
                        .userId(idCustomer)
                        .build())
                .build());

        List<Long> customerFriends = customerFriendRepository.findByIdCustomer(idCustomer);
        assertThat(customerFriends.size()).isEqualTo(4);
        // unfollow one following
        customerService.unfollowFriend(idFriend);
        customerFriends = customerFriendRepository.findByIdCustomer(idCustomer);
        assertThat(customerFriends.size()).isEqualTo(3);

        appContextMockedStatic.close();
    }

    // @Test
    @DatabaseSetup(value = "/datasets/init_data_for_customer.xml", type = DatabaseOperation.CLEAN_INSERT)
    public void test_get_suggested_friends() throws Exception {
        String cif = "cif_test_6";

        appContextMockedStatic = mockStatic(AppContext.class);
        appContextMockedStatic.when(AppContext::getRequestContextHolder).thenReturn(RequestContextHolder.builder()
                .authentication(CustomerAuthentication.builder()
                        .cif(cif)
                        .userId(6L)
                        .build())
                .build());

        searchInputRequest.setIdCustomer(6L);
        CustomerPage customerPage = customerService.getSuggestedFriends(new SearchInput());
        assertThat(customerPage.getData().size()).isEqualTo(1);
        assertThat(customerPage.getData().get(0).getCif()).isEqualTo("cif_test_3");

        appContextMockedStatic.close();
    }
}
