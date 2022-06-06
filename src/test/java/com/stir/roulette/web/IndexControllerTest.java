package com.stir.roulette.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.Charset;
import java.util.Optional;
import java.util.Random;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@WebMvcTest
public class IndexControllerTest {

    @Autowired
    private MockMvc mvc;


    @Test
    public void hello가_리턴된다() throws Exception {
        String hello = "hello";

        mvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string(hello));
    }

    @Test
    public void givenUsingPlainJava_whenGeneratingRandomStringUnbounded_thenCorrect() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();

        System.out.println(generatedString);
    }

    @Test
    public void test2() {
        String username = "이름";
        String result1 = Optional.ofNullable(username).orElse("no name");
        System.out.println(result1);

        String result2 = Optional.ofNullable(username).orElseGet(() -> "no name");
        System.out.println(result2);
    }

    @Test
    public void test3() {
        String username = "이름";
        long beforeTime;
        long afterTime;
        String result1 = "";
        String result2 = "";

        beforeTime = System.currentTimeMillis();
        for(int i=0; i<99999999; i++){
            result1 = Optional.ofNullable(username).orElse("no name");
        }

        afterTime = System.currentTimeMillis();

        System.out.println(result1 + "--" + (afterTime-beforeTime));

        beforeTime = System.currentTimeMillis();
        for(int i=0; i<99999999; i++) {
            result2 = Optional.ofNullable(username).orElseGet(() -> "no name");
        }
        afterTime = System.currentTimeMillis();
        System.out.println(result2 + "--" + (afterTime-beforeTime));
    }

   /* public String getRandomName() {
        System.out.println("getRandomName() method - start");

        Random random = new Random();
        int index = random.nextInt(5);

        System.out.println("getRandomName() method - end");
        return names.get(index);
    }*/
   private String getName() {
       return "stir";
   }



}
