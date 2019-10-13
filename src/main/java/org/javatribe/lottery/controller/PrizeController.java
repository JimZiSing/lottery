package org.javatribe.lottery.controller;

import org.javatribe.lottery.entity.Page;
import org.javatribe.lottery.entity.Prize;
import org.javatribe.lottery.entity.Result;
import org.javatribe.lottery.service.IPrizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author JimZiSing
 */
@RestController
public class PrizeController {
    @Autowired
    IPrizeService prizeService;

    @GetMapping("/api/prizes")
    public Result getPrizes(int pageNumber,int pageSize) {
        Page<Prize> prizePage = prizeService.selectPrizePage(pageNumber,pageSize);
        return Result.success(prizePage);
    }
}
