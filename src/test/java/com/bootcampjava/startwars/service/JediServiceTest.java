package com.bootcampjava.startwars.service;

import com.bootcampjava.startwars.model.Jedi;
import com.bootcampjava.startwars.repository.JediRepositoryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class JediServiceTest {

    @Autowired
    private JediService jediService;

    @MockBean
    private JediRepositoryImpl jediRepository;


    @Test
    @DisplayName("Should return Jedi with success")
    public void testFindByIdSuccess() {

        // cenario
        Jedi mockJedi = new Jedi(1, "Jedi Name", 10, 1);
        Mockito.doReturn(Optional.of(mockJedi)).when(jediRepository).findById(1);

        // execucao
        Optional<Jedi> returnedJedi  = jediService.findById(1);

        // assert
        Assertions.assertTrue(returnedJedi.isPresent(), "Jedi was not found");
        Assertions.assertSame(returnedJedi.get(), mockJedi, "Jedis must be the same");
    }

    // TODO: Criar teste de erro NOT FOUND
    @Test
    @DisplayName("Should return Jedi with error")
    public void testFindByIdError() {

        Jedi mockJedi = new Jedi(1, "Jedi Name", 10, 1);
        Mockito.doReturn(Optional.of(mockJedi)).when(jediRepository).findById(1);

        Optional<Jedi> returnedJedi  = jediService.findById(2);

        Assertions.assertFalse(returnedJedi.isPresent(), "Jedi was found");
    }

    // TODO: Criar um teste pro findAll();
    @Test
    @DisplayName("Should return Jedis with sucess")
    public void testeFindAllJedisSucess(){

        List<Jedi> mockJedis = new ArrayList<>();
        mockJedis.add(new Jedi(1, "Jedi Name", 10, 1));
        mockJedis.add(new Jedi(2, "Jedi Name2", 10, 1));
        Mockito.doReturn(mockJedis).when(jediRepository).findAll();

        List<Jedi> returnedJedi  = jediService.findAll();

        Assertions.assertTrue(returnedJedi.containsAll(mockJedis), "Jedis were not found");

    }
}
