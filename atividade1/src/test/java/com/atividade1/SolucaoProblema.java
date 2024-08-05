package com.atividade1;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


class Bar{
    public void doSomething(Foo b){
        System.out.println(b.conteudo);
    }
}

class Foo{
    public String conteudo;
}

public class SolucaoProblema{

    private Bar mockBar = mock(Bar.class);

    @Test
    public void teste(){
        Foo foo1 = new Foo();
        foo1.conteudo = "conteudo1";
        Foo foo2 = new Foo();
        foo2.conteudo = "conteudo2";
        mockBar.doSomething(foo1);
        mockBar.doSomething(foo2);

        ArgumentCaptor<Foo> fooCaptor = ArgumentCaptor.forClass(Foo.class);
        verify(mockBar, times(2)).doSomething(fooCaptor.capture());
        List<Foo> capturedFoos = fooCaptor.getAllValues();
        assertEquals("conteudo1", capturedFoos.get(0).conteudo);
        assertEquals("conteudo2", capturedFoos.get(1).conteudo);

    }
}