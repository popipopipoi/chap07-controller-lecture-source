package com.ohgiraffers.section01.view;

import com.ohgiraffers.section01.controller.OrderController;
import com.ohgiraffers.section01.model.dto.CategoryDTO;
import com.ohgiraffers.section01.model.dto.MenuDTO;
import com.ohgiraffers.section01.model.dto.OrderDTO;
import com.ohgiraffers.section01.model.dto.OrderMenuDTO;
import com.ohgiraffers.section01.model.service.OrderService;

import java.text.SimpleDateFormat;
import java.util.*;

public class OrderMenu {

    private OrderController orderController = new OrderController();
    public void displayMainMenu() {

        /* ---------- 반복 ------------------------
        * 1. 카테고리 조회
        * 2. 해당 카테고리의 메뉴 조회
        * 3. 사용자에게 어떤 메뉴를 주문 받을 것인지 입력
        * 4. 주문할 수량 입력
        * ----------------------------------------
        * 5. 주문
        * */

        List<OrderMenuDTO> orderMenuList = new ArrayList<>();
        int totalOrderPrice = 0;

        Scanner sc = new Scanner(System.in);

        do{
            System.out.println("======= 음식 주문 프로그램 =======");

            /* 1. 카테고리 조회 후 출력 */
            List<CategoryDTO> categoryList = orderController.selectAllCategory();
            for (CategoryDTO category : categoryList) {
                System.out.println(category.getName());
            }

            System.out.println("==============================");
            System.out.print("주문하실 카테고리를 선택해주세요 :");
            String inputCategory = sc.nextLine();

            int categoryCode = 0;
            for (CategoryDTO category : categoryList) {
                if(category.getName().equals(inputCategory)){
                    categoryCode = category.getCode();
                }
            }

            System.out.println("========== 주문 가능 메뉴 ==========");
            List<MenuDTO> menuList = orderController.selectMenuByCategoryCode(categoryCode);
            for(MenuDTO menu : menuList) {
                System.out.println(menu.getName() + " : " + menu.getPrice() + "원");
            }

            System.out.print("주문하실 메뉴를 선택해주세요 : ");
            String inputmenu = sc.nextLine();

            int menuCode = 0;
            int menuPrice = 0;
            for (MenuDTO menu : menuList) {
                if(menu.getName().equals(inputmenu)) {
                    menuCode = menu.getCode();
                    menuPrice = menu.getPrice();
                }
            }

            System.out.print("주문하실 수량을 입력하세요 : ");
            int orderAmount = sc.nextInt();

            OrderMenuDTO orderMenu = new OrderMenuDTO();
            orderMenu.setMenuCode(menuCode);
            orderMenu.setOrderAmount(orderAmount);

            orderMenuList.add(orderMenu);
            totalOrderPrice += (menuPrice * orderAmount);

            System.out.println("계속 주문 하시겠습니까?(예/아니오) : ");
            sc.nextLine();
            boolean isContinue = sc.nextLine().equals("예");

            if(!isContinue) break;

        } while (true);

        System.out.println("=========== 주문 목록 확인 ===========");
        for (OrderMenuDTO orderMenu : orderMenuList) {
            System.out.println(orderMenu);
        }

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("totalOrderPrice", totalOrderPrice);
        requestMap.put("orderMenuList", orderMenuList);

        /* 주문 내역을 전달하여 데이터베이스에 저장 */
        orderController.registOrder(requestMap);


    }
}
