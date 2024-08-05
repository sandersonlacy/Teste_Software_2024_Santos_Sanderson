**UNIVERSIDADE FEDERAL DE SERGIPE**

Teste de Software

Sanderson Lacy Alves dos Santos

Atividade 1

Testes unitários e o Stack Overflow


**1. Definição do problema**

A pergunta problema escolhida no Stack Overflow tem como título “Can Mockito capture arguments of a method called multiple times?”, e pode ser encontrada a
partir do endereço
[https://stackoverflow.com/questions/5981605/can-mockito-capture-arguments-of-a-method-called-multiple-times](https://stackoverflow.com/questions/5981605/can-mockito-capture-arguments-of-a-method-called-multiple-times)

Os artefatos utilizados na atividade incluindo a versão *markdown* deste documento está disponível em repositório público no Github disponível através do endereço
<https://github.com/sandersonlacy/Teste_Software_2024_Santos_Sanderson>.

A pergunta da thread no Stack Overflow contém quase 600 upvotes, e a resposta considerada como aceita pelo Stack Overflow possui mais de 1000 upvotes. Além disso, há outras 5 respostas com número de upvotes aceitável Portanto, trata-se de uma thread de qualidade, cuja resposta verificada está relativamente validada por outros usuários. A pergunta é relacionada à linguagem de programação Java, dentro do tópico de testes unitários, sobre o framework *Mockito*.


O problema se trata de uma tentativa de capturar o argumento da segunda chamada de método de uma classe “*mockada”*, ou seja, que possui um *mock* simulando seu comportamento. Para isso, utilizou-se a classe *ArgumentCaptor* que tem como objetivo capturar o valor dos argumentos passados a uma ou mais chamadas de método.


**2. Reprodução do problema em IDE**

Para reprodução do problema, foram usadas duas classes: *Foo e Bar,* além da usada para execução do teste chamada ReproducaoProblema.
```
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
```

A classe Bar possui um método *doSomething()* que recebe uma instância de *Foo*. Esta por sua vez, armazena uma informação qualquer, chamada *conteudo*. Na classe ReproducaoProblema, onde vamos fazer os testes, é instanciado o *mock* da classe *Bar*, juntamente com os objetos foo1 e foo2. Estes, serão passados como argumento para o mock através do método *doSomething()*. Da mesma forma que o problema no Stack Overflow, duas instâncias de ArgumentCaptor da classe *Foo* são criadas. Após isso, duas chamadas ao método *verify()*, passando para o *doSomething()* os objetos ArgumentCaptor criados anteriormente. Após isso, apenas para maior verossimilhança com o cenário passado na dúvida do Stack
Overflow, é chamado o método *assertEquals()* para testar o conteúdo do objeto secondFooCaptor. Como esperado, é lançada a exceção TooManyActualInvocations. Isso porque, a função *verify()* sem parâmetros adicionais, verifica se determinado método foi executado uma única vez. Isso não acontece na situação problema, dado que o *doSomething()* é chamado duas vezes, com o *verify()* sendo chamado também duas vezes.

**3. Reprodução da solução em IDE**

A reprodução do problema usa a resposta dada como aceita pelo Stack Overflow, mantendo as mesmas classes e métodos mencionados anteriormente.

```
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
```
Desta vez, ao invés de chamar *verify()* duas vezes para duas instâncias de ArgumentCaptor, é utilizado conforme documentação, o método *times()*, passando para este, a quantidade de vezes que o método *doSomething()* deve ser chamado. Após isso, é usado o método *getAllValues()* que retorna todos os argumentos que foram capturados, que são armazenados em uma List do tipo *Foo* chamada capturedFoos. Para verificar que os valores capturados estão corretos, dois *assertEquals()* são chamados comparando as strings armazenadas nos objetos *Foo’s*, passando como segundo argumento, capturedFoos e *gets* usando os índices da lista, e por fim acessado o atributo de conteúdo. Como esperado, não houve falha alguma no teste unitário.

**4. Comparação de respostas do Stack Overflow**

```
//I think it should be

verify(mockBar, times(2)).doSomething(...)
Sample from mockito javadoc:

ArgumentCaptor<Person> peopleCaptor = ArgumentCaptor.forClass(Person.class);
verify(mock, times(2)).doSomething(peopleCaptor.capture());

List<Person> capturedPeople = peopleCaptor.getAllValues();
assertEquals("John", capturedPeople.get(0).getName());
assertEquals("Jane", capturedPeople.get(1).getName());
```
*Resposta dada como a melhor pelo Stack Overflow*

Esta é a resposta usada para criação da solução em IDE, e já foi comentada anteriormente.




```
/*Since Mockito 2.0 there's also possibility to use static method Matchers.argThat(ArgumentMatcher). With the help of Java 8 it is now much cleaner and more readable to write:*/

verify(mockBar).doSth(argThat((arg) -> arg.getSurname().equals("OneSurname")));
verify(mockBar).doSth(argThat((arg) -> arg.getSurname().equals("AnotherSurname")));
/*If you're tied to lower Java version there's also not-that-bad:*/

verify(mockBar).doSth(argThat(new ArgumentMatcher<Employee>() {
        @Override
        public boolean matches(Object emp) {
            return ((Employee) emp).getSurname().equals("SomeSurname");
        }
    }));
/*Of course none of those can verify order of calls - for which you should use InOrder :*/

InOrder inOrder = inOrder(mockBar);

inOrder.verify(mockBar).doSth(argThat((arg) -> arg.getSurname().equals("FirstSurname")));
inOrder.verify(mockBar).doSth(argThat((arg) -> arg.getSurname().equals("SecondSurnam
```
*Segunda melhor resposta segundo o Stack Overflow*

Ao contrário da primeira resposta, a segunda propõe três formas distintas de resolver o problema. Usando funções e instâncias anônimas, e o método *inOrder()* provido pelo *Mockito.* As três formas apresentadas, podem ser utilizadas sem o lançamento da exceção em questão na thread. Com o detalhe de que o último método permite o conhecimento da ordem dos argumentos.


```
/*If you don't want to validate all the calls to doSomething(), only the last one, you can just use ArgumentCaptor.getValue(). According to the Mockito javadoc:

If the method was called multiple times then it returns the latest captured value

So this would work (assumes Foo has a method getName()):*/

ArgumentCaptor<Foo> fooCaptor = ArgumentCaptor.forClass(Foo.class);
verify(mockBar, times(2)).doSomething(fooCaptor.capture());
//getValue() contains value set in second call to doSomething()
assertEquals("2nd one", fooCaptor.getValue().getName());
```
*Terceira melhor resposta segundo o Stack Overflow*

A terceira resposta ao contrário da primeira, propõe o uso do *getValue()* que captura o último argumento armazenado, ao invés do uso de *Lists* , o que pode ser mais interessante, dado que somente o segundo argumento é requerido, e apenas duas invocações de *veriy()* são usadas.

```

/*You can also use @Captor annotated ArgumentCaptor. For example:*/

@Mock
List<String> mockedList;

@Captor
ArgumentCaptor<String> argCaptor;

@BeforeTest
public void init() {
    //Initialize objects annotated with @Mock, @Captor and @Spy.
    MockitoAnnotations.initMocks(this);
}

@Test
public void shouldCallAddMethodTwice() {
    mockedList.add("one");
    mockedList.add("two");
    Mockito.verify(mockedList, times(2)).add(argCaptor.capture());

    assertEquals("one", argCaptor.getAllValues().get(0));
    assertEquals("two", argCaptor.getAllValues().get(1));
}
```
*Quarta melhor resposta segundo o Stack Overflow*

A quarta melhor resposta complementa a thread, comentando que é possível usar um anotador específico para o *ArgumentCaptor,* mas de maneira geral, a solução proposta não foge do padrão usado na primeira resposta.


```
//With Java 8's lambdas, a convenient way is to use

org.mockito.invocation.InvocationOnMock

when(client.deleteByQuery(anyString(), anyString())).then(invocationOnMock -> {
    assertEquals("myCollection", invocationOnMock.getArgument(0));
    assertThat(invocationOnMock.getArgument(1), Matchers.startsWith("id:"));
}

```
*Quinta melhor resposta segundo o Stack Overflow*

A quinta melhor resposta propõe o uso de lambdas Java para resolução do problema.

```
/*First of all: you should always import mockito static, this way the code will be much more readable (and intuitive) - the code samples below require it to work:*/

import static org.mockito.Mockito.*;
/*In the verify() method you can pass the ArgumentCaptor to assure execution in the test and the ArgumentCaptor to evaluate the arguments:*/

ArgumentCaptor<MyExampleClass> argument = ArgumentCaptor.forClass(MyExampleClass.class);
verify(yourmock, atleast(2)).myMethod(argument.capture());

List<MyExampleClass> passedArguments = argument.getAllValues();

for (MyExampleClass data : passedArguments){
    //assertSometing ...
    System.out.println(data.getFoo());
}
/*The list of all passed arguments during your test is accessible via the argument.getAllValues() method.

The single (last called) argument's value is accessible via the argument.getValue() for further manipulation / checking or whatever you wish to do.*/
```
*Sexta melhor resposta segundo o Stack Overflow*

A sexta melhor resposta comenta padrões de importação para o *Mockito,* porém a solução proposta se mantém parecida com a primeira resposta.

**Considerações finais**: De maneira geral, todas as respostas resolvem o problema da thread, e apenas as abordagens mudam, sendo preferência do desenvolvedor, a escolha de qual delas utilizar.