package com.atividade1;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.mockito.ArgumentCaptor;
import static org.junit.jupiter.api.Assertions.assertEquals;


class Bar{
    public void doSomething(Foo b){
        System.out.println(b.conteudo);
    }
}

class Foo{
    public String conteudo;
}

public class ReproducaoProblema{

    private Bar mockBar = mock(Bar.class);

    @Test
    public void teste(){
        Foo foo1 = new Foo();
        foo1.conteudo = "conteudo1";
        Foo foo2 = new Foo();
        foo2.conteudo = "conteudo2";
        mockBar.doSomething(foo1);
        mockBar.doSomething(foo2);

        ArgumentCaptor<Foo> firstFooCaptor = ArgumentCaptor.forClass(Foo.class);
        ArgumentCaptor<Foo> secondFooCaptor = ArgumentCaptor.forClass(Foo.class);
        verify(mockBar).doSomething(firstFooCaptor.capture());
        verify(mockBar).doSomething(secondFooCaptor.capture());
        assertEquals("conteudo2", secondFooCaptor.getValue().conteudo);

    }
}