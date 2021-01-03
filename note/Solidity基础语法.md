# Solidity基础语法

Solidity是编写智能合约的语言，运行在ethereum虚拟机上。语法类似于JS，它拥有异常机制，一旦出现异常，所有的执行都会被撤回，这是为了保证合约执行的原子性，避免中间状态出现的数据不一致。

简单的合约例子：

```js
pragma solidity ^0.4.9;
contract Helloworld {
    function multi(uint a, uint b) returns (uint c) {
    uint result = a * b;
    return result;
    }
}
```

## 数据类型

- address 以太坊地址的长度，20个字节
- int/uint 变长的有符号或无符号整型。支持以8递增，uint8到uint256。uint 默认为uint256。
- bool 布尔型
- mapping 键值对映射关系，如mapping(address => uint)
- struct 结构体，如下例子


## 状态变量storage和局部变量memory

两者区别很容易理解，memory可以理解为临时变量，不会记录在链上，而storage是永久存储的。

- 变量定义时默认为storage，而作为函数参数时，默认为memory

```js
contract HelloWorld{
    //等价于 string storage public a;
    string public a;

    //参数等价于string memory _a
    function changeNum(string _a){
    }   
}
```

- 当函数参数为memory类型时，相当于值传递，storage才是指针传递

```js
contract HelloWorld2{
    string public a;
    
    function HelloWorld2(){
        a = "abc";
    }
    
    function f(){
        changeNum(a);
    }
    
    function changeNum(string _a){
        bytes(_a)[0] = "d";
      //由于_a默认为memory，所以_a只是值传递，所以此时修改a的值是不成功的，输出还是abc
      //需要把函数参数修改为string storage _a，才能输出dbc
    }
}
```

- 将变量赋值给一个新变量时，新变量的类型由赋值给它的类型决定。

```js
function changeNum(string _a){
        //_a默认为memory类型，所以b也为memory
        string b = _a;
        bytes(_a)[0] = "d";
    }
```

## 函数四种访问权限
函数声明有public、private、internal和external四种访问权限

1. 函数默认声明为public，即可以以internal方式调用，也可以通过external方式调用。可以理解为能够被内部合约访问和外部合约访问。
2. Internal声明的只允许通过internal方式调用，不能被外部合约。而external能够被外部合约访问。
3. private和internal类似，都不能被外部合约访问，唯一的不同是private函数不能被子类调用，而internal可以。

```js
contract FunctionTest{
    function publicFunc() {}

    function callFunc(){
        //以`internal`的方式调用函数
        publicFunc();
        
        //以`external`的方式调用函数
        this.publicFunc();
    }
    
    function internalFunc() internal{}
    
    function externalFunc() external{}   
}

contract FunctionTest1 {
    function externalCall(FuntionTest ft){
        //调用另一个合约的外部函数
        ft.publicFunc();
        ft.externalFunc();
       //ft.internalFunc();调用失败，无法调用internal函数
    }
}
```

