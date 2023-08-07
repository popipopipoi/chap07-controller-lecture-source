package com.ohgiraffers.section01.controller;

import com.ohgiraffers.section01.model.dto.CategoryDTO;
import com.ohgiraffers.section01.model.dto.MenuDTO;
import com.ohgiraffers.section01.model.dto.OrderDTO;
import com.ohgiraffers.section01.model.dto.OrderMenuDTO;
import com.ohgiraffers.section01.model.service.OrderService;
import com.ohgiraffers.section01.view.ResultView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class OrderController {
    /* Controller의 역할
    * View에 사용자가 입력한 정보를 파라미터 형태로 전달 받으면
    * 전달 받은 값을 검증하거나, 추가적인 정보가 필요한 경우 가공하여 Sevice로 전달하기 위한 인트턴스의 담고
    * 서비스의 비지니스 로직을 담당하는 method를 호출한다.
    * 또한 호출한 수행 결과를 반환 받아 어떠한 뷰를 다시 사용자에게 보여줄 것인지를 결정하는 역할을 한다.
    * */

    private OrderService orderService = new OrderService();

    public List<CategoryDTO> selectAllCategory() {

        List<CategoryDTO> categoryList = orderService.selectAllCategory();

        return categoryList;

    }

    public List<MenuDTO> selectMenuByCategoryCode(int categoryCode) {

        return orderService.selectMenuByCategoryCode(categoryCode);
    }

    public void registOrder(Map<String, Object> requestMap) {
        /* 1. 뷰에서 전달 받은 파라미터 꺼내기 */
        int totalOrderPrice = (Integer) requestMap.get("totalOrderPrice");
        List<OrderMenuDTO> orderMenuList = (List<OrderMenuDTO>) requestMap.get("orderMenuList");

        /* 2. 추가적으로 필요한 값이 있는 경우 생성하기 */
        Date orderTime = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yy/MM/dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String date = dateFormat.format(orderTime);
        String time = timeFormat.format(orderTime);

        /* 3. 서비스로 전달하기 위해 DTO 인스턴스에 담기 */
        OrderDTO order = new OrderDTO();
        order.setDate(date);
        order.setTime(time);
        order.setTotalOrederPrice(totalOrderPrice);
        order.setOrderMenuList(orderMenuList);

        /* 4. 서비스(비즈니스 로직)를 호출하고 결과를 리턴 받기 */
        int result = orderService.registOrder(order);

        /* 5. 서비스 처리 결과를 이용해 성공 실패 여부를 판단하여 사용자에게 보여줄 뷰를 결정함 */
        ResultView resultView = new ResultView();
        if(result > 0) {
            resultView.success();
        } else {
            resultView.failed();
        }



    }
}
