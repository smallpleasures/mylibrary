package com.yrainy.library.server.schedule;

/**
 * @author: zhanglun
 * @since: 1.0.0
 */

import com.yrainy.library.server.pojo.LendInfo;
import com.yrainy.library.server.service.ILendInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 定时任务，处理借阅书籍是否逾期
 */

@Component
public class Schedule {

    @Autowired
    private ILendInfoService lendInfoService;

    @Scheduled(cron="0 0 1 * * *") //每天凌晨 1 点执行一次
    public void updateLendInfoState() {
        List<LendInfo> lendInfos = lendInfoService.query().ne("state", 1).list();
        for (LendInfo lendInfo : lendInfos) {
            if (lendInfo.getRenewDate() != null) {
                if (Duration.between(lendInfo.getRenewDate(), LocalDateTime.now()).toDays() >= 30) {
                    lendInfo.setState(0);
                }
            } else {
                if (Duration.between(lendInfo.getLendDate(), LocalDateTime.now()).toDays() >= 30) {
                    lendInfo.setState(0);
                }
            }
            if (lendInfo.getState() == 0) {
                lendInfoService.update()
                        .eq("id", lendInfo.getId())
                        .set("state", lendInfo.getState()).update();
            }
        }
    }
}
