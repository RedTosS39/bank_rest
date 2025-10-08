//package com.example.bankcards.controller;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/cards")
//@ResponseBody
//public class CardController {
//
//
//
//
//    @GetMapping("/{id}")
//    public ResponseEntity<HttpStatus> showCards(@PathVariable("id") int userId) {
//        //TODO
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
//
//
//    //Только пользователь может смотреть
//    @GetMapping("/{id}/balance")
//    public ResponseEntity<HttpStatus> showBalance(@PathVariable("id") Long cardId) {
//        //TODO
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
//
//}
