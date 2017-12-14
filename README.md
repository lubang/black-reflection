# BlackReflection

Hello, Code. Good bye MAGIC string!

[![Build Status](https://travis-ci.org/lubang/black-reflection.svg?branch=master)](https://travis-ci.org/lubang/black-reflection)

---

BlackReflection helps to get a `name` or `method` of the getter/setter from a bean class.

And you don't need to write 'MAGIC' string. It makes that refactoring is easily.


## Basic Usage
```
final Method actual = BlackReflectionUtil.getMethod(User.class, User::getRegisteredAt);
```

*Powered by D.G.BANG*
