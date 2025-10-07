package com.example.bankcards.controller;

import com.example.bankcards.service.TransferMoneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@ResponseBody
@RequestMapping("/transfer")
public class TransferMoneyController {

    private final TransferMoneyService transferMoneyService;

    @Autowired
    public TransferMoneyController(TransferMoneyService transferMoneyService) {
        this.transferMoneyService = transferMoneyService;
    }

    @PostMapping()
    public ResponseEntity<HttpStatus> transactionBetweenCards(@RequestParam("cardIdFrom") Long cardIdFrom,
                                                              @RequestParam("cardIdTo") Long cardIdTo,
                                                              @RequestParam("balance") BigDecimal amount) {
        transferMoneyService.transferMoney(cardIdFrom, cardIdTo, amount);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
