package com.example.springdemo.service.menuService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.springdemo.dao.entity.menu.Menu;
import com.example.springdemo.dao.repository.MenuRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * 菜单数据初始化器
 */
@Component
@Slf4j
public class MenuInitializer implements CommandLineRunner {

    @Autowired
    private MenuRepository menuRepository;

    @Override
    public void run(String... args) throws Exception {
        log.info("正在检查菜单数据...");
        
        // 检查是否需要初始化数据
        if (menuRepository.count() == 0) {
            log.info("开始初始化菜单数据...");
            initMenus();
            log.info("菜单数据初始化完成!");
        } else {
            log.info("菜单数据已存在，无需初始化");
        }
    }
    
    /**
     * 初始化系统菜单
     */
    private void initMenus() {
        List<Menu> menus = new ArrayList<>();
        
        // 创建管理系统首页菜单
        Menu dashboard = new Menu();
        dashboard.setName("仪表盘");
        dashboard.setUrl("/admin/dashboard");
        dashboard.setIcon("dashboard");
        dashboard.setSort(1);
        dashboard.setType("admin");
        dashboard.setIsActive(true);
        menus.add(dashboard);
        
        Menu home = new Menu();
        home.setName("首页");
        home.setUrl("/admin/index");
        home.setIcon("home");
        home.setSort(2);
        home.setType("admin");
        home.setIsActive(true);
        menus.add(home);
        
        // 用户管理
        Menu userManagement = new Menu();
        userManagement.setName("用户管理");
        userManagement.setUrl("/admin/users");
        userManagement.setIcon("user");
        userManagement.setSort(3);
        userManagement.setType("admin");
        userManagement.setIsActive(true);
        menus.add(userManagement);
        
        // 批处理管理
        Menu batchManagement = new Menu();
        batchManagement.setName("批处理管理");
        batchManagement.setUrl("/admin/batch");
        batchManagement.setIcon("database");
        batchManagement.setSort(4);
        batchManagement.setType("admin");
        batchManagement.setIsActive(true);
        menus.add(batchManagement);
        
        // 保存主菜单
        menuRepository.saveAll(menus);
        
        // 添加批处理子菜单
        Menu batchImport = new Menu();
        batchImport.setName("批量导入");
        batchImport.setUrl("/admin/batch/import");
        batchImport.setIcon("upload");
        batchImport.setSort(1);
        batchImport.setType("admin");
        batchImport.setParentId(batchManagement.getId());
        batchImport.setIsActive(true);
        
        // 监控模块
        Menu monitor = new Menu();
        monitor.setName("监控模块");
        monitor.setUrl("/admin/monitor");
        monitor.setIcon("monitor");
        monitor.setSort(5);
        monitor.setType("admin");
        monitor.setIsActive(true);
        
        // 系统设置
        Menu settings = new Menu();
        settings.setName("系统设置");
        settings.setUrl("/admin/settings");
        settings.setIcon("setting");
        settings.setSort(6);
        settings.setType("admin");
        settings.setIsActive(true);
        
        // 保存子菜单和其他主菜单
        List<Menu> subMenus = new ArrayList<>();
        subMenus.add(batchImport);
        subMenus.add(monitor);
        subMenus.add(settings);
        menuRepository.saveAll(subMenus);
    }
} 