package org.javatribe.lottery.controller;

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
    public Result getPrizes() {
        List<Prize> prizes = prizeService.selectPrizes();
        return Result.success(prizes);
    }
}
