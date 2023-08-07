package com.ohgiraffers.section01.model.service;

import com.ohgiraffers.section01.model.dao.OrderDAO;
import com.ohgiraffers.section01.model.dto.CategoryDTO;
import com.ohgiraffers.section01.model.dto.MenuDTO;
import com.ohgiraffers.section01.model.dto.OrderDTO;
import com.ohgiraffers.section01.model.dto.OrderMenuDTO;

import java.sql.Connection;
import java.util.List;

import static com.ohgiraffers.common.JDBCTemplate.*;

public class OrderService {

    private OrderDAO orderDAO = new OrderDAO();

    /* 카테고리 전체 조회용 메소드 */
    public List<CategoryDTO> selectAllCategory() {
        /* 1. Connection 생성 */
        Connection con = getConnection();

        /* 2. DAO의 모든 카테고리 조회용 메소드를 호출하여 결과를 리턴 받기 */
        List<CategoryDTO> categoryList = orderDAO.selectAllCategory(con);

        /* 3. Connection 반납 */
        close(con);

        /* 4. 반환 받은 값 리턴 */
        return categoryList;

    }
    /* 카테고리별 메뉴 목록 조회용 메소드 */
    public List<MenuDTO> selectMenuByCategoryCode(int categoryCode) {

        Connection con = getConnection();

        List<MenuDTO> menuList = orderDAO.selectMenuByCategoryCode(con, categoryCode);

        close(con);

        return menuList;
    }

    /* 주문 정보 등록용 메소드 */
    public int registOrder(OrderDTO order) {

        int result = 0;
        Connection con = getConnection();

        /* tbl_order에 데이터를 삽입하는 메소드 호출 */
        int orderResult = orderDAO.insertOrder(con, order);

        /* tbl_order_menu에 데이터를 삽입하는 메소드 호출 */
        int orderMenuResult = 0;
        List<OrderMenuDTO> orderMenuList = order.getOrderMenuList();
        for(OrderMenuDTO orderMenu : orderMenuList) {
            orderMenuResult += orderDAO.insertOrderMenu(con, orderMenu);
        }

        /* 모든 로직이 잘 수행 되었는지 판단하여 트랜잭션 처리 */
        if(orderResult > 0 && orderMenuResult == orderMenuList.size()) {
            result = 1;
            commit(con);
        } else {
            rollback(con);
        }

        close(con);

        return result;
    }
}
