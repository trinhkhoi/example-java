package org.example.controller;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiOperation;
import org.example.dto.response.*;
import org.example.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.common.dto.response.ResponseDTO;
import org.common.dto.response.ResponseDTOCursor;
import org.common.exception.BusinessException;
import org.common.jpa.SearchInput;
import org.common.security.Secure;
import org.example.dto.StockCodeSuggestionResponse;
import org.example.dto.TopWatchingStockDTO;
import org.example.dto.request.CreateUpdateWatchlistRequest;
import org.example.dto.request.SearchCustomerFollowRequest;
import org.example.dto.request.TopicRequest;
import org.pist.dto.response.*;
import org.example.entity.Watchlist;
import org.pist.service.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(value = "/private", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Secure({Secure.ROLE_CUSTOMER})
public class PrivateController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private WatchlistService watchlistService;
    @Autowired
    private TopicService topicService;
    @Autowired
    private CustomerTopicService customerTopicService;
    @Autowired
    private CustomerThemeService customerThemeService;
    @Autowired
    private ThemeService themeService;

    @ApiOperation(value = "API update name and status share watchlist")
    @PostMapping("/watchlist/{watchlistId}/update")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseDTO<Watchlist> updateWatchlist(@PathVariable Long watchlistId, @RequestBody CreateUpdateWatchlistRequest request) throws BusinessException {
        return ResponseDTO.<Watchlist>builder()
                .data(watchlistService.updateWatchlist(watchlistId, request))
                .build();
    }

    @ApiOperation(value = "API create new watchlist")
    @PostMapping("/watchlist/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDTO<Watchlist> createWatchlist(@RequestBody CreateUpdateWatchlistRequest request) throws BusinessException {
        return ResponseDTO.<Watchlist>builder()
                .data(watchlistService.createWatchlist(request))
                .build();
    }

    @ApiOperation(value = "API get watchlist from himself")
    @GetMapping(value = "/watchlist/list")
    @ResponseStatus(OK)
    public ResponseDTO<List<WatchlistResponse>> getWatchlistList() throws BusinessException {
        return ResponseDTO.<List<WatchlistResponse>>builder()
                .data(watchlistService.getWatchlistList())
                .build();
    }

    @ApiOperation(value = "API get watchlist from other customer")
    @GetMapping(value = "/watchlist/list/{idCustomer}")
    @ResponseStatus(OK)
    public ResponseDTO<List<WatchlistResponse>> getListWatchlistOtherCustomer(@PathVariable Long idCustomer) throws BusinessException {
        return ResponseDTO.<List<WatchlistResponse>>builder()
                .data(watchlistService.getWatchlistListOtherCustomer(idCustomer))
                .build();
    }

    @ApiOperation(value = "API add remove stock to watchlist")
    @PutMapping("/watchlist/stock/{stockCode}/remove")
    @ResponseStatus(NO_CONTENT)
    public void removeStockFromWatchlist(@PathVariable String stockCode) throws BusinessException {
        watchlistService.removeStockFromWatchlist(stockCode);
    }

    @GetMapping(value = "/watchlist/top-watching")
    @ResponseStatus(OK)
    public ResponseDTOCursor<List<TopWatchingStockDTO>> getTopWatchingStock(
            @RequestParam(required = false, defaultValue = "5") int limit) throws BusinessException {
        return watchlistService.getTopWatchingStock("", limit);
    }

    @GetMapping(value = "/watchlist/top-watching/more")
    @ResponseStatus(OK)
    public ResponseDTOCursor<List<TopWatchingStockDTO>> getTopWatchingStockMore(
            @RequestParam(required = false, defaultValue = "") String cursor,
            @RequestParam(required = false, defaultValue = "5") int limit) throws BusinessException {
        return watchlistService.getTopWatchingStock(cursor, limit);
    }

    @ApiOperation(value = "API get stock code suggestion")
    @GetMapping(value = "/customer/watchlist/suggested/stockcodes")
    @ResponseStatus(OK)
    public ResponseDTO<List<StockCodeSuggestionResponse>> getWatchlistRecommendation(@RequestParam(required = false, defaultValue = "10") int limit) throws BusinessException {
        return ResponseDTO.<List<StockCodeSuggestionResponse>>builder()
                .data(watchlistService.getWatchlistRecommendation(limit))
                .build();
    }

    @ApiOperation(value = "Get Community section in Theme Detail screen")
    @GetMapping("/theme/{code}/subscribed-customers")
    @ResponseStatus(OK)
    public ResponseDTO<Page<CustomerInfo>> getSubscribedCustomersOfTheme(@PathVariable String code, @RequestParam Integer page, @RequestParam Integer pageSize) throws BusinessException {
        return ResponseDTO.<Page<CustomerInfo>>builder()
                .data(customerThemeService.getWatchingCustomers(code, page, pageSize))
                .build();
    }

    @ApiOperation(value = "API to save customer subscribe which theme")
    @PostMapping("/theme/subscribe")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDTO<List<ThemeDto>> saveCustomerSubscribeTheme(@RequestParam String themeCodes) throws BusinessException {
        return ResponseDTO.<List<ThemeDto>>builder()
                .data(customerThemeService.subscribeTheme(themeCodes))
                .build();
    }

    @ApiOperation(value = "API to make unsubscribe theme of one user")
    @PutMapping("/theme/unsubscribe")
    @ResponseStatus(OK)
    public ResponseDTO<List<ThemeDto>> saveCustomerUnsubscribeTheme(@RequestParam String themeCodes) throws BusinessException {
        return ResponseDTO.<List<ThemeDto>>builder()
                .data(customerThemeService.unsubscribeTheme(themeCodes))
                .build();
    }

    @ApiOperation(value = "API get list all themes")
    @GetMapping("/themes")
    @ResponseStatus(OK)
    public ResponseDTO<List<ThemeDto>> getAllActiveThemes() throws BusinessException {
        return ResponseDTO.<List<ThemeDto>>builder()
                .data(customerThemeService.getAllActiveThemes())
                .build();
    }

    @ApiOperation(value = "API get theme details")
    @GetMapping("/theme/{code}/details")
    @ResponseStatus(OK)
    public ResponseDTO<ThemeDto> getThemeDetails(@PathVariable String code) throws BusinessException {
        return ResponseDTO.<ThemeDto>builder()
                .data(customerThemeService.getThemeDetails(code))
                .build();
    }

    @ApiOperation(value = "API get list all customers who were subscribe theme")
    @GetMapping(value = "/theme/{themeCode}/subscribed-customers-full")
    @ResponseStatus(OK)
    public ResponseDTOCursor<List<CustomerThemeDto>> getList(
            @PathVariable String themeCode,
            @RequestParam(required = false) String cursor,
            @RequestParam(required = false, defaultValue = "20") Integer limit) throws BusinessException {
        return customerThemeService.getCustomerSubscribeTheme(themeCode, cursor, limit);
    }

    @ApiOperation(value = "API get list all themes which contain stock code")
    @GetMapping(value = "/theme/stock-code/{stockCode}")
    @ResponseStatus(OK)
    public ResponseDTO<List<ThemeDto>> getListThemeByStockCode(@PathVariable String stockCode) throws BusinessException {
        return ResponseDTO.<List<ThemeDto>>builder()
                .data(themeService.getThemesByStockCode(stockCode))
                .build();
    }

    @ApiOperation(value = "API get all themes which customer already subscribed")
    @GetMapping(value = "/subscribed/themes")
    @ResponseStatus(OK)
    public ResponseDTO<List<ThemeDto>> getListSubscribedTheme() throws BusinessException {
        return ResponseDTO.<List<ThemeDto>>builder()
                .data(customerThemeService.getSubscribedTheme())
                .build();
    }


    @ApiOperation(value = "API save customer follow someone")
    @PostMapping("/customer/follow")
    @ResponseStatus(OK)
    public void createCustomerFollow(Long idFriend) throws BusinessException {
        customerService.saveCustomerFollow(idFriend);
    }

    @ApiOperation(value = "API get list suggested friends", response = CustomerPage.class)
    @GetMapping("/customer/suggested/friends")
    @ResponseStatus(OK)
    public CustomerPage getSuggestedFriends(SearchInput searchInput) throws BusinessException {
        return new CustomerPage(0L, searchInput.getPage(), searchInput.getPageSize(), Lists.newArrayList());
         // return customerService.getSuggestedFriends(searchInput);
    }

    @ApiOperation(value = "API get list following friend", response = CustomerPage.class)
    @GetMapping("/customer/following/friends")
    @ResponseStatus(OK)
    public CustomerPage getListFollowing(SearchCustomerFollowRequest request) throws BusinessException {
        return customerService.getFollowing(request);
    }

    @ApiOperation(value = "API get list follower", response = CustomerPage.class)
    @GetMapping("/customer/follower/friends")
    @ResponseStatus(OK)
    public CustomerPage getListFollower(SearchCustomerFollowRequest request) throws BusinessException {
        return customerService.getFollower(request);
    }

    @ApiOperation(value = "API get list following from other customer", response = CustomerPage.class)
    @GetMapping("/customer/following/other")
    @ResponseStatus(OK)
    public CustomerPage getListFollowingFromOther(SearchCustomerFollowRequest request) throws BusinessException {
        return customerService.getFollowingFromOther(request);
    }

    @ApiOperation(value = "API get list follower from other customer", response = CustomerPage.class)
    @GetMapping("/customer/follower/other")
    @ResponseStatus(OK)
    public CustomerPage getListFollowerFromOther(SearchCustomerFollowRequest request) throws BusinessException {
        return customerService.getFollowerFromOther(request);
    }

    @ApiOperation(value = "API save customer follow someone")
    @PutMapping("/customer/unfollow")
    @ResponseStatus(NO_CONTENT)
    public void unfollowFriend(Long idFriend) throws BusinessException {
        customerService.unfollowFriend(idFriend);
    }

    @ApiOperation(value = "API get list topics")
    @GetMapping(value = "/topic/all")
    @ResponseStatus(OK)
    public ResponseDTO<List<TopicDto>> getTopics(
            @RequestParam(required = false, defaultValue = "5") int limit
    ) throws BusinessException {
        return ResponseDTO.<List<TopicDto>>builder()
                .data(topicService.getListTopics())
                .build();
    }

    @ApiOperation(value = "API selected topics")
    @PostMapping(value = "/topic/selected")
    @ResponseStatus(CREATED)
    public void selectedTopics(@RequestBody TopicRequest request) throws BusinessException {
        customerTopicService.selectedTopics(request);
    }

    @ApiOperation(value = "API selected topics")
    @GetMapping(value = "/topic/selected")
    @ResponseStatus(OK)
    public ResponseDTO<List<CustomerTopicDTO>> selectedTopics() throws BusinessException {
        return ResponseDTO.<List<CustomerTopicDTO>>builder()
                .data(customerTopicService.findAllTopicByCustomerId())
                .build();
    }

    @ApiOperation(value = "API unselected topics")
    @PutMapping(value = "/topic/unselected")
    @ResponseStatus(NO_CONTENT)
    public void unselectedTopics(@RequestBody TopicRequest request) throws BusinessException {
        customerTopicService.unselectedTopics(request);
    }
}